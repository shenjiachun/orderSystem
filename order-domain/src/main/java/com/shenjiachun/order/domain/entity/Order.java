package com.shenjiachun.order.domain.entity;

import com.shenjiachun.order.domain.valueobject.OrderItem;
import com.shenjiachun.order.domain.valueobject.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体
 */
@Data
public class Order {
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单项列表
     */
    private List<OrderItem> orderItems;
    
    /**
     * 订单总额
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态
     */
    private OrderStatus orderStatus;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建订单
     */
    public void create() {
        this.orderStatus = OrderStatus.CREATED;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        calculateTotalAmount();
    }
    
    /**
     * 更新状态
     */
    public void updateStatus(OrderStatus newStatus) {
        validateStatusTransition(newStatus);
        this.orderStatus = newStatus;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 计算订单总额
     */
    private void calculateTotalAmount() {
        if (orderItems != null && !orderItems.isEmpty()) {
            this.totalAmount = orderItems.stream()
                    .map(OrderItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
    
    /**
     * 验证状态转换是否合法
     */
    private void validateStatusTransition(OrderStatus newStatus) {
        if (this.orderStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("已取消的订单不能修改状态");
        }
        if (this.orderStatus == OrderStatus.COMPLETED) {
            throw new IllegalStateException("已完成的订单不能修改状态");
        }
    }
}
