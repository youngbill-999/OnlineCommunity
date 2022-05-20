package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CoockieUtil {
    public static  String getValue(HttpServletRequest request, String name)
    {
        if(request==null||name==null)throw new IllegalArgumentException("Fault to get parameter!");
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(name))return cookie.getValue().toString();
            }
        }
        return null;
    }
}
