package qu_Item.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.Order;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import qu_Item.order.mapper.OrderMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailService detailService;

    @Autowired
    private OrderStatusService orderStatusService;

    /**
     * 分页查询Order
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public PageResult<Order> queryOrderByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索订单号过滤
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("orderId","%"+key+"%");
        }
        //排序
        example.setOrderByClause("create_time DESC");
        //查询
        List<Order> orders = orderMapper.selectByExample(example);
        System.out.println("orders = " + orders);
        if (CollectionUtils.isEmpty(orders)){
            throw new quException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //解析订单商品和订单状态
        loadOrderSkuIdAndOrderStatus(orders);
        //返回结果
        PageInfo<Order> orderPageInfo = new PageInfo<>(orders);
        return new PageResult<>(orderPageInfo.getTotal(),orders);
    }

    /**
     * 解析订单商品号和订单状态；
     * @param orders
     */

    private void loadOrderSkuIdAndOrderStatus(List<Order> orders) {
        for (Order order : orders) {
            //1164825274324488192
            System.out.println("order.getOrderId() = " + order.getOrderId());
            order.setSkuId(detailService.queryDetailSkuId(order.getOrderId()));
            order.setStatus(orderStatusService.queryStatus(order.getOrderId()));
        }
    }

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    public Order queryOrderForForm(Long orderId) {
        Order orders = orderMapper.selectByPrimaryKey(orderId);
        if (orders == null){
            throw new quException(ExceptionEnum.ORDER_FORM_NOT_FOUND);
        }
        return orders;
    }

    /**
     * 修改订单状态;
     * @param order
     */
    public void upDateOrders(Order order) {
        if (order.getOrderId() == null){
            throw new quException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        int i = orderMapper.updateByPrimaryKeySelective(order);
        if (i!=1){
            throw new quException(ExceptionEnum.ORDER_UPDATE_ERROR);
        }
    }
}
