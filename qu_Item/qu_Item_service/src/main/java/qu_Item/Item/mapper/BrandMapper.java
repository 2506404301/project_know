package qu_Item.Item.mapper;

import com.qu.item.unity.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand,Long> {

    //新增加一个中间级联表“tb_Brand_Category”
    @Insert("INSERT INTO tb_category_brand (category_id ,brand_id) VALUE (#{cid},#{bid})")
    int insertIntoCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);

    //双表关联的查询方式;
    @Select("SELECT b.* FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> queryCategoryByCid(@Param("cid") Long cid);

}
