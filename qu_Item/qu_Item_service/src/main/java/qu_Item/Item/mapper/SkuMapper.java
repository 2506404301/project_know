package qu_Item.Item.mapper;

import com.qu.item.unity.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku,Long> {
}
