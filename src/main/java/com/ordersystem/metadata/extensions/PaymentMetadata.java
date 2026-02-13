package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.BaseMetadata;

/**
 * Payment metadata extension for orders.
 * Stores payment-related information.
 */
public class PaymentMetadata extends BaseMetadata {
    
    public static final String TYPE = "PAYMENT";
    
    // Attribute keys
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String PAYMENT_STATUS = "paymentStatus";
    private static final String PAYMENT_TIME = "paymentTime";
    
    public PaymentMetadata() {
        super(TYPE);
    }
    
    public String getPaymentMethod() {
        return (String) getAttribute(PAYMENT_METHOD);
    }
    
    public void setPaymentMethod(String paymentMethod) {
        setAttribute(PAYMENT_METHOD, paymentMethod);
    }
    
    public String getTransactionId() {
        return (String) getAttribute(TRANSACTION_ID);
    }
    
    public void setTransactionId(String transactionId) {
        setAttribute(TRANSACTION_ID, transactionId);
    }
    
    public String getPaymentStatus() {
        return (String) getAttribute(PAYMENT_STATUS);
    }
    
    public void setPaymentStatus(String paymentStatus) {
        setAttribute(PAYMENT_STATUS, paymentStatus);
    }
    
    public String getPaymentTime() {
        return (String) getAttribute(PAYMENT_TIME);
    }
    
    public void setPaymentTime(String paymentTime) {
        setAttribute(PAYMENT_TIME, paymentTime);
    }
    
    @Override
    public boolean validate() {
        // Validate required fields
        return getPaymentMethod() != null && !getPaymentMethod().trim().isEmpty();
    }
}
