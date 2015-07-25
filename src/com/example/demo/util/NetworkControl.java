package com.example.demo.util;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkControl {
	public static final String CLIENT_USER_AGENT = "android coco";
	private static HttpClient mClientInstance = null;

	public static boolean getNetworkState(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if(info != null)
		{
			return info.isAvailable();
		}
		return false;
	}

	public static final NetType getNetType(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null) return new NetType();

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info == null) return new NetType();

		String type = info.getTypeName();

		NetType netType = new NetType();

		if (type.equalsIgnoreCase("WIFI")) {
			netType.setWifi(true);
			netType.setTypeName("wifi");
		} else if(type.equalsIgnoreCase("MOBILE")) {
			String proxyHost = android.net.Proxy.getDefaultHost();
	        if (proxyHost != null && !proxyHost.equals("")) {
	            netType.setProxy(proxyHost);
	        	netType.setPort(android.net.Proxy.getDefaultPort());
	        	netType.setWap(true);
	        	netType.setTypeName("wap");
	        	return netType;
	        }
	        else {
	        	switch (info.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
					netType.set2G(true);
					netType.setTypeName("2G");
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					netType.set3G(true);
					netType.setTypeName("3G");
					break;
				default:
					netType.setTypeName("mobile");
					break;
				}
	        }
		}
		return netType;
	}

	public static synchronized HttpClient getHttpClient(Context context) {
		if (mClientInstance == null || true) {
			//String channel = MyUtils.getChannel(context);

			HttpParams params = new BasicHttpParams();

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);


            ConnManagerParams.setTimeout(params, 10000);
            ConnManagerParams.setMaxTotalConnections(params, 200);
            ConnManagerParams.setMaxTotalConnections(params, 50);


            SchemeRegistry schReg =new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));


            ClientConnectionManager conMgr =new ThreadSafeClientConnManager(
                    params, schReg);
            mClientInstance = new DefaultHttpClient(conMgr, params);

			mClientInstance.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 15*1000);
			mClientInstance.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 30*1000);
			
//			String deviceId = new DevicePreferences(context).getUniqueId();
//			String version = MyUtils.getVersion(context);
//			String channel = MyUtils.getChannel(context);
//			String useragent = "quanquan|Android|" + deviceId + "|" + version + "|" + channel;
//			mClientInstance.getParams().setParameter(HttpProtocolParams.USER_AGENT, useragent);
//			System.out.println("The useragent is: " + useragent);

			NetType netType = getNetType(context);
			if (netType != null && netType.isWap()) {
				HttpHost proxy = new HttpHost(netType.getProxy(), netType.getPort());
				mClientInstance.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
			}
		}

		return mClientInstance;
	}

	public static HttpPost getHttpPost(String url)
	{
		HttpPost httpPost = new HttpPost(url);
		return httpPost;
	}

	public static HttpGet getHttpGet(String url)
	{
		HttpGet httpGet = new HttpGet(url);
		return httpGet;
	}
}
