package com.qu.item.unity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Table;

@Data
@Table(name = "tb_category_brand")
public class Category_Brand {

    private Long categoryId;
    @KeySql(useGeneratedKeys = true)
    private Long brandId;

}
