package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//表示这个注解可以写在方法之上用来描述方法
@Retention(RetentionPolicy.RUNTIME)//有效的时机
public @interface LoginRequiredAnnotation {
}
