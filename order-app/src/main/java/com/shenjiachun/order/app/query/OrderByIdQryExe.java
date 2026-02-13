package com.shenjiachun.order.app.query;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.shenjiachun.order.app.convertor.OrderConvertor;
import com.shenjiachun.order.client.dto.OrderByIdQry;
import com.shenjiachun.order.client.dto.OrderDTO;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.gateway.OrderGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 根据ID查询订单执行器
 */
@Component
public class OrderByIdQryExe {
    
    @Resource
    private OrderGateway orderGateway;
    
    @CatchAndLog
    public SingleResponse<OrderDTO> execute(OrderByIdQry qry) {
        Order order = orderGateway.findById(qry.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }
        
        OrderDTO orderDTO = OrderConvertor.toDTO(order);
        return SingleResponse.of(orderDTO);
    }
}
