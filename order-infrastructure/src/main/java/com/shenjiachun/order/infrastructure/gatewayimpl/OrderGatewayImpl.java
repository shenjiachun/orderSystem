package com.shenjiachun.order.infrastructure.gatewayimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.gateway.OrderGateway;
import com.shenjiachun.order.infrastructure.converter.DataConverter;
import com.shenjiachun.order.infrastructure.dataobject.OrderDO;
import com.shenjiachun.order.infrastructure.dataobject.OrderItemDO;
import com.shenjiachun.order.infrastructure.mapper.OrderItemMapper;
import com.shenjiachun.order.infrastructure.mapper.OrderMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单网关实现
 */
@Component
public class OrderGatewayImpl implements OrderGateway {
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private OrderItemMapper orderItemMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(Order order) {
        // 保存订单
        OrderDO orderDO = DataConverter.toOrderDO(order);
        orderMapper.insert(orderDO);
        
        // 保存订单项
        Long orderId = orderDO.getOrderId();
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            order.getOrderItems().forEach(item -> {
                OrderItemDO itemDO = DataConverter.toOrderItemDO(item, orderId);
                orderItemMapper.insert(itemDO);
            });
        }
        
        return orderId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order order) {
        OrderDO orderDO = DataConverter.toOrderDO(order);
        orderMapper.updateById(orderDO);
    }
    
    @Override
    public Order findById(Long orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId);
        if (orderDO == null) {
            return null;
        }
        
        // 查询订单项
        LambdaQueryWrapper<OrderItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItemDO::getOrderId, orderId);
        List<OrderItemDO> itemDOs = orderItemMapper.selectList(wrapper);
        
        return DataConverter.toOrder(orderDO, itemDOs);
    }
    
    @Override
    public List<Order> findByCondition(Long userId, String orderStatus, int pageNum, int pageSize) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(OrderDO::getUserId, userId);
        }
        if (StringUtils.hasText(orderStatus)) {
            wrapper.eq(OrderDO::getOrderStatus, orderStatus);
        }
        
        wrapper.orderByDesc(OrderDO::getCreateTime);
        
        Page<OrderDO> page = new Page<>(pageNum, pageSize);
        IPage<OrderDO> orderPage = orderMapper.selectPage(page, wrapper);
        
        return orderPage.getRecords().stream()
                .map(orderDO -> {
                    LambdaQueryWrapper<OrderItemDO> itemWrapper = new LambdaQueryWrapper<>();
                    itemWrapper.eq(OrderItemDO::getOrderId, orderDO.getOrderId());
                    List<OrderItemDO> itemDOs = orderItemMapper.selectList(itemWrapper);
                    return DataConverter.toOrder(orderDO, itemDOs);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public long count(Long userId, String orderStatus) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(OrderDO::getUserId, userId);
        }
        if (StringUtils.hasText(orderStatus)) {
            wrapper.eq(OrderDO::getOrderStatus, orderStatus);
        }
        
        return orderMapper.selectCount(wrapper);
    }
}
