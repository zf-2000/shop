package com.fh.cart.controller;

import com.fh.cart.service.CartService;
import com.fh.common.MemberAnnotation;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("cart")
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("buyShop")
    public ResponseCode buyShop(Integer productId, Integer count , @MemberAnnotation Member member){
        return cartService.buyShop(productId,count,member);
    }

    //查询购物车中的数量
    @RequestMapping("queryCartProductCount")
    public ResponseCode queryCartProductCount(@MemberAnnotation Member member){
        return cartService.queryCartProductCount(member);
    }

    //查询购物车信息
    @RequestMapping("queryList")
    public ResponseCode queryList(@MemberAnnotation Member member){
        return cartService.queryList(member);
    }

    //删除单个商品数据
    @RequestMapping("deleteProduct/{productId}")
    public ResponseCode deleteProduct(@PathVariable("productId") Integer productId ,@MemberAnnotation Member member){
        return cartService.deleteProduct(productId,member);
    }

    //删除多个商品数据
    @RequestMapping("deleteAllProduct")
    public ResponseCode deleteAllProduct(String deleteList ,@MemberAnnotation Member member){
        return cartService.deleteAllProduct(deleteList,member);
    }
}
