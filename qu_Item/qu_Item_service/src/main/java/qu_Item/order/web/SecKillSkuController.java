package qu_Item.order.web;


import com.qu.item.unity.SecKillSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.common.vo.PageResult;
import qu_Item.order.service.SecKillSkuService;


@RestController
@RequestMapping("secKillSku")
public class SecKillSkuController {
    @Autowired
    private SecKillSkuService secKillSkuService;

    @GetMapping("page")
    public ResponseEntity<PageResult<SecKillSku>> querySecKillSkuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){
       return ResponseEntity.ok(secKillSkuService.querySecKillSkuByPage(page,rows,sortBy,desc,key));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteSeckillsku(@RequestParam("id") Long skuId){
        secKillSkuService.deleteSeckillsku(skuId);
        return ResponseEntity.status(HttpStatus.MULTI_STATUS.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<Void> saveSecKillSku(@RequestBody SecKillSku secKillSku){
        secKillSkuService.saveSecKillSku(secKillSku);
        return ResponseEntity.status(HttpStatus.MULTI_STATUS.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateSecKillSku(@RequestBody SecKillSku secKillSku){
        secKillSkuService.updateSecKillSku(secKillSku);
        return ResponseEntity.status(HttpStatus.MULTI_STATUS.CREATED).build();
    }
}
