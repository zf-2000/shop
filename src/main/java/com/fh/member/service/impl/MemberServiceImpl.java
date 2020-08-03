package com.fh.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ResponseCode;
import com.fh.member.mapper.MemberMaopper;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMaopper memberMaopper;

    @Override
    public ResponseCode queryUserByName(String name) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Member member = memberMaopper.selectOne(queryWrapper);
        if(member==null){
            ResponseCode.success();
        }else {
            return ResponseCode.error("用户已存在！");
        }
        return ResponseCode.success();

    }

    @Override
    public ResponseCode queryUserByPhone(String phone) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member member = memberMaopper.selectOne(queryWrapper);
        if(member==null){
            ResponseCode.success();
        }else {
            return ResponseCode.error("手机号已存在！");
        }
        return ResponseCode.success();

    }

    @Override
    public ResponseCode register(Member member) {
        String redisCode = RedisUtil.get(member.getPhone());
        if(redisCode==null){
            return  ResponseCode.error("验证码已失效！");
        }
        if(!redisCode.equals(member.getCode())){
            return ResponseCode.error("验证码错误");
        }
        memberMaopper.insert(member);
        return ResponseCode.success();
    }

    @Override
    public ResponseCode login(Member member) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",member.getName());
        queryWrapper.or();
        queryWrapper.eq("phone",member.getName());
        Member memberDB = memberMaopper.selectOne(queryWrapper);
        //判断用户名或手机号是否存在
        if(memberDB==null){
            return ResponseCode.error("用户名或手机号不存在");
        }
        //判断密码是否正确
        if(!memberDB.getPassword().equals(member.getPassword())){
            return ResponseCode.error("密码错误");
        }
        //账号密码正确之后  生产token返回给前端
        String token = null;
        try {
            //将用户转换为json
            String jsonString = JSONObject.toJSONString(memberDB);
            //
            String encode = URLEncoder.encode(jsonString,"utf-8");
            token = JwtUtil.sign(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseCode.success(token);
    }

    @Override
    public ResponseCode isLogin(HttpServletRequest request) {
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if(member==null){
            return ResponseCode.success();
        }
        return ResponseCode.success();
    }
}
