package qu_Item.Item.web;

import com.qu.item.unity.Calegory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.Item.service.Catesource;

import java.util.List;

@RestController
@RequestMapping("Category")
public class CategoryController {

    @Autowired
    private Catesource catesource;


    /**
     * 根基pid查询商品分类;
     * @param pid
     * @return
     */
    @RequestMapping("list")
    public ResponseEntity<List<Calegory>> queryCategoryListBypid(@RequestParam("pid") Long pid) {
        //REST风格的字符;
        return ResponseEntity.ok(catesource.queryCategoryListBypid(pid));
    }


    /**
     * 保存商品分类的信息;
     * @param calegory
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> saveCategory(@RequestBody Calegory calegory){
        catesource.saveCategory(calegory);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据传过来的id来删除这个节点信息;
     * @param id
     * @return
     */
    @GetMapping("cid")
    public ResponseEntity<Void> DeleteCategoryId(@RequestParam(value = "id") Long id){
        catesource.DeleteCategoryId(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 更新节点信息;
     * @param id
     * @param name
     * @return
     */
    @GetMapping("uid")
    public ResponseEntity<String> UpdateCategory(@RequestParam(value = "id",defaultValue = "0") Long id,
                                                 @RequestParam(value = "name", defaultValue = "null") String name){
        System.out.println("这个id信息时"+id+"名称发信息是"+name);
        return ResponseEntity.ok(catesource.UpdateCategory(id,name));
    }

    /**
     * 根据ids查询所有的list<Category>
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Calegory>> queryCategoryByids(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(catesource.queryCategotyList(ids));
    }

}
