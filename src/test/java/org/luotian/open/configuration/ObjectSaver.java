package org.luotian.open.configuration;

import com.luotian.json.JSONDeserializer;
import com.luotian.json.JSONSerializer;

import java.io.*;
import java.lang.reflect.Method;

public class ObjectSaver {

    public static void main(String[] args) throws Exception {
        JSONSerializer jsonSerializer = new JSONSerializer();
        JSONDeserializer<Object> deserializer = new JSONDeserializer<Object>();

        /*其中的  D:\\objectFile.obj 表示存放序列化对象的文件*/
        RPC rpc = new RPC();
        Object o = String.class;
        try {
            Method method = rpc.getClass().getMethod("hello", (Class<?>) o);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("D:\\objectFile.obj"));
            out.writeObject(method.getParameterTypes());
            out.close();
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("D:\\objectFile.obj"));
            Object obj3 =  in.readObject();

            Method method1 = rpc.getClass().getMethod("hello", (Class<?>[]) obj3);
            method1.invoke(rpc, "luotian");
            in.close();
            byte[] bytes = jsonSerializer.deepSerialize(method.getParameterTypes()).getBytes("utf-8");
            Method method2 = rpc.getClass().getMethod("hello", (Class<?>[]) deserializer.deserialize(new String(bytes, "utf-8"), Class[].class));
            method2.invoke(rpc, "luotian");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        


    }
}

class Customer implements Serializable {
    private String name;
    private int age;
    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "name=" + name + ", age=" + age;
    }
}