package com.use.test;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Component;
import org.springframework.annotation.Scope;
import org.springframework.beans.BeanNameAware;
import org.springframework.constant.BeanScopeConstant;

/**
 * 测试Service
 *
 * @author zhangshh
 * @date 2022-02-16 09:35
 **/
@Component
@Scope(BeanScopeConstant.SCOPE_PROTOTYPE)
public class UseService implements UseInterface, BeanNameAware {
    @Autowired
    private Use2Service use2Service;
    private String beanName;

    @Override
    public void doTest() {
        System.out.println("beanName:" + beanName);
        use2Service.doTest();
    }

    @Override
    public void setBeanName(String var1) {
        this.beanName = var1;
    }
}
