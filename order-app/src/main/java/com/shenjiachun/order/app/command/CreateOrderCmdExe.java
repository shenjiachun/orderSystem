package com.shenjiachun.order.app.command;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.SingleResponse;
import com.shenjiachun.order.app.convertor.OrderConvertor;
import com.shenjiachun.order.client.dto.CreateOrderCmd;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.gateway.OrderGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建订单命令执行器
 */
@Component
public class CreateOrderCmdExe {
    
    @Resource
    private OrderGateway orderGateway;
    
    @CatchAndLog
    public SingleResponse<Long> execute(CreateOrderCmd cmd) {
        // 转换为领域对象
        Order order = new Order();
        order.setUserId(cmd.getUserId());
        order.setOrderItems(cmd.getOrderItems().stream()
                .map(OrderConvertor::toOrderItem)
                .collect(java.util.stream.Collectors.toList()));
        order.setRemark(cmd.getRemark());
        
        // 创建订单
        order.create();
        
        // 保存订单
        Long orderId = orderGateway.save(order);
        
        return SingleResponse.of(orderId);
    }
}
