package com.luotian.json;

import java.lang.reflect.Type;

import com.luotian.core.domain.ConvertException;

/**
 * General interface serialize java object.
 * 
 * @author LIU Fangran
 */
public class JsonCodec {
	/**
	 * default value convenient for Andriod developing
	 */
	boolean serializeWithClassInfo = false;

	//Charset charset = Charset.forName("UTF-8");
	public static final String UTF8 = "UTF-8";
	JSONSerializer serializer = new JSONSerializer();
	JSONDeserializer<Object> deserializer = new JSONDeserializer<Object>();

	public JsonCodec() {
	}

	public Object toObject(byte[] bytes) {
		try {
			String text = new String(bytes, UTF8);

			return toObject(text);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConvertException(e);
		}

	}

	public Object toObject(String text) {
		try {
			return deserializer.deserialize(text);
		} catch (Exception e) {
			throw new ConvertException("Error Occurs while processing text<"
					+ text + ">", e);
		}

	}

	public byte[] toBytes(Object object) {
		try {
			return toJson(object).getBytes(UTF8);
		} catch (Exception e) {
			throw new ConvertException(e);
		}

	}

	public String toJson(Object object) {
		try {
			// if (serializeWithClassInfo) {
			// return Someting without class info.
			// }
			return serializer.deepSerialize(object, serializeWithClassInfo);
		} catch (Exception e) {
			throw new ConvertException(e);
		}

	}

	public Object toObject(String text, Type type) {
		try {
			return this.deserializer.deserialize(text, type);
		} catch (Exception e) {
			throw new ConvertException(e);
		}

	}

	public Object toObject(byte[] bytes, Type type) {
		try {
			String text = new String(bytes, UTF8);
			return this.deserializer.deserialize(text, type);
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

	public boolean isSerializeWithClassInfo() {
		return serializeWithClassInfo;
	}

	public void setSerializeWithClassInfo(boolean serializeWithClassInfo) {
		this.serializeWithClassInfo = serializeWithClassInfo;
	}

}
