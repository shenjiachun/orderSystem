package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.Command;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单命令
 */
@Data
public class CreateOrderCmd extends Command {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemDTO> orderItems;
    
    @NotNull(message = "订单总额不能为空")
    private BigDecimal totalAmount;
    
    private String remark;
}
