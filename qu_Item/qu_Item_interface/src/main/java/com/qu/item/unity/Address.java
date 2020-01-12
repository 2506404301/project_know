package com.qu.item.unity;


import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_address")
public class Address {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long userId; //user_id userId
    private String name;
    private String phone;
    private String zipCode;
    private String state;
    private String city;
    private String district;
    private String address;
    private Boolean defaultAddress;
    private String label;
}
