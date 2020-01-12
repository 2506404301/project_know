package qu_Item.order.service;

import com.qu.item.unity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.order.mapper.OrderDetailMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {

    @Autowired

    private OrderDetailMapper orderDetailMapper;

    public List<Long> queryDetailSkuId(Long orderId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> select = orderDetailMapper.select(orderDetail);
        List<Long> skuId = select.stream().map(OrderDetail::getSkuId).collect(Collectors.toList());
        System.out.println("skuId = " + skuId);
        return skuId;
    }

    public List<OrderDetail> queryOrderDetailForForm(Long orderId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new quException(ExceptionEnum.ORDER_DETAIL_FORM_NOT_FOUND);
        }
        return orderDetails;
    }

    /**
     * 修改商品信息。
     * @param orderDetail
     */
    public void updateDetail(List<OrderDetail> orderDetail) {
        for (OrderDetail detail : orderDetail) {
            if (detail.getId()== null){
                throw new quException(ExceptionEnum.ORDER_DETAIL_UPDATE_ERROR);
            }
            int i = orderDetailMapper.updateByPrimaryKeySelective(detail);
            if (i!=1){
                throw new quException(ExceptionEnum.ORDER_DETAIL_UPDATE_ERROR);
            }
        }

    }
}
