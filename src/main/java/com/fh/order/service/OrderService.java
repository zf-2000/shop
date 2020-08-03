package com.fh.order.service;

import com.fh.Settle.model.Settle;
import com.fh.cart.model.Cart;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;

import java.util.List;

public interface OrderService {
    ResponseCode buildOrder(List<Cart> cartList, List<Settle> settleList, Integer payType, Member member);
}
