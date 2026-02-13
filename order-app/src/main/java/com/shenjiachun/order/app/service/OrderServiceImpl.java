package com.shenjiachun.order.app.service;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.shenjiachun.order.app.command.CreateOrderCmdExe;
import com.shenjiachun.order.app.command.UpdateOrderStatusCmdExe;
import com.shenjiachun.order.app.query.OrderByIdQryExe;
import com.shenjiachun.order.app.query.OrderListQryExe;
import com.shenjiachun.order.client.api.OrderService;
import com.shenjiachun.order.client.dto.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单应用服务实现
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private CreateOrderCmdExe createOrderCmdExe;
    
    @Resource
    private UpdateOrderStatusCmdExe updateOrderStatusCmdExe;
    
    @Resource
    private OrderByIdQryExe orderByIdQryExe;
    
    @Resource
    private OrderListQryExe orderListQryExe;
    
    @Override
    public SingleResponse<Long> createOrder(CreateOrderCmd cmd) {
        return createOrderCmdExe.execute(cmd);
    }
    
    @Override
    public SingleResponse<OrderDTO> getOrderById(OrderByIdQry qry) {
        return orderByIdQryExe.execute(qry);
    }
    
    @Override
    public Response updateOrderStatus(UpdateOrderStatusCmd cmd) {
        return updateOrderStatusCmdExe.execute(cmd);
    }
    
    @Override
    public MultiResponse<OrderDTO> listOrders(OrderListQry qry) {
        return orderListQryExe.execute(qry);
    }
}
