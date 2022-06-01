package com.nowcoder.community.service.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequiredAnnotation;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    //首先要知晓用户是否登陆
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method =handlerMethod.getMethod();
            LoginRequiredAnnotation loginRequiredAnnotation = method.getAnnotation(LoginRequiredAnnotation.class);
            if(loginRequiredAnnotation!=null &&hostHolder.getUser()==null)
            {
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
