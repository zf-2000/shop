package com.fh.Interceptor;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.SystemConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInterceptory implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //处理客户端传过来的自定义头信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,mtoken,content-type");
        //处理客户端发过来的put，delete
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT,POST,DELETE,GET");

        //获取方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法上是否有该注解   如果有则放过拦截
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }
        //获取请求头里面的token
        String token = request.getHeader("x-auth");
        //如果Header没有token  返回给前台登陆
        if(StringUtils.isEmpty(token)){
            throw new LoginException();
        }
        //验证token
        boolean res = JwtUtil.verify(token);
        if(res){
            String userString = JwtUtil.getUser(token);
            String jsonUser = URLDecoder.decode(userString, "utf-8");
            Member member = JSONObject.parseObject(jsonUser, Member.class);
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,member);
        }else {
            throw new LoginException();
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
