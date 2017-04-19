package com.luotian.json.factories;


import java.lang.reflect.Type;

import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

public class BooleanObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        return (Boolean)value;
    }
}
