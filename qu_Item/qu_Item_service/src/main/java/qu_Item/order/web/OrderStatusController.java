package qu_Item.order.web;

import com.qu.item.unity.Order;
import com.qu.item.unity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.order.service.OrderStatusService;


@RestController
@RequestMapping("orderStatus")
public class OrderStatusController {
    @Autowired
    private OrderStatusService orderStatusService;

    @GetMapping("statusForm")
    public ResponseEntity<OrderStatus> queryOderStatusForForm(@RequestParam("orderId") Long orderId){
        return ResponseEntity.ok(orderStatusService.queryOrderStatusForForm(orderId));
    }

    @PostMapping("save")
    public ResponseEntity<Void> updateStatus(@RequestBody OrderStatus orderStatus){
        orderStatusService.updateStatus(orderStatus);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
