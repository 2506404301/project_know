package qu_Item.common.enume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(404, "品牌分类没有找到!"),
    BRAND_DELETE_ERROR(404, "品牌分类删除失败!"),
    BRAND_UPDATE_ERROR(404, "品牌分类修改失败!"),
    USER_NOT_FOUND(404, "用户没有找到!"),
    ADMIN_CONTROLLER_NOT_FOUND(404, "管理员没有找到!"),
    ADDRESS_NOT_FOUND(404, "地址没有找到!"),
    ORDER_DETAIL_NOT_FOUND(404, "订单详情没有找到!"),
    ORDER_NOT_FOUND(404, "订单没有找到!"),
    SECKILLSKU_NOT_FOUND(404, "秒杀订单没有找到!"),
    DELETE_SECKILLSKU_ERROR(404, "删除秒杀订单没有失败!"),
    SECKILLSKU_INSERT_ERROR(404, "插入秒杀订单没有失败!"),
    SECKILLSKU_UPDATE_ERROR(404, "更新秒杀订单没有失败!"),
    ORDER_UPDATE_ERROR(500 , "订单更新失败!"),
    ORDER_STATUS_FORM_NOT_FOUND(500 , "订单状ff态码没有找到!"),
    ORDER_STATUS_NOT_FOUND(500 , "订单状态码没有找到!"),
    ORDER_STATUS_UPDATE_ERROR(500 , "订单状态码更新失败!"),
    ORDER_FORM_NOT_FOUND(404, "订单单没有找到!"),
    ORDER_DETAIL_FORM_NOT_FOUND(404, "订单详情没有找到!"),
    ORDER_DETAIL_UPDATE_ERROR(500, "订单详情更新失败!"),
    GOODSDETAIL_NOT_FOUND(404, "商品详细信息没有找到!"),
    GOODSDSKU_NOT_FOUND(404, "商品SKU信息没有找到!"),
    CAREGORY_NULL_SELECT(404,"商品分类没有查到"),
    CAREGORY_INSERT_ERROR(500,"商品分类新增失败"),
    CAREGORY_UPDATE_ERROR(500,"商品分类修改失败"),
    CAREGORY_DELETE_ERROR(500,"商品分类节点删除失败"),
    CATEGORY_BRAND_SAVE_ERROR(500,"新增品牌分类中间表失败！"),
    CATEGORY_BRAND_DELETE_ERROR(500,"删除品牌分类中间表失败！"),
    BRAND_SAVE_ERROR(500,"新增品牌失败！"),
    FILE_UPLOAD_ERROR(500,"文件上传失败！"),
    GOODS_UPDATE_ERROR(500,"商品修改失败！"),
    FILE_TYPR_ERROR(500,"文件上传类型错误！"),
    GOODS_SAVE_ERROR(500,"文件上传类型错误！"),
    SPU_DELETE_ERROR(500,"SPU记录删除失败！"),
    SPU_QUERT_ERROR(500,"SPU没有找到！"),
    SKU_DELETE_ERROR(500,"SKU记录删除失败！"),
    SKU_QUERY_ERROR(500,"SKU查找失败！"),
    SKU_GOODSOUT_ANDUP_ERROR(500,"SKU记录下架和上架失败！"),
    SPU_GOODSOU_ANDUP_ERROR(500,"SPU记录下架和上架失败！"),
    STOCK_DELETE_ERROR(500,"STOCK记录删除失败！"),
    STOCK_NOT_ENOUGH(505,"STOCK库存不足！"),
    SPUDETAIL_DELETE_ERROR(500,"SPUDetail记录删除失败！"),
    Specification_NOT_FOUND(500, "商品分类没有找到!"),
    GOODS_NOT_FOUND(404, "商品没有找到!"),
    ERROR_USER_DATA_TYPE(400, "传入的参数有误!"),
    ERROR_USERNAME_PASSWORD(400, "用户名或密码有误!"),
    CREATED_TOKEN_ERROR(500, "用户生成TOKEN失败!"),
    UNKNOWVERIFY_NOT(403, "没有权限访问"),
    REDIS_KEY_NOT_ERROR(404, "REDIS中没有对应的KEY"),
    REDIS_VALUE_NOT_ERROR(404, "REDIS中没有对应的VALUE"),
    CREATED_ORDER_ERROR(404, "创建订单失败"),
    QUERT_ORDER_ERROR(404, "订单查询失败"),
    QUERT_ORDERID_ERROR(404, "订单号查询失败"),
    USERT_ADDRESS_ERROR(404, "用户地址查询失败"),
    USERT_ADDRESS_DELETE_ERROR(404, "用户地址删除失败"),
    USERT_ADDRESS_UPDATE_ERROR(404, "用户地址删除失败"),
    USERT_INSERT_ADDRESS_ERROR(404, "用户添加失败"),
    ;
    private int code;
    private String msg;

}
