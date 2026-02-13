package com.shenjiachun.order.domain.gateway;

import com.shenjiachun.order.domain.entity.Order;

import java.util.List;

/**
 * 订单网关接口
 */
public interface OrderGateway {
    
    /**
     * 保存订单
     */
    Long save(Order order);
    
    /**
     * 更新订单
     */
    void update(Order order);
    
    /**
     * 根据ID查询订单
     */
    Order findById(Long orderId);
    
    /**
     * 查询订单列表
     */
    List<Order> findByCondition(Long userId, String orderStatus, int pageNum, int pageSize);
    
    /**
     * 统计订单数量
     */
    long count(Long userId, String orderStatus);
}
