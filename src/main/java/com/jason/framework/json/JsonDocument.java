package com.jason.framework.json;

import java.io.IOException;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import static com.jason.common.SymbolConstants.B_COMMA;
import static com.jason.common.SymbolConstants.B_QUOT;
import static com.jason.common.SymbolConstants.B_COLON;
import static com.jason.common.SymbolConstants.B_L_BRACE;
import static com.jason.common.SymbolConstants.B_R_BRACE;
import static com.jason.common.SymbolConstants.B_L_BRACKET;
import static com.jason.common.SymbolConstants.B_R_BRACKET;


public class JsonDocument {

	private SerializeWriter out;
	
	private JSONSerializer serializer;
	
	private boolean first = true;
	
	public JsonDocument() {
		out = new SerializeWriter();
		serializer = new JSONSerializer(out);
	}
	
	public void reset() {
		out = new SerializeWriter();
		serializer = new JSONSerializer(out);
		first = true;
	}
	
	/**
	 * 创建对象节点
	 * @param elementName
	 */
	public void startObject(String elementName) {
		if(!first) {
			append(B_COMMA);
		}
		append(B_QUOT).append(elementName.toCharArray()).append(B_QUOT).append(B_COLON).append(B_L_BRACE);
		first = true;
	}
	
	
	public void startObject() {
		if(!first) {
			append(B_COMMA);
		}
		append(B_L_BRACE);
		first = true;
	}
	
	/**
	 * 结束对象节点
	 */
	public void endObject() {
		append(B_R_BRACE);
		first = false;
	}
	
	/**
	 * 创建数组节点
	 * @param elementName
	 */
	public void startArray(String elementName) {
		if(!first) {
			append(B_COMMA);
		}
		append(B_QUOT).append(elementName.toCharArray()).append(B_QUOT).append(B_COLON).append(B_L_BRACKET);
		first = true;
	}
	
	public void startArray() {
		if(!first) {
			append(B_COMMA);
		}
		append(B_L_BRACKET);
		first = true;
	}
	
	
	public void endArray() {
		append(B_R_BRACKET);
		first = false;
	}
	
	public void createElement(String elementName, Object o) {
		if(!first) {
			append(B_COMMA);
		}
		append(B_QUOT).append(elementName.toCharArray()).append(B_QUOT)
		.append(B_COLON);
		createValue(o);
		first = false;
	}
	
	public void createElement(Object o) {
		if(!first) {
			append(B_COMMA);
		}
		createValue(o);
		first = false;
	}

	private void createValue(Object o) {
		serializer.write(o);
	}
	
	public void appendJson(final byte[] json) {
		if(!first) {
			append(B_COMMA);
		}
		append(json);
		first = false;
	}
	
	public void appendJson(final String elementName, final byte[] json) {
		if(!first) {
			append(B_COMMA);
		}
		append(B_QUOT).append(elementName.toCharArray()).append(B_QUOT)
		.append(B_COLON);
		append(json);
		first = false;
	}
	
	public void appendObjectJson(final String elementName, final byte[] json) {
		if(!first) {
			append(B_COMMA);
		}
		append(B_QUOT).append(elementName.toCharArray()).append(B_QUOT).append(B_COLON).append(B_L_BRACE);
		append(json);
		append(B_R_BRACE);
		first = false;
	}
	
	public String toString() {
		return new String(out.toBytes("UTF-8"));
	}
	
	public byte[] toByte() {
		return out.toBytes("UTF-8");
	}
	
	private JsonDocument append(final char[] bytes) {
		try {
			out.write(bytes);
			return this;
		} catch (IOException t) {
			throw new RuntimeException("", t);
		}
	}
	
	private JsonDocument append(final byte[] bytes) {
		out.write(new String(bytes, 0, bytes.length));
		return this;
	}
	
	
}
