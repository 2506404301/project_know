package qu_Item.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class cartDTO {

        private Long price; //价格

        private Long skuId;//sku的id

        private Integer num;//数量;

}
