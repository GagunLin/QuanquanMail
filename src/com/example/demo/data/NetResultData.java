package com.example.demo.data;

public class NetResultData {
	private int code;
	private String message;
	private String dataString;
	private Object dataObject;
	
	private String extendString;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDataString() {
		return dataString;
	}
	public void setDataString(String dataString) {
		this.dataString = dataString;
	}
	public Object getDataObject() {
		return dataObject;
	}
	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}
	public String getExtendString() {
		return extendString;
	}
	public void setExtendString(String extendString) {
		this.extendString = extendString;
	}
	
}
