package com.nowcoder.community.controller.advice;


import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//Spring boot 统一的controller 端异常处理
@ControllerAdvice(annotations = Controller.class)//只处理带有controller注解的bean，因为服务器结构的原因，所有的报错最终都会反映到顶层controller，从而被捕获
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)//ControllerAdvice下处理异常的方法
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.error("There is something wrong with server:"+e.getMessage());
            for(StackTraceElement element:e.getStackTrace()){
                logger.error(element.toString());
            }

            //当发现错误时，根据同步或者异步的请求，服务器应该有两种重定向机制，ajax的xml应该返回的是json字符
            String xRequestedWith = request.getHeader("x-requested-with");
            if("XMLHttpRequest".equals(xRequestedWith)){
                response.setContentType("application/;charset=utf-8");
                //在页面上显示提示框
                PrintWriter writer = response.getWriter();
                writer.write(CommunityUtil.getJSONString(1,"Server response wrong"));
            }
            else {
                response.sendRedirect(request.getContextPath()+"/error");
            }
    }
}
