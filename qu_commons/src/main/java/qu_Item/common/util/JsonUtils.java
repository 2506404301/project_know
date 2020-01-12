package qu_Item.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: xain
 * @create: 2018-04-24 17:20
 **/
public class JsonUtils {

  public static final ObjectMapper mapper = new ObjectMapper();

  private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

  @Nullable
    public static String serialize(Object obj) {
    if (obj == null) {
      return null;
    }
    if (obj.getClass() == String.class) {
      return (String) obj;
    }
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.error("json序列化出错：" + obj, e);
      return null;
    }
  }

  @Nullable
  public static <T> T parse(String json, Class<T> tClass) {
    try {
      return mapper.readValue(json, tClass);
    } catch (IOException e) {
      logger.error("json解析出错：" + json, e);
      return null;
    }
  }

  @Nullable
  public static <E> List<E> parseList(String json, Class<E> eClass) {
    try {
      return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
    } catch (IOException e) {
      logger.error("json解析出错：" + json, e);
      return null;
    }
  }

  @Nullable
  public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
    try {
      return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
    } catch (IOException e) {
      logger.error("json解析出错：" + json, e);
      return null;
    }
  }

  @Nullable
  public static <T> T nativeRead(String json, TypeReference<T> type) {
    try {
      return mapper.readValue(json, type);
    } catch (IOException e) {
      logger.error("json解析出错：" + json, e);
      return null;
    }
  }

 /* public static void main(String[] args) {

    //tomap
    *//*String json = "{\"name\":\"ff\",\"age\": \"ee\"}";
    Map<String,String> map = parseMap(json,String.class,String.class);
    System.out.println("map = "+map);
*//*
    //复杂 list里面带map；
    String json = "[{\"name\":\"ff\",\"age\": \"ee\"},{\"name\":\"ffd\",\"age\": \"ee\"}]";
    //匿名类; 点var 加上类型;
      List<Map<String, String>> maps = nativeRead(json, new TypeReference<List<Map<String, String>>>() {

    });

    //soutv时快捷件
    //maps.for是遍历;
    for (Map<String, String> map : maps) {
      System.out.println("map = " + map);
    }


  }*/

}