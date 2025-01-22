package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.Users;
import com.yunzhi.retailmanagementsystem.service.UsersService;
import com.yunzhi.retailmanagementsystem.utils.RSAKeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    /*@Value("${jwt.private-key}")
    private String privateKeyBase64;

    @Value("${jwt.public-key}")
    private String publicKeyBase64;*/

    private static final long EXPIRATION_TIME = 3600000; // 1小时，毫秒
    //private static final String SECRET_KEY = "ThisIsASecretKeyForJWTTokenGenerationAndValidation";

    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Users user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request body");
            errorResponse.put("details", "Username and password fields are required.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        boolean isRegistered = usersService.registerUser(user);
        if (isRegistered) {
            Users registeredUser = usersService.getByUsername(user.getUsername());
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("userID", registeredUser.getUserID());
            successResponse.put("message", "User registered successfully.");
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username or email already exists.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Users user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request body");
            errorResponse.put("details", "Username and password fields are required.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Users loggedInUser = usersService.login(user.getUsername(), user.getPassword());

        if (loggedInUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // 检查用户是否已审核
        if (!loggedInUser.isApproved()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User is not approved yet.");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String token = generateToken(loggedInUser);
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("token", token);
        successResponse.put("role", loggedInUser.getRole());
        successResponse.put("expiresIn", EXPIRATION_TIME / 1000);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    private String generateToken(Users user) {
        try {
            PrivateKey privateKey = RSAKeyUtil.getPrivateKeyFromEnv("PRIVATE_KEY");
            Claims claims = Jwts.claims().setSubject(user.getUserID());
            claims.put("role", user.getRole());
            Date now = new Date();
            Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    private Claims parseToken(String token) {
        try {
            PublicKey publicKey = RSAKeyUtil.getPublicKeyFromEnv("PUBLIC_KEY");
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }


    // 获取所有未审核用户
    @GetMapping("/unapproved")
    public ResponseEntity<Object> getUnapprovedUsers() {
        List<Users> unapprovedUsers = usersService.getUnapprovedUsers();
        if (unapprovedUsers!= null) {
            return new ResponseEntity<>(unapprovedUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // 审核用户
    @PutMapping("/{userID}/approveAndRole")
    public ResponseEntity<Object> approveUserAndUpdateRole(
            @PathVariable String userID,
            @RequestBody Map<String, Object> requestBody) {
        String newRole = (String) requestBody.get("role");
        Boolean approve = (Boolean) requestBody.get("approve");

        if (newRole == null || approve == null) {
            return new ResponseEntity<>(Map.of("error", "Invalid request body. Role and approve fields are required."), HttpStatus.BAD_REQUEST);
        }

        boolean roleUpdateSuccess = usersService.assignRole(userID, newRole);
        if (!roleUpdateSuccess) {
            return new ResponseEntity<>(Map.of("error", "User not found for role update."), HttpStatus.NOT_FOUND);
        }

        boolean approvalSuccess = !approve || usersService.approveUser(userID);
        if (!approvalSuccess) {
            return new ResponseEntity<>(Map.of("error", "User not found for approval."), HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userID", userID);
        response.put("newRole", newRole);
        response.put("message", "User approved and role updated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}