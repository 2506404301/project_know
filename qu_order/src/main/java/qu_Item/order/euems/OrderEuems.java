package qu_Item.order.euems;


public enum OrderEuems {

    NOT_Pay(1,"未付款"),
    PAY_NOT_DELIVEEY(2,"已付款,未发货"),
    DELIVERY_NOT_SURE(3,"已发货,未确认"),
    EXCHANGE_SUCCESS(4,"交易成功"),
    EXCHANGE_CLOSE(5,"交易关闭"),
    DECRIPTION_END(6,"已评价"),

    ;
    private int code;
    private String decription;

    OrderEuems(int code,String decription){
        this.code = code;
        this.decription = decription;
    }

    public  int value(){
        return this.code;
    }
}
