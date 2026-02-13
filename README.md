# Order System - Extensible Order Metadata

基于Java技术栈的具有扩展性的订单元数据系统 (Extensible Order Metadata System Based on Java)

## 概述 (Overview)

这是一个高度可扩展的订单元数据系统，使用Java实现。系统采用工厂模式和注册表模式，允许动态扩展订单的元数据功能，无需修改核心代码。

This is a highly extensible order metadata system implemented in Java. The system uses factory and registry patterns to allow dynamic extension of order metadata functionality without modifying core code.

## 核心特性 (Key Features)

- **扩展性 (Extensibility)**: 通过实现 `Metadata` 接口和 `MetadataFactory` 接口，可以轻松添加新的元数据类型
- **类型安全 (Type Safety)**: 使用强类型的Java类来表示不同类型的元数据
- **验证机制 (Validation)**: 每种元数据类型都可以定义自己的验证逻辑
- **注册表模式 (Registry Pattern)**: 使用中央注册表管理所有元数据工厂
- **灵活性 (Flexibility)**: 支持任意键值对的自定义字段

## 项目结构 (Project Structure)

```
orderSystem/
├── pom.xml                                     # Maven配置文件
└── src/
    ├── main/java/com/ordersystem/metadata/
    │   ├── Metadata.java                       # 元数据核心接口
    │   ├── BaseMetadata.java                   # 元数据基础实现
    │   ├── MetadataFactory.java                # 元数据工厂接口
    │   ├── MetadataRegistry.java               # 元数据注册表
    │   ├── Order.java                          # 订单实体
    │   ├── OrderMetadataDemo.java              # 示例程序
    │   └── extensions/                         # 元数据扩展
    │       ├── ShippingMetadata.java           # 物流元数据
    │       ├── ShippingMetadataFactory.java
    │       ├── PaymentMetadata.java            # 支付元数据
    │       ├── PaymentMetadataFactory.java
    │       ├── CustomFieldsMetadata.java       # 自定义字段元数据
    │       └── CustomFieldsMetadataFactory.java
    └── test/java/com/ordersystem/metadata/     # 单元测试
```

## 快速开始 (Quick Start)

### 前置要求 (Prerequisites)

- Java 8 或更高版本
- Maven 3.x

### 构建项目 (Build)

```bash
mvn clean compile
```

### 运行测试 (Run Tests)

```bash
mvn test
```

### 运行示例程序 (Run Demo)

```bash
mvn exec:java -Dexec.mainClass="com.ordersystem.metadata.OrderMetadataDemo"
```

## 使用示例 (Usage Examples)

### 1. 注册元数据工厂 (Register Metadata Factories)

```java
MetadataRegistry registry = MetadataRegistry.getInstance();
registry.registerFactory(new ShippingMetadataFactory());
registry.registerFactory(new PaymentMetadataFactory());
registry.registerFactory(new CustomFieldsMetadataFactory());
```

### 2. 创建订单 (Create Order)

```java
Order order = new Order("ORDER-001", "CUSTOMER-001");
order.setTotalAmount(new BigDecimal("299.99"));
order.setStatus("PENDING");
```

### 3. 添加物流元数据 (Add Shipping Metadata)

```java
ShippingMetadata shipping = (ShippingMetadata) registry.createMetadata("SHIPPING");
shipping.setAddress("123 Main Street, Beijing, China");
shipping.setCarrier("SF Express");
shipping.setTrackingNumber("SF-2026-001");
order.addMetadata(shipping);
```

### 4. 添加支付元数据 (Add Payment Metadata)

```java
PaymentMetadata payment = (PaymentMetadata) registry.createMetadata("PAYMENT");
payment.setPaymentMethod("Alipay");
payment.setTransactionId("TXN-98765");
payment.setPaymentStatus("COMPLETED");
order.addMetadata(payment);
```

### 5. 添加自定义字段 (Add Custom Fields)

```java
CustomFieldsMetadata customFields = (CustomFieldsMetadata) registry.createMetadata("CUSTOM_FIELDS");
customFields.addField("promotion", "SPRING2026");
customFields.addField("giftWrapping", true);
order.addMetadata(customFields);
```

### 6. 检索元数据 (Retrieve Metadata)

```java
ShippingMetadata shipping = (ShippingMetadata) order.getMetadataByType("SHIPPING");
if (shipping != null) {
    String carrier = shipping.getCarrier();
}
```

### 7. 验证元数据 (Validate Metadata)

```java
boolean isValid = order.validateMetadata();
```

## 扩展系统 (Extending the System)

要添加新的元数据类型，按以下步骤操作：

To add a new metadata type, follow these steps:

### 1. 创建元数据类 (Create Metadata Class)

```java
public class InventoryMetadata extends BaseMetadata {
    public static final String TYPE = "INVENTORY";
    
    public InventoryMetadata() {
        super(TYPE);
    }
    
    public void setWarehouseId(String warehouseId) {
        setAttribute("warehouseId", warehouseId);
    }
    
    public String getWarehouseId() {
        return (String) getAttribute("warehouseId");
    }
    
    @Override
    public boolean validate() {
        return getWarehouseId() != null;
    }
}
```

### 2. 创建工厂类 (Create Factory Class)

```java
public class InventoryMetadataFactory implements MetadataFactory {
    @Override
    public String getType() {
        return InventoryMetadata.TYPE;
    }
    
    @Override
    public Metadata create() {
        return new InventoryMetadata();
    }
}
```

### 3. 注册工厂 (Register Factory)

```java
registry.registerFactory(new InventoryMetadataFactory());
```

### 4. 使用新元数据 (Use New Metadata)

```java
InventoryMetadata inventory = (InventoryMetadata) registry.createMetadata("INVENTORY");
inventory.setWarehouseId("WH-001");
order.addMetadata(inventory);
```

## 设计模式 (Design Patterns)

本系统采用以下设计模式：

- **工厂模式 (Factory Pattern)**: `MetadataFactory` 接口用于创建元数据实例
- **注册表模式 (Registry Pattern)**: `MetadataRegistry` 管理所有已注册的工厂
- **模板方法模式 (Template Method)**: `BaseMetadata` 提供通用实现
- **策略模式 (Strategy Pattern)**: 每种元数据类型可以有自己的验证策略

## 技术栈 (Technology Stack)

- Java 8+
- Maven 3.x
- JUnit 4.13.2

## 许可证 (License)

MIT License

## 贡献 (Contributing)

欢迎提交问题和拉取请求！

Welcome to submit issues and pull requests!
