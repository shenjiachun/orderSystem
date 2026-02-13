package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.Command;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新订单状态命令
 */
@Data
public class UpdateOrderStatusCmd extends Command {
    
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "订单状态不能为空")
    private String orderStatus;
}
