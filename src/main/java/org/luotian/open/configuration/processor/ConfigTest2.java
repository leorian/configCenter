package org.luotian.open.configuration.processor;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

//class User{
//    //示例类
//}

class MySelector implements ImportSelector{
    //自定义一个ImportSelector实现类
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //会生成两个Bean（java.util.Date和User）
        return new String[]{"org.luotian.open.configuration.processor.User"};
    }
}
@Configuration
@Import({MySelector.class})
public class ConfigTest2 {
    public static void main(String[] args) {
        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        registry.registerBeanDefinition("test", new RootBeanDefinition(ConfigTest2.class));

        postProcessor.postProcessBeanDefinitionRegistry(registry);

        System.out.println(registry.getBeanDefinition("test"));//
        System.out.println(registry.getBeanDefinition("org.luotian.open.configuration.processor.User"));//可以输出
       // System.out.println(registry.getBeanDefinition("java.util.Date"));//可以输出
    }
}