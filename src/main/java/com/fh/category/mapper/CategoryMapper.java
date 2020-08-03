package com.fh.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.category.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryMapper extends BaseMapper<Category> {
    List<Map<String, Object>> queryCategoryList();
}
