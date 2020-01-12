package qu_Item.order.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.common.vo.PageResult;
import qu_Item.order.DTO.OrderDTO;
import qu_Item.order.service.OrderService;
import qu_Item.order.unity.Address;
import qu_Item.order.unity.Order;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询我的地址;
     * @return
     */
    @GetMapping("address")
    public ResponseEntity<List<Address>> queryListAddress(){
        return ResponseEntity.ok(orderService.queryListAddress());
    }

    /**
     * 创建地址;
     * @param address
     * @return
     */
    @PostMapping("addForm")
    public ResponseEntity<Void> addFormAddress(@RequestBody  Address address){
        orderService.addFormAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除地址信息;
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> DeleteAddress(@PathVariable("id") Long id) {
        orderService.deleteAddress(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 通过id来查询地址信息;
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Address> queryAddressById(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.queryAddressById(id));
    }

    /**
     * 更新地址信息;
     * @param address
     * @return
     */
    @PutMapping("addForm")
    public ResponseEntity<Void> UpdateAddress(@RequestBody Address address){
        orderService.UpdateAddress(address);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * auto
     * 创建订单;
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<String> createdOrder(@RequestBody OrderDTO orderDTO){
        return  ResponseEntity.ok(orderService.createdOrder(orderDTO));
    }


    /**
     * 查询订单列表
     * @param page
     * @param rows
     * @param status
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<Order>>  queryOrderList(@RequestParam("page") int page,
                                                           @RequestParam("rows") int rows,
                                                           @RequestParam(value = "status" ,required = false)String status){
        return ResponseEntity.ok(orderService.queryOrderList(page, rows, status));
    }

}
