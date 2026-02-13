package com.shenjiachun.order.app.query;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.MultiResponse;
import com.shenjiachun.order.app.convertor.OrderConvertor;
import com.shenjiachun.order.client.dto.OrderDTO;
import com.shenjiachun.order.client.dto.OrderListQry;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.gateway.OrderGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单列表查询执行器
 */
@Component
public class OrderListQryExe {
    
    @Resource
    private OrderGateway orderGateway;
    
    @CatchAndLog
    public MultiResponse<OrderDTO> execute(OrderListQry qry) {
        int pageNum = qry.getPageIndex();
        int pageSize = qry.getPageSize();
        
        List<Order> orders = orderGateway.findByCondition(
                qry.getUserId(), 
                qry.getOrderStatus(), 
                pageNum, 
                pageSize
        );
        
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderConvertor::toDTO)
                .collect(Collectors.toList());
        
        return MultiResponse.of(orderDTOs);
    }
}
