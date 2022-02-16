package org.springframework.beans;

/**
 * InitializingBean 初始化方法
 *
 * @author zhangshh
 * @date 2022-02-16 14:05
 **/
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
