package com.luotian.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Introspector {

	static Map<Class, BeanInfo> beanInfoMap = new HashMap<Class, BeanInfo>();

	/**
	 * Introspect on a Java Bean and learn about all its properties, exposed
	 * methods, and events.
	 * <p>
	 * If the BeanInfo class for a Java Bean has been previously Introspected
	 * then the BeanInfo class is retrieved from the BeanInfo cache.
	 * 
	 * @param beanClass
	 *            The bean class to be analyzed.
	 * @return A BeanInfo object describing the target bean.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 * @see #flushCaches
	 * @see #flushFromCaches
	 */

	public static BeanInfo getBeanInfo(Class<?> beanClass)
			throws IntrospectionException {

		// Not to synchronize to speed up.

		BeanInfo beanInfo = beanInfoMap.get(beanClass);
		if (beanInfo == null) {
			beanInfo = new Introspector().introspect(beanClass);
			beanInfoMap.put(beanClass, beanInfo);
		}
		return beanInfo;

	}

	BeanInfo introspect(Class<?> beanClass) {

		BeanInfo beanInfo = new BeanInfo();
		Map<String, PropertyDescriptor> propertyDescriptorMap = new TreeMap<String, PropertyDescriptor>();

		Method[] methods = beanClass.getMethods();

		for (Method method : methods) {
			PropertyDescriptor propertyDescriptor = toPropertyDescriptor(
					beanClass, method);
			if (propertyDescriptor != null) {
				propertyDescriptorMap.put(propertyDescriptor.getName(),
						propertyDescriptor);
			}
		}

		int size = propertyDescriptorMap.size();
		PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[size];
		int i = 0;
		for (Entry<String, PropertyDescriptor> entry : propertyDescriptorMap
				.entrySet()) {
			propertyDescriptors[i] = entry.getValue();
			i++;
		}

		beanInfo.setPropertyDescriptors(propertyDescriptors);

		return beanInfo;
	}

	PropertyDescriptor toPropertyDescriptor(Class<?> beanClass, Method method) {
		if (!isGetterMethod(method)) {
			return null;
		}

		// check whether has writer method.
		String methodName = method.getName();
		String propertyName = null;
		if (methodName.startsWith("is")) {
			propertyName = methodName.substring(2);
		} else { // assume as set.
			propertyName = methodName.substring(3);
		}

		Method setterMethod = null;
		try {
			setterMethod = beanClass.getMethod("set" + propertyName,
					new Class<?>[] { method.getReturnType() });

			if (!isSetterMethod(setterMethod)) {
				setterMethod = null;
			}

		} catch (Exception e) {
			// ignore it.
		}

		// Only check the user defined property
		if (checkField(beanClass, propertyName)) {
			return createPropertyDescriptor(method, propertyName, setterMethod);
		} else {
			return null;
		}
	}

	boolean checkField(Class<?> beanClass, String propertyName) {

		String fieldName = propertyName.substring(0, 1).toLowerCase()
				+ propertyName.substring(1);


		if ("class".equals(fieldName)) {
			return true;
		}

		while (beanClass != Object.class) {

			try {
				Field declaredField = beanClass.getDeclaredField(fieldName);
				int mod = declaredField.getModifiers();
				if (Modifier.isTransient(mod) || Modifier.isStatic(mod)) {
					return false; // skip transient and static field.
				}
				return true;
			} catch (Exception e2) {
				beanClass = beanClass.getSuperclass();
				if (beanClass == null) {
					return true;
				}			
			}
		}
		
		System.err.println("Introspector: No field was found: " + fieldName);
		// No field was found, give up, return true.
		return true;

	}

	private PropertyDescriptor createPropertyDescriptor(Method method,
			String propertyName, Method setterMethod) {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor();
		propertyDescriptor.setName(uncapitalize(propertyName));
		propertyDescriptor.setPropertyType(method.getGenericReturnType());
		propertyDescriptor.setReadMethod(method);
		propertyDescriptor.setWriteMethod(setterMethod);
		return propertyDescriptor;
	}

	String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen)
				.append(Character.toLowerCase(str.charAt(0)))
				.append(str.substring(1)).toString();
	}

	boolean isGetterMethod(Method method) {
		int modifiers = method.getModifiers();
		if (Modifier.isStatic(modifiers)) {
			return false;
		}

		if (!Modifier.isPublic(modifiers)) {
			return false;
		}

		String name = method.getName();

		if (method.getParameterTypes().length != 0) {
			return false;
		}

		if (name.startsWith("get")) {

			return isUpperCaseAfterPrefix(name, "get");
		}

		if (name.startsWith("is")) {
			Class<?> returnType = method.getReturnType();

			if (returnType == Boolean.class || returnType == Boolean.TYPE) {
				return isUpperCaseAfterPrefix(name, "is");

			}
		}

		return false;

	}

	boolean isSetterMethod(Method method) {
		int modifiers = method.getModifiers();
		if (Modifier.isStatic(modifiers)) {
			return false;
		}

		if (!Modifier.isPublic(modifiers)) {
			return false;
		}

		String name = method.getName();

		if (method.getParameterTypes().length != 1) {
			return false;
		}

		if (name.startsWith("set")) {
			return isUpperCaseAfterPrefix(name, "set");
		}
		return false;

	}

	private boolean isUpperCaseAfterPrefix(String name, String prefix) {
		int prefixLength = prefix.length();
		if (name.length() <= prefixLength) {
			return false;
		}

		if (Character.isUpperCase(name.charAt(prefixLength))) {
			return true;
		} else {
			return false;
		}
	}

}
