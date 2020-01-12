package com.qu.item.unity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String indexes;//商品特殊规格下标
    private String ownSpec;//商品特殊规格建值树;
    private Boolean enable;//是否有效;
    private Date createTime;//创建时间;
    private Date lastUpdateTime;//最后更新时间;

    @Transient
    private Integer stock;//库存;
}
