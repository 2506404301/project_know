package qu_Item.Item.service;

import com.qu.item.unity.Calegory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import qu_Item.Item.mapper.CategoryMapper;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import java.util.List;

@Service
public class Catesource {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Calegory> queryCategoryListBypid(Long pid){
        /*查询条件*/
        Calegory t = new Calegory();
        /*把这个对象的非空条件作为参数传过来*/
        t.setParentId(pid);
        List<Calegory> list = categoryMapper.select(t);
        /*判断查询结果*/
        if (CollectionUtils.isEmpty(list)){
            throw new quException(ExceptionEnum.CAREGORY_NULL_SELECT);
        }
        return list;
    }

    /**
     * 通过三级类目cid来查询，返回这个category集合;
     * @param ids
     * @return
     */
    public List<Calegory> queryCategotyList(List<Long> ids){
        List<Calegory> idList = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(idList)){
            throw new quException(ExceptionEnum.CAREGORY_NULL_SELECT);
        }
        return idList;
    }

    /**
     *  保存商品分类的消息;
     * @param calegory
     */
    @Transactional
    public void saveCategory(Calegory calegory) {
        calegory.setId(null);
        //保存本节点
        int count = categoryMapper.insert(calegory);
        if (count!=1){
            throw new quException(ExceptionEnum.CAREGORY_INSERT_ERROR);
        }
        //修改父节点 这里其实就是根据传过来的父id，来时本来不是父节点的变成父节点;
        Calegory parent = new Calegory();
        parent.setId(calegory.getParentId());
        parent.setIsParent(true);
        categoryMapper.updateByPrimaryKeySelective(parent);
    }

    /**
     * 对商品节点的更新;
     * @param id
     * @param name;
     */
    @Transactional
    public String UpdateCategory(Long id , String name) {
        Calegory calegory = categoryMapper.selectByPrimaryKey(id);
        calegory.setName(name);
        int i = categoryMapper.updateByPrimaryKeySelective(calegory);
        if (i!=1){
            throw new quException(ExceptionEnum.CAREGORY_UPDATE_ERROR);
        }
        return name;
    }

    /**
     * 根据传过来的id来删除这个节点信息;
     * 判断一下如果是父节点，
     * @param id
     */
    @Transactional
    public void DeleteCategoryId(Long id) {
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count!=1){
            throw new quException(ExceptionEnum.CAREGORY_DELETE_ERROR);
        }
    }

}
