package com.use.test;

import org.springframework.annotation.Component;
import org.springframework.beans.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * BeanPostProcessor 使用测试
 *
 * @author zhangshh
 * @date 2022-02-16 14:21
 **/
@Component
public class UseBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        if ("useService".equals(beanName)) {
            // 模拟代理
            Object proxyInstance = Proxy.newProxyInstance(UseBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("切面逻辑");
                    // 执行原本方法
                    return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}
