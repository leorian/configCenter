package org.luotian.open.configuration;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;

/**
 * Created by xiezg@317hu.com on 2017/4/19 0019.
 */
public class RPC {

    public void hello(String name) {
        System.out.println(name);
    }

    public static void main(String args[]) {
        RPC rpc = new RPC();
        Object o = String.class;
        try {
            Method method = rpc.getClass().getMethod("hello", (Class<?>) o);
          // rpc.getClass().getMethod(method.getName(), method.getParameterTypes());
           // Object methodStr = JSON.toJSON(method);
           // JSON.toJSONB
            //System.out.println(JSON.parseObject(JSON.toJSONString(methodStr), Method.class));
            System.out.println(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
