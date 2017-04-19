package com.luotian.json;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class PropertyDescriptor {

	String name;
	Method readMethod;
	Method writeMethod;
	Type propertyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	
	@Override
	public String toString() {
		return "PropertyDescriptor [name=" + name + ", propertyType="
				+ getPropertyType() + "]";
	}

	public Type getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Type propertyType) {
		this.propertyType = propertyType;
	}

}
