package org.springframework.beans;

/**
 * BeanDefinition核心类
 *
 * @author zhangshh
 * @date 2022-02-16 11:09
 **/
public class BeanDefinition {
    private Class clz;
    private String scope;
    private boolean isLazy;

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
