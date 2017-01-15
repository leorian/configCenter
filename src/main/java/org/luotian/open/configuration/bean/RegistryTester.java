package org.luotian.open.configuration.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;

import java.util.Arrays;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class RegistryTester {
    public static void main(String args[]) {
        SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        BeanDefinition definition_1 = new GenericBeanDefinition();
        registry.registerBeanDefinition("d1", definition_1);
        BeanDefinition definition_2 = new GenericBeanDefinition();
        registry.registerBeanDefinition("d2", definition_2);
        System.out.println(registry.containsBeanDefinition("d1"));
        System.out.println(registry.getBeanDefinitionCount());
        System.out.println(Arrays.asList(registry.getBeanDefinitionNames()));
    }
}
