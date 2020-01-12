package qu_Item.Search.qu_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qu.item.unity.*;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import qu_Item.Search.qu_Repository.GoodsRepository;
import qu_Item.Search.qu_client.AllClient;
import qu_Item.Search.qu_untity.Goods;
import qu_Item.Search.qu_untity.searchParams;
import qu_Item.Search.qu_untity.searchResult;
import qu_Item.common.util.JsonUtils;
import qu_Item.common.vo.PageResult;

import java.util.*;
import java.util.stream.Collectors;

import static qu_Item.common.util.JsonUtils.nativeRead;

@Service
public class GoodService {

    @Autowired
    private AllClient allClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;


    /**
     * 对索引库的构建;
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu){
          // 1 );
          // 查询分类;
          List<Calegory> calegories = allClient.queryCategoryByids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
          List<String> collect = calegories.stream().map(Calegory::getName).collect(Collectors.toList());
          //查询品牌;
          Brand brand = allClient.queryBrandById(spu.getBrandId());
          //收索字段拼接;
          String serviceAll = spu.getTitle()+ StringUtils.join(collect,"/")+brand.getName();
          // 2 );
          //查询sku;
          List<Sku> skus = allClient.querySkuSpuId(spu.getId());

          //对sku进行处理,因为sku数据过于冗杂,只留下部分要用的;
          List<Map<String, Object>> mapList = new ArrayList<>();
          //价格集合
          Set<Long> priceList = new HashSet<>();
          for (Sku sku : skus) {
              Map<String,Object> map = new HashMap<>();
              map.put("id",sku.getId());
              map.put("title",sku.getTitle());
              map.put("price",sku.getPrice());
              /*由于sku有多个，所以多张图片之前以“,”隔开 */
              map.put("image",StringUtils.substringBefore(sku.getImages(),","));
              mapList.add(map);
              priceList.add(sku.getPrice());
          }

          // 3 );
          //对spec处理
          SpuDetail spuDetail = allClient.querySpuDetailById(spu.getId());
          //提取共有属性;
          List<Map<String, Object>>  genericSpecs = nativeRead(spuDetail.getSpecifications(), new TypeReference<List<Map<String,Object>>>() {

          });
          //提取特有属性;
         /* Map<String,Object> specialSpecs = nativeRead(spuDetail.getSpecTemplate(), new TypeReference<Map<String, Object>>() {

          });*/
          //把这个可以搜索的信息保存到map中;
          Map<String,Object> map = new HashMap<>();

          String searchable = "searchable";
          String v = "v";
          String k = "k";
          String options = "options";

          for (Map<String, Object> genSpec : genericSpecs) {
              //获得这个键值对
              List<Map<String,Object>> mapList1 = (List<Map<String, Object>>) genSpec.get("params");
              /*System.out.println("mapList1 = " + mapList1);*/
              for (Map<String, Object> stringObjectMap : mapList1) {
                  //这表示可以搜索;
                  if((Boolean) stringObjectMap.get("searchable")){
                      if (stringObjectMap.get("v") != null){
                          map.put(stringObjectMap.get("k").toString(),stringObjectMap.get("v").toString());
                          /*System.out.println("map的信息是 = " + map);*/
                      }else if (stringObjectMap.get("options")!=null){
                          map.put(stringObjectMap.get("k").toString(),stringObjectMap.get("options"));
                          /*System.out.println("map||options = " + map);*/
                      }
                  }
              }
          }

          Goods goods = new Goods();
          goods.setId(spu.getId());
          goods.setCid1(spu.getCid1());
          goods.setCid2(spu.getCid2());
          goods.setCid3(spu.getCid3());
          goods.setAll(serviceAll);//所有的搜索字段;
          goods.setBrandId(spu.getBrandId());
          goods.setCreateTime(spu.getCreateTime());
          goods.setPrice(priceList);// 所有sku的价格集合;
          goods.setSubTitle(spu.getSubTitle());
          goods.setSpec(map);//规格参数;
          goods.setSkus(JsonUtils.serialize(mapList));//所有sku集合的json对象;

          return goods;

      }

    /**
     * 根据searchParams的值来分页或者过滤返回查询的结果;
     * @param searchParams
     * @return
     */
    public PageResult<Goods> search(searchParams searchParams) {
        //当前页;
        int page = searchParams.getPage() - 1;
        //每页大小;
        int  size = searchParams.getSize();
        /*查询构建起，NativeSearchQueryBuilder*/
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //分页; 默认从第0页开始的，但是前端数据默认为第一页;
        queryBuilder.withPageable(PageRequest.of(page, size));
        //过滤; 这里搜索和过滤不能一块写，通过bool构建;
        QueryBuilder querybasic = basicQueryBuilder(searchParams);
        System.out.println("querybasic = " + querybasic);
        //过滤字段;(要显示的字段)
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        queryBuilder.withQuery(querybasic);
        //聚合分类和品牌
        String categoryName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));

        String brandName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));

        //查询; 聚合结果来查询;
        AggregatedPage<Goods> search = template.queryForPage(queryBuilder.build(), Goods.class);

        //分页的结果;
        /* Page<Goods> search = goodsRepository.search(queryBuilder.build());*/

        //解析结果;
        //:1)这是当前页的结果
        List<Goods> content = search.getContent();
        //总页数;
        int totalPages = search.getTotalPages();
        //总数据大小;
        long totalElements = search.getTotalElements();

        //:2)解析聚合结果;
        Aggregations aggs = search.getAggregations();

        //够照两个函数分别生成解析结果集合；
        System.out.println("aggs.get(categoryName) = " + aggs.get(categoryName));
        List<Calegory> calegoryList = parseCategoryAgg(aggs.get(categoryName));
        System.out.println("calegoryList = " + calegoryList);
        List<Brand> brandList = parseBrandAgg(aggs.get(brandName));

        //完成对应规格参数的聚合;
        List<Map<String,Object>> specs = null;
        if (calegoryList != null ){
            //商品分类存在，并且为1;
            System.out.println("calegoryList.get(0).getId() = " + calegoryList.get(0).getId());
            specs = buildSpecifictionAggs(calegoryList.get(0).getId(),querybasic);
        }
        //返回分页后的结果;
        return new searchResult(totalElements,totalPages,content,calegoryList,brandList,specs);
    }

    /**
     * 封装这个过滤和查询的方法;
     * @param searchParams
     * @return
     */
    private QueryBuilder basicQueryBuilder(searchParams searchParams) {
        //创建bool查询;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询条件;
        boolQuery.must(QueryBuilders.matchQuery("all",searchParams.getKey()));

        Map<String, String> map = searchParams.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            System.out.println("key = " + key);
            if (!key.equals("key")){
                //只有brandID和cid是直接存的,其他规格参数是存的spec.**.**的
                if (!"cid3".equals(key) && !"brandId".equals(key)){
                    key = "spec."+ key  +".keyword";
                }
                String value = entry.getValue();
                boolQuery.filter(QueryBuilders.termQuery(key,value));
            }
        }
        return boolQuery;
    }


    /**
     * 对聚合品牌;
     * @param aggregation
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets().stream()
                    .map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            System.out.println("ids = " + ids);
            List<Brand> brandList = allClient.queryBrandByIds(ids);
            return brandList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对应聚合分类结果;
     * @param aggregation
     * @return
     */
    private List<Calegory> parseCategoryAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets().stream()
                    .map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Calegory> calegoryList = allClient.queryCategoryByids(ids);
            return calegoryList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param cid
     * @param querybasic
     * @return
     */

    private List<Map<String, Object>> buildSpecifictionAggs(Long cid, QueryBuilder querybasic) {
        String s = allClient.QuerySpecificationByCategoryId(cid);
        List<Map<String,Object>> specs = JsonUtils.nativeRead(s, new TypeReference<List<Map<String,Object>>>() {
        });
        List<Map<String,Object>> specs2 = new ArrayList<>();
        List<Map<String,Object>> specs3 = new ArrayList<>();
        //查询需要聚合的规格参数;

        System.out.println("specs = " + specs);

        String searchable = "searchable";
        String k = "k";
        String unit = "unit";


        for (Map<String, Object> spec : specs) {
            List<Map<String,Object>> map = (List<Map<String,Object>>)spec.get("params");
            for (Map<String, Object> stringObjectMap : map) {
                //首先判断是否为共有共享属性;
                if ((boolean)stringObjectMap.get(searchable)){
                     specs2.add(stringObjectMap);
                     System.out.println("specs2 = " + specs2);
                }

            }
        }
        // 2:聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.1带上原有的查询条件;
        queryBuilder.withQuery(querybasic);
        //2.2进行聚合;
        for (Map<String, Object> spec : specs2) {
            System.out.println("spec = " + spec);
                Object d = spec.get(k);
                System.out.println("d = " + d);
                queryBuilder.addAggregation(AggregationBuilders.terms(d.toString())
                        .field("spec."+d.toString()+".keyword"));

        }

       //获取聚合结果;
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        //解析聚合结果;
        Aggregations aggregations = result.getAggregations();
        for (Map<String, Object> spec : specs2) {
                String k1 = spec.get(k).toString();
                Object units = spec.get(unit);
                StringTerms terms = aggregations.get(k1);
                List<String> stringList = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
                System.out.println("stringList = " + stringList);

                Map<String,Object>  strMap = new HashMap<>();

                strMap.put("k",k1);
                strMap.put("unit",units);
                strMap.put("options",stringList);
                System.out.println("strMap = " + strMap);

                specs3.add(strMap);

            }
        return specs3;
    }




    /**
     * 进行spu的修改操作;
     * @param spuId
     */
    public void createItemUpdateOrInsertIndex(Long spuId) {
        //根据spuId查询spu;
        Spu spu = allClient.querySpuById(spuId);
        //查询商品;
        Goods goods = buildGoods(spu);
        //存入索引库;
        goodsRepository.save(goods);
    }

    /**
     * 对索引库进行删除;
     *
     * @param spuId
     */
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId) ;
    }
}
