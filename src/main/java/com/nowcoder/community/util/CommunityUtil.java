package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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


    //将文本转化成Json文件，用于前后端的交互
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJSONString(int code, String msg) {
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code) {
        return getJSONString(code,null,null);
    }


}
