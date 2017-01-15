package org.luotian.open.configuration.properties;

import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class Tester {
    public static void main(String args[]) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "wang");
        map.put("age", 23);
        MapPropertySource source_1 = new MapPropertySource("person", map);
        System.out.println(source_1.getProperty("name"));
        System.out.println(source_1.getProperty("age"));
        System.out.println(source_1.getName());
        System.out.println(source_1.containsProperty("class"));
    }
}
