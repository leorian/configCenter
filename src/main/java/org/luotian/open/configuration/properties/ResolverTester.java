package org.luotian.open.configuration.properties;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.HashMap;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class ResolverTester {
    public static void main(String args[]) {
        MutablePropertySources sources = new MutablePropertySources();
        sources.addLast(new MapPropertySource("map", new HashMap<String, Object>(){
            {
                put("name", "wang");
                put("age",12);
            }
        }));
        PropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
        System.out.println(resolver.containsProperty("name"));
        System.out.println(resolver.getProperty("age"));
        System.out.println(resolver.resolvePlaceholders("My name is ${name} .I am ${age}."));
    }
}
