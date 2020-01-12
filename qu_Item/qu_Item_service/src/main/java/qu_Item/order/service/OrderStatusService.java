package qu_Item.order.service;

import com.qu.item.unity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;

import qu_Item.order.mapper.OrderStatusMapper;

@Service
public class OrderStatusService {

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    public Long queryStatus(Long orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        Long status = orderStatusMapper.selectOne(orderStatus).getStatus();
        if (status == null){
            throw new quException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        return status;
    }

    public OrderStatus queryOrderStatusForForm(Long orderId) {
        OrderStatus orderStatus1 = orderStatusMapper.selectByPrimaryKey(orderId);
        if (orderStatus1 == null){
            throw new quException(ExceptionEnum.ORDER_STATUS_FORM_NOT_FOUND);
        }
        return orderStatus1;
    }

    public void updateStatus(OrderStatus orderStatus) {
        if (orderStatus.getOrderId() == null){
            throw new quException(ExceptionEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        int i = orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
        if (i!=1){
            throw new quException(ExceptionEnum.ORDER_STATUS_UPDATE_ERROR);
        }
    }
}
