package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 定义包范围扫描注解
 *
 * @author zhangshh
 * @date 2022-02-16 09:47
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {
    String value() default "";
}
