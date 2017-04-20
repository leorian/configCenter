/**
 * Copyright 2007 Charlie Hubbard and Brandon Goodin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.luotian.json;

//!import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.*;

import com.luotian.json.transformer.Transformer;
import com.luotian.json.transformer.TypeTransformerMap;

public class JSONContext {

	private static ThreadLocal<JSONContext> context = new ThreadLocal<JSONContext>() {
		protected JSONContext initialValue() {
			return new JSONContext();
		}
	};

	private String rootName;
	private OutputHandler out;
	private boolean prettyPrint = false;
	private Stack<TypeContext> typeContextStack = new Stack<TypeContext>();

	private int indent = 0;
	private TypeTransformerMap typeTransformerMap;
	private List<PathExpression> pathExpressions;

	private SerializationType serializationType = SerializationType.SHALLOW;

	private ChainedSet visits = new ChainedSet(Collections.EMPTY_SET);
	private LinkedList<Object> objectStack = new LinkedList<Object>();

	private Path path = new Path();

	private boolean serializeWithClassInfo = true;

	public JSONContext() {
	}

	// CONFIGURE SERIALIZATION
	public void serializationType(SerializationType serializationType) {
		this.serializationType = serializationType;
	}

	// CONFIGURE TRANSFORMERS

	/**
	 * Run a transformer on the provided object
	 * 
	 * @param object
	 * @return
	 */
	public void transform(Object object) {

		Transformer transformer = getTypeTransformer(object);
		if (transformer == null) {
			System.err.println("-*- can't find transformer for object["
					+ ((object == null) ? "null" : object.getClass().getName())
					+ "]: " + object);
		}
		transformer.transform(object);

	}

	/**
	 * Retrieves a transformer for the provided object
	 * 
	 * @param object
	 * @return
	 */
	public Transformer getTransformer(Object object) {

		Transformer transformer = getTypeTransformer(object);

		return transformer;

	}

	private Transformer getTypeTransformer(Object object) {
		return typeTransformerMap.getTransformer(object);
	}

	/**
	 * used to pass in configured transformers from the JsonSerializer
	 * 
	 * @param typeTransformerMap
	 */
	public void setTypeTransformers(TypeTransformerMap typeTransformerMap) {
		this.typeTransformerMap = typeTransformerMap;
	}

	// OUTPUT

	/**
	 * configures the context to output JSON with new lines and indentations
	 * 
	 * @param prettyPrint
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public void pushTypeContext(TypeContext contextEnum) {
		typeContextStack.push(contextEnum);
	}

	public void popTypeContext() {
		typeContextStack.pop();
	}

	public TypeContext peekTypeContext() {
		if (!typeContextStack.isEmpty()) {
			return typeContextStack.peek();
		} else {
			return null;
		}
	}

	/**
	 * Set the output handler.
	 * 
	 * @param out
	 */
	public void setOut(OutputHandler out) {
		this.out = out;
	}

	/**
	 * getTransformer output handler
	 * 
	 * @return
	 */
	public OutputHandler getOut() {
		return out;
	}

	/**
	 * write a simple non-quoted value to output
	 * 
	 * @param value
	 */
	public void write(String value) {
		TypeContext currentTypeContext = peekTypeContext();
		if (currentTypeContext != null
				&& currentTypeContext.getBasicType() == BasicType.ARRAY) {
			writeIndent();
		}
		out.write(value);
	}

	public TypeContext writeOpenObject() {
		if (prettyPrint) {
			TypeContext currentTypeContext = peekTypeContext();
			if (currentTypeContext != null
					&& currentTypeContext.getBasicType() == BasicType.ARRAY) {
				writeIndent();
			}
		}
		TypeContext typeContext = new TypeContext(BasicType.OBJECT);
		pushTypeContext(typeContext);
		out.write("{");
		if (prettyPrint) {
			indent += 4;
			out.write("\n");
		}
		return typeContext;
	}

	public void writeCloseObject() {
		if (prettyPrint) {
			out.write("\n");
			indent -= 4;
			writeIndent();
		}
		out.write("}");
		popTypeContext();
	}

	public void writeName(String name) {
		if (prettyPrint)
			writeIndent();
		writeQuoted(name);
		out.write(":");
		if (prettyPrint)
			out.write(" ");
	}

	public void writeComma() {
		out.write(",");
		if (prettyPrint) {
			out.write("\n");
		}
	}

	public TypeContext writeOpenArray() {
		if (prettyPrint) {
			TypeContext currentTypeContext = peekTypeContext();
			if (currentTypeContext != null
					&& currentTypeContext.getBasicType() == BasicType.ARRAY) {
				writeIndent();
			}
		}
		TypeContext typeContext = new TypeContext(BasicType.ARRAY);
		pushTypeContext(typeContext);
		out.write("[");
		if (prettyPrint) {
			indent += 4;
			out.write("\n");
		}
		return typeContext;
	}

	public void writeCloseArray() {
		if (prettyPrint) {
			out.write("\n");
			indent -= 4;
			writeIndent();
		}
		out.write("]");
		popTypeContext();
	}

	public void writeIndent() {
		for (int i = 0; i < indent; i++) {
			out.write(" ");
		}
	}

	/**
	 * write a quoted and escaped value to the output
	 * 
	 * @param value
	 */
	public void writeQuoted(String value) {
		if (prettyPrint) {
			TypeContext currentTypeContext = peekTypeContext();
			if (currentTypeContext != null
					&& currentTypeContext.getBasicType() == BasicType.ARRAY) {
				writeIndent();
			}
		}

		out.write("\"");
		int last = 0;
		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			if (c == '"') {
				last = out.write(value, last, i, "\\\"");
			} else if (c == '\\') {
				last = out.write(value, last, i, "\\\\");
			} else if (c == '\b') {
				last = out.write(value, last, i, "\\b");
			} else if (c == '\f') {
				last = out.write(value, last, i, "\\f");
			} else if (c == '\n') {
				last = out.write(value, last, i, "\\n");
			} else if (c == '\r') {
				last = out.write(value, last, i, "\\r");
			} else if (c == '\t') {
				last = out.write(value, last, i, "\\t");
			} else if (Character.isISOControl(c)) {
				last = out.write(value, last, i) + 1;
				unicode(c);
			}
		}
		if (last < value.length()) {
			out.write(value, last, value.length());
		}
		out.write("\"");
	}

	private void unicode(char c) {
		out.write("\\u");
		int n = c;
		for (int i = 0; i < 4; ++i) {
			int digit = (n & 0xf000) >> 12;
			out.write(String.valueOf(JSONSerializer.HEX[digit]));
			n <<= 4;
		}
	}

	// MANAGE CONTEXT

	/**
	 * static method to getTransformer the context for this thread
	 * 
	 * @return
	 */
	public static JSONContext get() {
		return context.get();
	}

	/**
	 * static moethod to clean up thread when serialization is complete
	 */
	public static void cleanup() {
		context.remove();
	}

	// INCLUDE/EXCLUDE METHODS

	public ChainedSet getVisits() {
		return visits;
	}

	public void setVisits(ChainedSet visits) {
		this.visits = visits;
	}

	public LinkedList<Object> getObjectStack() {
		return objectStack;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public Path getPath() {
		return this.path;
	}

	public void setPathExpressions(List<PathExpression> pathExpressions) {
		this.pathExpressions = pathExpressions;
	}

	public boolean isIncluded(PropertyDescriptor prop) {
		if (!serializeWithClassInfo) {
			if ("class".equals(prop.getName())) {
				return false;
			}
		}
		PathExpression expression = matches(pathExpressions);
		if (expression != null) {
			return expression.isIncluded();
		}

		Method accessor = prop.getReadMethod();
		if (accessor.isAnnotationPresent(JSON.class)) {
			return accessor.getAnnotation(JSON.class).include();
		}

		if (serializationType == SerializationType.SHALLOW) {
			Class propType = getTargetClass(prop.getPropertyType());
			return !(propType.isArray() || Iterable.class
					.isAssignableFrom(propType));
		} else {
			return true;
		}

	}

	private Class getTargetClass(Type targetType) {
		if (targetType == null) {
			return null;
		} else if (targetType instanceof Class) {
			return (Class) targetType;
		} else if (targetType instanceof ParameterizedType) {
			return (Class) ((ParameterizedType) targetType).getRawType();
		} else if (targetType instanceof GenericArrayType) {
			return Array.class;
		} else if (targetType instanceof WildcardType) {
			return null; // nothing you can do about these. User will have to
							// specify this with use()
		} else if (targetType instanceof TypeVariable) {
			return null; // nothing you can do about these. User will have to
							// specify this with use()
		} else {
			return (Class) targetType;
		}
	}

	public boolean isIncluded(String key, Object value) {

		PathExpression expression = matches(pathExpressions);
		if (expression != null) {
			return expression.isIncluded();
		}

		String rootName = context.get().getRootName();

		/*
		 * We have a double check here because of the way lists are handled in a
		 * shallow. Normally lists are ignored. but, in the case when a rootName
		 * is added the object being serialized get wrapped with a Map and may
		 * be a List/Iterable. We don't want the List to get ignored. So, we
		 * check if a rootName has been specified and then make sure we are past
		 * the root element serialization before we begin to ingore List and
		 * Iterable.
		 */

		if ((serializationType == SerializationType.SHALLOW && (rootName != null && path
				.length() > 1))
				|| (serializationType == SerializationType.SHALLOW && (rootName == null))) {

			Class type = value.getClass();
			return !(type.isArray() || Iterable.class.isAssignableFrom(type));

		} else {
			return true;
		}
	}

	public boolean isIncluded(Field field) {
		PathExpression expression = matches(pathExpressions);
		if (expression != null) {
			return expression.isIncluded();
		}

		if (field.isAnnotationPresent(JSON.class)) {
			return field.getAnnotation(JSON.class).include();
		}

		if (serializationType == SerializationType.SHALLOW) {
			Class type = field.getType();
			return !(type.isArray() || Iterable.class.isAssignableFrom(type));
		} else {
			return true;
		}
	}

	public boolean isValidField(Field field) {
		return !Modifier.isStatic(field.getModifiers())
				&& Modifier.isPublic(field.getModifiers())
				&& !Modifier.isTransient(field.getModifiers());
	}

	protected PathExpression matches(List<PathExpression> expressions) {
		for (PathExpression expr : expressions) {
			if (expr.matches(path)) {
				return expr;
			}
		}
		return null;
	}

	public boolean isSerializeWithClassInfo() {
		return serializeWithClassInfo;
	}

	public void setSerializeWithClassInfo(boolean serializeWithClassProperty) {
		this.serializeWithClassInfo = serializeWithClassProperty;
	}

}