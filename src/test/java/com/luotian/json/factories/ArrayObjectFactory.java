package com.luotian.json.factories;


import java.lang.reflect.Type;
import java.lang.reflect.Array;
import java.util.List;

import com.luotian.json.JSONException;
import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

public class ArrayObjectFactory implements ObjectFactory {

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        List list = (List) value;
        context.getCurrentPath().enqueue("values");
        try {
            Class memberClass = targetClass.getComponentType() != null ? targetClass.getComponentType() : null;
            if( memberClass == null ) throw new JSONException("Missing concrete class for array.  You might require a use() method.");
            Object array = Array.newInstance( memberClass, list.size() );
            for( int i = 0; i < list.size(); i++ ) {
                Object v = context.bind( list.get(i), memberClass );
                Array.set( array, i, v );
            }
            return array;
        } finally {
            context.getCurrentPath().pop();
        }
    }
}
