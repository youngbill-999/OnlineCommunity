package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CoockieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    //所有请求之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket= CoockieUtil.getValue(request,"ticket");//先找到一个叫ticket的cookie，然后获取其value
        if(ticket!=null){
            //find the ticket
           LoginTicket loginTicket = userService.findLoginTicket(ticket);
           //check if the ticket is valid
            if(loginTicket!=null&&loginTicket.getExpired().after(new Date())&&loginTicket.getStatus()==0)
            {
                 //get UserInfo by searching ticket
                 User user = userService.findUserById(loginTicket.getUserId());
                 //store userInfo but here need multi-thread wise
                 hostHolder.setUser(user);

            }
        }
        return true;
    }

    //请求后，模板发送前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }
    //模板发送后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.remove();
    }
}
