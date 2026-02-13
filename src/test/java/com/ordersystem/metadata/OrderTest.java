package com.ordersystem.metadata;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Test for Order with metadata
 */
public class OrderTest {
    
    @Test
    public void testCreateOrder() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        
        assertEquals("ORDER-001", order.getOrderId());
        assertEquals("CUSTOMER-001", order.getCustomerId());
        assertNotNull(order.getCreateTime());
    }
    
    @Test
    public void testAddMetadata() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        Metadata metadata = new TestMetadata();
        
        order.addMetadata(metadata);
        
        assertEquals(1, order.getMetadataList().size());
        assertEquals("TEST", order.getMetadataList().get(0).getType());
    }
    
    @Test
    public void testGetMetadataByType() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        Metadata metadata = new TestMetadata();
        
        order.addMetadata(metadata);
        
        Metadata retrieved = order.getMetadataByType("TEST");
        assertNotNull(retrieved);
        assertEquals("TEST", retrieved.getType());
    }
    
    @Test
    public void testGetMetadataByTypeNotFound() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        
        Metadata retrieved = order.getMetadataByType("UNKNOWN");
        assertNull(retrieved);
    }
    
    @Test
    public void testRemoveMetadataByType() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        Metadata metadata = new TestMetadata();
        
        order.addMetadata(metadata);
        assertEquals(1, order.getMetadataList().size());
        
        boolean removed = order.removeMetadataByType("TEST");
        assertTrue(removed);
        assertEquals(0, order.getMetadataList().size());
    }
    
    @Test
    public void testValidateMetadata() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        
        // Add valid metadata
        Metadata validMetadata = new TestMetadata();
        order.addMetadata(validMetadata);
        
        assertTrue(order.validateMetadata());
    }
    
    @Test
    public void testSetAndGetTotalAmount() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        BigDecimal amount = new BigDecimal("99.99");
        
        order.setTotalAmount(amount);
        
        assertEquals(amount, order.getTotalAmount());
    }
    
    @Test
    public void testSetAndGetStatus() {
        Order order = new Order("ORDER-001", "CUSTOMER-001");
        
        order.setStatus("PENDING");
        
        assertEquals("PENDING", order.getStatus());
    }
    
    // Test helper class
    
    private static class TestMetadata extends BaseMetadata {
        TestMetadata() {
            super("TEST");
        }
    }
}
