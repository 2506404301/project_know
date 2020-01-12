package qu_Item.Item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.Brand;
import com.qu.item.unity.Category_Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import qu_Item.Item.mapper.BrandMapper;
import qu_Item.Item.mapper.CategoryBrandMapper;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    public PageResult<Brand> quertBrandByPage(Integer page, Integer row, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, row);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name", "%d" + key + "%d").orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + ( desc ? " DESC " : " ASC ");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new quException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //转化为要显示的数据和页数;
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),list);
    }

    /**
     * 保存品牌信息和级联表;
     * @param brand
     * @param clds
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> clds) {
        //新增品牌
        int conut = brandMapper.insert(brand);
        if (conut !=1 ){
            throw new quException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //做级联分配;
        for (Long cid : clds){
            int countd = brandMapper.insertIntoCategoryBrand(cid, brand.getId());
            if (countd != 1){
                throw new quException(ExceptionEnum.CATEGORY_BRAND_SAVE_ERROR);
            }
        }
    }

    /**
     * 根据id来查询信息;
     * @param id
     * @return
     */
    public Brand QueryBrandById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null){
            throw new quException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * 根据穿过来的cid来通过双表关联来查询对应brand信息;
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid){
        List<Brand> brands = brandMapper.queryCategoryByCid(cid);
        if (brands == null){
            throw new quException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }


    public Brand updateByBrandId(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null){
            throw new quException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * 根据处穿过来的id来删除信息;
     * @param id
     */
    @Transactional
    public void DeleteByBrandId(Long id) {
        int i = brandMapper.deleteByPrimaryKey(id);
        if (i!=1){
            throw new quException(ExceptionEnum.BRAND_DELETE_ERROR);
        }
        Category_Brand cb = new Category_Brand();
        cb.setBrandId(id);
        i = categoryBrandMapper.delete(cb);
        if (i!=1){
            throw new quException(ExceptionEnum.CATEGORY_BRAND_DELETE_ERROR);
        }
    }

    /**
     * 根据处穿过来的ids来查询品牌信息;
     * @param ids
     */
    public List<Brand> QueryBrandByIds(List<Long> ids) {
        List<Brand> brandList = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brandList)){
            throw new quException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brandList;
    }

    /**
     * 根据brand来更新数据;
     * @param brand
     */
    public void updateBrand(Brand brand) {
        int i = brandMapper.updateByPrimaryKey(brand);
        if (i!=1){
            throw new quException(ExceptionEnum.BRAND_UPDATE_ERROR);
        }
    }
}
