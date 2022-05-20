package com.nowcoder.community.util;


import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

//hold different users' info
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){users.set(user);}//单独开辟一个线程，将user存入users
    public User getUser(){return users.get();}
    public void remove(){users.remove();}


}
