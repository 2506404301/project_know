package qu_Item.cart.unity;

import lombok.Data;

@Data
public class Cart {

    //购买的数量;
    private Integer num;
    //sku的id;
    private Long skuId;
    //标题
    private String title;
    //图片
    private String image;

    //加入购物车的价格;
    private Long price;
    //规格参数;
    private String ownSpec;

    @Override
    public String toString() {
        return "Cart{" +
                "num=" + num +
                ", skuId=" + skuId +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", ownSpec='" + ownSpec + '\'' +
                '}';
    }
}
