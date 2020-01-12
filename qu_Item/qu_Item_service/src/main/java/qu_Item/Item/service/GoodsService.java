package qu_Item.Item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import qu_Item.Item.mapper.SkuMapper;
import qu_Item.Item.mapper.SpuDeatilMapper;
import qu_Item.Item.mapper.SpuMapper;
import qu_Item.Item.mapper.StockMapper;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDeatilMapper spuDeatilMapper;

    @Autowired
    private Catesource catesource;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 实现查询、分页、过滤，并返回结果;
      * @param page
     * @param row
     * @param saleable
     * @param key
     * @return
     */
    public PageResult<Spu> querySpuByPage(Integer page, Integer row, Boolean saleable, String key) {
        //分页;
        PageHelper.startPage(page,row);
        //过滤;
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //默认按时间排序;
        example.setOrderByClause("last_update_time DESC");
        //查询显示;
        List<Spu> list =  spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new quException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //加载显示信息;
        LoadQueryList(list);
        //解析分页的结果;
        PageInfo<Spu> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(),list);
    }

    /**
     * 处理字串显示名称;
     * @param spus
     */

    public void LoadQueryList(List<Spu> spus){
        for( Spu spu : spus){
            // 处理分类的名称;
            //通过Attays.asList拼接成List<Spu>集合;
            List<String> collect = catesource.queryCategotyList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    //在这里获得列List<Category>集合信息, 通过getName获得名称,然后扎转化为list；
                    .stream().map(Calegory::getName).collect(Collectors.toList());
            //在以这个“名字”"/"进行分割;
            spu.setCname(StringUtils.join(collect, "/"));
            //处理品牌的名称;
            spu.setBname(brandService.QueryBrandById(spu.getBrandId()).getName());
        }
    }

    /**
     * 根据spu来存储指定的信息;
     * @param spu
     */
    @Transactional
    public void SaveSpuList(Spu spu) {
        //sku表的存储;
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if (count != 1){
            throw  new  quException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //spuDetail表的存储;
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDeatilMapper.insert(spuDetail);

        //新增
        SaveStockAndSku(spu);

    }


    /**
     * 根据id查询出来详细内容。
     * @param id
     * @return
     */
    public SpuDetail querySpuDetailById(Long id) {
        SpuDetail spuDetail = spuDeatilMapper.selectByPrimaryKey(id);
        if (spuDetail == null){
            throw new quException(ExceptionEnum.GOODSDETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
      * 查询spu下所有的sku;
     * @param spuId
     * @return
     */

    public List<Sku> quereSkuList(Long spuId) {
        //查询spu下的所有sku;
        Sku skus = new Sku();
        skus.setSpuId(spuId);
        List<Sku> count = skuMapper.select(skus);
        if (CollectionUtils.isEmpty(count)){
            throw new quException(ExceptionEnum.GOODSDSKU_NOT_FOUND);
        }
        //查询库存stock;
        //这里采用流的方式，提高性能;
        List<Long> collect = count.stream().map(Sku::getId).collect(Collectors.toList());
        System.out.println("collect = " + collect);
        List<Stock> stockList = stockMapper.selectByIdList(collect);
        if (CollectionUtils.isEmpty(stockList)){
            throw new quException(ExceptionEnum.GOODSDSKU_NOT_FOUND);
        }
        //我们把stock当做一个map,key:sku的id;value:是库存值;
        Map<Long,Integer> longMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId,Stock::getStock));
        System.out.println("longMap = " + longMap);
        count.forEach(s -> s.setStock(longMap.get(s.getId())));
        System.out.println("count = " + count);
        return count;
    }

    /**
     * 来更新spu表;
     * @param spu
     */
    @Transactional
    public void updateSpuList(Spu spu) {
        if (spu.getId() == null){
            throw new quException(ExceptionEnum.GOODSDSKU_NOT_FOUND);
        }
        //查询sku;
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)){
            //删除sku；
            skuMapper.delete(sku);
            //删除Stock;
            List<Long> collect = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            System.out.println("collect = " + collect);
            stockMapper.deleteByIdList(collect);
        }
        //修改spu;
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        int i = spuMapper.updateByPrimaryKeySelective(spu);
        if (i != 1){
            throw new quException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //修改detail;
        spuDeatilMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (i != 1){
            throw new quException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //新增stock和spu
        SaveStockAndSku(spu);
    }

    /**
     * 这是商品库存的增加或修改;
     * @param spu
     */
    @Transactional
    public void SaveStockAndSku(Spu spu){
        //定义库存list
        List<Stock> list = new ArrayList<>();
        //新增sku;
        List<Sku> skus = spu.getSkus();
        System.out.println("skus = " + skus);
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            int count = skuMapper.insert(sku);
            if (count != 1){
                throw  new  quException(ExceptionEnum.GOODS_NOT_FOUND);
            }
            //stock表的存储;
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            list.add(stock);
        }
        //做批量的新增;
        if (CollectionUtils.isEmpty(skus)){
            //这里是防止用户忘记勾选是否启用而导致错误。
            throw  new  quException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        int count = stockMapper.insertList(list);

         if (count != list.size()){
            throw  new  quException(ExceptionEnum.GOODS_NOT_FOUND);
        }
    }


    /**
     * 根据spu的id来删除指定的信息;
     * 这要实现sku,sku_detail,spu表的id删除;
     * @param ids
     * @returna
     */
    @Transactional
    public void DeleteBySpuId(Long ids) {
        //删除spu;
        int count = spuMapper.deleteByPrimaryKey(ids);
        if (count != 1){
            throw new quException(ExceptionEnum.SPU_DELETE_ERROR);
        }
        //删除spuDetail;
        Example example = new Example(SpuDetail.class);
        example.createCriteria().andEqualTo("spuId",ids);
        count = spuDeatilMapper.deleteByExample(example);
        if (count != 1){
            throw new quException(ExceptionEnum.SPUDETAIL_DELETE_ERROR);
        }
        //删除sku;
        Example em = new Example(Sku.class);
        em.createCriteria().andEqualTo("spuId",ids);
        List<Sku> skus = skuMapper.selectByExample(em);
        if (CollectionUtils.isEmpty(skus)){
            throw new quException(ExceptionEnum.GOODSDSKU_NOT_FOUND);
        }
        for (Sku sku : skus) {
            skuMapper.deleteByPrimaryKey(sku.getId());
        }

        //删除stock信息;
        List<Long> longList = skus.stream().map(Sku::getId).collect(Collectors.toList());
        count = stockMapper.deleteByIdList(longList);
        if (count<=0){
            throw new quException(ExceptionEnum.STOCK_DELETE_ERROR);
        }

    }

    /**
     * 根据传过来的id来实现商品的下架的方法;
     * @param id
     * @return
     */
    @Transactional
    public void goodsOutAndUp(Long id) {
        int i=0 , j=0 ;
        Spu spu = spuMapper.selectByPrimaryKey(id);

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId" ,id);

        List<Sku> skus = skuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(skus)){
            throw new quException(ExceptionEnum.GOODSDSKU_NOT_FOUND);
        }
        if (spu.getSaleable()){
            //下架
            spu.setSaleable(false);
             i = spuMapper.updateByPrimaryKey(spu);
            for (Sku sku : skus) {
                sku.setEnable(false);
                j = skuMapper.updateByPrimaryKey(sku);
            }
        }else {
            //上架
            spu.setSaleable(true);
            i = spuMapper.updateByPrimaryKey(spu);
            for (Sku sku : skus) {
                sku.setEnable(true);
                j = skuMapper.updateByPrimaryKey(sku);
            }
        }
        if (i<=0){
            throw new quException(ExceptionEnum.SPU_GOODSOU_ANDUP_ERROR);
        }else if (j<=0){
            throw new quException(ExceptionEnum.SKU_GOODSOUT_ANDUP_ERROR);
        }
    }

    /**
     * 根据id来查询spu
     * * 因为商品详情也需要sku和skudetail这里乐意一次性查出来;
     * @param id
     * @return
     */
    public Spu querySpuById(Long id) {
        //查询spu;
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null){
            throw new quException(ExceptionEnum.SPU_QUERT_ERROR);
        }
        //查询sku；
        spu.setSkus(quereSkuList(id));
        //查询detail;
        spu.setSpuDetail(querySpuDetailById(id));
        //返回;
        return spu;
    }
}
