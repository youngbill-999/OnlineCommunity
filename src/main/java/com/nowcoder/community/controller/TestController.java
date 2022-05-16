package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.HttpCookie;

@Controller
public class TestController {


    @RequestMapping(path = "/cookie/set", method= RequestMethod.GET)
    @ResponseBody
    public  String testSetCookie(HttpServletResponse response)
    //服务器返回的时候需要携带创建好的cookie，用response携带
    {
        Cookie cookie = new Cookie("name","Cookie");
        cookie.setPath("/community");
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);
        return "cookie set successfully!";
    }
    @RequestMapping(path = "/cookie/set2", method= RequestMethod.GET)
    @ResponseBody
    public  String testSetCookie2(HttpServletResponse response)
    //服务器返回的时候需要携带创建好的cookie，用response携带
    {
        Cookie cookie = new Cookie("name","Cookie2");
        cookie.setPath("/community");
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);
        return "cookie set successfully!";
    }

    @RequestMapping(path = "/cookie/get", method= RequestMethod.GET)
    @ResponseBody
    public  String testGetCookie()
    {
        return "get";
    }


    @RequestMapping(path = "/session/set1", method= RequestMethod.GET)
    @ResponseBody
    public  String testSetSession(HttpSession session)
    {
        session.setAttribute("name","secssion1");
        session.setAttribute("id",1);
        return "set session1";
    }


    @RequestMapping(path = "/session/set2", method= RequestMethod.GET)
    @ResponseBody
    public  String testSetSession2(HttpSession session)
    {
        session.setAttribute("name","secssion2");
        session.setAttribute("id",2);
        return "set session2";
    }

    @RequestMapping(path = "/session/get", method= RequestMethod.GET)
    @ResponseBody
    public  String testGetSession(HttpSession session)
    {

        return "session name"+session.getAttribute("name");
    }

}
