package com.example.demo.util;

public class NetType {
	private String apn = "";
	private String proxy = "";
	private String typeName = "";
	private int port = 0;
	private boolean isWap = false;
	private boolean isWifi = false;
	private boolean is2G = false;
	private boolean is3G = false;
	
	public boolean is2G() {
		return is2G;
	}
	public void set2G(boolean is2g) {
		is2G = is2g;
	}
	public boolean is3G() {
		return is3G;
	}
	public void set3G(boolean is3g) {
		is3G = is3g;
	}
	public boolean isWifi() {
		return isWifi;
	}
	public void setWifi(boolean isWifi) {
		this.isWifi = isWifi;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public boolean isWap() {
		return isWap;
	}
	public void setWap(boolean isWap) {
		this.isWap = isWap;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
