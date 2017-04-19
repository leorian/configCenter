package com.luotian.json.factories;

import java.lang.reflect.Type;

import com.luotian.json.Base64;
import com.luotian.json.ObjectFactory;
import com.luotian.json.ObjectBinder;

public class ByteArrayObjectFactory implements ObjectFactory {

    public Object instantiate(ObjectBinder context, Object value,
            Type targetType, Class targetClass) {

        return Base64.decodeBase64((String) value);
    }
}
