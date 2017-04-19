package com.luotian.json.factories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.luotian.json.JSONException;
import com.luotian.json.ObjectBinder;
import com.luotian.json.ObjectFactory;

/**
 * Newly added for bind class type.
 * 
 * @author LIU Fangran
 * 
 */
public class ClassObjectFactory implements ObjectFactory {
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		try {

			// Special handling for java.lang.Class.
			// System.out.println(value);

			// There is bug/mis-design in flexjson,
			// flexjson could only correctly handle
			// Class, but gets wrong with Class<?>
			// We need to handle it.
			if (value instanceof ArrayList) {
				// We guess it's Class[].

				ArrayList valueList = (ArrayList) value;
				Class<?>[] clazzArray = new Class<?>[valueList.size()];
				int n = clazzArray.length;
				for (int i = 0; i < n; i++) {
					Class<?> clazz = null;
					String className = (String) valueList.get(i);
					clazz = toClass(className);

					clazzArray[i] = clazz;
				}
				return clazzArray;
			} else {
				Class<?> clazz = toClass((String) value);
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			throw new JSONException(
					context.getCurrentPath()
							+ ":There was an exception trying to instantiate an instance of "
							+ targetClass.getName() + ", type of " + value, e);
		}
	}

	Class<?> toClass(String className) throws ClassNotFoundException {
		Class<?> clazz = null;
		if ("boolean".equals(className)) {
			clazz = Boolean.TYPE;
		} else if ("int".equals(className)) {
			clazz = Integer.TYPE;
		} else if ("long".equals(className)) {
			clazz = Long.TYPE;
		} else if ("double".equals(className)) {
			clazz = Double.TYPE;
		} else if ("float".equals(className)) {
			clazz = Float.TYPE;
		} else if ("byte".equals(className)) {
			clazz = Byte.TYPE;
		} else if ("short".equals(className)) {
			clazz = Short.TYPE;
		} else if ("char".equals(className)) {
			clazz = Character.TYPE;
		} else {
			clazz = Class.forName(className);

		}
		return clazz;
	}
}
