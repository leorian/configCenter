package com.luotian.json.transformer;

//!import java.beans.BeanInfo;
//!import java.beans.Introspector;
//!import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.luotian.json.JSONContext;
import com.luotian.json.JSONException;
import com.luotian.json.PropertyDescriptor;
import com.luotian.json.BeanInfo;
import com.luotian.json.ChainedSet;
import com.luotian.json.Introspector;
import com.luotian.json.Path;
import com.luotian.json.TypeContext;

public class ThrowableTransformer extends AbstractTransformer {

	@Override
	public void transform(Object object) {
		JSONContext context = getContext();
		Path path = context.getPath();
		ChainedSet visits = context.getVisits();
		try {
			if (!visits.contains(object)) {
				context.setVisits(new ChainedSet(visits));
				context.getVisits().add(object);
				// traverse object
				BeanInfo info = Introspector.getBeanInfo(resolveClass(object));
				PropertyDescriptor[] props = info.getPropertyDescriptors();
				TypeContext typeContext = context.writeOpenObject();
				for (PropertyDescriptor prop : props) {
					String name = prop.getName();
					path.enqueue(name);
					Method accessor = prop.getReadMethod();
					if (accessor != null && context.isIncluded(prop)) {
						Object value = accessor.invoke(object, (Object[]) null);
						if (value != null) { // only output the not null values.
							if (!context.getVisits().contains(value)) {

								TransformerWrapper transformer = (TransformerWrapper) context
										.getTransformer(value);

								if (!transformer.isInline()) {
									if (!typeContext.isFirst())
										context.writeComma();
									typeContext.setFirst(false);
									context.writeName(name);
								}
								typeContext.setPropertyName(name);

								transformer.transform(value);

							}
						}

					}
					path.pop();
				}
				for (Class current = object.getClass(); current != null; current = current
						.getSuperclass()) {
					Field[] ff = current.getDeclaredFields();
					for (Field field : ff) {
						path.enqueue(field.getName());
						if (context.isValidField(field)
								&& context.isIncluded(field)) {
							if (!context.getVisits()
									.contains(field.get(object))) {

								Object value = field.get(object);

								TransformerWrapper transformer = (TransformerWrapper) context
										.getTransformer(value);

								if (!transformer.isInline()) {
									if (!typeContext.isFirst())
										context.writeComma();
									typeContext.setFirst(false);
									context.writeName(field.getName());
								}
								typeContext.setPropertyName(field.getName());

								transformer.transform(value);

							}
						}
						path.pop();
					}
				}

				context.writeCloseObject();
				context.setVisits((ChainedSet) context.getVisits().getParent());

			}
		} catch (JSONException e) {
			throw e;
		} catch (Exception e) {
			throw new JSONException("Error trying to deepSerialize", e);
		}
	}

	protected Class resolveClass(Object obj) {
		return obj.getClass();
	}
}
