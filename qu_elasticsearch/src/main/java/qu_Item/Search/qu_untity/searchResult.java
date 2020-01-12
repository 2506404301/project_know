package qu_Item.Search.qu_untity;

import com.qu.item.unity.Brand;
import com.qu.item.unity.Calegory;
import qu_Item.common.vo.PageResult;

import java.util.List;
import java.util.Map;

public class searchResult extends PageResult<Goods> {

    private List<Calegory> category; //分类待选项;

    private List<Brand> brands;//品牌待选项;

    private List<Map<String,Object>> specs;//规格参数 key即待选项;

    public searchResult(){

    }


    public searchResult(Long total, Integer totalPage, List<Goods> items, List<Calegory> category, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.category = category;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Calegory> getCategory() {
        return category;
    }

    public void setCategory(List<Calegory> category) {
        this.category = category;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
