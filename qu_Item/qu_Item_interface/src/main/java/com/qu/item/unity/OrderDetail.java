package com.qu.item.unity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_order_detail")
public class OrderDetail {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id; //id
    private Long orderId; //订单id
    private Long skuId; //sku的id;
    private Long num; //订单数量;
    private String title; //订单的标题;
    private String ownSpec; //规格参数;
    private Long price; //价格;
    private String image; //图片;
}
