package com.shenjiachun.order.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单项值对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 单价
     */
    private BigDecimal price;
    
    /**
     * 小计
     */
    private BigDecimal amount;
    
    /**
     * 计算小计
     */
    public void calculateAmount() {
        if (price != null && quantity != null) {
            this.amount = price.multiply(new BigDecimal(quantity));
        }
    }
}
