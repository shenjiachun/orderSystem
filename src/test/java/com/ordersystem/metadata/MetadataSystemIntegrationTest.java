package com.ordersystem.metadata;

import com.ordersystem.metadata.extensions.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Integration test demonstrating the extensibility of the metadata system
 */
public class MetadataSystemIntegrationTest {
    
    private MetadataRegistry registry;
    
    @Before
    public void setUp() {
        registry = MetadataRegistry.getInstance();
        registry.clear();
        
        // Register all available metadata factories
        registry.registerFactory(new ShippingMetadataFactory());
        registry.registerFactory(new PaymentMetadataFactory());
        registry.registerFactory(new CustomFieldsMetadataFactory());
    }
    
    @Test
    public void testCompleteOrderWorkflow() {
        // Create an order
        Order order = new Order("ORDER-2026-001", "CUSTOMER-001");
        order.setTotalAmount(new BigDecimal("299.99"));
        order.setStatus("PENDING");
        
        // Add shipping metadata
        ShippingMetadata shipping = (ShippingMetadata) registry.createMetadata("SHIPPING");
        shipping.setAddress("123 Main St, Beijing, China");
        shipping.setCarrier("SF Express");
        shipping.setTrackingNumber("SF-123456");
        shipping.setEstimatedDelivery("2026-02-15");
        order.addMetadata(shipping);
        
        // Add payment metadata
        PaymentMetadata payment = (PaymentMetadata) registry.createMetadata("PAYMENT");
        payment.setPaymentMethod("Alipay");
        payment.setTransactionId("TXN-98765");
        payment.setPaymentStatus("COMPLETED");
        order.addMetadata(payment);
        
        // Add custom fields
        CustomFieldsMetadata customFields = (CustomFieldsMetadata) registry.createMetadata("CUSTOM_FIELDS");
        customFields.addField("promotion", "SPRING2026");
        customFields.addField("giftWrapping", true);
        customFields.addField("customerLevel", "VIP");
        order.addMetadata(customFields);
        
        // Validate all metadata
        assertTrue("All metadata should be valid", order.validateMetadata());
        
        // Verify metadata count
        assertEquals("Order should have 3 metadata items", 3, order.getMetadataList().size());
        
        // Retrieve and verify shipping metadata
        ShippingMetadata retrievedShipping = (ShippingMetadata) order.getMetadataByType("SHIPPING");
        assertNotNull("Shipping metadata should exist", retrievedShipping);
        assertEquals("SF Express", retrievedShipping.getCarrier());
        assertEquals("123 Main St, Beijing, China", retrievedShipping.getAddress());
        
        // Retrieve and verify payment metadata
        PaymentMetadata retrievedPayment = (PaymentMetadata) order.getMetadataByType("PAYMENT");
        assertNotNull("Payment metadata should exist", retrievedPayment);
        assertEquals("Alipay", retrievedPayment.getPaymentMethod());
        assertEquals("COMPLETED", retrievedPayment.getPaymentStatus());
        
        // Retrieve and verify custom fields
        CustomFieldsMetadata retrievedCustom = (CustomFieldsMetadata) order.getMetadataByType("CUSTOM_FIELDS");
        assertNotNull("Custom fields metadata should exist", retrievedCustom);
        assertEquals("SPRING2026", retrievedCustom.getField("promotion"));
        assertEquals(true, retrievedCustom.getField("giftWrapping"));
        assertEquals("VIP", retrievedCustom.getField("customerLevel"));
    }
    
    @Test
    public void testDynamicMetadataExtension() {
        // Register a custom metadata type at runtime
        registry.registerFactory(new InventoryMetadataFactory());
        
        // Create an order and add the custom metadata
        Order order = new Order("ORDER-002", "CUSTOMER-002");
        
        InventoryMetadata inventory = (InventoryMetadata) registry.createMetadata("INVENTORY");
        inventory.setWarehouseId("WH-001");
        inventory.setStockLevel(100);
        order.addMetadata(inventory);
        
        // Verify the custom metadata
        InventoryMetadata retrieved = (InventoryMetadata) order.getMetadataByType("INVENTORY");
        assertNotNull("Inventory metadata should exist", retrieved);
        assertEquals("WH-001", retrieved.getWarehouseId());
        assertEquals(100, retrieved.getStockLevel());
    }
    
    @Test
    public void testMetadataRemoval() {
        Order order = new Order("ORDER-003", "CUSTOMER-003");
        
        ShippingMetadata shipping = (ShippingMetadata) registry.createMetadata("SHIPPING");
        shipping.setAddress("Test Address");
        order.addMetadata(shipping);
        
        assertEquals(1, order.getMetadataList().size());
        
        boolean removed = order.removeMetadataByType("SHIPPING");
        assertTrue("Metadata should be removed", removed);
        assertEquals(0, order.getMetadataList().size());
    }
    
    @Test
    public void testInvalidMetadataValidation() {
        Order order = new Order("ORDER-004", "CUSTOMER-004");
        
        // Add invalid shipping metadata (missing address)
        ShippingMetadata shipping = (ShippingMetadata) registry.createMetadata("SHIPPING");
        // Not setting address - should fail validation
        order.addMetadata(shipping);
        
        assertFalse("Metadata validation should fail", order.validateMetadata());
    }
    
    // Custom metadata extension for demonstration
    private static class InventoryMetadata extends BaseMetadata {
        public static final String TYPE = "INVENTORY";
        
        public InventoryMetadata() {
            super(TYPE);
        }
        
        public void setWarehouseId(String warehouseId) {
            setAttribute("warehouseId", warehouseId);
        }
        
        public String getWarehouseId() {
            return (String) getAttribute("warehouseId");
        }
        
        public void setStockLevel(int stockLevel) {
            setAttribute("stockLevel", stockLevel);
        }
        
        public int getStockLevel() {
            Integer level = (Integer) getAttribute("stockLevel");
            return level != null ? level : 0;
        }
        
        @Override
        public boolean validate() {
            return getWarehouseId() != null && getStockLevel() >= 0;
        }
    }
    
    private static class InventoryMetadataFactory implements MetadataFactory {
        @Override
        public String getType() {
            return InventoryMetadata.TYPE;
        }
        
        @Override
        public Metadata create() {
            return new InventoryMetadata();
        }
    }
}
