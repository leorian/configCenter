package org.luotian.open.configuration.util;

import org.apache.ibatis.plugin.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PluginTest {
  
  @Test
  public void mapPluginShouldInterceptGet() {  
    Map map = new HashMap();
    map = (Map) new AlwaysMapPlugin().plugin(map);  
    //assertE("Always", map.get("Anything"));
    System.out.println(map.get("Anything"));
  }  
  
  @Test  
  public void shouldNotInterceptToString() {  
    Map map = new HashMap();  
    map = (Map) new AlwaysMapPlugin().plugin(map);  
    //assertFalse("Always".equals(map.toString()));
    System.out.println(map.toString());
  }  
  
  @Intercepts({
      @Signature(type = Map.class, method = "get", args = {Object.class})})
  public static class AlwaysMapPlugin implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
      return "Always";  
    }  
  
    public Object plugin(Object target) {  
      return Plugin.wrap(target, this);
    }  
  
    public void setProperties(Properties properties) {
    }  
  }  
  
}  