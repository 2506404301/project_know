package com.qu.item.unity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_stock")
public class Stock {

    @Id
    private Long skuId;
    private Integer stock;//可用的库存;


}
