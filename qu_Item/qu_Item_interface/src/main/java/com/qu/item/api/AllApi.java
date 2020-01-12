package com.qu.item.api;

import com.qu.item.unity.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import qu_Item.common.vo.PageResult;

import java.util.List;

public interface AllApi {

    /**
     * 根据分类ids来查询节点信息;
     * @param ids
     * @return
     */
    @GetMapping("Category/list/ids")
    List<Calegory> queryCategoryByids(@RequestParam("ids")List<Long> ids);

    /**
     * 根据id来查询分类
     * @param id
     * @return
     */
    @GetMapping("Brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

    /**
     * 根据ids来查询分类
     * @param ids
     * @return
     */
    @GetMapping("Brand/brands")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);



    /**
     * 分页查询spu;
     *
     * @param page
     * @param row
     * @param saleable
     * @param key
     * @return
     */

    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "row", defaultValue = "5") Integer row,
                                   @RequestParam(value = "saleable", required = false) Boolean saleable,
                                   @RequestParam(value = "key", required = false) String key);


    /**
     * 根据id来查询spu；
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);


    /**
     * 查询spu下所有的sku;
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuSpuId(@RequestParam(value = "id") Long id);


    /**
     * 根据id来查询规格参数;
     * @param id
     * @return
     */
    @GetMapping("spec/{id}")
    String QuerySpecificationByCategoryId(@PathVariable("id") Long id);


    /**
     * 根据传过来的id来查询详细信息;
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);
}
