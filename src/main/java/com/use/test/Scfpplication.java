package com.use.test;

import org.springframework.context.AnnotationConfigApplicationContext;

/**
 * 定义框架启动类
 *
 * @author zhangshh
 * @date 2022-02-16 09:34
 **/
public class Scfpplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UseInterface useService = (UseInterface) context.getBean("useService");
        useService.doTest();

    }
}
