package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 定义Component Bean识别注解
 *
 * @author zhangshh
 * @date 2022-02-16 09:47
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Scope {
    String value() default "";
}
