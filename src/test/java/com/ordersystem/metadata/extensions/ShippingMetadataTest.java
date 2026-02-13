package com.ordersystem.metadata.extensions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for ShippingMetadata
 */
public class ShippingMetadataTest {
    
    @Test
    public void testCreateShippingMetadata() {
        ShippingMetadata metadata = new ShippingMetadata();
        
        assertEquals("SHIPPING", metadata.getType());
    }
    
    @Test
    public void testSetAndGetAddress() {
        ShippingMetadata metadata = new ShippingMetadata();
        metadata.setAddress("123 Main St, City, Country");
        
        assertEquals("123 Main St, City, Country", metadata.getAddress());
    }
    
    @Test
    public void testSetAndGetCarrier() {
        ShippingMetadata metadata = new ShippingMetadata();
        metadata.setCarrier("FedEx");
        
        assertEquals("FedEx", metadata.getCarrier());
    }
    
    @Test
    public void testSetAndGetTrackingNumber() {
        ShippingMetadata metadata = new ShippingMetadata();
        metadata.setTrackingNumber("TRACK-12345");
        
        assertEquals("TRACK-12345", metadata.getTrackingNumber());
    }
    
    @Test
    public void testValidateWithAddress() {
        ShippingMetadata metadata = new ShippingMetadata();
        metadata.setAddress("123 Main St");
        
        assertTrue(metadata.validate());
    }
    
    @Test
    public void testValidateWithoutAddress() {
        ShippingMetadata metadata = new ShippingMetadata();
        
        assertFalse(metadata.validate());
    }
    
    @Test
    public void testValidateWithEmptyAddress() {
        ShippingMetadata metadata = new ShippingMetadata();
        metadata.setAddress("   ");
        
        assertFalse(metadata.validate());
    }
}
