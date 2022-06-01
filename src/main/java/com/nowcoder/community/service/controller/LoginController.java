package com.nowcoder.community.service.controller;

import org.apache.commons.lang3.StringUtils;
import com.google.code.kaptcha.Producer;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    private  static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegister(){
        return "/site/register";
    }
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLogin(){
        return "/site/login";
    }
    @Autowired
    UserService userService;
    @RequestMapping(path="/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if(map==null || map.isEmpty()){
            model.addAttribute("msg","Your registration was success, please check your email to activate!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordeMsg"));
            model.addAttribute("EmailMsg",map.get("EmailMsg"));
            return "/site/register";
        }
    }
    @RequestMapping(path="/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId")int userId, @PathVariable("code")String code){
        int result = userService.activation(userId,code);
        if(result==ACTIVATION_SUCCESS) {
            model.addAttribute("msg","Activation Successful! You can login now!");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","Invalid Activation! Because this account was already activated!");
            model.addAttribute("target","/index");
        }
        else{
            model.addAttribute("msg","Activation Failed!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
    @Autowired
    private Producer producer;
    @RequestMapping(path="/kaptcha",method = RequestMethod.GET)

    public void getKaptcha(HttpServletResponse response, HttpSession session)
   {
            String text = producer.createText();
            BufferedImage image = producer.createImage(text);
       session.setAttribute("kaptcha",text);//store verification code into session
       response.setContentType("image/png");
       try {
           OutputStream os = response.getOutputStream();
           ImageIO.write(image,"png",os);
       } catch (IOException e) {
           logger.error("Failed to request a verification code!"+e.getMessage());
       }
   }

   @RequestMapping(path = "/login", method=RequestMethod.POST)
   /*
   * 执行逻辑， 用户在浏览器中访问/login，界面，最开始是访问最上面用get方法标注的函数，在用户填登陆信息后则切换到
   * 该方法中，login.html文件中标注了传入的参数username，password。。。，再调用该方法实现相关操作
   * */
    public String login(String username, String password, String code, boolean rememberme,
   Model model, HttpSession session,HttpServletResponse response){
        //get verification code from last func
         String kaptcha = (String)session.getAttribute("kaptcha");
         if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
               model.addAttribute("codeMsg","Incorrect Verification Code!");
               return "/site/login";
         }

         //check id and password 这里涉及到用户是否点击保存登录状态，不同的选择有不同的expired time
         int expiredSeconds=rememberme?REMEMBER_EXPIRED_SECOND:DEFAULT_EXPIRED_SECOND;
         Map<String,Object> map=userService.login(username,password,expiredSeconds);
         if(map.containsKey("ticket")){
             Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
             cookie.setPath(contextPath);
             cookie.setMaxAge(expiredSeconds);
             response.addCookie(cookie);
             return "redirect:/index";
         }else{
             model.addAttribute("usernameMsg",map.get("usernameMsg"));
             model.addAttribute("passwordMsg",map.get("passwordMsg"));
             return "/site/login";
         }
   }
   @RequestMapping(path = "logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
             userService.logOut(ticket);
             return "redirect:/login";
   }
}