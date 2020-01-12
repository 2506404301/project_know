package com.qu.item.unity;


import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_admin")
public class Admin {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String adminName;
    private String password;
}
