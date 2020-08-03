package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;
import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    @Select("select count(*) from t_product")
    Long queryTotalCount();

    @Select(" select * from t_product order by id desc limit #{start},#{pageSize}")
    List<Product> queryList(@Param("start")long start,@Param("pageSize")long pageSize);

    Long updateAmount(@Param("id") Integer id,@Param("count") Integer count);
}
