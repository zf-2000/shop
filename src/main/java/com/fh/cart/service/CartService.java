package com.fh.cart.service;

import com.fh.common.ResponseCode;
import com.fh.member.model.Member;

public interface CartService {
    ResponseCode buyShop(Integer productId, Integer count, Member member);

    ResponseCode queryCartProductCount(Member member);

    ResponseCode queryList(Member member);

    ResponseCode deleteProduct(Integer productId,Member member);

    ResponseCode deleteAllProduct(String deleteList, Member member);
}
