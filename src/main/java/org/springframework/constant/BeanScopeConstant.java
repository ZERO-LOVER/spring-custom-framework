package org.springframework.constant;

/**
 * bean作用域常量
 *
 * @author zhangshh
 * @date 2022-02-16 11:00
 **/
public class BeanScopeConstant {
    /**
     * singleton
     */
    public static final String SCOPE_SINGLETON = "singleton";

    public static final String SCOPE_PROTOTYPE = "prototype";

    public static final String SCOPE_REQUEST = "request";

    public static final String SCOPE_SESSION = "session";

    public static final String SCOPE_GLOBAL_SESSION = "globalSession";
}
