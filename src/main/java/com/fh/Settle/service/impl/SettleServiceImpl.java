package com.fh.Settle.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.Settle.mapper.SettleMapper;
import com.fh.Settle.model.Settle;
import com.fh.Settle.service.SettleService;
import com.fh.common.ResponseCode;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.SystemConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettleServiceImpl implements SettleService {

    @Resource
    private SettleMapper settleMapper;
    @Resource
    private ProductService productService;

    @Override
    public ResponseCode addSettle(Settle settle) {
        settle.setStatus(2);
        settleMapper.insert(settle);
        return ResponseCode.success();
    }

    @Override
    public ResponseCode queryList() {
        QueryWrapper<Settle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Status",2);
        List<Settle> settleList = settleMapper.selectList(queryWrapper);
        return ResponseCode.success(settleList);
    }

    @Override
    public ResponseCode deleteSettle(Integer id) {
        settleMapper.deleteById(id);
        return ResponseCode.success();
    }

    @Override
    public ResponseCode queryStatusList() {
        QueryWrapper<Settle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Status",1);
        List<Settle> selectList = settleMapper.selectList(queryWrapper);
        return ResponseCode.success(selectList);
    }

    @Override
    public ResponseCode updateStatus(Integer id) {
        QueryWrapper<Settle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Status",1);
        List<Settle> selectList = settleMapper.selectList(queryWrapper);
        for (Settle settle : selectList) {
            settleMapper.updateStatus(settle.getId());
        }

        settleMapper.selectUpdateById(id);
        return ResponseCode.success();
    }

    @Override
    public ResponseCode updSettle(Settle settle) {
        settleMapper.updateById(settle);
        return ResponseCode.success();
    }

}
