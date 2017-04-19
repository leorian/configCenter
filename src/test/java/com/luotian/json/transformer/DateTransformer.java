package com.luotian.json.transformer;


import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.reflect.Type;

import com.luotian.json.JSONException;
import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

/**
 * User: brandongoodin
 * Date: Dec 12, 2007
 * Time: 11:20:39 PM
 */
public class DateTransformer extends AbstractTransformer implements ObjectFactory {

    SimpleDateFormat simpleDateFormatter;

    public DateTransformer(String dateFormat) {
        simpleDateFormatter = new SimpleDateFormat(dateFormat);
    }


    public void transform(Object value) {
        getContext().writeQuoted(simpleDateFormatter.format(value));
    }

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        try {
            return simpleDateFormatter.parse( value.toString() );
        } catch (ParseException e) {
            throw new JSONException(String.format( "Failed to parse %s with %s pattern.", value, simpleDateFormatter.toPattern() ), e );
        }
    }
}
