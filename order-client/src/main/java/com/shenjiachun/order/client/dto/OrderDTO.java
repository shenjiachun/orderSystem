package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.DTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDTO extends DTO {
    
    private Long orderId;
    
    private Long userId;
    
    private List<OrderItemDTO> orderItems;
    
    private BigDecimal totalAmount;
    
    private String orderStatus;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
