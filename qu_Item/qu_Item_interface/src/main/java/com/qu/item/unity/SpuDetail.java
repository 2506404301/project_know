package com.qu.item.unity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;
    private String description;//商品描述;
    private String specifications;//商品特殊规格名称及可选模板;
    private String specTemplate;//商品全局的规格属性;
    private String packingList;//包装清单;
    private String afterService;//售后服务;


}
