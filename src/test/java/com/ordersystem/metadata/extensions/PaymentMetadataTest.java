package com.ordersystem.metadata.extensions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for PaymentMetadata
 */
public class PaymentMetadataTest {
    
    @Test
    public void testCreatePaymentMetadata() {
        PaymentMetadata metadata = new PaymentMetadata();
        
        assertEquals("PAYMENT", metadata.getType());
    }
    
    @Test
    public void testSetAndGetPaymentMethod() {
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.setPaymentMethod("Credit Card");
        
        assertEquals("Credit Card", metadata.getPaymentMethod());
    }
    
    @Test
    public void testSetAndGetTransactionId() {
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.setTransactionId("TXN-98765");
        
        assertEquals("TXN-98765", metadata.getTransactionId());
    }
    
    @Test
    public void testSetAndGetPaymentStatus() {
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.setPaymentStatus("COMPLETED");
        
        assertEquals("COMPLETED", metadata.getPaymentStatus());
    }
    
    @Test
    public void testValidateWithPaymentMethod() {
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.setPaymentMethod("PayPal");
        
        assertTrue(metadata.validate());
    }
    
    @Test
    public void testValidateWithoutPaymentMethod() {
        PaymentMetadata metadata = new PaymentMetadata();
        
        assertFalse(metadata.validate());
    }
}
