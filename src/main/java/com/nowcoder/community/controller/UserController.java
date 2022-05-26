package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequiredAnnotation;
import com.nowcoder.community.entity.PassWordChange;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequiredAnnotation
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getUserSetting(){
         return "/site/setting";
     }

     @LoginRequiredAnnotation
     @RequestMapping(path="/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
         if(headerImage==null)
         {
             model.addAttribute("error","you haven't uploaded image!");
             return "/site/setting";
         }
        String fileName = headerImage.getOriginalFilename();
        String surffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(surffix)){
            model.addAttribute("error","Incorrect file format!");
            return "/site/setting";
        }

        //生成随机文件名，防止不同文件重名带来的冲突
        fileName= CommunityUtil.genUUID()+surffix;
        //确定文件路径
        File dest= new File(uploadPath+"/"+fileName);
        try{
            headerImage.transferTo(dest);
        }catch(IOException e)
         {
             logger.error("Unsuccessful Upload!"+e.getMessage());
         }

        //更新当前头像图片访问路径
         //damain+contextpath+“/user/header/xxx.png”
         String imagePath = domain+contextPath+"/user/header/"+fileName;//web请求和服务器内部存储的路径匹配，见下方func
         User user = hostHolder.getUser();
         userService.updateHeader(user.getId(),imagePath);

         return "redirect:/index";
    }
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    //该方法不需要给浏览器返回页面，而是返回图片文件的数据流，
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
          //先将文件名转化成后台的存放路径
          filename = uploadPath+"/"+filename;
          //get format
          String surffix = filename.substring(filename.lastIndexOf("."));
          //response image
          response.setContentType("image/"+surffix);

        try (
                FileInputStream file=new FileInputStream(filename);
                ){
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b=0;
            while((b=file.read(buffer))!=-1)//每次读取最多1024字节存入buffer中，然后返回buffer的长度，如果读到最后，可能会出现不够1024的情况，要用一个b来记录
            {
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
           logger.error("Fault to read image!"+e.getMessage());
        }
    }

    //密码修改模块
    @LoginRequiredAnnotation
    @RequestMapping(path="/changepassword", method = RequestMethod.POST)
    public String updatePassword(Model model,PassWordChange passWordChange){
        User user=hostHolder.getUser();
        int userId = user.getId();
        Map<String,Object> map = userService.updatePass(passWordChange,userId);
        if(map==null || map.isEmpty()){
            model.addAttribute("oldPassMsg","Error");
            return "/site/setting";
        }
        else if(map.get("changeMsg")!=null){
            return "redirect:/logout";
        }
        else {
            model.addAttribute("oldPassMsg",map.get("usernameMsg"));
            model.addAttribute("newPassMsg",map.get("passwordeMsg"));
            return "/site/setting";
        }

    }
}
