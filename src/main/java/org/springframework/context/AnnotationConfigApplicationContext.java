package org.springframework.context;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Component;
import org.springframework.annotation.ComponentScan;
import org.springframework.annotation.Scope;
import org.springframework.beans.BeanDefinition;
import org.springframework.beans.BeanNameAware;
import org.springframework.beans.BeanPostProcessor;
import org.springframework.beans.InitializingBean;
import org.springframework.constant.BeanScopeConstant;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring上下文
 *
 * @author zhangshh
 * @date 2022-02-16 09:29
 **/
public class AnnotationConfigApplicationContext {
    private Class appConfig;

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonPoolMap = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AnnotationConfigApplicationContext(Class appConfig) {
        this.appConfig = appConfig;
        // 扫描
        doScan(appConfig);

        // 创建bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            String scope = beanDefinition.getScope();
            switch (scope) {
                case BeanScopeConstant.SCOPE_SINGLETON:
                    Object bean = createBean(beanName, beanDefinition);
                    singletonPoolMap.put(beanName, bean);
                    break;
                case BeanScopeConstant.SCOPE_PROTOTYPE:
                    break;
                case BeanScopeConstant.SCOPE_REQUEST:
                    break;
                case BeanScopeConstant.SCOPE_SESSION:
                    break;
                case BeanScopeConstant.SCOPE_GLOBAL_SESSION:
                    break;
            }
        }

    }

    /**
     * 创建bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clz = beanDefinition.getClz();
        Object newInstance = null;
        try {
            // 无参构造参数实例化对象
            newInstance = clz.getConstructor().newInstance();
            // 获取对象所有属性
            for (Field field : clz.getDeclaredFields()) {
                // 判断属性是否包含Autowired注解，并赋值
                // 先找类型，再找名
                // TODO 存在循环依赖问题
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(newInstance, getBean(field.getName()));
                }
            }
            // 回调
            if (newInstance instanceof BeanNameAware) {
                ((BeanNameAware) newInstance).setBeanName(beanName);
            }

            // 初始化前调用
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                newInstance = beanPostProcessor.postProcessBeforeInitialization(newInstance, beanName);
            }

            // 初始化
            // 判断bean是否实现了InitializingBean接口，如果实现了就调用里面的afterPropertiesSet()方法。
            if (newInstance instanceof InitializingBean) {
                ((InitializingBean) newInstance).afterPropertiesSet();
            }

            // 初始化后调用
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                newInstance = beanPostProcessor.postProcessAfterInitialization(newInstance, beanName);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    /**
     * 扫描
     * 得到BeanDefinitionMap
     * @param appConfig
     */
    private void doScan(Class appConfig) {
        // 拿到扫描路径
        ComponentScan componentScan = (ComponentScan) appConfig.getAnnotation(ComponentScan.class);
        String scanPath = componentScan.value();
        // 扫描路径文件路径格式化
        String path = scanPath.replace(".", "/");
        // 获取配置文件路径位置，从而定位扫描文件路径
        ClassLoader classLoader = appConfig.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            // 判断是否是文件夹
            // 如果是，则扫描
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    // 文件夹需要继续扫描，只到扫描玩所有文件
                } else {
                    try {
                        String absolutePath = f.getAbsolutePath();
                        // 文件路径分隔符统一转换成/识别，支持Linux和Windows操作系统
                        absolutePath = absolutePath.replace("\\", "/");
                        absolutePath = absolutePath.substring(absolutePath.indexOf(path), absolutePath.indexOf(".class"));
                        absolutePath = absolutePath.replace("/", ".");
                        Class<?> clz = classLoader.loadClass(absolutePath);

                        // 解析扫描路径下面的类，判断是否有Component注解
                        if (clz.isAnnotationPresent(Component.class)) {
                            // 判断类是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(clz)) {
                                BeanPostProcessor beanPostProcessorInstance = (BeanPostProcessor) clz.getConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessorInstance);
                            }

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClz(clz);
                            String beanName = clz.getAnnotation(Component.class).value();
                            if (null == beanName || "".equals(beanName)) {
                                beanName = Introspector.decapitalize(clz.getSimpleName());
                            }
                            // 设置Bean的类型
                            if (clz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clz.getAnnotation(Scope.class);
                                String scope = scopeAnnotation.value();
                                beanDefinition.setScope(scope);
                            } else {
                                beanDefinition.setScope(BeanScopeConstant.SCOPE_SINGLETON);
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取Clz
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String scope = beanDefinition.getScope();
        switch (scope) {
            case BeanScopeConstant.SCOPE_SINGLETON:
                Object clzBean = singletonPoolMap.get(beanName);
                if (null == clzBean) {
                    clzBean = createBean(beanName, beanDefinition);
                    singletonPoolMap.put(beanName, clzBean);
                }
                return clzBean;
            case BeanScopeConstant.SCOPE_PROTOTYPE:
                return createBean(beanName, beanDefinition);
            case BeanScopeConstant.SCOPE_REQUEST:
                break;
            case BeanScopeConstant.SCOPE_SESSION:
                break;
            case BeanScopeConstant.SCOPE_GLOBAL_SESSION:
                break;
        }
        return null;
    }
}
