package com.luotian.json.factories;


import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.luotian.json.JSONException;
import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

public class DateObjectFactory implements ObjectFactory {
    static List<DateFormat> dateFormats;

    static {
    	
		dateFormats = new ArrayList<DateFormat>();
		// 2012-02-07 13:05:22
		

		dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
		dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
		dateFormats.add(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"));
    }
    
    public DateObjectFactory() {
  
    	

//    	dateFormats.add( DateFormat.getDateTimeInstance() );
//        dateFormats.add(DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG ) );
//        dateFormats.add(DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM ) );
//        dateFormats.add(DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ) );
//        dateFormats.add( new SimpleDateFormat("EEE MMM d hh:mm:ss a z yyyy") );
//        dateFormats.add( new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy") );
//        dateFormats.add( new SimpleDateFormat("MM/dd/yy hh:mm:ss a"));
//        dateFormats.add( new SimpleDateFormat("MM/dd/yy") );
    }


    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        try {
            if( value instanceof Number ) {
                Date d = (Date)((Class)targetType).newInstance();
                d.setTime( ((Number)value).longValue() );
                return d;
            } else {
                for( DateFormat format : dateFormats ) {
                    try {
                        return format.parse( value.toString() );
                    } catch (ParseException e) {
                        // try next format
                    }
                }
                throw new JSONException( String.format("%s:  Parsing date %s was not recognized as a date format", context.getCurrentPath(), value ) );
            }
        } catch (IllegalAccessException e) {
            throw new JSONException( String.format("%s:  Error encountered trying to instantiate %s", context.getCurrentPath(), ((Class)targetType).getName() ), e);
        } catch (InstantiationException e) {
            throw new JSONException( String.format("%s:  Error encountered trying to instantiate %s.  Make sure there is a public no-arg constructor.", context.getCurrentPath(), ((Class)targetType).getName() ), e);
        }
    }
}
