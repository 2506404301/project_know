package qu_Item.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import qu_Item.user.mapper.UserMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public PageResult<User> queryUserByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(User.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("username","%"+key+"%")
                    .orLike("phone","%"+key+"%");
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            throw new quException(ExceptionEnum.USER_NOT_FOUND);
        }
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return new PageResult<>(userPageInfo.getTotal(),users);
    }

    /**
     * 查询数量
     * @return
     */
    public Long queryUserTotal() {
        Long total = userMapper.queryTotalByUser();
        return total;
    }
}
