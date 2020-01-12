package qu_Item.Item.web;

import com.qu.item.unity.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qu_Item.Item.service.SpecAllService;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecAllService specAllService;

    /**
     * 根据穿过来categoryId来查寻对应的模板;
     * @param id
     * @return
     */

    @GetMapping("{id}")
    public ResponseEntity<String> QuerySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = specAllService.queryId(id);
        if (spec == null){
            throw new quException(ExceptionEnum.Specification_NOT_FOUND);
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    /**
     * 根据穿过来的spec来插入数据库信息;
     * @param specification
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> InsertSpecifiction( Specification specification){
        specAllService.InsetSpec(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据穿过来的spec来修改入数据库;
     * 因为这个更新和插入的请求相同，所以用put把post请求给覆盖掉;
     * @param specification
     * @return
     */
    @PutMapping
    public  ResponseEntity<Void> UpdateSpectifiction( Specification specification){
        specAllService.UpdateSpecifiction(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

