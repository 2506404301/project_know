package qu_Item.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qu.item.unity.SecKillSku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;
import qu_Item.common.vo.PageResult;
import qu_Item.order.mapper.SecKillSkuMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SecKillSkuService {

    @Autowired
    private SecKillSkuMapper secKillSkuMapper;

    public PageResult<SecKillSku> querySecKillSkuByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(SecKillSku.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("skuId", "%" + key + "%").andLike("seckillPrice", "%" + key + "%");
        }
        //排序
        example.setOrderByClause("sku_id DESC");
        //查询
        List<SecKillSku> secKillSkus = secKillSkuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(secKillSkus)) {
            throw new quException(ExceptionEnum.SECKILLSKU_NOT_FOUND);
        }
        //解析结果
        PageInfo<SecKillSku> secKillSkuPageInfo = new PageInfo<>(secKillSkus);
        //返回
        return new PageResult<>(secKillSkuPageInfo.getTotal(), secKillSkus);
    }

    public void deleteSeckillsku(Long skuId) {
        //校验id是否存在
        if (!StringUtils.isNotBlank(String.valueOf(skuId))) {
            throw new quException(ExceptionEnum.DELETE_SECKILLSKU_ERROR);
        }
        SecKillSku secKillSku = new SecKillSku();
        secKillSku.setSkuId(skuId);
        int i = secKillSkuMapper.delete(secKillSku);
        if (i != 1) {
            throw new quException(ExceptionEnum.DELETE_SECKILLSKU_ERROR);
        } else {
            System.out.println("删除成功");
        }
    }

    public void saveSecKillSku(SecKillSku secKillSku) {
        int i = secKillSkuMapper.insert(secKillSku);
        if (i != 1) {
            throw new quException(ExceptionEnum.SECKILLSKU_INSERT_ERROR);
        }
    }

    public void updateSecKillSku(SecKillSku secKillSku) {
        int i = secKillSkuMapper.updateByPrimaryKeySelective(secKillSku);
        if (i != 1) {
            throw new quException(ExceptionEnum.SECKILLSKU_UPDATE_ERROR);
        }
    }
}
