package com.shenjiachun.order.domain.entity;

import com.shenjiachun.order.domain.valueobject.OrderItem;
import com.shenjiachun.order.domain.valueobject.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单实体单元测试
 */
class OrderTest {
    
    @Test
    void testCreateOrder() {
        // Arrange
        Order order = new Order();
        order.setUserId(1001L);
        
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductId(2001L);
        item.setProductName("Test Product");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("99.99"));
        item.calculateAmount();
        items.add(item);
        
        order.setOrderItems(items);
        
        // Act
        order.create();
        
        // Assert
        assertNotNull(order.getOrderStatus());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertNotNull(order.getCreateTime());
        assertNotNull(order.getUpdateTime());
        assertEquals(new BigDecimal("199.98"), order.getTotalAmount());
    }
    
    @Test
    void testUpdateOrderStatus() {
        // Arrange
        Order order = new Order();
        order.setUserId(1001L);
        order.setOrderStatus(OrderStatus.CREATED);
        
        // Act
        order.updateStatus(OrderStatus.PAID);
        
        // Assert
        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        assertNotNull(order.getUpdateTime());
    }
    
    @Test
    void testUpdateCancelledOrderShouldThrowException() {
        // Arrange
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CANCELLED);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            order.updateStatus(OrderStatus.PAID);
        });
    }
    
    @Test
    void testUpdateCompletedOrderShouldThrowException() {
        // Arrange
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETED);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            order.updateStatus(OrderStatus.SHIPPED);
        });
    }
}
