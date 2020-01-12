package qu_Item.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.Address;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import qu_Item.order.mapper.AddressMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public PageResult<Address> queryDataFromPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Address.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%");
        }
        //排序
        /*if (StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }*/
        //查询
        List<Address> list = addressMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new quException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
        PageInfo<Address> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),list);
    }
}
