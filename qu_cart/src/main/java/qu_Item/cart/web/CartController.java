package qu_Item.cart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.cart.service.CartService;
import qu_Item.cart.unity.Cart;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 登录状态下的增加购物车功能
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
   }

    /**
     *
     * 查询Cart;
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        return ResponseEntity.ok(cartService.queryCartList());
    }


    /**
     * 实现数量的更新
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> UpdateNum(@RequestParam("skuId") Long skuId,
                                          @RequestParam("num") Integer num){
        cartService.UpdateNum(skuId,num);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据skuId来删除购物车中的数据;
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> DeleteCart(@PathVariable("skuId") Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 正加我的到我的收藏;
     * @param cart
     * @return
     */
    @PostMapping("collection")
    public ResponseEntity<Void> addCollection(@RequestBody Cart cart){
        cartService.addCollection(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询我的收场列表;
     * @return
     */

    @GetMapping("collectionList")
    public ResponseEntity<List<Cart>> queryCollectionList(){
        return ResponseEntity.ok(cartService.queryCollectionList());
    }


}
