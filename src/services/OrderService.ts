/**
 * Order Service
 * Handles order operations with metadata and extensible fields support
 */

import {
  Order,
  OrderItem,
  OrderStatus,
  CreateOrderRequest,
  UpdateOrderRequest,
  Metadata,
  FieldDefinition,
  ValidationResult
} from '../types';
import { MetadataValidator } from '../validators/MetadataValidator';
import { MetadataSchemaManager } from './MetadataSchemaManager';

/**
 * Service for managing orders with metadata capabilities
 */
export class OrderService {
  private orders: Map<string, Order> = new Map();
  private orderCounter: number = 0;
  private metadataValidator: MetadataValidator;
  private schemaManager: MetadataSchemaManager;

  constructor(schemaManager: MetadataSchemaManager) {
    this.metadataValidator = new MetadataValidator();
    this.schemaManager = schemaManager;
  }

  /**
   * Create a new order
   */
  createOrder(request: CreateOrderRequest, schemaId?: string): Order {
    // Validate custom fields if schema is provided
    if (schemaId && request.customFields) {
      const fieldDefinitions = this.schemaManager.getFieldDefinitions(schemaId);
      const validation = this.validateCustomFields(request.customFields, fieldDefinitions);
      if (!validation.valid) {
        throw new Error(`Validation failed: ${validation.errors.map(e => e.message).join(', ')}`);
      }
    }

    // Generate order ID and number
    this.orderCounter++;
    const orderId = `ORD-${String(this.orderCounter).padStart(6, '0')}`;
    const orderNumber = `ON-${Date.now()}-${this.orderCounter}`;

    // Process items
    const items: OrderItem[] = request.items.map((item, index) => {
      const totalPrice = item.quantity * item.unitPrice;
      return {
        ...item,
        id: `${orderId}-ITEM-${index + 1}`,
        totalPrice
      };
    });

    // Calculate total amount
    const totalAmount = items.reduce((sum, item) => sum + item.totalPrice, 0);

    // Create order
    const order: Order = {
      id: orderId,
      orderNumber,
      customerId: request.customerId,
      items,
      status: OrderStatus.PENDING,
      totalAmount,
      createdAt: new Date(),
      updatedAt: new Date(),
      metadata: request.metadata,
      customFields: request.customFields
    };

    this.orders.set(orderId, order);
    return order;
  }

  /**
   * Get an order by ID
   */
  getOrder(orderId: string): Order | undefined {
    return this.orders.get(orderId);
  }

  /**
   * Get all orders
   */
  getAllOrders(): Order[] {
    return Array.from(this.orders.values());
  }

  /**
   * Update an order
   */
  updateOrder(orderId: string, updates: UpdateOrderRequest, schemaId?: string): Order | undefined {
    const order = this.orders.get(orderId);
    if (!order) {
      return undefined;
    }

    // Validate custom fields if schema is provided
    if (schemaId && updates.customFields) {
      const fieldDefinitions = this.schemaManager.getFieldDefinitions(schemaId);
      const validation = this.validateCustomFields(updates.customFields, fieldDefinitions);
      if (!validation.valid) {
        throw new Error(`Validation failed: ${validation.errors.map(e => e.message).join(', ')}`);
      }
    }

    // Update order
    const updatedOrder: Order = {
      ...order,
      status: updates.status ?? order.status,
      metadata: updates.metadata ? { ...order.metadata, ...updates.metadata } : order.metadata,
      customFields: updates.customFields ? { ...order.customFields, ...updates.customFields } : order.customFields,
      updatedAt: new Date()
    };

    this.orders.set(orderId, updatedOrder);
    return updatedOrder;
  }

  /**
   * Delete an order
   */
  deleteOrder(orderId: string): boolean {
    return this.orders.delete(orderId);
  }

  /**
   * Get orders by status
   */
  getOrdersByStatus(status: OrderStatus): Order[] {
    return Array.from(this.orders.values()).filter(order => order.status === status);
  }

  /**
   * Get orders by customer ID
   */
  getOrdersByCustomer(customerId: string): Order[] {
    return Array.from(this.orders.values()).filter(order => order.customerId === customerId);
  }

  /**
   * Update order status
   */
  updateOrderStatus(orderId: string, status: OrderStatus): Order | undefined {
    return this.updateOrder(orderId, { status });
  }

  /**
   * Validate custom fields against field definitions
   */
  validateCustomFields(customFields: Metadata, fieldDefinitions: FieldDefinition[]): ValidationResult {
    return this.metadataValidator.validate(customFields, fieldDefinitions);
  }

  /**
   * Add metadata to an order
   */
  addOrderMetadata(orderId: string, metadata: Metadata): Order | undefined {
    const order = this.orders.get(orderId);
    if (!order) {
      return undefined;
    }

    const updatedOrder: Order = {
      ...order,
      metadata: { ...order.metadata, ...metadata },
      updatedAt: new Date()
    };

    this.orders.set(orderId, updatedOrder);
    return updatedOrder;
  }

  /**
   * Add custom fields to an order
   */
  addOrderCustomFields(orderId: string, customFields: Metadata, schemaId?: string): Order | undefined {
    const order = this.orders.get(orderId);
    if (!order) {
      return undefined;
    }

    // Validate if schema is provided
    if (schemaId) {
      const fieldDefinitions = this.schemaManager.getFieldDefinitions(schemaId);
      const validation = this.validateCustomFields(customFields, fieldDefinitions);
      if (!validation.valid) {
        throw new Error(`Validation failed: ${validation.errors.map(e => e.message).join(', ')}`);
      }
    }

    const updatedOrder: Order = {
      ...order,
      customFields: { ...order.customFields, ...customFields },
      updatedAt: new Date()
    };

    this.orders.set(orderId, updatedOrder);
    return updatedOrder;
  }
}
