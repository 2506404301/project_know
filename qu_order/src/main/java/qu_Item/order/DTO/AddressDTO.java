package qu_Item.order.DTO;

import lombok.Data;

//Data Transfer Object
@Data
public class AddressDTO {

    private String name; //用户姓名;
    private String phone; //用户电话;

    private String AllAddress;//总地址;

}
