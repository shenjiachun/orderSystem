package com.shenjiachun.order.client.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.shenjiachun.order.client.dto.*;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    SingleResponse<Long> createOrder(CreateOrderCmd cmd);
    
    /**
     * 根据ID查询订单
     */
    SingleResponse<OrderDTO> getOrderById(OrderByIdQry qry);
    
    /**
     * 更新订单状态
     */
    Response updateOrderStatus(UpdateOrderStatusCmd cmd);
    
    /**
     * 查询订单列表
     */
    MultiResponse<OrderDTO> listOrders(OrderListQry qry);
}
