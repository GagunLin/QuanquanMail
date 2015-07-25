package com.example.demo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyUtils {
	public static String getChannel(Context context) {
		ApplicationInfo ai = null;
		String channel = "test";
		try {
			ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ai != null) {
			channel = String.valueOf(ai.metaData.get("UMENG_CHANNEL"));
		}

		return channel;
	}

	public static String getVersion(Context context) {
		String version = null;
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return version;
	}
}
