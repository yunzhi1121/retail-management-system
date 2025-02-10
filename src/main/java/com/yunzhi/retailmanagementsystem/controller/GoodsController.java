package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.yunzhi.retailmanagementsystem.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    // 2.3.1 添加货物
    @PostMapping
    public ResponseEntity<Map<String, Object>> addGoods(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = (String) requestBody.get("name");
            String description = (String) requestBody.get("description");
            BigDecimal price = new BigDecimal(requestBody.get("price").toString());
            Integer quantity = (Integer) requestBody.get("quantity");

            if (price.compareTo(BigDecimal.ZERO) <= 0 || quantity < 0) {
                response.put("error", "InvalidRequestBody");
                response.put("message", "Price must be greater than 0 and quantity must be a non-negative integer.");
                return ResponseEntity.badRequest().body(response);
            }

            // 调用服务层方法，获取返回的 Goods 对象
            Goods goods = goodsService.addGoods(name, description, quantity, price);

            if (goods != null) {
                response.put("goodID", goods.getGoodID());
                response.put("message", "Good added successfully.");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                // 商品名称已存在，添加失败
                response.put("error", "DuplicateProductName");
                response.put("message", "A product with the same name already exists.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception e) {
            response.put("error", "InternalServerError");
            response.put("message", "An unexpected error occurred. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // 2.3.2 更新货物信息
    @PutMapping("/{goodID}")
    public ResponseEntity<Map<String, Object>> updateGoods(@PathVariable String goodID, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();

        try {
            Goods existingGoods = goodsService.getGoodsById(goodID);
            if (existingGoods == null) {
                response.put("error", "goodNotFound");
                response.put("message", "The good with ID '" + goodID + "' does not exist.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            String description = (String) requestBody.get("description");
            BigDecimal price = new BigDecimal(requestBody.get("price").toString());
            Integer quantity = (Integer) requestBody.get("quantity");

            if (price.compareTo(BigDecimal.ZERO) <= 0 || quantity < 0) {
                response.put("error", "InvalidRequestBody");
                response.put("message", "Price must be greater than 0 and quantity must be a non-negative integer.");
                return ResponseEntity.badRequest().body(response);
            }

            existingGoods.setDescription(description);
            existingGoods.setPrice(price);
            existingGoods.setQuantity(quantity);

            boolean isUpdated = goodsService.updateGoods(existingGoods.getGoodID(), existingGoods.getName(), existingGoods.getDescription(), existingGoods.getQuantity(), existingGoods.getPrice());
            if (isUpdated) {
                response.put("goodID", existingGoods.getGoodID());
                response.put("message", "good updated successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "InternalServerError");
                response.put("message", "An unexpected error occurred. Please try again later.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("error", "InternalServerError");
            response.put("message", "An unexpected error occurred. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 2.3.3 查询货物
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGoods(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                response.put("error", "InvalidQueryParameters");
                response.put("message", "Minimum price cannot be greater than maximum price.");
                return ResponseEntity.badRequest().body(response);
            }

            List<Goods> goodsList = goodsService.getGoodsByName(name);
            // 这里可以根据minPrice和maxPrice进一步过滤goodsList
            // 这里可以根据page和pageSize进行分页处理

            response.put("goods", goodsList);
            response.put("pagination", Map.of(
                    "currentPage", page,
                    "totalPages", (int) Math.ceil((double) goodsList.size() / pageSize),
                    "pageSize", pageSize,
                    "totalItems", goodsList.size()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "InternalServerError");
            response.put("message", "An unexpected error occurred. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}