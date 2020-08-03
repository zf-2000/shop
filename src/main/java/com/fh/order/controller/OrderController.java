package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.Settle.model.Settle;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.Ignore;
import com.fh.common.MemberAnnotation;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("buildOrder")
    @Idempotent
    public ResponseCode buildOrder(String cartParam, String settleParam, Integer payType, @MemberAnnotation Member member){
        List<Cart> cartList = new ArrayList<>();
        List<Settle> settleList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cartParam)){
            cartList = JSONObject.parseArray(cartParam, Cart.class);
        }
        if(StringUtils.isNotEmpty(settleParam)){
           settleList = JSONObject.parseArray(settleParam, Settle.class);
        }
        return orderService.buildOrder(cartList,settleList,payType,member);
    }

    @RequestMapping("getToken")
    public ResponseCode getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ResponseCode.success(mtoken);
    }
}
