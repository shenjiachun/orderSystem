package com.shenjiachun.order.app.convertor;

import com.shenjiachun.order.client.dto.OrderDTO;
import com.shenjiachun.order.client.dto.OrderItemDTO;
import com.shenjiachun.order.domain.entity.Order;
import com.shenjiachun.order.domain.valueobject.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单转换器
 */
public class OrderConvertor {
    
    public static Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setUserId(dto.getUserId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setRemark(dto.getRemark());
        order.setCreateTime(dto.getCreateTime());
        order.setUpdateTime(dto.getUpdateTime());
        
        if (dto.getOrderItems() != null) {
            List<OrderItem> items = dto.getOrderItems().stream()
                    .map(OrderConvertor::toOrderItem)
                    .collect(Collectors.toList());
            order.setOrderItems(items);
        }
        
        return order;
    }
    
    public static OrderDTO toDTO(Order entity) {
        if (entity == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(entity.getOrderId());
        dto.setUserId(entity.getUserId());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setOrderStatus(entity.getOrderStatus() != null ? entity.getOrderStatus().name() : null);
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        
        if (entity.getOrderItems() != null) {
            List<OrderItemDTO> items = entity.getOrderItems().stream()
                    .map(OrderConvertor::toOrderItemDTO)
                    .collect(Collectors.toList());
            dto.setOrderItems(items);
        }
        
        return dto;
    }
    
    public static OrderItem toOrderItem(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        item.setProductId(dto.getProductId());
        item.setProductName(dto.getProductName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        item.setAmount(dto.getAmount());
        item.calculateAmount();
        return item;
    }
    
    public static OrderItemDTO toOrderItemDTO(OrderItem entity) {
        if (entity == null) {
            return null;
        }
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setAmount(entity.getAmount());
        return dto;
    }
}
