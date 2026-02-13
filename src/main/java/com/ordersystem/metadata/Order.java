package com.ordersystem.metadata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Order entity with extensible metadata support.
 * Metadata can be added dynamically to extend order functionality.
 */
public class Order {
    
    private String orderId;
    private String customerId;
    private Date createTime;
    private BigDecimal totalAmount;
    private String status;
    private List<Metadata> metadataList;
    
    public Order() {
        this.metadataList = new ArrayList<>();
        this.createTime = new Date();
    }
    
    public Order(String orderId, String customerId) {
        this();
        this.orderId = orderId;
        this.customerId = customerId;
    }
    
    // Getters and setters
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // Metadata management methods
    
    /**
     * Add metadata to the order
     * @param metadata metadata to add
     */
    public void addMetadata(Metadata metadata) {
        if (metadata != null) {
            metadataList.add(metadata);
        }
    }
    
    /**
     * Get all metadata attached to the order
     * @return list of metadata
     */
    public List<Metadata> getMetadataList() {
        return new ArrayList<>(metadataList);
    }
    
    /**
     * Get metadata by type
     * @param type metadata type
     * @return metadata instance or null if not found
     */
    public Metadata getMetadataByType(String type) {
        for (Metadata metadata : metadataList) {
            if (metadata.getType().equals(type)) {
                return metadata;
            }
        }
        return null;
    }
    
    /**
     * Remove metadata by type
     * @param type metadata type
     * @return true if removed, false if not found
     */
    public boolean removeMetadataByType(String type) {
        return metadataList.removeIf(m -> m.getType().equals(type));
    }
    
    /**
     * Validate all metadata
     * @return true if all metadata is valid, false otherwise
     */
    public boolean validateMetadata() {
        for (Metadata metadata : metadataList) {
            if (!metadata.validate()) {
                return false;
            }
        }
        return true;
    }
}
