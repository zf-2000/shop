package com.fh.member.service;

import com.fh.common.ResponseCode;
import com.fh.member.model.Member;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {
    ResponseCode queryUserByName(String name);

    ResponseCode queryUserByPhone(String phone);

    ResponseCode register(Member member);

    ResponseCode login(Member member);

    ResponseCode isLogin(HttpServletRequest request);
}
