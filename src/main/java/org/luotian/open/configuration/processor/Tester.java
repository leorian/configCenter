package org.luotian.open.configuration.processor;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String[] args) throws Exception {
        //创建一个BeanFactory，并注册一个bean，bean的类名用占位符表示
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition("${beanName}");//占位符
        beanDefinition.setScope("${scop}");//占位符
        factory.registerBeanDefinition("myBean", beanDefinition);

        //创建一个PropertySourcesPlaceholderConfigurer，
        //并加入含有占位符字符串对应的 MutablePropertySources
        PropertySourcesPlaceholderConfigurer configurer
                 = new PropertySourcesPlaceholderConfigurer();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beanName", "org.luotian.open.configuration.processor.Tester");//占位符对应
        map.put("scop", RootBeanDefinition.SCOPE_PROTOTYPE);//占位符对应“prototype”
        MutablePropertySources sources = new MutablePropertySources();
        sources.addLast(new MapPropertySource("abc", map));
        configurer.setPropertySources(sources);

        //对 BeanFactory增强，会替换掉占位符
        configurer.postProcessBeanFactory(factory);

        //获取这个bean
        System.out.println(factory.getBeanDefinition("myBean"));
        System.out.println(factory.getBean("myBean"));

    }

    @Override
    public String toString() {
        return "A bean";
    }
}