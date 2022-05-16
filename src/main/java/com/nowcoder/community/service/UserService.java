package com.nowcoder.community.service;

import com.mysql.cj.util.StringUtils;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;


    //注册是要发邮件，其中有个激活码，这块服务要包括域名和项目名
    @Value("${community.path.domain}")
    private String domain;//声明domian来接受这个值
    @Value("${server.servlet.context-path}")
    private String contextPath;//声明domian来接受这个值
    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }

    public Map<String,Object> register(User user)
    {
        Map<String,Object> map=new HashMap<>();
        if(user==null)
        {
            throw new IllegalArgumentException("Parameter couldn't be empty!");
        }
        if(StringUtils.isEmptyOrWhitespaceOnly(user.getUsername()))
        {
            map.put("usernameMsg","The ID can not be empty!");
            return map;
        }
        if(StringUtils.isEmptyOrWhitespaceOnly(user.getPassword()))
        {
            map.put("passwordeMsg","The passwod can not be empty!");
            return map;
        }
        if(StringUtils.isEmptyOrWhitespaceOnly(user.getEmail()))
        {
            map.put("EmailMsg","The Email can not be empty!");
            return map;
        }

        //Verification the register
        User u= userMapper.selectByName(user.getUsername());
        if(u!=null)
        {
            map.put("usernameMsg","The Account already exists!");
            return map;
        }
        u=userMapper.selectByEmail(user.getEmail());
        if(u!=null)
        {
            map.put("EmailMsg","The Email already exists!");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.genUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.genUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //Activation the new ID   templates/mail/activation/activation.html
        Context context= new Context();
        context.setVariable("email",user.getEmail());//这个emial在html文件下声明了，名字需要一致
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.setMailSender(user.getEmail(),"Activation Account",content);

        return null;
    }

    public int activation(int id, String code)
    {
        User user = userMapper.selectById(id);
        if(user.getStatus()==1)
        {
            return ACTIVATION_REPEAT;
        }
        else if(user.getActivationCode().equals(code))
        {
            userMapper.updateStatus(id,1);
            return ACTIVATION_SUCCESS;
        }

        else {
            return ACTIVATION_FAILURE;
        }

    }
}
