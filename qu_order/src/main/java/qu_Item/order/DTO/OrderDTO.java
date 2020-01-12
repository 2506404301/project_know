package qu_Item.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qu_Item.order.unity.OrderDetail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @NotNull
    private Long addressId; //收获的id;

    @NotNull
    private Long actualPay; //实际的价格；
    @NotNull
    private Long totalPay;//总价格;
    @NotNull
    private String receiverZip;//编号;
    @NotNull
    private String receiverState;//收货地址（省）
    @NotNull
    private String receiverDistrict;//收获地址（区/县）
    @NotNull
    private String receiverAddress;//收货人地址1
    @NotNull
    private String receiverCity;//收货地址（市）
    @NotNull
    private String receiverMobile;//收货人手机
    @NotNull
    private String receiver;//收货人1

    private Long postFee;

    @NotNull
    private Integer paymentType;//字符方式;

    @NotNull
    private List<OrderDetail> orderDetails;//订单详情;


}
