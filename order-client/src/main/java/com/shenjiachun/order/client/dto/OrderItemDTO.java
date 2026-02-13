package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.DTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单项数据传输对象
 */
@Data
public class OrderItemDTO extends DTO {
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    private String productName;
    
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    
    @NotNull(message = "单价不能为空")
    private BigDecimal price;
    
    private BigDecimal amount;
}
