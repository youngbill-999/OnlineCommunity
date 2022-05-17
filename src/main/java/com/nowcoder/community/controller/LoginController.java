package com.nowcoder.community.controller;


import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
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
       session.setAttribute("kaptcha",text);
       response.setContentType("image/png");
       try {
           OutputStream os = response.getOutputStream();
           ImageIO.write(image,"png",os);
       } catch (IOException e) {
           logger.error("Failed to request a verification code!"+e.getMessage());
       }


   }

}