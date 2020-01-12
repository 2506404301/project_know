package qu.page.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qu.item.unity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import qu.page.cilent.PageAllClient;
import qu_Item.common.util.JsonUtils;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

@Service
@Slf4j
public class ItemModelService {

    @Autowired
    private PageAllClient pageAllClient;

    @Autowired
    private TemplateEngine templateEngine;


    /**
     * 通过spuId来声明把查询数据付map返回;
     * @param spuId
     * @return
     */
    public Map<String, Object> queryAtts(Long spuId) {
        Map<String,Object> map = new HashMap<>();
        //查询spu;
        Spu spu = pageAllClient.querySpuById(spuId);
        //查询skus；
        List<Sku> skus = spu.getSkus();
        //查询detail；
        SpuDetail spuDetail = spu.getSpuDetail();

        //查询brand；
        Brand brand = pageAllClient.queryBrandById(spu.getBrandId());

        //查询商品的分类;
        List<Calegory> categories = pageAllClient.queryCategoryByids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //查询规格参数组;

        // 1.1):获取通过规格参数;
        String specifications = spuDetail.getSpecifications();
        // 1.2):转化为list->map;
        List<Map<String, Object>> mapList = JsonUtils.nativeRead(specifications, new TypeReference<List<Map<String, Object>>>() {
        });
        // 1.3):转被相应的队列;
        Map<Integer ,String> strName = new HashMap<>();
        Map<Integer ,Object> strValue = new HashMap<>();
        this.saveListMapSpecifications(mapList,strName,strValue);

        //2.1):获取特有规格参数;
        String specTemplate = spuDetail.getSpecTemplate();
        //2.2):转化为list->map[]类型;
        Map<String, String[]> maps = JsonUtils.nativeRead(specTemplate, new TypeReference<Map<String, String[]>>() {
        });
        //3.3):转化为相应的map;
        Map<Integer,String> SpecStrName = new HashMap<>();
        Map<Integer,String[]> SpecStrValue = new HashMap<>();
        try {
            this.savespecListMapspecTemlate(maps,SpecStrName,SpecStrValue,strName,strValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //3.1):构建规格参数组;
        List<Map<String,Object>> groups = this.makegroudSpec(mapList,strName,strValue);


        map.put("spu",spu);
        map.put("skus",skus);
        map.put("spuDetail",spuDetail);
        map.put("brand",brand);
        map.put("categories",categories);
        map.put("specName",strName);
        map.put("specValue",strValue);
        map.put("groups",groups);
        map.put("specialParamName",SpecStrName);
        map.put("specialParamValue",SpecStrValue);

        return map;
    }


    /**
     * 用来构建规格参数组;
     * @param mapList
     * @param strName
     * @param strValue
     * @return
     */
    private List<Map<String, Object>> makegroudSpec(List<Map<String, Object>> mapList, Map<Integer, String> strName, Map<Integer, Object> strValue) {

        List<Map<String, Object>> groups = new ArrayList<>();
        int i = 0;
        int j = 0;
        for (Map<String,Object> spec : mapList){
            List<Map<String, Object>> params = (List<Map<String, Object>>) spec.get("params");
            List<Map<String,Object>> temp = new ArrayList<>();
            for (Map<String,Object> param :params) {
                for (Map.Entry<Integer, String> entry : strName.entrySet()) {
                    if (entry.getValue().equals(param.get("k").toString())) {
                        String value = strValue.get(entry.getKey()) != null ? strValue.get(entry.getKey()).toString() : "无";
                        System.out.println("value = " + value);
                        System.out.println("entry.getValue() = " + entry.getValue());
                        Map<String, Object> temp3 = new HashMap<>(16);
                        temp3.put("id", ++j);
                        temp3.put("name", entry.getValue());
                        temp3.put("value", value);
                        temp.add(temp3);
                    }
                }
            }
            Map<String,Object> temp2 = new HashMap<>(16);
            temp2.put("params",temp);
            temp2.put("id",++i);
            temp2.put("name",spec.get("group"));
            groups.add(temp2);
        }
        return groups;
    }

    /**
     * 保存特有SpecTemplate的规格参数;
     * @param maps
     * @param specStrName
     * @param specStrValue
     * @param strName
     * @param strValue
     */
    private void savespecListMapspecTemlate(Map<String, String[]> maps, Map<Integer, String> specStrName, Map<Integer, String[]> specStrValue, Map<Integer, String> strName, Map<Integer, Object> strValue) throws Exception {
        if (maps != null){
            for (Map.Entry<String, String[]> entry : maps.entrySet()) {
                String key = entry.getKey();
                System.out.println("key = " + key);
                for (Map.Entry<Integer, String> stringEntry : strName.entrySet()) {
                    //获取到特有属性键值对中的key等于共有属性中key值;
                    if (stringEntry.getValue().equals(key)){
                        //送入sprcStrName中;
                        specStrName.put(stringEntry.getKey(), stringEntry.getValue());
                        System.out.println("specStrName = " + specStrName);
                        //因为是放在数组里面，所以要先去除两个方括号，然后再以逗号分割成数组
                        String  s = strValue.get(stringEntry.getKey()).toString();
                        System.out.println("s = " + s);
                        String result = StringUtils.substring(s,1,s.length()-1);
                        System.out.println("result = " + result);
                        specStrValue.put(stringEntry.getKey(), result.split(","));
                        System.out.println("specStrValue = " + specStrValue);
                    }
                }
            }

        }

    }

    /**
     * 出入通用规格参数;
     * @param mapList
     * @param strName
     * @param strValue
     */
    private void saveListMapSpecifications(List<Map<String, Object>> mapList, Map<Integer, String> strName, Map<Integer, Object> strValue) {
        String k = "k";
        String v = "v";
        String unit = "unit";
        String numerical = "numerical";
        String options = "options";
        int i = 0;
        //循环获取数值;
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                List<Map<String, Object>> params = (List<Map<String, Object>>) map.get("params");
                for (Map<String, Object> param : params) {
                    String result;
                    //获得k的值;
                    k = param.get("k").toString();
                    if (k != null) {
                        result = k;
                    } else {
                        result = "空";
                    }
                    //1):判断单位是否为数字类型;
                    if (param.containsKey(numerical) && (boolean) param.get(numerical)) {
                        //1.1):单位为double类型;
                        if (numerical.contains(".")) {
                            Double d = Double.valueOf(result);
                            //判断是否int值等于他本身;
                            if (d.intValue() == d) {
                                result = d.intValue() + "";
                            }
                        }
                        i++;
                        strName.put(i, param.get("k").toString());
                        strValue.put(i, result + param.get(unit).toString());
                        System.out.println("111:strName = " + strName);
                        System.out.println("112:strValue = " + strValue);
                    } else if (param.containsKey(options)) {
                        //2):判断options是否存在;
                        i++;
                        strName.put(i, param.get("k").toString());
                        strValue.put(i, param.get("options"));
                        System.out.println("221:strName = " + strName);
                        System.out.println("222:strValue = " + strValue);
                    } else {
                        //3):其他类型;
                        i++;
                        strName.put(i, param.get("k").toString());
                        strValue.put(i, param.get("v"));
                        System.out.println("331:strName = " + strName);
                        System.out.println("332:strValue = " + strValue);
                    }
                }
            }
        }
    }

}
