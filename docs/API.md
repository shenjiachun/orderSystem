# API Documentation

## Table of Contents

1. [Core Types](#core-types)
2. [MetadataSchemaManager](#metadataschemamanager)
3. [OrderService](#orderservice)
4. [MetadataValidator](#metadatavalidator)

---

## Core Types

### FieldType

Enum defining supported field types for extensible business fields.

```typescript
enum FieldType {
  STRING = 'string',
  NUMBER = 'number',
  BOOLEAN = 'boolean',
  DATE = 'date',
  ARRAY = 'array',
  OBJECT = 'object'
}
```

### FieldValidation

Interface defining validation rules for fields.

```typescript
interface FieldValidation {
  required?: boolean;          // Field is required
  min?: number;                // Minimum value (number) or length (string)
  max?: number;                // Maximum value (number) or length (string)
  pattern?: string;            // Regex pattern (string)
  enum?: any[];                // Allowed values
  custom?: (value: any) => boolean | string;  // Custom validation function
}
```

### FieldDefinition

Interface defining a field in a metadata schema.

```typescript
interface FieldDefinition {
  name: string;                // Field identifier
  type: FieldType;             // Field type
  label: string;               // Human-readable label
  description?: string;        // Field description
  validation?: FieldValidation; // Validation rules
  defaultValue?: any;          // Default value
}
```

### MetadataSchema

Interface defining a complete metadata schema.

```typescript
interface MetadataSchema {
  id: string;                  // Unique schema identifier
  name: string;                // Schema name
  description?: string;        // Schema description
  version: string;             // Schema version
  fields: FieldDefinition[];   // Field definitions
  createdAt: Date;             // Creation timestamp
  updatedAt: Date;             // Last update timestamp
}
```

### Order

Interface defining an order entity.

```typescript
interface Order {
  id: string;                  // Order ID
  orderNumber: string;         // Human-readable order number
  customerId: string;          // Customer identifier
  items: OrderItem[];          // Order items
  status: OrderStatus;         // Order status
  totalAmount: number;         // Total order amount
  createdAt: Date;             // Creation timestamp
  updatedAt: Date;             // Last update timestamp
  metadata?: Metadata;         // Generic metadata
  customFields?: Metadata;     // Extensible business fields
}
```

### OrderStatus

Enum defining order status values.

```typescript
enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}
```

---

## MetadataSchemaManager

Service for managing metadata schemas and field definitions.

### Constructor

```typescript
const schemaManager = new MetadataSchemaManager();
```

### Methods

#### registerSchema(schema)

Register a new metadata schema.

**Parameters:**
- `schema: Omit<MetadataSchema, 'createdAt' | 'updatedAt'>` - Schema definition

**Returns:** `MetadataSchema` - The registered schema with timestamps

**Example:**
```typescript
const schema = schemaManager.registerSchema({
  id: 'ecommerce-v1',
  name: 'E-commerce Schema',
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
    }
  ]
});
```

#### getSchema(id)

Get a schema by ID.

**Parameters:**
- `id: string` - Schema identifier

**Returns:** `MetadataSchema | undefined` - The schema if found

**Example:**
```typescript
const schema = schemaManager.getSchema('ecommerce-v1');
```

#### getAllSchemas()

Get all registered schemas.

**Returns:** `MetadataSchema[]` - Array of all schemas

**Example:**
```typescript
const allSchemas = schemaManager.getAllSchemas();
```

#### updateSchema(id, updates)

Update an existing schema.

**Parameters:**
- `id: string` - Schema identifier
- `updates: Partial<Omit<MetadataSchema, 'id' | 'createdAt'>>` - Schema updates

**Returns:** `MetadataSchema | undefined` - Updated schema if found

**Example:**
```typescript
const updated = schemaManager.updateSchema('ecommerce-v1', {
  version: '2.0.0',
  description: 'Updated schema'
});
```

#### deleteSchema(id)

Delete a schema.

**Parameters:**
- `id: string` - Schema identifier

**Returns:** `boolean` - True if deleted, false if not found

**Example:**
```typescript
const deleted = schemaManager.deleteSchema('old-schema');
```

#### getFieldDefinitions(schemaId)

Get field definitions for a schema.

**Parameters:**
- `schemaId: string` - Schema identifier

**Returns:** `FieldDefinition[]` - Array of field definitions

**Example:**
```typescript
const fields = schemaManager.getFieldDefinitions('ecommerce-v1');
```

#### addFieldDefinition(schemaId, field)

Add a field definition to a schema.

**Parameters:**
- `schemaId: string` - Schema identifier
- `field: FieldDefinition` - Field definition to add

**Returns:** `boolean` - True if added, false if schema not found or field already exists

**Example:**
```typescript
const added = schemaManager.addFieldDefinition('ecommerce-v1', {
  name: 'giftWrap',
  type: FieldType.BOOLEAN,
  label: 'Gift Wrap',
  defaultValue: false
});
```

#### updateFieldDefinition(schemaId, fieldName, updates)

Update a field definition in a schema.

**Parameters:**
- `schemaId: string` - Schema identifier
- `fieldName: string` - Field name
- `updates: Partial<FieldDefinition>` - Field updates

**Returns:** `boolean` - True if updated, false if not found

**Example:**
```typescript
const updated = schemaManager.updateFieldDefinition('ecommerce-v1', 'giftWrap', {
  label: 'Gift Wrapping Service'
});
```

#### removeFieldDefinition(schemaId, fieldName)

Remove a field definition from a schema.

**Parameters:**
- `schemaId: string` - Schema identifier
- `fieldName: string` - Field name

**Returns:** `boolean` - True if removed, false if not found

**Example:**
```typescript
const removed = schemaManager.removeFieldDefinition('ecommerce-v1', 'oldField');
```

---

## OrderService

Service for managing orders with metadata capabilities.

### Constructor

```typescript
const orderService = new OrderService(schemaManager);
```

**Parameters:**
- `schemaManager: MetadataSchemaManager` - Schema manager instance

### Methods

#### createOrder(request, schemaId?)

Create a new order.

**Parameters:**
- `request: CreateOrderRequest` - Order creation request
- `schemaId?: string` - Optional schema ID for custom field validation

**Returns:** `Order` - The created order

**Throws:** Error if custom field validation fails

**Example:**
```typescript
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
  metadata: { source: 'web' },
  customFields: { deliveryMethod: 'express' }
}, 'ecommerce-v1');
```

#### getOrder(orderId)

Get an order by ID.

**Parameters:**
- `orderId: string` - Order identifier

**Returns:** `Order | undefined` - The order if found

**Example:**
```typescript
const order = orderService.getOrder('ORD-000001');
```

#### getAllOrders()

Get all orders.

**Returns:** `Order[]` - Array of all orders

**Example:**
```typescript
const orders = orderService.getAllOrders();
```

#### updateOrder(orderId, updates, schemaId?)

Update an order.

**Parameters:**
- `orderId: string` - Order identifier
- `updates: UpdateOrderRequest` - Order updates
- `schemaId?: string` - Optional schema ID for custom field validation

**Returns:** `Order | undefined` - Updated order if found

**Throws:** Error if custom field validation fails

**Example:**
```typescript
const updated = orderService.updateOrder('ORD-000001', {
  status: OrderStatus.SHIPPED,
  metadata: { trackingNumber: 'TRACK-123' }
});
```

#### deleteOrder(orderId)

Delete an order.

**Parameters:**
- `orderId: string` - Order identifier

**Returns:** `boolean` - True if deleted, false if not found

**Example:**
```typescript
const deleted = orderService.deleteOrder('ORD-000001');
```

#### getOrdersByStatus(status)

Get orders by status.

**Parameters:**
- `status: OrderStatus` - Order status

**Returns:** `Order[]` - Array of orders with the given status

**Example:**
```typescript
const pendingOrders = orderService.getOrdersByStatus(OrderStatus.PENDING);
```

#### getOrdersByCustomer(customerId)

Get orders by customer ID.

**Parameters:**
- `customerId: string` - Customer identifier

**Returns:** `Order[]` - Array of orders for the customer

**Example:**
```typescript
const customerOrders = orderService.getOrdersByCustomer('CUST-001');
```

#### updateOrderStatus(orderId, status)

Update order status.

**Parameters:**
- `orderId: string` - Order identifier
- `status: OrderStatus` - New status

**Returns:** `Order | undefined` - Updated order if found

**Example:**
```typescript
const updated = orderService.updateOrderStatus('ORD-000001', OrderStatus.CONFIRMED);
```

#### validateCustomFields(customFields, fieldDefinitions)

Validate custom fields against field definitions.

**Parameters:**
- `customFields: Metadata` - Custom fields to validate
- `fieldDefinitions: FieldDefinition[]` - Field definitions

**Returns:** `ValidationResult` - Validation result

**Example:**
```typescript
const fields = schemaManager.getFieldDefinitions('ecommerce-v1');
const result = orderService.validateCustomFields({ priority: 10 }, fields);
if (!result.valid) {
  console.log('Errors:', result.errors);
}
```

#### addOrderMetadata(orderId, metadata)

Add metadata to an order.

**Parameters:**
- `orderId: string` - Order identifier
- `metadata: Metadata` - Metadata to add

**Returns:** `Order | undefined` - Updated order if found

**Example:**
```typescript
const updated = orderService.addOrderMetadata('ORD-000001', {
  paymentMethod: 'credit-card'
});
```

#### addOrderCustomFields(orderId, customFields, schemaId?)

Add custom fields to an order.

**Parameters:**
- `orderId: string` - Order identifier
- `customFields: Metadata` - Custom fields to add
- `schemaId?: string` - Optional schema ID for validation

**Returns:** `Order | undefined` - Updated order if found

**Throws:** Error if validation fails

**Example:**
```typescript
const updated = orderService.addOrderCustomFields('ORD-000001', {
  giftMessage: 'Happy Birthday!'
}, 'ecommerce-v1');
```

---

## MetadataValidator

Validator for metadata against field definitions.

### Constructor

```typescript
const validator = new MetadataValidator();
```

### Methods

#### validate(metadata, fieldDefinitions)

Validate metadata against field definitions.

**Parameters:**
- `metadata: Metadata` - Metadata to validate
- `fieldDefinitions: FieldDefinition[]` - Field definitions

**Returns:** `ValidationResult` - Validation result

**Example:**
```typescript
const result = validator.validate(
  { priority: 5, deliveryMethod: 'express' },
  fieldDefinitions
);

if (!result.valid) {
  result.errors.forEach(error => {
    console.log(`${error.field}: ${error.message}`);
  });
}
```

---

## Validation Examples

### Required Field Validation

```typescript
const field: FieldDefinition = {
  name: 'email',
  type: FieldType.STRING,
  label: 'Email',
  validation: { required: true }
};

// ✅ Valid
validator.validate({ email: 'test@example.com' }, [field]);

// ❌ Invalid - missing required field
validator.validate({}, [field]);
```

### Number Range Validation

```typescript
const field: FieldDefinition = {
  name: 'age',
  type: FieldType.NUMBER,
  label: 'Age',
  validation: { min: 18, max: 100 }
};

// ✅ Valid
validator.validate({ age: 25 }, [field]);

// ❌ Invalid - out of range
validator.validate({ age: 15 }, [field]);
validator.validate({ age: 150 }, [field]);
```

### String Length Validation

```typescript
const field: FieldDefinition = {
  name: 'name',
  type: FieldType.STRING,
  label: 'Name',
  validation: { min: 2, max: 50 }
};

// ✅ Valid
validator.validate({ name: 'John' }, [field]);

// ❌ Invalid - too short or too long
validator.validate({ name: 'J' }, [field]);
validator.validate({ name: 'A'.repeat(51) }, [field]);
```

### Pattern Validation

```typescript
const field: FieldDefinition = {
  name: 'email',
  type: FieldType.STRING,
  label: 'Email',
  validation: { pattern: '^[a-z]+@[a-z]+\\.[a-z]+$' }
};

// ✅ Valid
validator.validate({ email: 'test@example.com' }, [field]);

// ❌ Invalid - doesn't match pattern
validator.validate({ email: 'invalid-email' }, [field]);
```

### Enum Validation

```typescript
const field: FieldDefinition = {
  name: 'status',
  type: FieldType.STRING,
  label: 'Status',
  validation: { enum: ['active', 'inactive', 'pending'] }
};

// ✅ Valid
validator.validate({ status: 'active' }, [field]);

// ❌ Invalid - not in enum
validator.validate({ status: 'invalid' }, [field]);
```

### Custom Validation

```typescript
const field: FieldDefinition = {
  name: 'age',
  type: FieldType.NUMBER,
  label: 'Age',
  validation: {
    custom: (value) => {
      if (value >= 18 && value <= 65) {
        return true;
      }
      return 'Age must be between 18 and 65';
    }
  }
};

// ✅ Valid
validator.validate({ age: 30 }, [field]);

// ❌ Invalid - custom validation fails
validator.validate({ age: 70 }, [field]);
```

---

## Complete Example

```typescript
import {
  MetadataSchemaManager,
  OrderService,
  FieldType,
  OrderStatus
} from './index';

// 1. Initialize services
const schemaManager = new MetadataSchemaManager();
const orderService = new OrderService(schemaManager);

// 2. Define schema
const schema = schemaManager.registerSchema({
  id: 'retail-v1',
  name: 'Retail Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'storeId',
      type: FieldType.STRING,
      label: 'Store ID',
      validation: { required: true }
    },
    {
      name: 'salesPerson',
      type: FieldType.STRING,
      label: 'Sales Person'
    },
    {
      name: 'discount',
      type: FieldType.NUMBER,
      label: 'Discount Percentage',
      validation: { min: 0, max: 100 }
    }
  ]
});

// 3. Create order
const order = orderService.createOrder({
  customerId: 'CUST-001',
  items: [{
    productId: 'PROD-001',
    productName: 'Product',
    quantity: 1,
    unitPrice: 100
  }],
  customFields: {
    storeId: 'STORE-123',
    salesPerson: 'John Doe',
    discount: 10
  }
}, 'retail-v1');

// 4. Update order
orderService.updateOrderStatus(order.id, OrderStatus.CONFIRMED);

// 5. Query orders
const confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);
console.log(`Found ${confirmedOrders.length} confirmed orders`);
```
