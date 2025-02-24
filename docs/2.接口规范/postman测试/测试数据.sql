-- 禁用外键约束检查
SET FOREIGN_KEY_CHECKS = 0;

-- 执行 TRUNCATE 操作，删除表中所有数据
TRUNCATE TABLE tracking;
TRUNCATE TABLE servicerequests;
TRUNCATE TABLE reports;
TRUNCATE TABLE order_good;
TRUNCATE TABLE customers;
TRUNCATE TABLE goods;
TRUNCATE TABLE orders;
TRUNCATE TABLE users;

-- 重新启用外键约束检查
SET FOREIGN_KEY_CHECKS = 1;

-- 插入基础测试用户数据
INSERT INTO users (UserID, Username, Password, Role) VALUES
    ('adminID', 'admin', '5e9004f681c7b8f2fd9641f2263b493e', 'ADMIN'),
    ('inventoryID', 'inventory', '5e9004f681c7b8f2fd9641f2263b493e', 'INVENTORT_ADMIN'),
    ('old_userID', 'old_user', '5e9004f681c7b8f2fd9641f2263b493e', 'TRACK_ADMIN');

-- 插入基础测试客户数据
INSERT INTO customers (CustomerID, Name, Email, Phone)
VALUES
    ('cust1_ID', '张三', 'zhangsan@test.com', '13800000001'),
    ('cust2_ID', '李四', 'lisi@test.com', '13800000002');


-- 插入基础商品数据
INSERT INTO goods (GoodID, Name, Description, Quantity, Price)
VALUES
    ('g1_ID', 'iPhone 15', '旗舰智能手机', 100, 7999.00),
    ('g2_ID', '小米电视', '4K 智能电视', 50, 2999.00),
    ('g3_ID', '华为笔记本', '13英寸轻薄本', 80, 5999.00);



-- 插入低库存测试商品
INSERT INTO goods (GoodID, Name, Quantity, Price)
VALUES ('low1_ID', '低库存商品', 5, 99.00);


INSERT INTO orders (OrderID, CustomerID, OrderDate, Status, TotalAmount) VALUES
                                                                             ('order1_ID', 'cust1_ID', '2024-03-01', 'COMPLETED', 21997.00),
                                                                             ('order2_ID', 'cust1_ID', '2024-03-05', 'SHIPPED', 23997.00),
                                                                             ('order3_ID', 'cust2_ID', '2024-03-10', 'CANCELLED', 11996.00),
                                                                             ('order4_ID', 'cust2_ID', '2024-03-15', 'PENDING', 5999.00),
                                                                             ('order5_ID', 'cust1_ID', '2024-03-16', 'PENDING', 5999.00);

INSERT INTO order_good (OrderID, GoodID, Quantity) VALUES
                                                       ('order1_ID', 'g1_ID', 2),
                                                       ('order1_ID', 'g3_ID', 1),
                                                       ('order2_ID', 'g1_ID', 3),
                                                       ('order3_ID', 'g2_ID', 4),
                                                       ('order4_ID', 'g3_ID', 1),
                                                       ('order5_ID', 'g3_ID', 1);

INSERT INTO servicerequests (RequestID, CustomerID, Description, Status)
VALUES ('req1_ID', 'cust1_ID', '屏幕维修请求', 'PENDING'),
       ('req2_ID', 'cust1_ID', '电池更换请求', 'PROCESSING');


-- 订单 order001 的物流轨迹
INSERT INTO tracking (TrackingID, OrderID, Location, Timestamp) VALUES
                                                                    ('track002', 'order1_ID', '杭州转运中心', '2024-03-20 14:30:00'),
                                                                    ('track003', 'order1_ID', '西湖区配送站', '2024-03-21 09:15:00'),
                                                                    ('track004', 'order1_ID', '已抵达收件人小区快递柜', '2024-03-21 18:20:00');

-- 订单 order002 跨境物流轨迹
INSERT INTO tracking (TrackingID, OrderID, Location, Timestamp) VALUES
                                                                    ('track005', 'order2_ID', '香港国际货运中心', '2024-03-19 22:45:00'),
                                                                    ('track006', 'order2_ID', '深圳保税仓', '2024-03-20 11:10:00'),
                                                                    ('track007', 'order2_ID', '广州白云机场清关完成', '2024-03-21 16:00:00');
