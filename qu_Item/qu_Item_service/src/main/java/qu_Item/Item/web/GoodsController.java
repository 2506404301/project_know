package qu_Item.Item.web;

import com.qu.item.unity.Sku;
import com.qu.item.unity.Spu;
import com.qu.item.unity.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.Item.service.GoodsService;
import qu_Item.common.vo.PageResult;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询spu;
     * @param page
     * @param row
     * @param saleable
     * @param key
     * @return
     */

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                          @RequestParam(value = "row" ,defaultValue = "5") Integer row,
                                                          @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                          @RequestParam(value = "key",required = false) String key){

        return ResponseEntity.ok(goodsService.querySpuByPage(page,row,saleable,key));
    }

    /**
     * 根据id来查询spu;
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySouById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }

    /**
     * 根据spu来存储指定的信息;
     * @param spu
     */
    @PostMapping("goods")
    public ResponseEntity<Void> SaveSpuList(@RequestBody Spu spu){
        goodsService.SaveSpuList(spu);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据spu的id来删除指定的信息;
     * 这要实现sku,sku_detail,spu表的id删除;
     * @param ids
     * @return
     */
    @DeleteMapping("goods")
    public ResponseEntity<Void> DeleteBySpuId(@RequestParam(value = "id")Long ids){
        goodsService.DeleteBySpuId(ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据传过来的id来查询详细信息;
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public  ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuDetailById(id));
    }
    /**
     * 查询spu下所有的sku;
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public  ResponseEntity<List<Sku>> querySkuSpuId(@RequestParam(value = "id") Long id){
        return ResponseEntity.ok(goodsService.quereSkuList(id));
    }


    /**
     * 传过来spu来跟新Spu信息;
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateSpuList(@RequestBody Spu spu){
        goodsService.updateSpuList(spu);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据传过来的id来实现商品的下家;
     * @param id
     * @return
     */
    @GetMapping("spu/out/{id}")
    public ResponseEntity<Void> goodsOut(@PathVariable("id")Long id){
        goodsService.goodsOutAndUp(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
