package com.guoshengkai.gpt.cn.core;

import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: 郭胜凯
 * @Date: 2019-05-29 19:43
 * @Email 719348277@qq.com
 * @Description: Spring Boot 程序全局容器管理工具类
 */
@Component
public class SpringBootApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    protected Logger logger = LoggerFactory.getLogger(SpringBootApplicationUtil.class);

    public static<T> List<T> getBeanListByAnnotation(Class<? extends Annotation> annotation, Class<T> clazz){
        List<T> beanList = new LinkedList<>();
        String[] beanNamesForAnnotation = getApplicationContext().getBeanNamesForAnnotation(annotation);
        for (String beanName : beanNamesForAnnotation) {
            Object bean = getApplicationContext().getBean(beanName);
            beanList.add((T) bean);
        }
        return beanList;
    }

    public static<T> List<T> getBeanListBySuper(Class<?> superClass, Class<T> clazz){
        List<T> beanList = new LinkedList<>();
        String[] beanNamesForAnnotation = getApplicationContext().getBeanNamesForType(superClass);
        for (String beanName : beanNamesForAnnotation) {
            Object bean = getApplicationContext().getBean(beanName);
            if (superClass.isAssignableFrom(bean.getClass())){
                beanList.add((T) bean);
            }
        }
        return beanList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringBootApplicationUtil.applicationContext == null) {
            SpringBootApplicationUtil.applicationContext = applicationContext;
        }
        logger.info("---------------------------------------------------------------------");
        logger.info("------------SpringBootApplicationUtil Init Successfully--------------");
        logger.info("---------------------------------------------------------------------");
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     *      Bean Name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     * @param clazz
     *      预获取对象的Class
     * @param <T>
     *      对象泛型
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     *      对象名称
     * @param clazz
     *      对象类型
     * @param <T>
     *      对象泛型
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 将对象注册到Spring托管
     * @param name
     *      对象名称
     * @param clazz
     *      对象类型
     * @return
     */
    public static <T> T register(String name, Class<T> clazz){
        AbstractApplicationContext applicationContext =
                (AbstractApplicationContext) SpringBootApplicationUtil.getApplicationContext();
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.rootBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
        return beanFactory.getBean(name, clazz);

    }

    public static Object register(String name, Object obj){
        AbstractApplicationContext applicationContext =
                (AbstractApplicationContext) SpringBootApplicationUtil.getApplicationContext();
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        beanFactory.registerSingleton(name, obj);
        return obj;
    }

    /**
     * 注销托管的对象
     * @param name
     *      实例名称
     */
    public static void destroy(String name){
        AbstractApplicationContext applicationContext =
                (AbstractApplicationContext) SpringBootApplicationUtil.getApplicationContext();
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        beanFactory.destroySingleton(name);
    }

}
