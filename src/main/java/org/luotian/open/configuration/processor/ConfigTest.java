package org.luotian.open.configuration.processor;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;

import java.util.Date;

@Configuration
class User{
   //示例类
}
@Configuration
@Import({User.class})
public class ConfigTest {

    @Bean
    Date date() {
        return new Date();
    }

    public static void main(String[] args) {
        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        registry.registerBeanDefinition("test", new RootBeanDefinition(ConfigTest.class));

        //处理BeanDefinitionRegistry
        postProcessor.postProcessBeanDefinitionRegistry(registry);
        //输出结果
        System.out.println(registry.getBeanDefinition("test"));//可以输出
        System.out.println(registry.getBeanDefinition("date"));//可以输出
        System.out.println(registry.getBeanDefinition("org.luotian.open.configuration.processor.User"));//可以输出
    }
}