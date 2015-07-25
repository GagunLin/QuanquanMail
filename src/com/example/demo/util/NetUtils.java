/**
 *
 * Author: linsiran
 * Date: 2012-5-14下午02:55:43
 */
package com.example.demo.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class NetUtils {

	/**
	 * 通过url地址返回网络数据包
	 * */
	public static String httpGetUtil(Context context, String url) {
		String result = null;
		HttpGet request = NetworkControl.getHttpGet(url);
		HttpClient client = NetworkControl.getHttpClient(context);
		try {

//			MyAccountManager accountManager = new MyAccountManager(context);
//			AccountEntity account = accountManager.getAccount();
//			request.setHeader(
//					"Authorization",
//					BasicAuthUtils.encodeAuth(account.getUserid(),
//							account.getAccountPassword()));

			
			HttpResponse res = client.execute(request);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity(), "UTF-8");
				MyLog.log("token get is: " + url + " " + res.getStatusLine().getStatusCode() + " " + result);
			} else {
				result = null;
			}
			MyLog.log("The http get is: " + url + " " + res.getStatusLine().getStatusCode() + " " + result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			result = null;
		} finally {
			request.abort();
		}

		return result;
	}

	/**
	 * 通过url地址返回网络数据包
	 * */
	public static HttpResponse httpGetUtilResponse(Context context, String url) {
		HttpResponse result = null;

		try {
			HttpGet request = NetworkControl.getHttpGet(url);
			HttpClient client = NetworkControl.getHttpClient(context);

			//set common header
//			MyAccountManager accountManager = new MyAccountManager(context);
//			AccountEntity account = accountManager.getAccount();
//			request.setHeader(
//					"Authorization",
//					BasicAuthUtils.encodeAuth(account.getUserid(),
//							account.getAccountPassword()));

			HttpResponse res = client.execute(request);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = res;
			} else {
				result = null;
			}
			MyLog.log("The http get is: " + url + " " + res.getStatusLine().getStatusCode() + " " + result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	/**
	 * post因为中文编码通过url地址返回网络数据包
	 * */
	public static String httpPostUtil(Context context, String url, List<NameValuePair> params) {
		String result = null;
		HttpClient client = NetworkControl.getHttpClient(context);
		HttpPost request = NetworkControl.getHttpPost(url);
		HttpResponse res;
		try {
			//set common header
//			MyAccountManager accountManager = new MyAccountManager(context);
//			AccountEntity account = accountManager.getAccount();
//			request.setHeader(
//					"Authorization",
//					BasicAuthUtils.encodeAuth(account.getUserid(),
//							account.getAccountPassword()));

			if (params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			
			res = client.execute(request);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity(), "UTF8");
			} else {
				result = null;
			}
			System.out.println("The login post is: " + request.getRequestLine().getUri() + "/" + EntityUtils.toString(request.getEntity(), "UTF8") + " " + res.getStatusLine().getStatusCode() + " " + result);
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
            request.abort();
        }

		return result;
	}

	public static HttpResponse httpPostUtilResponse(Context context, String url, List<NameValuePair> params) {
		HttpResponse result = null;
		HttpClient client = NetworkControl.getHttpClient(context);
		HttpPost request = NetworkControl.getHttpPost(url);
		try {
			//set common header
//			MyAccountManager accountManager = new MyAccountManager(context);
//			AccountEntity account = accountManager.getAccount();
//			request.setHeader(
//					"Authorization",
//					BasicAuthUtils.encodeAuth(account.getUserid(),
//							account.getAccountPassword()));

			if (params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}

			HttpResponse res = client.execute(request);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = res;
			} else {
				result = null;
			}
			MyLog.log("The http post is: " + request.getRequestLine().getUri() + "/" + EntityUtils.toString(request.getEntity(), "UTF8") + " " + res.getStatusLine().getStatusCode() + " " + result);
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			request.abort();
		}

		return result;
	}
	
	public static String httpPostUtilContentType(Context context, String url, List<NameValuePair> params) {
		String result = null;
		HttpClient client = NetworkControl.getHttpClient(context);
		HttpPost request = NetworkControl.getHttpPost(url);
		try {
			//set common header
//			MyAccountManager accountManager = new MyAccountManager(context);
//			AccountEntity account = accountManager.getAccount();
//			request.setHeader(
//					"Authorization",
//					BasicAuthUtils.encodeAuth(account.getUserid(),
//							account.getAccountPassword()));

			if (params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				request.addHeader("Content-type", "application/x-www-form-urlencoded");  
			}

			HttpResponse res = client.execute(request);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity(), "UTF8");
			} else {
				result = null;
			}
			MyLog.log("The http post is: " + request.getRequestLine().getUri() + "/" + EntityUtils.toString(request.getEntity(), "UTF8") + " " + res.getStatusLine().getStatusCode() + " " + result);

		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			request.abort();
		}

		return result;
	}
	public boolean isRequestSuccess(Context context, String result) {
		if (result == null) {
			return false;
		}
		try {
			JSONObject jObject = new JSONObject(result);
			if (jObject.getString("state").equals("ok")) {
				return true;
			}
			MyLog.log("The state is: " + jObject.getString("state"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
