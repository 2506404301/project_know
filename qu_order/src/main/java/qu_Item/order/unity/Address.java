package qu_Item.order.unity;


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
    private Long userId; //用户id;
    private String name; //用户姓名;
    private String phone; //用户电话;
    private String zipCode; //用户编码;
    private String state; //省;
    private String city; //市;
    private String district; //区
    private String address; //详细地址
    private Boolean defaultAddress; //默认编号
    private String label; //标签;
}
