package qu_Item.Search.qu_Repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import qu_Item.Search.qu_untity.Goods;

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

}
