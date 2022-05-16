package com.nowcoder.community.util;

import com.mysql.cj.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommunityUtil {
    public static String genUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //对注册的密码进行MD5加密
    public static String md5(String key)//key is the user input password
    {
        if(StringUtils.isEmptyOrWhitespaceOnly(key))
        {return null;}
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
