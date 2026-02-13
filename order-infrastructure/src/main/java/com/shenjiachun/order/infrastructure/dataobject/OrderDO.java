package com.shenjiachun.order.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据对象
 */
@Data
@TableName("orders")
public class OrderDO {
    
    @TableId(type = IdType.AUTO)
    private Long orderId;
    
    private Long userId;
    
    private BigDecimal totalAmount;
    
    private String orderStatus;
    
    private String remark;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}
