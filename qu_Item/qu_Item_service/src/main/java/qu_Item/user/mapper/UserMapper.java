package qu_Item.user.mapper;

import com.qu.item.unity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {
    @Select("SELECT COUNT(*) FROM tb_order")
    Long queryTotalByUser();
}
