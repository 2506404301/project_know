package qu_Item.order.web;


import com.qu.item.unity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.order.service.OrderDetailService;

import java.util.List;


@RestController
@RequestMapping("orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 查询详情
     * @param orderId
     * @return
     */
    @GetMapping("detailForm")
    public ResponseEntity<List<OrderDetail>> queryOderDetailForForm(@RequestParam("orderId") Long orderId){
        return ResponseEntity.ok(orderDetailService.queryOrderDetailForForm(orderId));
    }

    /**
     * 保存商品
     * @param orderDetail
     * @return
     */
    @PostMapping("save")
    public ResponseEntity<Void> updateDetail(@RequestBody List<OrderDetail> orderDetail){
        orderDetailService.updateDetail(orderDetail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
