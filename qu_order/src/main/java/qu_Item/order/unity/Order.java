package qu_Item.order.unity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_order")
public class Order {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;//订单id1
    private Long totalPay;//总金额，单位：分
    private Long actualPay;//实付金额，单位：分
    private String promotionIds;//促销订单id
    private Boolean paymentType;//支付类型
    private Long postFee = 0L;//邮费
    private Date createTime;//订单创建时间
    private String shippingName;//物流名称
    private String shippingCode;//物流单号
    private String userId;//用户id1
    private String buyerMessage;//买家留言
    private String buyerNick;//买家昵称
    private Boolean buyerRate;//买家是否已评价
    private String receiverState;//收货地址（省）
    private String receiverCity;//收货地址（市）
    private String receiverDistrict;//收获地址（区/县）
    private String receiverAddress;//收货人地址1
    private String receiverMobile;//收货人手机
    private String receiverZip;//收货人邮编
    private String receiver;//收货人1
    private Long invoiceType;//发票类型
    private Long sourceType;//订单来源

    @Transient
    private Long skuId;

    @Transient
    private Long status;

    @Transient
    private OrderStatus orderStatus;

    @Transient
    private List<OrderStatus> orderStatusList;

    @Transient
    private OrderDetail orderDetail;

    @Transient
    private List<OrderDetail> orderDetails;

}
