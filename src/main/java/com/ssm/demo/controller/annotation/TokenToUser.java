package com.ssm.demo.controller.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER}) // 表示该注解可以用在什么地方 （PARAMETER：参数声明）
@Retention(RetentionPolicy.RUNTIME)  // VM将在运行期间保留注解，因此可以通过反射机制读取注解的信息
@Documented // 将注解包含在Javadoc中
public @interface TokenToUser {
    /**
     * 当前用户在request中的名字
     */
    String value() default "user";
}
