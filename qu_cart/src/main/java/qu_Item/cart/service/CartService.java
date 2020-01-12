package qu_Item.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.cart.Interceptor.UserInterceptor;
import qu_Item.cart.unity.Cart;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.util.JsonUtils;
import qu_Item.entry.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final String KEY_PRO = "cart:id:";
    private static final String COOLLECTION = "collection:";

    /**
     * 正加购物车的功能;
     * @param cart
     */
    public void addCart(Cart cart) {
        //获取用户;
        UserInfo user = UserInterceptor.getUser();
        //获取id;
        String key = KEY_PRO + user.getId();
        //绑定对应的key;
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate
                .boundHashOps(key);
        //找到对应商品的id作为hashkey判断是否存在 对应商品;
        addCartToRedis(cart, ops);
    }

    /**
     * 查询所有cart
     * @return
     */
    public List<Cart> queryCartList() {
        //获取用户;
        UserInfo user = UserInterceptor.getUser();
        //获取id;
        String key = KEY_PRO + user.getId();

        if (!stringRedisTemplate.hasKey(key)) {
          throw new quException(ExceptionEnum.REDIS_KEY_NOT_ERROR);
        }

        return  queryListRedis(key);
    }

    /**
     * 事项购买数量的更新;
     * @param skuId
     * @param num
     */
    public void UpdateNum(Long skuId, Integer num) {
        //获取用户;
        UserInfo user = UserInterceptor.getUser();
        //获取id;
        String key = KEY_PRO + user.getId();

        //绑定对应的值;
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(key);

        //获得购物车中对应的数据;
        String json = hashOps.get(skuId.toString()).toString();

        Cart cart = JsonUtils.parse(json, Cart.class);

        if (cart == null){
            throw new quException(ExceptionEnum.REDIS_VALUE_NOT_ERROR);
        }

        cart.setNum(num);
        //skuid是redis里面的key;
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));

    }

    /**
     * 来根据skuid来来删除购物车中的数据;
     * @param skuId
     */
    public void deleteCart(Long skuId) {
        //获取用户;
        UserInfo user = UserInterceptor.getUser();
        //获取id;
        String key = KEY_PRO + user.getId();

        //绑定对应的值;
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(key);

        //删除数据;
        hashOps.delete(skuId.toString());
    }


    /**
     * 增加到我的收藏;
     * @param cart
     */
    public void addCollection(Cart cart) {
        //获取用户;
        UserInfo user = UserInterceptor.getUser();
        //获取id;
        String key = COOLLECTION + KEY_PRO + user.getId();

        //绑定对应的key;
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(key);

        addCartToRedis(cart, ops);
    }


    /**
     * 增加redis中的数据;
     * @param cart
     * @param ops
     */
    private void addCartToRedis(Cart cart, BoundHashOperations<String, Object, Object> ops) {
        String hashkey = cart.getSkuId().toString();
        if (ops.hasKey(hashkey)) {
            //存在则修改数量;
            //json格式的字节码;
            String json = ops.get(hashkey).toString();
            //获得对应的对相;
            Cart NewCart = JsonUtils.parse(json, Cart.class);
            //新的加上原来的数据;
            NewCart.setNum(cart.getNum() + NewCart.getNum());
            //写会redis;
            ops.put(hashkey, JsonUtils.serialize(NewCart));
        } else {
            //不存在完成新增;
            ops.put(hashkey, JsonUtils.serialize(cart));
        }
    }

    /**
     * 查询我的收场列表;
     * @return
     */
    public List<Cart> queryCollectionList() {
        //获得用户;
        UserInfo info = UserInterceptor.getUser();
        //获得key
        String key = COOLLECTION + KEY_PRO + info.getId();

        if (key == null){
            throw new quException(ExceptionEnum.REDIS_KEY_NOT_ERROR);
        }

        return  queryListRedis(key);
    }

    /**
     * 定义查询的方法;
     * @param key
     * @return
     */
    private List<Cart> queryListRedis(String key) {
        //绑定对应的key;
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(key);

        List<Object> values = ops.values();

        if (CollectionUtils.isEmpty(values)){
            throw new quException(ExceptionEnum.REDIS_VALUE_NOT_ERROR);
        }

        List<Cart> cartList = values.stream()
                .map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());

        return cartList;
    }
}
