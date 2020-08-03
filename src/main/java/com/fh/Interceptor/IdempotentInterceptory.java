package com.fh.Interceptor;

import com.fh.common.Idempotent;
import com.fh.common.Ignore;
import com.fh.common.MyException;
import com.fh.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptory implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法上是否需要验证幂等性   如果有Idempotent 则放过拦截
        if(!method.isAnnotationPresent(Idempotent.class)){
            return true;
        }
        //验证幂等性
        String mtoken = request.getHeader("mtoken");
        if(StringUtils.isEmpty(mtoken)){
            throw new MyException("没有mtoken");
        }
        boolean exist = RedisUtil.exist(mtoken);
        if(!exist){
            throw new MyException("mtoken失效");
        }
        Long  hdel = RedisUtil.del1(mtoken);
        if(hdel==0){
            throw new MyException("重复登陆");
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
