package qu_Item.Search.qu_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import qu_Item.Search.qu_service.GoodService;
import qu_Item.Search.qu_untity.Goods;
import qu_Item.Search.qu_untity.searchParams;
import qu_Item.common.vo.PageResult;

@RestController
public class SearchController {

    @Autowired
    private GoodService goodsService;

    /**
     * 搜索
     * @param searchParams
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody searchParams searchParams){
        return ResponseEntity.ok(goodsService.search(searchParams));
    }

}
