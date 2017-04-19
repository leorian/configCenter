package com.luotian.json.factories;

import java.lang.reflect.Type;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.luotian.json.JSONException;
import com.luotian.json.ObjectFactory;
import com.luotian.json.ObjectBinder;

public class ThrowableObjectFactory implements ObjectFactory {

	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		try {
			Object target = instantiate(targetClass,
					(String) ((Map) value).get("message"));
			return context.bindIntoObject((Map) value, target, targetType);
		} catch (InstantiationException e) {
			throw new JSONException(
					context.getCurrentPath()
							+ ":There was an exception trying to instantiate an instance of "
							+ targetClass.getName(), e);
		} catch (IllegalAccessException e) {
			throw new JSONException(
					context.getCurrentPath()
							+ ":There was an exception trying to instantiate an instance of "
							+ targetClass.getName(), e);
		} catch (InvocationTargetException e) {
			throw new JSONException(
					context.getCurrentPath()
							+ ":There was an exception trying to instantiate an instance of "
							+ targetClass.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new JSONException(
					context.getCurrentPath()
							+ ": "
							+ targetClass.getName()
							+ " lacks a no argument constructor.  Flexjson will instantiate any protected, private, or public no-arg constructor.",
					e);
		}
	}

	protected Object instantiate(Class clazz, String message)
			throws IllegalAccessException, InvocationTargetException,
			InstantiationException, NoSuchMethodException {
		try {
			if (message != null && message.length() > 0) {

				Constructor constructor = clazz
						.getDeclaredConstructor(String.class);
				constructor.setAccessible(true);

				return constructor.newInstance(message);
			} else {
				Constructor constructor = clazz.getDeclaredConstructor();
				constructor.setAccessible(true);
				return constructor.newInstance();

			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Can't instantiate a remote exception: Class: " + clazz
							+ ", message: " + message);
		}
	}
}
