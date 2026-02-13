package com.shenjiachun.order.infrastructure.converter;

import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.valueobject.OrderItem;
import com.shenjiachun.order.domain.valueobject.OrderStatus;
import com.shenjiachun.order.infrastructure.dataobject.OrderDO;
import com.shenjiachun.order.infrastructure.dataobject.OrderItemDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据转换器
 */
public class DataConverter {
    
    public static OrderDO toOrderDO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId(order.getOrderId());
        orderDO.setUserId(order.getUserId());
        orderDO.setTotalAmount(order.getTotalAmount());
        orderDO.setOrderStatus(order.getOrderStatus() != null ? order.getOrderStatus().name() : null);
        orderDO.setRemark(order.getRemark());
        orderDO.setCreateTime(order.getCreateTime());
        orderDO.setUpdateTime(order.getUpdateTime());
        return orderDO;
    }
    
    public static Order toOrder(OrderDO orderDO, List<OrderItemDO> itemDOs) {
        if (orderDO == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderId(orderDO.getOrderId());
        order.setUserId(orderDO.getUserId());
        order.setTotalAmount(orderDO.getTotalAmount());
        order.setOrderStatus(orderDO.getOrderStatus() != null ? OrderStatus.valueOf(orderDO.getOrderStatus()) : null);
        order.setRemark(orderDO.getRemark());
        order.setCreateTime(orderDO.getCreateTime());
        order.setUpdateTime(orderDO.getUpdateTime());
        
        if (itemDOs != null) {
            List<OrderItem> items = itemDOs.stream()
                    .map(DataConverter::toOrderItem)
                    .collect(Collectors.toList());
            order.setOrderItems(items);
        }
        
        return order;
    }
    
    public static OrderItemDO toOrderItemDO(OrderItem item, Long orderId) {
        if (item == null) {
            return null;
        }
        OrderItemDO itemDO = new OrderItemDO();
        itemDO.setOrderId(orderId);
        itemDO.setProductId(item.getProductId());
        itemDO.setProductName(item.getProductName());
        itemDO.setQuantity(item.getQuantity());
        itemDO.setPrice(item.getPrice());
        itemDO.setAmount(item.getAmount());
        return itemDO;
    }
    
    public static OrderItem toOrderItem(OrderItemDO itemDO) {
        if (itemDO == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        item.setProductId(itemDO.getProductId());
        item.setProductName(itemDO.getProductName());
        item.setQuantity(itemDO.getQuantity());
        item.setPrice(itemDO.getPrice());
        item.setAmount(itemDO.getAmount());
        return item;
    }
}
