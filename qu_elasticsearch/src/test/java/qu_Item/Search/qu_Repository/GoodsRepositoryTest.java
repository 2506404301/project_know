package qu_Item.Search.qu_Repository;

import com.qu.item.unity.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import qu_Item.Search.qu_client.AllClient;
import qu_Item.Search.qu_service.GoodService;
import qu_Item.Search.qu_untity.Goods;
import qu_Item.common.vo.PageResult;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private AllClient allClient;

    @Autowired
    private GoodService goodService;

    @Test
    public void testCreats(){
        //创建索引;
        template.createIndex(Goods.class);
        //添加映射关系;
        template.putMapping(Goods.class);

    }
    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            //查询spu信息;
            PageResult<Spu> spuPageResult = allClient.querySpuByPage(page, rows, true, null);
            //构建goods
            List<Spu> items = spuPageResult.getItems();
            if (CollectionUtils.isEmpty(items)){
                break;
            }
            List<Goods> collect = items.stream().map(goodService::buildGoods)
                    .collect(Collectors.toList());

            //存入索引库;
            goodsRepository.saveAll(collect);

            //翻页
            page++;
            size = items.size();

        }while (size == 100);

    }
}

