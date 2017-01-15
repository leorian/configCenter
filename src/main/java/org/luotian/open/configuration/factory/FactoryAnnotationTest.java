package org.luotian.open.configuration.factory;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

/**
 * Created by xiezhonggui on 17-1-15.
 */
@Controller(value = "OK")
public class FactoryAnnotationTest {
    public static void main(String args[]) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RootBeanDefinition definition = new RootBeanDefinition(FactoryAnnotationTest.class);
        factory.registerBeanDefinition("test", definition);
        System.out.println(Arrays.toString(factory.getBeanNamesForAnnotation(Controller.class)));
        System.out.println(factory.getBeansWithAnnotation(Controller.class));
        System.out.println(factory.findAnnotationOnBean("test", Controller.class));
    }
}
