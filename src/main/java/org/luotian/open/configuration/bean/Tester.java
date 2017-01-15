package org.luotian.open.configuration.bean;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by xiezhonggui on 17-1-15.
 */
@Component("t")
public class Tester {
    public static void main(String args[]) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(Tester.class);
        System.out.println(beanDefinition.getMetadata().getAnnotationTypes());
        System.out.println(beanDefinition.isSingleton());
        System.out.println(beanDefinition.getBeanClassName());
    }
}
