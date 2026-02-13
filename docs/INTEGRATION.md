# Integration Guide

This guide shows how to integrate the Order System with Metadata Capabilities into different environments and use cases.

## Table of Contents

1. [Express.js Integration](#expressjs-integration)
2. [REST API Example](#rest-api-example)
3. [Database Integration](#database-integration)
4. [Multiple Schema Use Cases](#multiple-schema-use-cases)

---

## Express.js Integration

### Setup

First, install Express and related dependencies:

```bash
npm install express @types/express
```

### Basic Express Server

```typescript
import express, { Request, Response } from 'express';
import {
  MetadataSchemaManager,
  OrderService,
  FieldType,
  OrderStatus,
  CreateOrderRequest
} from './index';

const app = express();
app.use(express.json());

// Initialize services
const schemaManager = new MetadataSchemaManager();
const orderService = new OrderService(schemaManager);

// Register default schema
schemaManager.registerSchema({
  id: 'default-v1',
  name: 'Default Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'deliveryMethod',
      type: FieldType.STRING,
      label: 'Delivery Method',
      validation: {
        enum: ['standard', 'express', 'pickup']
      }
    },
    {
      name: 'notes',
      type: FieldType.STRING,
      label: 'Notes',
      validation: { max: 500 }
    }
  ]
});

// Routes
app.post('/api/orders', (req: Request, res: Response) => {
  try {
    const schemaId = req.body.schemaId || 'default-v1';
    const order = orderService.createOrder(req.body, schemaId);
    res.status(201).json(order);
  } catch (error) {
    res.status(400).json({
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
});

app.get('/api/orders', (req: Request, res: Response) => {
  const orders = orderService.getAllOrders();
  res.json(orders);
});

app.get('/api/orders/:id', (req: Request, res: Response) => {
  const order = orderService.getOrder(req.params.id);
  if (!order) {
    return res.status(404).json({ error: 'Order not found' });
  }
  res.json(order);
});

app.patch('/api/orders/:id', (req: Request, res: Response) => {
  try {
    const schemaId = req.body.schemaId;
    const order = orderService.updateOrder(req.params.id, req.body, schemaId);
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }
    res.json(order);
  } catch (error) {
    res.status(400).json({
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
});

app.delete('/api/orders/:id', (req: Request, res: Response) => {
  const deleted = orderService.deleteOrder(req.params.id);
  if (!deleted) {
    return res.status(404).json({ error: 'Order not found' });
  }
  res.status(204).send();
});

// Schema management routes
app.post('/api/schemas', (req: Request, res: Response) => {
  try {
    const schema = schemaManager.registerSchema(req.body);
    res.status(201).json(schema);
  } catch (error) {
    res.status(400).json({
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
});

app.get('/api/schemas', (req: Request, res: Response) => {
  const schemas = schemaManager.getAllSchemas();
  res.json(schemas);
});

app.get('/api/schemas/:id', (req: Request, res: Response) => {
  const schema = schemaManager.getSchema(req.params.id);
  if (!schema) {
    return res.status(404).json({ error: 'Schema not found' });
  }
  res.json(schema);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

---

## REST API Example

### Create Order

**Request:**
```bash
POST /api/orders
Content-Type: application/json

{
  "customerId": "CUST-001",
  "items": [
    {
      "productId": "PROD-001",
      "productName": "Laptop",
      "quantity": 1,
      "unitPrice": 1200
    }
  ],
  "metadata": {
    "source": "mobile-app",
    "campaign": "black-friday"
  },
  "customFields": {
    "deliveryMethod": "express",
    "notes": "Please call before delivery"
  },
  "schemaId": "default-v1"
}
```

**Response:**
```json
{
  "id": "ORD-000001",
  "orderNumber": "ON-1234567890-1",
  "customerId": "CUST-001",
  "items": [
    {
      "id": "ORD-000001-ITEM-1",
      "productId": "PROD-001",
      "productName": "Laptop",
      "quantity": 1,
      "unitPrice": 1200,
      "totalPrice": 1200
    }
  ],
  "status": "PENDING",
  "totalAmount": 1200,
  "metadata": {
    "source": "mobile-app",
    "campaign": "black-friday"
  },
  "customFields": {
    "deliveryMethod": "express",
    "notes": "Please call before delivery"
  },
  "createdAt": "2026-02-13T07:00:00.000Z",
  "updatedAt": "2026-02-13T07:00:00.000Z"
}
```

### Get All Orders

**Request:**
```bash
GET /api/orders
```

**Response:**
```json
[
  {
    "id": "ORD-000001",
    "orderNumber": "ON-1234567890-1",
    "customerId": "CUST-001",
    ...
  }
]
```

### Update Order

**Request:**
```bash
PATCH /api/orders/ORD-000001
Content-Type: application/json

{
  "status": "CONFIRMED",
  "metadata": {
    "confirmedBy": "admin@example.com",
    "confirmationTime": "2026-02-13T08:00:00.000Z"
  }
}
```

### Create Schema

**Request:**
```bash
POST /api/schemas
Content-Type: application/json

{
  "id": "wholesale-v1",
  "name": "Wholesale Order Schema",
  "version": "1.0.0",
  "fields": [
    {
      "name": "companyName",
      "type": "string",
      "label": "Company Name",
      "validation": {
        "required": true,
        "min": 2
      }
    },
    {
      "name": "bulkDiscount",
      "type": "number",
      "label": "Bulk Discount %",
      "validation": {
        "min": 0,
        "max": 50
      }
    }
  ]
}
```

---

## Database Integration

### MongoDB Example

```typescript
import { MongoClient, Db } from 'mongodb';
import { Order, MetadataSchema } from './types';

class OrderRepository {
  private db: Db;

  constructor(db: Db) {
    this.db = db;
  }

  async saveOrder(order: Order): Promise<void> {
    await this.db.collection('orders').insertOne(order);
  }

  async findOrderById(id: string): Promise<Order | null> {
    return await this.db.collection('orders').findOne({ id });
  }

  async findAllOrders(): Promise<Order[]> {
    return await this.db.collection('orders').find().toArray();
  }

  async updateOrder(id: string, updates: Partial<Order>): Promise<void> {
    await this.db.collection('orders').updateOne(
      { id },
      { $set: { ...updates, updatedAt: new Date() } }
    );
  }

  async deleteOrder(id: string): Promise<void> {
    await this.db.collection('orders').deleteOne({ id });
  }
}

class SchemaRepository {
  private db: Db;

  constructor(db: Db) {
    this.db = db;
  }

  async saveSchema(schema: MetadataSchema): Promise<void> {
    await this.db.collection('schemas').insertOne(schema);
  }

  async findSchemaById(id: string): Promise<MetadataSchema | null> {
    return await this.db.collection('schemas').findOne({ id });
  }

  async findAllSchemas(): Promise<MetadataSchema[]> {
    return await this.db.collection('schemas').find().toArray();
  }
}

// Usage
async function main() {
  const client = new MongoClient('mongodb://localhost:27017');
  await client.connect();
  
  const db = client.db('order-system');
  const orderRepo = new OrderRepository(db);
  const schemaRepo = new SchemaRepository(db);
  
  // Use repositories with OrderService
  // ...
}
```

### PostgreSQL Example with TypeORM

```typescript
import { Entity, Column, PrimaryColumn, CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity('orders')
class OrderEntity {
  @PrimaryColumn()
  id: string;

  @Column()
  orderNumber: string;

  @Column()
  customerId: string;

  @Column('json')
  items: any[];

  @Column()
  status: string;

  @Column('decimal')
  totalAmount: number;

  @Column('json', { nullable: true })
  metadata?: any;

  @Column('json', { nullable: true })
  customFields?: any;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}

@Entity('metadata_schemas')
class SchemaEntity {
  @PrimaryColumn()
  id: string;

  @Column()
  name: string;

  @Column({ nullable: true })
  description?: string;

  @Column()
  version: string;

  @Column('json')
  fields: any[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
```

---

## Multiple Schema Use Cases

### E-commerce Platform

```typescript
// Regular customer schema
const b2cSchema = schemaManager.registerSchema({
  id: 'b2c-v1',
  name: 'B2C Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'giftWrap',
      type: FieldType.BOOLEAN,
      label: 'Gift Wrap',
      defaultValue: false
    },
    {
      name: 'giftMessage',
      type: FieldType.STRING,
      label: 'Gift Message',
      validation: { max: 200 }
    },
    {
      name: 'deliverySlot',
      type: FieldType.STRING,
      label: 'Delivery Time Slot',
      validation: {
        enum: ['morning', 'afternoon', 'evening']
      }
    }
  ]
});

// Business customer schema
const b2bSchema = schemaManager.registerSchema({
  id: 'b2b-v1',
  name: 'B2B Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'poNumber',
      type: FieldType.STRING,
      label: 'PO Number',
      validation: {
        required: true,
        pattern: '^PO-[0-9]{6}$'
      }
    },
    {
      name: 'billingAddress',
      type: FieldType.OBJECT,
      label: 'Billing Address'
    },
    {
      name: 'paymentTerms',
      type: FieldType.NUMBER,
      label: 'Payment Terms (days)',
      validation: { enum: [30, 60, 90] }
    }
  ]
});
```

### Food Delivery Platform

```typescript
const foodDeliverySchema = schemaManager.registerSchema({
  id: 'food-delivery-v1',
  name: 'Food Delivery Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'restaurantId',
      type: FieldType.STRING,
      label: 'Restaurant ID',
      validation: { required: true }
    },
    {
      name: 'deliveryAddress',
      type: FieldType.OBJECT,
      label: 'Delivery Address',
      validation: { required: true }
    },
    {
      name: 'utensils',
      type: FieldType.BOOLEAN,
      label: 'Include Utensils',
      defaultValue: true
    },
    {
      name: 'specialInstructions',
      type: FieldType.STRING,
      label: 'Special Instructions',
      validation: { max: 300 }
    },
    {
      name: 'scheduledDeliveryTime',
      type: FieldType.DATE,
      label: 'Scheduled Delivery Time'
    },
    {
      name: 'contactlessDelivery',
      type: FieldType.BOOLEAN,
      label: 'Contactless Delivery',
      defaultValue: false
    }
  ]
});
```

### Subscription Service

```typescript
const subscriptionSchema = schemaManager.registerSchema({
  id: 'subscription-v1',
  name: 'Subscription Order Schema',
  version: '1.0.0',
  fields: [
    {
      name: 'subscriptionId',
      type: FieldType.STRING,
      label: 'Subscription ID',
      validation: { required: true }
    },
    {
      name: 'billingCycle',
      type: FieldType.STRING,
      label: 'Billing Cycle',
      validation: {
        required: true,
        enum: ['monthly', 'quarterly', 'yearly']
      }
    },
    {
      name: 'autoRenew',
      type: FieldType.BOOLEAN,
      label: 'Auto Renew',
      defaultValue: true
    },
    {
      name: 'renewalDate',
      type: FieldType.DATE,
      label: 'Next Renewal Date',
      validation: { required: true }
    }
  ]
});
```

---

## Advanced Use Cases

### Dynamic Schema Selection

```typescript
function determineSchema(customerId: string): string {
  // Logic to determine which schema to use based on customer type
  const customer = getCustomer(customerId);
  
  if (customer.type === 'business') {
    return 'b2b-v1';
  } else if (customer.type === 'subscription') {
    return 'subscription-v1';
  } else {
    return 'b2c-v1';
  }
}

// Use in order creation
app.post('/api/orders', (req, res) => {
  const schemaId = determineSchema(req.body.customerId);
  const order = orderService.createOrder(req.body, schemaId);
  res.json(order);
});
```

### Multi-tenant Support

```typescript
class TenantOrderService {
  private services: Map<string, OrderService> = new Map();
  private schemaManagers: Map<string, MetadataSchemaManager> = new Map();

  getServiceForTenant(tenantId: string): OrderService {
    if (!this.services.has(tenantId)) {
      const schemaManager = new MetadataSchemaManager();
      const orderService = new OrderService(schemaManager);
      
      this.schemaManagers.set(tenantId, schemaManager);
      this.services.set(tenantId, orderService);
    }
    
    return this.services.get(tenantId)!;
  }

  getSchemaManagerForTenant(tenantId: string): MetadataSchemaManager {
    return this.schemaManagers.get(tenantId)!;
  }
}

// Usage
const tenantService = new TenantOrderService();
app.post('/api/:tenantId/orders', (req, res) => {
  const orderService = tenantService.getServiceForTenant(req.params.tenantId);
  const order = orderService.createOrder(req.body);
  res.json(order);
});
```

---

## Testing Integration

```typescript
import request from 'supertest';
import express from 'express';
import { setupOrderRoutes } from './routes';

describe('Order API Integration Tests', () => {
  let app: express.Application;

  beforeEach(() => {
    app = express();
    app.use(express.json());
    setupOrderRoutes(app);
  });

  it('should create an order', async () => {
    const response = await request(app)
      .post('/api/orders')
      .send({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Test Product',
            quantity: 1,
            unitPrice: 100
          }
        ]
      });

    expect(response.status).toBe(201);
    expect(response.body.id).toBeDefined();
    expect(response.body.totalAmount).toBe(100);
  });

  it('should validate custom fields', async () => {
    const response = await request(app)
      .post('/api/orders')
      .send({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Test Product',
            quantity: 1,
            unitPrice: 100
          }
        ],
        customFields: {
          deliveryMethod: 'invalid-method'
        },
        schemaId: 'default-v1'
      });

    expect(response.status).toBe(400);
    expect(response.body.error).toContain('Validation failed');
  });
});
```
