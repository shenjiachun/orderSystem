/**
 * Tests for OrderService
 */

import { OrderService } from '../services/OrderService';
import { MetadataSchemaManager } from '../services/MetadataSchemaManager';
import { OrderStatus, FieldType } from '../types';

describe('OrderService', () => {
  let schemaManager: MetadataSchemaManager;
  let orderService: OrderService;

  beforeEach(() => {
    schemaManager = new MetadataSchemaManager();
    orderService = new OrderService(schemaManager);
  });

  describe('createOrder', () => {
    it('should create an order with basic fields', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 2,
            unitPrice: 100
          }
        ]
      });

      expect(order.id).toBeDefined();
      expect(order.orderNumber).toBeDefined();
      expect(order.customerId).toBe('CUST-001');
      expect(order.items).toHaveLength(1);
      expect(order.totalAmount).toBe(200);
      expect(order.status).toBe(OrderStatus.PENDING);
    });

    it('should create an order with metadata', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ],
        metadata: {
          source: 'web',
          campaign: 'summer-sale'
        }
      });

      expect(order.metadata).toEqual({
        source: 'web',
        campaign: 'summer-sale'
      });
    });

    it('should create an order with custom fields', () => {
      schemaManager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: [
          {
            name: 'priority',
            type: FieldType.NUMBER,
            label: 'Priority',
            validation: { min: 1, max: 5 }
          }
        ]
      });

      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ],
        customFields: {
          priority: 3
        }
      }, 'test-schema');

      expect(order.customFields).toEqual({ priority: 3 });
    });

    it('should validate custom fields against schema', () => {
      schemaManager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: [
          {
            name: 'priority',
            type: FieldType.NUMBER,
            label: 'Priority',
            validation: { min: 1, max: 5 }
          }
        ]
      });

      expect(() => {
        orderService.createOrder({
          customerId: 'CUST-001',
          items: [
            {
              productId: 'PROD-001',
              productName: 'Product 1',
              quantity: 1,
              unitPrice: 100
            }
          ],
          customFields: {
            priority: 10 // Out of range
          }
        }, 'test-schema');
      }).toThrow();
    });

    it('should calculate total amount correctly', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 2,
            unitPrice: 100
          },
          {
            productId: 'PROD-002',
            productName: 'Product 2',
            quantity: 3,
            unitPrice: 50
          }
        ]
      });

      expect(order.totalAmount).toBe(350); // 200 + 150
    });
  });

  describe('getOrder', () => {
    it('should get an order by ID', () => {
      const created = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ]
      });

      const retrieved = orderService.getOrder(created.id);
      expect(retrieved).toEqual(created);
    });

    it('should return undefined for non-existent order', () => {
      const order = orderService.getOrder('NON-EXISTENT');
      expect(order).toBeUndefined();
    });
  });

  describe('updateOrder', () => {
    it('should update order status', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ]
      });

      const updated = orderService.updateOrder(order.id, {
        status: OrderStatus.CONFIRMED
      });

      expect(updated?.status).toBe(OrderStatus.CONFIRMED);
    });

    it('should update metadata', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ],
        metadata: { key1: 'value1' }
      });

      const updated = orderService.updateOrder(order.id, {
        metadata: { key2: 'value2' }
      });

      expect(updated?.metadata).toEqual({
        key1: 'value1',
        key2: 'value2'
      });
    });
  });

  describe('deleteOrder', () => {
    it('should delete an order', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [
          {
            productId: 'PROD-001',
            productName: 'Product 1',
            quantity: 1,
            unitPrice: 100
          }
        ]
      });

      const deleted = orderService.deleteOrder(order.id);
      expect(deleted).toBe(true);
      expect(orderService.getOrder(order.id)).toBeUndefined();
    });
  });

  describe('Query methods', () => {
    beforeEach(() => {
      orderService.createOrder({
        customerId: 'CUST-001',
        items: [{ productId: 'PROD-001', productName: 'Product 1', quantity: 1, unitPrice: 100 }]
      });

      const order2 = orderService.createOrder({
        customerId: 'CUST-002',
        items: [{ productId: 'PROD-002', productName: 'Product 2', quantity: 1, unitPrice: 200 }]
      });
      orderService.updateOrderStatus(order2.id, OrderStatus.CONFIRMED);

      const order3 = orderService.createOrder({
        customerId: 'CUST-001',
        items: [{ productId: 'PROD-003', productName: 'Product 3', quantity: 1, unitPrice: 300 }]
      });
      orderService.updateOrderStatus(order3.id, OrderStatus.SHIPPED);
    });

    it('should get all orders', () => {
      const orders = orderService.getAllOrders();
      expect(orders).toHaveLength(3);
    });

    it('should get orders by status', () => {
      const pendingOrders = orderService.getOrdersByStatus(OrderStatus.PENDING);
      expect(pendingOrders).toHaveLength(1);

      const confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);
      expect(confirmedOrders).toHaveLength(1);
    });

    it('should get orders by customer', () => {
      const customerOrders = orderService.getOrdersByCustomer('CUST-001');
      expect(customerOrders).toHaveLength(2);
    });
  });

  describe('addOrderMetadata', () => {
    it('should add metadata to an order', () => {
      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [{ productId: 'PROD-001', productName: 'Product 1', quantity: 1, unitPrice: 100 }]
      });

      const updated = orderService.addOrderMetadata(order.id, {
        newKey: 'newValue'
      });

      expect(updated?.metadata).toEqual({ newKey: 'newValue' });
    });
  });

  describe('addOrderCustomFields', () => {
    it('should add custom fields to an order', () => {
      schemaManager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: [
          {
            name: 'note',
            type: FieldType.STRING,
            label: 'Note'
          }
        ]
      });

      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [{ productId: 'PROD-001', productName: 'Product 1', quantity: 1, unitPrice: 100 }]
      });

      const updated = orderService.addOrderCustomFields(order.id, {
        note: 'Important order'
      }, 'test-schema');

      expect(updated?.customFields).toEqual({ note: 'Important order' });
    });

    it('should validate custom fields when adding', () => {
      schemaManager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: [
          {
            name: 'priority',
            type: FieldType.NUMBER,
            label: 'Priority',
            validation: { min: 1, max: 5 }
          }
        ]
      });

      const order = orderService.createOrder({
        customerId: 'CUST-001',
        items: [{ productId: 'PROD-001', productName: 'Product 1', quantity: 1, unitPrice: 100 }]
      });

      expect(() => {
        orderService.addOrderCustomFields(order.id, {
          priority: 10 // Out of range
        }, 'test-schema');
      }).toThrow();
    });
  });
});
