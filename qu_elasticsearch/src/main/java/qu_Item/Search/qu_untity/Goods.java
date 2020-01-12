package qu_Item.Search.qu_untity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods" ,type =  "docs",shards = 1,replicas = 0)
public class Goods {

    @Id
    private Long id;//spuId

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String all;//所有被搜索的信息;包括标题，分类，品牌 (text)用于分词;

    @Field(type = FieldType.Keyword,index = false)
    private String  subTitle;//卖点;

    private Long brandId;//品牌id;
    private Long cid1;//1级分类id;
    private Long cid2;//2及分类id;
    private Long cid3;//3级分类id;

    private Date createTime;//创建时间;

    private Set<Long> price;//价格;

    @Field(type = FieldType.Keyword,index = false)
    private String skus;//skus的json信息,用来展示 ,这样直接显示出来;

    private Map<String,Object> spec;//由于规格参数是不确定性的，所以采用map来存取;

}
