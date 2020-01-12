package qu_Item.Item.service;

import com.qu.item.unity.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qu_Item.Item.mapper.SpecificationMapper;
@Service
public class SpecAllService {

    @Autowired
    private SpecificationMapper specificationMapper;

    /**
     * 根据CategotyId查寻的方法;
     * @param id
     * @return
     */
    public Specification queryId(Long id) {
       return specificationMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据穿过来的spec来出入数据库;
     * @param specification
     */
    public void InsetSpec(Specification specification) {
        specificationMapper.insert(specification);
    }

    /**
     * 根据穿过来的spec来跟新数据库;
     * @param specification
     */
    public void UpdateSpecifiction(Specification specification) {
        specificationMapper.updateByPrimaryKey(specification);
    }
}
