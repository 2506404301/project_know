package qu_Item.Item.web;

import com.qu.item.unity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.Item.service.BrandService;
import qu_Item.common.vo.PageResult;

import java.util.List;

@RestController
@RequestMapping("Brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌;
     * @author:me
     * @time:2019-07-18
     * @param page
     * @param row
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                              @RequestParam(value = "row" ,defaultValue = "5") Integer row,
                                                              @RequestParam(value = "sortBy",required = false) String sortBy,
                                                              @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
                                                              @RequestParam(value = "key",required = false) String key){
        PageResult<Brand> brandPageResult =  brandService.quertBrandByPage(page,row,sortBy,desc,key);
        return ResponseEntity.ok(brandPageResult);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     * 一般400就是参数没有对应上;
     */
    @PostMapping
    public ResponseEntity<Void>  saveBrand (Brand brand,@RequestParam(value = "cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        //返回201创建成功,表示无返回值;BUILD;
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新数据
     * @param brand
     * @return
     */
    @PutMapping()
    public ResponseEntity<Void> UpdateBrand(Brand brand){
        brandService.updateBrand(brand);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //PathVariable是获取Mapping中的{}变量
    //requestParam是获取请求的参数;
    @GetMapping("/cid/{cid}")
    public  ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){
        List<Brand> brandList = brandService.queryBrandByCid(cid);
        return ResponseEntity.ok(brandList);

    }

    /**
     * 通过id来查询出数据用来更新;
     * @param id
     * @return
     */
    @GetMapping("BrandId")
    public  ResponseEntity<Brand> UpdateByBrandId(@RequestParam(value = "id") Long id){
        return ResponseEntity.ok(brandService.updateByBrandId(id));
    }


    /**
     * 根据id来删除信息;
     * @param id
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> DeleteByBrandId(@RequestParam(value = "id") Long id) {
        brandService.DeleteByBrandId(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    /**
     * 根据id来查询分类
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.QueryBrandById(id));
    }

    /**
     * 根据id来查询分类
     * @param ids
     * @return
     */

    @GetMapping("brands")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(brandService.QueryBrandByIds(ids));
    }

}
