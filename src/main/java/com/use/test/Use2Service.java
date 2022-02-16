package com.use.test;

import org.springframework.annotation.Component;
import org.springframework.annotation.Scope;
import org.springframework.constant.BeanScopeConstant;

/**
 * 测试Service
 *
 * @author zhangshh
 * @date 2022-02-16 09:35
 **/
@Component
@Scope(BeanScopeConstant.SCOPE_PROTOTYPE)
public class Use2Service {
    public void doTest() {
        System.out.println("Use Use2Service");
    }

}
