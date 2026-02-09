package com.easylive.admin.intersecptor;

import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.ResponseEnum;
import com.easylive.exception.BaseException;
import com.easylive.exception.BusinessException;
import com.easylive.utils.StringTools;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

@Component
public class AppInterceptor implements HandlerInterceptor {
    private final static String URL_ACCOUNT = "/account";
    private final static String URL_FILE = "/file";

    @Resource
    private RedisComponent redisComponent;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handler 的目的：知道当前请求要访问的方法
        if (null==handler) {
            return false;
        }
        // 如果不是访问 controller 层，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 访问用户登录等操作 直接放行
        if (request.getRequestURL().toString().contains(URL_ACCOUNT)) {
            return true;
        }
//        String token = request.getHeader(Constants.TOKEN_ADMIN);
        String token = getTokenFromCookie(request);
        if (request.getRequestURL().toString().contains(URL_FILE)) {
            // 如果是请求是获取图片 没有办法从 header 里拿到，只能从 cookie 中去获取
            token = getTokenFromCookie(request);
        }
        if (StringTools.isEmpty(token)) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        Object sessionObj = redisComponent.getTokenInfo4Admin(token);
        if (null == sessionObj) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        return true;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.TOKEN_ADMIN)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
