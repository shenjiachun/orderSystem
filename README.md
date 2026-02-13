# Order System with Metadata Capabilities

一个具有元数据能力和可扩展业务字段的订单处理系统。

## 功能特性 (Features)

- ✅ **元数据支持** - 订单可以附加任意元数据，用于追踪额外信息（如来源、活动、用户代理等）
- ✅ **可扩展业务字段** - 通过定义字段模式(Schema)动态添加业务字段，无需修改代码
- ✅ **类型安全** - 使用 TypeScript 实现，提供完整的类型检查
- ✅ **字段验证** - 支持多种验证规则（必填、最小值/最大值、正则表达式、枚举值等）
- ✅ **多种字段类型** - 支持字符串、数字、布尔值、日期、数组、对象等类型
- ✅ **订单生命周期管理** - 完整的订单创建、更新、查询、删除功能

## 安装 (Installation)

```bash
npm install
```

## 构建 (Build)

```bash
npm run build
```

## 测试 (Test)

```bash
npm test
```

## 使用示例 (Usage Examples)

### 1. 基本使用 - 创建订单

```typescript
import { OrderService, MetadataSchemaManager } from './index';

const schemaManager = new MetadataSchemaManager();
const orderService = new OrderService(schemaManager);

// 创建一个简单订单
const order = orderService.createOrder({
  customerId: 'CUST-001',
  items: [
    {
      productId: 'PROD-001',
      productName: 'Laptop',
      quantity: 1,
      unitPrice: 1200
    }
  ]
});

console.log(order);
```

### 2. 使用元数据

```typescript
// 创建带有元数据的订单
const order = orderService.createOrder({
  customerId: 'CUST-001',
  items: [
    {
      productId: 'PROD-001',
      productName: 'Laptop',
      quantity: 1,
      unitPrice: 1200
    }
  ],
  metadata: {
    source: 'web',
    campaign: 'summer-sale-2026',
    userAgent: 'Mozilla/5.0...'
  }
});

// 添加更多元数据
orderService.addOrderMetadata(order.id, {
  paymentMethod: 'credit-card',
  transactionId: 'TXN-123456'
});
```

### 3. 定义可扩展字段模式

```typescript
import { FieldType } from './types';

// 定义电商订单的扩展字段模式
const ecommerceSchema = schemaManager.registerSchema({
  id: 'ecommerce-v1',
  name: 'E-commerce Order Schema',
  description: 'Custom fields for e-commerce orders',
  version: '1.0.0',
  fields: [
    {
      name: 'deliveryMethod',
      type: FieldType.STRING,
      label: 'Delivery Method',
      validation: {
        required: true,
        enum: ['standard', 'express', 'pickup']
      }
    },
    {
      name: 'giftMessage',
      type: FieldType.STRING,
      label: 'Gift Message',
      validation: {
        max: 200
      }
    },
    {
      name: 'priority',
      type: FieldType.NUMBER,
      label: 'Priority',
      validation: {
        min: 1,
        max: 5
      },
      defaultValue: 3
    },
    {
      name: 'isGift',
      type: FieldType.BOOLEAN,
      label: 'Is Gift',
      defaultValue: false
    }
  ]
});
```

### 4. 使用扩展字段创建订单

```typescript
// 使用扩展字段创建订单
const order = orderService.createOrder({
  customerId: 'CUST-001',
  items: [
    {
      productId: 'PROD-001',
      productName: 'Laptop',
      quantity: 1,
      unitPrice: 1200
    }
  ],
  customFields: {
    deliveryMethod: 'express',
    giftMessage: 'Happy Birthday!',
    priority: 5,
    isGift: true
  }
}, 'ecommerce-v1'); // 指定使用的模式ID

// 系统会自动验证字段类型和约束
```

### 5. B2B 订单示例

```typescript
// 定义 B2B 订单模式
const b2bSchema = schemaManager.registerSchema({
  id: 'b2b-v1',
  name: 'B2B Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'poNumber',
      type: FieldType.STRING,
      label: 'Purchase Order Number',
      validation: {
        required: true,
        pattern: '^PO-[0-9]{6}$'
      }
    },
    {
      name: 'accountManager',
      type: FieldType.STRING,
      label: 'Account Manager',
      validation: { required: true }
    },
    {
      name: 'paymentTerms',
      type: FieldType.NUMBER,
      label: 'Payment Terms (days)',
      validation: {
        enum: [30, 60, 90]
      }
    }
  ]
});

// 创建 B2B 订单
const b2bOrder = orderService.createOrder({
  customerId: 'CORP-001',
  items: [{
    productId: 'PROD-100',
    productName: 'Enterprise Software License',
    quantity: 100,
    unitPrice: 50
  }],
  customFields: {
    poNumber: 'PO-123456',
    accountManager: 'John Smith',
    paymentTerms: 60
  }
}, 'b2b-v1');
```

### 6. 订单查询

```typescript
// 获取所有订单
const allOrders = orderService.getAllOrders();

// 按状态查询
const pendingOrders = orderService.getOrdersByStatus(OrderStatus.PENDING);
const confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);

// 按客户查询
const customerOrders = orderService.getOrdersByCustomer('CUST-001');

// 获取单个订单
const order = orderService.getOrder('ORD-000001');
```

### 7. 更新订单

```typescript
// 更新订单状态
orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);

// 更新元数据和扩展字段
orderService.updateOrder(orderId, {
  status: OrderStatus.PROCESSING,
  metadata: { trackingNumber: 'TRACK-123' },
  customFields: { priority: 5 }
}, 'ecommerce-v1');
```

## 运行示例 (Run Examples)

```bash
npm run dev
```

这将运行 `src/examples.ts` 文件，展示系统的各种功能。

## 核心概念 (Core Concepts)

### 元数据 (Metadata)

元数据是附加在订单上的键值对，用于存储额外的上下文信息，如：
- 订单来源（web, mobile, api）
- 营销活动信息
- 支付信息
- 追踪信息

### 扩展字段 (Custom Fields)

扩展字段通过定义字段模式(Schema)来添加业务特定的字段，特点：
- 类型安全 - 定义字段类型
- 自动验证 - 支持多种验证规则
- 可复用 - 同一模式可用于多个订单
- 动态扩展 - 无需修改代码即可添加新字段

### 字段类型 (Field Types)

- `STRING` - 字符串
- `NUMBER` - 数字
- `BOOLEAN` - 布尔值
- `DATE` - 日期
- `ARRAY` - 数组
- `OBJECT` - 对象

### 验证规则 (Validation Rules)

- `required` - 必填字段
- `min` / `max` - 最小值/最大值（数字）或最小/最大长度（字符串）
- `pattern` - 正则表达式验证（字符串）
- `enum` - 枚举值验证
- `custom` - 自定义验证函数

## 架构设计 (Architecture)

```
src/
├── types/              # 类型定义
│   ├── metadata.ts     # 元数据和字段定义类型
│   ├── order.ts        # 订单类型
│   └── index.ts
├── services/           # 业务逻辑
│   ├── MetadataSchemaManager.ts  # 模式管理
│   └── OrderService.ts           # 订单服务
├── validators/         # 验证器
│   └── MetadataValidator.ts      # 元数据验证
├── __tests__/          # 测试文件
├── examples.ts         # 使用示例
└── index.ts            # 主入口
```

## 贡献 (Contributing)

欢迎提交 Issue 和 Pull Request。

## 许可证 (License)

MIT
