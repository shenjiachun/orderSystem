package com.ordersystem.metadata;

import com.ordersystem.metadata.extensions.*;

import java.math.BigDecimal;

/**
 * Demo application showing how to use the extensible order metadata system
 */
public class OrderMetadataDemo {
    
    public static void main(String[] args) {
        // Initialize the metadata registry with available extensions
        MetadataRegistry registry = MetadataRegistry.getInstance();
        registry.registerFactory(new ShippingMetadataFactory());
        registry.registerFactory(new PaymentMetadataFactory());
        registry.registerFactory(new CustomFieldsMetadataFactory());
        
        // Create an order
        Order order = new Order("ORDER-2026-001", "CUST-12345");
        order.setTotalAmount(new BigDecimal("299.99"));
        order.setStatus("PENDING");
        
        System.out.println("=== Order Metadata System Demo ===\n");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer ID: " + order.getCustomerId());
        System.out.println("Total Amount: $" + order.getTotalAmount());
        System.out.println("Status: " + order.getStatus());
        System.out.println();
        
        // Add shipping metadata
        ShippingMetadata shipping = (ShippingMetadata) registry.createMetadata("SHIPPING");
        shipping.setAddress("123 Main Street, Beijing, China");
        shipping.setCarrier("SF Express");
        shipping.setTrackingNumber("SF-2026-0213-001");
        shipping.setEstimatedDelivery("2026-02-15");
        order.addMetadata(shipping);
        
        System.out.println("Shipping Metadata Added:");
        System.out.println("  Address: " + shipping.getAddress());
        System.out.println("  Carrier: " + shipping.getCarrier());
        System.out.println("  Tracking: " + shipping.getTrackingNumber());
        System.out.println("  Estimated Delivery: " + shipping.getEstimatedDelivery());
        System.out.println();
        
        // Add payment metadata
        PaymentMetadata payment = (PaymentMetadata) registry.createMetadata("PAYMENT");
        payment.setPaymentMethod("Alipay");
        payment.setTransactionId("ALIPAY-TXN-98765");
        payment.setPaymentStatus("COMPLETED");
        payment.setPaymentTime("2026-02-13T07:18:39Z");
        order.addMetadata(payment);
        
        System.out.println("Payment Metadata Added:");
        System.out.println("  Method: " + payment.getPaymentMethod());
        System.out.println("  Transaction ID: " + payment.getTransactionId());
        System.out.println("  Status: " + payment.getPaymentStatus());
        System.out.println("  Payment Time: " + payment.getPaymentTime());
        System.out.println();
        
        // Add custom fields metadata
        CustomFieldsMetadata customFields = (CustomFieldsMetadata) registry.createMetadata("CUSTOM_FIELDS");
        customFields.addField("promotion", "SPRING2026");
        customFields.addField("giftWrapping", true);
        customFields.addField("specialInstructions", "请在工作日配送");
        customFields.addField("customerLevel", "VIP");
        order.addMetadata(customFields);
        
        System.out.println("Custom Fields Metadata Added:");
        System.out.println("  Promotion: " + customFields.getField("promotion"));
        System.out.println("  Gift Wrapping: " + customFields.getField("giftWrapping"));
        System.out.println("  Special Instructions: " + customFields.getField("specialInstructions"));
        System.out.println("  Customer Level: " + customFields.getField("customerLevel"));
        System.out.println();
        
        // Validate all metadata
        boolean isValid = order.validateMetadata();
        System.out.println("All Metadata Valid: " + isValid);
        System.out.println();
        
        // Retrieve specific metadata
        System.out.println("Retrieving Specific Metadata:");
        ShippingMetadata retrievedShipping = (ShippingMetadata) order.getMetadataByType("SHIPPING");
        if (retrievedShipping != null) {
            System.out.println("  Shipping carrier: " + retrievedShipping.getCarrier());
        }
        
        PaymentMetadata retrievedPayment = (PaymentMetadata) order.getMetadataByType("PAYMENT");
        if (retrievedPayment != null) {
            System.out.println("  Payment method: " + retrievedPayment.getPaymentMethod());
        }
        System.out.println();
        
        // Show all metadata
        System.out.println("Total Metadata Attached: " + order.getMetadataList().size());
        for (Metadata metadata : order.getMetadataList()) {
            System.out.println("  - " + metadata.getType());
        }
        
        System.out.println("\n=== Demo Complete ===");
    }
}
