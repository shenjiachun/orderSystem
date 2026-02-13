# Order System - 订单处理系统

基于阿里 COLA (Clean Object-Oriented and Layered Architecture) 框架的订单处理系统。

## 项目概述

本项目采用 COLA 架构，实现了一个完整的订单处理系统，支持订单的创建、查询、状态更新等核心功能。

## 技术栈

- **架构框架**: COLA 4.3.2
- **开发语言**: Java 8+
- **构建工具**: Maven 3.x
- **Spring Boot**: 2.7.18
- **持久化**: MyBatis-Plus 3.5.5
- **数据库**: H2 (内存数据库)
- **其他**: Lombok, JUnit 5

## 项目结构

```
order-system
├── order-client          # 客户端 API 层
│   └── api              # 服务接口定义
│   └── dto              # 数据传输对象
├── order-domain         # 领域层
│   ├── entity           # 实体对象
│   ├── valueobject      # 值对象
│   └── gateway          # 领域网关接口
├── order-app            # 应用层
│   ├── command          # 命令处理器
│   ├── query            # 查询处理器
│   ├── service          # 应用服务
│   └── convertor        # 数据转换器
├── order-infrastructure # 基础设施层
│   ├── gatewayimpl      # 网关实现
│   ├── mapper           # MyBatis Mapper
│   ├── dataobject       # 数据对象
│   └── converter        # 数据转换器
├── order-adapter        # 适配器层
│   ├── web              # REST API 控制器
│   └── config           # 配置类
└── order-start          # 启动模块
    └── Application.java # Spring Boot 启动类
```

## 核心功能

### 1. 创建订单
- **接口**: `POST /api/orders`
- **命令**: `CreateOrderCmd`
- **处理器**: `CreateOrderCmdExe`

### 2. 查询订单
- **接口**: `GET /api/orders/{id}`
- **查询**: `OrderByIdQry`
- **处理器**: `OrderByIdQryExe`

### 3. 更新订单状态
- **接口**: `PUT /api/orders/{id}/status?status={status}`
- **命令**: `UpdateOrderStatusCmd`
- **处理器**: `UpdateOrderStatusCmdExe`

### 4. 订单列表查询
- **接口**: `GET /api/orders?userId={userId}&orderStatus={status}&pageIndex={page}&pageSize={size}`
- **查询**: `OrderListQry`
- **处理器**: `OrderListQryExe`

## 领域模型

### Order (订单实体)
- `orderId`: 订单ID
- `userId`: 用户ID
- `orderItems`: 订单项列表
- `totalAmount`: 订单总额
- `orderStatus`: 订单状态
- `createTime`: 创建时间
- `updateTime`: 更新时间

### OrderStatus (订单状态枚举)
- `CREATED`: 已创建
- `PAID`: 已支付
- `SHIPPED`: 已发货
- `COMPLETED`: 已完成
- `CANCELLED`: 已取消

### OrderItem (订单项值对象)
- `productId`: 商品ID
- `productName`: 商品名称
- `quantity`: 数量
- `price`: 单价
- `amount`: 小计

## 快速开始

### 前置条件
- JDK 8 或更高版本
- Maven 3.x

### 构建项目
```bash
mvn clean install
```

### 运行应用
```bash
cd order-start
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

### 访问 H2 控制台
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:orderdb`
- 用户名: `sa`
- 密码: (留空)

## API 示例

### 创建订单
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1001,
    "orderItems": [
      {
        "productId": 2001,
        "productName": "商品A",
        "quantity": 2,
        "price": 99.99
      }
    ],
    "totalAmount": 199.98,
    "remark": "测试订单"
  }'
```

### 查询订单
```bash
curl http://localhost:8080/api/orders/1
```

### 更新订单状态
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=PAID"
```

### 查询订单列表
```bash
curl "http://localhost:8080/api/orders?userId=1001&pageIndex=1&pageSize=10"
```

## 架构特点

1. **分层清晰**: 严格遵循 COLA 四层架构（适配器层、应用层、领域层、基础设施层）
2. **CQRS 模式**: 命令和查询分离，提高系统的可维护性
3. **领域驱动**: 核心业务逻辑封装在领域层，保持纯粹性
4. **依赖倒置**: 通过 Gateway 接口实现领域层与基础设施层的解耦
5. **统一响应**: 使用 COLA 提供的 Response、SingleResponse、MultiResponse 统一返回格式

## 测试

运行所有测试：
```bash
mvn test
```

## 许可证

本项目采用 MIT 许可证
