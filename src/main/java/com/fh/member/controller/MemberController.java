package com.fh.member.controller;

import com.fh.common.Ignore;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("member")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    //根据名城进行查询啥
    @RequestMapping("queryUserByName")
    @Ignore
    public ResponseCode queryUserByName(String name){
        return memberService.queryUserByName(name);
    }

    //根据手机号进行查询
    @RequestMapping("queryUserByPhone")
    @Ignore
    public ResponseCode queryUserByPhone(String phone){
        return memberService.queryUserByPhone(phone);
    }

    //注册
    @RequestMapping("register")
    @Ignore
    public ResponseCode register(Member member){
        return memberService.register(member);
    }

    //登录
    @RequestMapping("login")
    @Ignore
    public ResponseCode login(Member member){
        return memberService.login(member);
    }

    //查询用户是否已经登录
    @RequestMapping("isLogin")
    public ResponseCode isLogin(HttpServletRequest request){
        return memberService.isLogin(request);
    }

    @RequestMapping("zhuxiao")
    public ResponseCode zhuxiao(HttpServletRequest request){
        //让Token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.Token_KEY);
        RedisUtil.del(SystemConstant.Token_KEY+token);
        //清除session中的用户信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return ResponseCode.success();
    }
}
