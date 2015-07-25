package com.example.demo.util;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class DevicePreferences {
	private SharedPreferences pref;
	private final static String name = "device_preference";
	private final static String deviceId = "device_id";
	private final static String uniqueId = "unique_id";
	private final static String mac_addr = "mac_addr";
	
	public DevicePreferences(Context context) {
		pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	public String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (!pref.contains(deviceId)) {
			String id = tm.getDeviceId();
			if (id == null || id.equals("0") || id.equals("")) {
				id = UUID.randomUUID().toString();
			}
			pref.edit().putString(deviceId, id).commit();
		}
		return pref.getString(deviceId, tm.getDeviceId());
	}
	
	public String getUniqueId() {
		if (!pref.contains(uniqueId)) {
			pref.edit().putString(uniqueId, UUID.randomUUID().toString()).commit();
		}
		
		return pref.getString(uniqueId, "");
	}

	public String getMacAddr(Context context) {
		if (!pref.contains(mac_addr)) {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
	        WifiInfo info = wifi.getConnectionInfo();  
	        if (info.getMacAddress() != null) {
	        	pref.edit().putString(mac_addr, info.getMacAddress()).commit();  
	        }
	        else {
	        	pref.edit().putString(mac_addr, UUID.randomUUID().toString()).commit();  
	        }
		}
		return pref.getString(mac_addr, null);
	}
	
}
