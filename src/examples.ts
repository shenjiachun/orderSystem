/**
 * Example usage of the Order System with Metadata
 */

import {
  MetadataSchemaManager,
  OrderService,
  FieldType,
  OrderStatus,
  CreateOrderRequest
} from './index';

// Initialize managers
const schemaManager = new MetadataSchemaManager();
const orderService = new OrderService(schemaManager);

// Example 1: Define a custom schema for e-commerce orders
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
    },
    {
      name: 'preferredDeliveryDate',
      type: FieldType.DATE,
      label: 'Preferred Delivery Date'
    }
  ]
});

console.log('Registered schema:', ecommerceSchema.name);

// Example 2: Create an order with custom fields and metadata
const orderRequest: CreateOrderRequest = {
  customerId: 'CUST-001',
  items: [
    {
      productId: 'PROD-001',
      productName: 'Laptop',
      quantity: 1,
      unitPrice: 1200
    },
    {
      productId: 'PROD-002',
      productName: 'Mouse',
      quantity: 2,
      unitPrice: 25
    }
  ],
  metadata: {
    source: 'web',
    campaign: 'summer-sale-2026',
    userAgent: 'Mozilla/5.0...'
  },
  customFields: {
    deliveryMethod: 'express',
    giftMessage: 'Happy Birthday!',
    priority: 5,
    isGift: true,
    preferredDeliveryDate: new Date('2026-03-01')
  }
};

try {
  const order = orderService.createOrder(orderRequest, 'ecommerce-v1');
  console.log('\nCreated order:', {
    id: order.id,
    orderNumber: order.orderNumber,
    totalAmount: order.totalAmount,
    status: order.status,
    metadata: order.metadata,
    customFields: order.customFields
  });

  // Example 3: Update order status
  const updated = orderService.updateOrderStatus(order.id, OrderStatus.CONFIRMED);
  console.log('\nUpdated order status:', updated?.status);

  // Example 4: Add additional metadata
  orderService.addOrderMetadata(order.id, {
    paymentMethod: 'credit-card',
    transactionId: 'TXN-123456'
  });

  // Example 5: Add more custom fields
  orderService.addOrderCustomFields(order.id, {
    giftMessage: 'Happy Birthday! Enjoy your new laptop!'
  }, 'ecommerce-v1');

  const finalOrder = orderService.getOrder(order.id);
  console.log('\nFinal order:', {
    id: finalOrder?.id,
    status: finalOrder?.status,
    metadata: finalOrder?.metadata,
    customFields: finalOrder?.customFields
  });

  // Example 6: Query orders
  const allOrders = orderService.getAllOrders();
  console.log('\nTotal orders:', allOrders.length);

  const confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);
  console.log('Confirmed orders:', confirmedOrders.length);

} catch (error) {
  console.error('Error:', error instanceof Error ? error.message : error);
}

// Example 7: Define another schema for B2B orders
const b2bSchema = schemaManager.registerSchema({
  id: 'b2b-v1',
  name: 'B2B Order Schema',
  description: 'Custom fields for B2B orders',
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
      validation: {
        required: true
      }
    },
    {
      name: 'paymentTerms',
      type: FieldType.NUMBER,
      label: 'Payment Terms (days)',
      validation: {
        enum: [30, 60, 90]
      },
      defaultValue: 30
    },
    {
      name: 'approvers',
      type: FieldType.ARRAY,
      label: 'Approvers'
    }
  ]
});

console.log('\nRegistered B2B schema:', b2bSchema.name);

// Example 8: Create a B2B order
const b2bOrderRequest: CreateOrderRequest = {
  customerId: 'CORP-001',
  items: [
    {
      productId: 'PROD-100',
      productName: 'Enterprise Software License',
      quantity: 100,
      unitPrice: 50
    }
  ],
  customFields: {
    poNumber: 'PO-123456',
    accountManager: 'John Smith',
    paymentTerms: 60,
    approvers: ['manager1@company.com', 'cfo@company.com']
  }
};

try {
  const b2bOrder = orderService.createOrder(b2bOrderRequest, 'b2b-v1');
  console.log('\nCreated B2B order:', {
    id: b2bOrder.id,
    orderNumber: b2bOrder.orderNumber,
    totalAmount: b2bOrder.totalAmount,
    customFields: b2bOrder.customFields
  });
} catch (error) {
  console.error('Error creating B2B order:', error instanceof Error ? error.message : error);
}

// Example 9: Demonstrate validation error
console.log('\n--- Validation Error Example ---');
try {
  const invalidOrder = orderService.createOrder({
    customerId: 'CUST-002',
    items: [{
      productId: 'PROD-003',
      productName: 'Keyboard',
      quantity: 1,
      unitPrice: 75
    }],
    customFields: {
      deliveryMethod: 'invalid-method', // Invalid enum value
      priority: 10 // Out of range
    }
  }, 'ecommerce-v1');
} catch (error) {
  console.log('Caught validation error:', error instanceof Error ? error.message : error);
}

console.log('\n--- Examples completed ---');
