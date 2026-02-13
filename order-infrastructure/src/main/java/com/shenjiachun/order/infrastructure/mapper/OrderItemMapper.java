package com.shenjiachun.order.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shenjiachun.order.infrastructure.dataobject.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {
}
