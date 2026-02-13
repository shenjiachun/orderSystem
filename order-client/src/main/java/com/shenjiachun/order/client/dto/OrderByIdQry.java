package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.Query;
import lombok.Data;

/**
 * 根据ID查询订单
 */
@Data
public class OrderByIdQry extends Query {
    
    private Long orderId;
}
