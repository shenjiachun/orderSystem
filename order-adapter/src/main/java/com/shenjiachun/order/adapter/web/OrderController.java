package com.shenjiachun.order.adapter.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.shenjiachun.order.client.api.OrderService;
import com.shenjiachun.order.client.dto.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Resource
    private OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public SingleResponse<Long> createOrder(@Validated @RequestBody CreateOrderCmd cmd) {
        return orderService.createOrder(cmd);
    }
    
    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public SingleResponse<OrderDTO> getOrderById(@PathVariable("id") Long id) {
        OrderByIdQry qry = new OrderByIdQry();
        qry.setOrderId(id);
        return orderService.getOrderById(qry);
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public Response updateOrderStatus(@PathVariable("id") Long id, 
                                      @RequestParam("status") String status) {
        UpdateOrderStatusCmd cmd = new UpdateOrderStatusCmd();
        cmd.setOrderId(id);
        cmd.setOrderStatus(status);
        return orderService.updateOrderStatus(cmd);
    }
    
    /**
     * 查询订单列表
     */
    @GetMapping
    public MultiResponse<OrderDTO> listOrders(@RequestParam(value = "userId", required = false) Long userId,
                                              @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                              @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        OrderListQry qry = new OrderListQry();
        qry.setUserId(userId);
        qry.setOrderStatus(orderStatus);
        qry.setPageIndex(pageIndex);
        qry.setPageSize(pageSize);
        return orderService.listOrders(qry);
    }
}
