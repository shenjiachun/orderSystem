/**
 * Core order types
 */

import { Metadata } from './metadata';

/**
 * Order status enum
 */
export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

/**
 * Order item
 */
export interface OrderItem {
  id: string;
  productId: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  metadata?: Metadata;
}

/**
 * Order entity with metadata support
 */
export interface Order {
  id: string;
  orderNumber: string;
  customerId: string;
  items: OrderItem[];
  status: OrderStatus;
  totalAmount: number;
  createdAt: Date;
  updatedAt: Date;
  
  // Metadata capabilities
  metadata?: Metadata;
  
  // Extensible business fields (stored as key-value pairs)
  customFields?: Metadata;
}

/**
 * Order creation request
 */
export interface CreateOrderRequest {
  customerId: string;
  items: Omit<OrderItem, 'id' | 'totalPrice'>[];
  metadata?: Metadata;
  customFields?: Metadata;
}

/**
 * Order update request
 */
export interface UpdateOrderRequest {
  status?: OrderStatus;
  metadata?: Metadata;
  customFields?: Metadata;
}
