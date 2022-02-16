package org.springframework.beans;


/**
 * BeanPostProcessor 初始化前后接口
 *
 * @author zhangshh
 * @date 2022-02-16 14:09
 **/

public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
