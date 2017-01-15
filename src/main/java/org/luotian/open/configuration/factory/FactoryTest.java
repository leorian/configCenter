package org.luotian.open.configuration.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class FactoryTest implements FactoryBean<String> {
    public String getObject() throws Exception {
        return "test12345";
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    public String toString() {
        return "FactoryTest{}";
    }

    public static void main(String args[]) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("test", new RootBeanDefinition(FactoryTest.class));
        System.out.println(factory.getBean("test"));
        System.out.println(factory.getBean("&test"));
    }
}
