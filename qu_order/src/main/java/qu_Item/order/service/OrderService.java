package qu_Item.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.util.IdWorker;
import qu_Item.common.vo.PageResult;
import qu_Item.entry.UserInfo;
import qu_Item.order.DTO.OrderDTO;
import qu_Item.order.Interceptor.UserInterceptor;
import qu_Item.order.euems.OrderEuems;
import qu_Item.order.mapper.*;
import qu_Item.order.unity.Address;
import qu_Item.order.unity.Order;
import qu_Item.order.unity.OrderDetail;
import qu_Item.order.unity.OrderStatus;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private IdWorker idWorker;



    /**
     * 创建订单;
     * @param orderDTO
     * @return
     */
    @Transactional
    public String createdOrder(OrderDTO orderDTO) {
        //1:新增订单;
        Order order = new Order();
        //1.1:订单编号，基本信息
        long nextId = idWorker.nextId();
        //时间
        order.setCreateTime(new Date());
        //编号
        order.setOrderId(nextId);
        //自付方式
        order.setPaymentType(orderDTO.getPaymentType()==1);
        //1.2:用户信息
        UserInfo info = UserInterceptor.getUser();
        //用户id
        order.setUserId(info.getId().toString());
        //用户姓名
        order.setBuyerNick(info.getUsername());
        //卖家名称
        order.setBuyerRate(false);
        //订单金额;
        order.setTotalPay(orderDTO.getTotalPay());
        //实际的金额
        order.setActualPay(orderDTO.getActualPay());
        //无发票;
        order.setInvoiceType(0L);
        //来源 pc端
        order.setSourceType(2L);

        //1.3:收货人地址
        order.setReceiver(orderDTO.getReceiver());
        order.setReceiverAddress(orderDTO.getReceiverAddress());
        order.setReceiverState(orderDTO.getReceiverState());
        order.setReceiverCity(orderDTO.getReceiverCity());
        order.setReceiverDistrict(orderDTO.getReceiverDistrict());
        order.setReceiverMobile(orderDTO.getReceiverMobile());
        order.setReceiverZip(orderDTO.getReceiverZip());

        //1.4;订单详情
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails();

        //1.5：加入数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1){
            log.error("||||订单加入数据库失败||||");
            throw new quException(ExceptionEnum.CREATED_ORDER_ERROR);
        }

        //添加订单Id;
        orderDetails.forEach(orderDetail -> {
            orderDetail.setOrderId(nextId);
        });

        //2:新增订单详情
        count = orderDetailMapper.insertList(orderDetails);
        if (count < 1){
            log.error("||||订单详情加入数据库失败||||");
            throw new quException(ExceptionEnum.CREATED_ORDER_ERROR);
        }

        //3:新增订单状态;
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(nextId);
        orderStatus.setCreateTime(order.getCreateTime());
        //初始状态未未付款：1
        orderStatus.setStatus(OrderEuems.NOT_Pay.value());
        //保存状态数据;
        count = orderStatusMapper.insertSelective(orderStatus);
        if (count != 1){
            log.error("||||订单状态加入数据库失败||||");
            throw new quException(ExceptionEnum.CREATED_ORDER_ERROR);
        }

        //4:减库存;
        //同步调用（调用商品微服务的接口），和异步调用（使用reabbitMQ进行通知调用）
        try {
            orderDetails.forEach(orderDetail ->
                    stockMapper.reduceStock(orderDetail.getSkuId(), orderDetail.getNum()));
        }catch (Exception e){
            throw new quException(ExceptionEnum.STOCK_NOT_ENOUGH);
        }

        System.out.println("nextId = " + nextId);
        return   String.valueOf(nextId);
    }

    /**
     * 查询地址
     * @return
     */
    public List<Address> queryListAddress() {
        //获得用户id;
        Long userId = UserInterceptor.getUser().getId();
        //根据用户id来查出地址信息集合;
        Address address = new Address();
        address.setUserId(userId);
        List<Address> select = addressMapper.select(address);
        if (CollectionUtils.isEmpty(select)) {
            throw new quException(ExceptionEnum.USERT_ADDRESS_ERROR);
        }
        return select;
    }



    /**
     * 新增地址
     * @param address
     */
    @Transactional
    public void addFormAddress(Address address) {
        //获得用户id;
        Long userId = UserInterceptor.getUser().getId();

        address.setUserId(userId);
        address.setZipCode("222222");
        address.setDefaultAddress(true);

        int i = addressMapper.insertSelective(address);
        if ( i!=1){
            throw new quException(ExceptionEnum.USERT_INSERT_ADDRESS_ERROR);
        }
    }

    /**
     * 删除指定信息';
     * @param id
     */
    public void deleteAddress(Long id) {
        int i = addressMapper.deleteByPrimaryKey(id);
        if (i!=1){
            throw new quException(ExceptionEnum.USERT_ADDRESS_DELETE_ERROR);
        }
    }

    /**
     * 通过id来查询地址信息
     * @param id
     * @return
     */
    public Address queryAddressById(Long id) {
        Address address = addressMapper.selectByPrimaryKey(id);
        if (address == null){
            throw new quException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
        return address;
    }

    /**
     * 更新地址信息;
     * @param address
     */
    public void UpdateAddress(Address address) {
        int i = addressMapper.updateByPrimaryKey(address);
        if (i!=1){
            throw new quException(ExceptionEnum.USERT_ADDRESS_UPDATE_ERROR);
        }
    }


    /**
     * 返回订单的列表
     * @param page
     * @param rows
     * @param status
     * @return
     */
    public PageResult<Order> queryOrderList(int page, int rows, String status) {
        OrderDetail orderDetail = new OrderDetail();
        Order t = new Order();
        //1:分页;
        PageHelper.startPage(page, rows);
        //2:查询出来;
        //2.1获得登录状态下的用户id;
        Long userId = UserInterceptor.getUser().getId();
        t.setUserId(userId.toString());
        //2.2查询该用户下的订单
        List<Order> orderList = orderMapper.select(t);
        System.out.println("orderList = " + orderList);
        if (CollectionUtils.isEmpty(orderList)){
            throw new quException(ExceptionEnum.QUERT_ORDER_ERROR);
        }
        //2.3查询对应订单下的订单号;
        List<Long> collect = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        System.out.println("collect = " + collect);
        if (CollectionUtils.isEmpty(collect)){
            throw new quException(ExceptionEnum.QUERT_ORDERID_ERROR);
        }
        List<List<OrderDetail>> lists = new ArrayList<>();
         //2.4 遍历订单号;
        for (Long orderId : collect) {
            orderDetail.setOrderId(orderId);
            List<OrderDetail> select = orderDetailMapper.select(orderDetail);
            if (CollectionUtils.isEmpty(select)) {
                throw new quException(ExceptionEnum.ORDER_DETAIL_FORM_NOT_FOUND);
            }
            orderList.forEach(order -> order.setOrderDetails(select));
            System.out.println("orderDetail = " + select);

        }

        //4:转化为数据列表返回;
        PageInfo<Order> info = new PageInfo<>(orderList);
        return new PageResult<>(info.getTotal(),orderList);
        
    }


}
