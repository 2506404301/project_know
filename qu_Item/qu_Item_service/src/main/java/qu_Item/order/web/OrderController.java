package qu_Item.order.web;


import com.qu.item.unity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.common.vo.PageResult;
import qu_Item.order.service.OrderService;



@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Order>> queryOrderByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){
        return ResponseEntity.ok(orderService.queryOrderByPage(page,rows,sortBy,desc,key));
    }

    /**
     * 根据
     * @param orderId
     * @return
     */
    @GetMapping("orderForm")
    public ResponseEntity<Order> queryOderForForm(@RequestParam("orderId") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderForForm(orderId));
    }

    /**
     * 保存信息的
     * @param order
     * @return
     */
    @PostMapping("save")
    public  ResponseEntity<Void> upDateOrders(@RequestBody Order order){
        orderService.upDateOrders(order);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
