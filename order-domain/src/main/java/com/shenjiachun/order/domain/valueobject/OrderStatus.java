package com.shenjiachun.order.domain.valueobject;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    
    /**
     * 已创建
     */
    CREATED("已创建"),
    
    /**
     * 已支付
     */
    PAID("已支付"),
    
    /**
     * 已发货
     */
    SHIPPED("已发货"),
    
    /**
     * 已完成
     */
    COMPLETED("已完成"),
    
    /**
     * 已取消
     */
    CANCELLED("已取消");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equals(status)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + status);
    }
}
