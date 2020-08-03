package com.fh.category.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fh.category.mapper.CategoryMapper;
import com.fh.category.service.CategoryService;
import com.fh.common.ResponseCode;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseCode queryCategoryList() {
        boolean exist = RedisUtil.exist(SystemConstant.REDIS_CATEGORY_KEY);
        //判断该数据是否存在该Redis中
        if(exist){
            String s = RedisUtil.get(SystemConstant.REDIS_CATEGORY_KEY);
            List<Map> mapList = JSONArray.parseArray(s, Map.class);
            return ResponseCode.success(mapList);
        }
        List<Map<String,Object>> allList = categoryMapper.queryCategoryList();
        List<Map<String,Object>> parentList = new ArrayList<Map<String, Object>>();
        for (Map map : allList) {
            if (map.get("pid").equals(0)) {
                parentList.add(map);
            }
        }
        queryChildren(parentList,allList);
        String toJSONString = JSONArray.toJSONString(parentList);
        //把查出来的category数据，放到Redis中
        RedisUtil.set(SystemConstant.REDIS_CATEGORY_KEY,toJSONString);
        return ResponseCode.success(parentList);

    }

    public void  queryChildren(List<Map<String,Object>> parentList,List<Map<String,Object>> allList){
        for (Map<String ,Object> pmap : parentList){
            List<Map<String,Object>> childrenList = new ArrayList<Map<String,Object>>();
            for(Map<String ,Object> amap : allList){
                if(pmap.get("id").equals(amap.get("pid"))){
                    childrenList.add(amap);
                }
            }
            if(childrenList!=null && childrenList.size()>0){
                pmap.put("children",childrenList);
                queryChildren(childrenList,allList);
            }
        }
    }
}
