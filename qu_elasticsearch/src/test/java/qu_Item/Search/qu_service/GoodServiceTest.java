package qu_Item.Search.qu_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qu.item.unity.Spu;
import com.qu.item.unity.SpuDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import qu_Item.Search.qu_client.AllClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static qu_Item.common.util.JsonUtils.nativeRead;

//@SpringBootTest
//@RunWith(SpringRunner.class)
/*
public class GoodServiceTest {

    @Autowired
    private AllClient allClient;

    @Test
    public void textClient(){
        //对spec处理
        SpuDetail spuDetail = allClient.querySpuDetailById(30L);
        //提取共有属性;
        List<Map<String, Object>> genericSpecs = nativeRead(spuDetail.getSpecifications(), new TypeReference<List<Map<String,Object>>>() {

        });
        //提取特有属性;
        Map<String,Object> specialSpecs = nativeRead(spuDetail.getSpecTemplate(), new TypeReference<Map<String, Object>>() {

        });
        //把这个可以搜索的信息保存到map中;
        Map<String,Object> map = new HashMap<>();

        String searchable = "searchable";
        String v = "v";
        String k = "k";
        String options = "options";

        for (Map<String, Object> genSpec : genericSpecs) {
            //获得这个键值对
            List<Map<String,Object>> mapList1 = (List<Map<String, Object>>) genSpec.get("params");
            System.out.println("mapList1 = " + mapList1);
            for (Map<String, Object> stringObjectMap : mapList1) {
                //这表示可以搜索;
                if((Boolean) stringObjectMap.get("searchable")){
                    if (stringObjectMap.get("v") != null){
                        map.put(stringObjectMap.get("k").toString(),stringObjectMap.get("v").toString());
                        System.out.println("map的信息是 = " + map);
                    }else if (stringObjectMap.get("options")!=null){
                        map.put(stringObjectMap.get("k").toString(),stringObjectMap.get("options"));
                        System.out.println("map||options = " + map);
                    }
                }
            }
        }
    }
}*/
