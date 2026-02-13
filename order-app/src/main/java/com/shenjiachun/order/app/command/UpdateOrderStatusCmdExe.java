package com.shenjiachun.order.app.command;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.shenjiachun.order.client.dto.UpdateOrderStatusCmd;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.gateway.OrderGateway;
import com.shenjiachun.order.domain.valueobject.OrderStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 更新订单状态命令执行器
 */
@Component
public class UpdateOrderStatusCmdExe {
    
    @Resource
    private OrderGateway orderGateway;
    
    @CatchAndLog
    public Response execute(UpdateOrderStatusCmd cmd) {
        // 查询订单
        Order order = orderGateway.findById(cmd.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }
        
        // 更新状态
        OrderStatus newStatus = OrderStatus.fromString(cmd.getOrderStatus());
        order.updateStatus(newStatus);
        
        // 保存
        orderGateway.update(order);
        
        return Response.buildSuccess();
    }
}
