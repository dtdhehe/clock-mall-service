package com.example.clockmallservice.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.entity.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 15:47
 * @description
 **/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 查询商品列表
     * @param iPage
     * @param wrapper
     * @return
     */
    @Select("select t.*,c.category_name,b.brand_name,b.brand_type from goods t left join category c on c.category_code=t.category_code " +
            "left join brand b on b.id=t.brand_id ${ew.customSqlSegment}")
    IPage<Goods> queryGoodsList(IPage<Goods> iPage, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询首页商品
     * @param iPage
     * @param wrapper
     * @return
     */
    @Select("select t.*,c.category_name,b.brand_name,b.brand_type from goods t left join category c on c.category_code=t.category_code " +
            "left join brand b on b.id=t.brand_id ${ew.customSqlSegment}")
    IPage<Goods> queryGoodsListForHome(IPage<Goods> iPage, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询热门商品
     * @param limit
     * @return
     */
    @Select("SELECT g.*,SUM(t.buy_quantity) goods_count from orders_info t\n" +
            "LEFT JOIN goods g ON t.goods_id = g.id\n" +
            "WHERE g.valid_flag = '1'\n" +
            "GROUP BY t.goods_id\n" +
            "ORDER BY goods_count DESC\n" +
            "LIMIT #{limit}")
    List<Goods> queryHotGoods(@Param("limit")Integer limit);

    /**
     * 按品牌查询首页商品
     * @param wrapper
     * @return
     */
    @Select("select * from brand ${ew.customSqlSegment}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "goodsList",column = "id",
                    many = @Many(select = "com.example.clockmallservice.mapper.GoodsMapper.queryByBrandId"))
    })
    List<Brand> queryBrandGoods(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 根据品牌id查询最新修改的5条商品
     * @param brandId
     * @return
     */
    @Select("select t.* from goods t where t.brand_id = #{brandId} order by t.update_time desc limit 5")
    List<Goods> queryByBrandId(@Param("brandId")String brandId);
}
