package com.jason.framework.common.json;

import com.jason.mvc.view.ResultState;

public class JsonBuilder {

	public static byte[] getJson(ResultState resultState, byte[] json) {
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		createNamedElement(doc, "state", Integer.valueOf(resultState.getValue()));
		doc.appendJson("data", json);
		doc.endObject();
		
		return doc.toByte();
	}
	
	public static byte[] getJson(ResultState resultState, String msg) {
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		createNamedElement(doc, "state", Integer.valueOf(resultState.getValue()));
		doc.startObject("data");
		createNamedElement(doc, "msg", msg);
		doc.endObject();
		doc.endObject();
		
		return doc.toByte();
	}
	
	public static byte[] getFailJson(String msg) {
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		createNamedElement(doc, "state", Integer.valueOf(ResultState.FAIL.getValue()));
		createNamedElement(doc, "msg", msg);
		doc.endObject();
		
		return doc.toByte();
	}
	
	public static void createNamedElement(JsonDocument doc, String name, Object value) {
		doc.createElement(name, value);
	}
	
	
	
}
