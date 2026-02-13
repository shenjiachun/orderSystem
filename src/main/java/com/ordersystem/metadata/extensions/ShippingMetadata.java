package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.BaseMetadata;

/**
 * Shipping metadata extension for orders.
 * Demonstrates how to extend the metadata system with custom attributes.
 */
public class ShippingMetadata extends BaseMetadata {
    
    public static final String TYPE = "SHIPPING";
    
    // Attribute keys
    private static final String ADDRESS = "address";
    private static final String CARRIER = "carrier";
    private static final String TRACKING_NUMBER = "trackingNumber";
    private static final String ESTIMATED_DELIVERY = "estimatedDelivery";
    
    public ShippingMetadata() {
        super(TYPE);
    }
    
    public String getAddress() {
        return (String) getAttribute(ADDRESS);
    }
    
    public void setAddress(String address) {
        setAttribute(ADDRESS, address);
    }
    
    public String getCarrier() {
        return (String) getAttribute(CARRIER);
    }
    
    public void setCarrier(String carrier) {
        setAttribute(CARRIER, carrier);
    }
    
    public String getTrackingNumber() {
        return (String) getAttribute(TRACKING_NUMBER);
    }
    
    public void setTrackingNumber(String trackingNumber) {
        setAttribute(TRACKING_NUMBER, trackingNumber);
    }
    
    public String getEstimatedDelivery() {
        return (String) getAttribute(ESTIMATED_DELIVERY);
    }
    
    public void setEstimatedDelivery(String estimatedDelivery) {
        setAttribute(ESTIMATED_DELIVERY, estimatedDelivery);
    }
    
    @Override
    public boolean validate() {
        // Validate required fields
        return getAddress() != null && !getAddress().trim().isEmpty();
    }
}
