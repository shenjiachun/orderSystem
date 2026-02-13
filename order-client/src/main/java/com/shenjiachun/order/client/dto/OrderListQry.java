package com.shenjiachun.order.client.dto;

import com.alibaba.cola.dto.PageQuery;
import lombok.Data;

/**
 * 订单列表查询
 */
@Data
public class OrderListQry extends PageQuery {
    
    private Long userId;
    
    private String orderStatus;
}
