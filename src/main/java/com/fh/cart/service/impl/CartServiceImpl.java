package com.fh.cart.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;


    @Override
    public ResponseCode buyShop(Integer productId,Integer count,Member member) {
        //验证商品是否存在
        Product product = productService.selectById(productId);
        if(product==null){
            return ResponseCode.error("该商品不存在");
        }

        //验证商品是否上架
        if(product.getStatus()==2){
            return ResponseCode.error("该商品已下架");
        }

        //验证购物车中是否有该商品
        boolean exist = RedisUtil.exist(SystemConstant.CATR_KEY + member.getId(), productId.toString());
        if(!exist){
            //如果购物车中没有该商品   则加入购物车
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setCount(count);
            cart.setName(product.getName());
            cart.setPrice(product.getPrice());
            cart.setFileImage(product.getFilgImage());
            String cateJson = JSONObject.toJSONString(cart);
            //key field value   会员ID:member 商品ID:productId  商品:productId
            RedisUtil.hset(SystemConstant.CATR_KEY+member.getId(),productId.toString(),cateJson);
        }else {
            //如果存在 则修改数量
            String productJson = RedisUtil.hget(SystemConstant.CATR_KEY + member.getId(), productId.toString());
            Cart cart = JSONObject.parseObject(productJson, Cart.class);
            cart.setCount(cart.getCount()+count);
            String cateJson = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CATR_KEY+member.getId(),productId.toString(),cateJson);
        }
        return ResponseCode.success();
    }


    //查询购物车中的数量
    @Override
    public ResponseCode queryCartProductCount(Member member) {
        List<String> stringList = RedisUtil.hget(SystemConstant.CATR_KEY + member.getId());
        long totalCount = 0;
        if(stringList != null && stringList.size()>0) {
            for (String s : stringList) {
                Cart cart = JSONObject.parseObject(s, Cart.class);
                totalCount+=cart.getCount();
            }
        }else {
            ResponseCode.success(0);
        }
        return ResponseCode.success(totalCount);
    }

    //查询购物车信息
    @Override
    public ResponseCode queryList(Member member) {
        List<String> stringList = RedisUtil.hget(SystemConstant.CATR_KEY + member.getId());
        List<Cart> cartList = new ArrayList<>();
        if(stringList != null && stringList.size()>0) {
            for (String s : stringList) {
                Cart cart = JSONObject.parseObject(s, Cart.class);
                cartList.add(cart);
            }
        }else {
            ResponseCode.success("购物车没得数据，请前往首页添加数据");
        }
        return ResponseCode.success(cartList);
    }

    @Override
    public ResponseCode deleteProduct(Integer productId,Member member) {
        RedisUtil.hdel(SystemConstant.CATR_KEY+member.getId(),productId.toString());
        return ResponseCode.success();
    }

    @Override
    public ResponseCode deleteAllProduct(String deleteList,Member member) {
        RedisUtil.hdel(SystemConstant.CATR_KEY+member.getId(),deleteList);
        return ResponseCode.success();
    }
}
