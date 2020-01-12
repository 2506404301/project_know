package qu_Item.Item.mapper;


import com.qu.item.unity.Calegory;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Calegory>, IdListMapper<Calegory, Long> {

}
