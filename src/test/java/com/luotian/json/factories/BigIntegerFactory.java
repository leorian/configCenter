package com.luotian.json.factories;


import java.lang.reflect.Type;
import java.math.BigInteger;

import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

public class BigIntegerFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        return new BigInteger( value.toString() );
    }
}
