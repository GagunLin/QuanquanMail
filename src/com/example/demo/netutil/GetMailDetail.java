package com.example.demo.netutil;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;
import com.example.demo.data.UserInforData;
import com.example.demo.util.NetUtils;

import android.content.Context;

public class GetMailDetail {
	private Context mcontext;
	private UserInforData User;
	
	public GetMailDetail(Context context){
		mcontext = context;
		User = new UserInforData(context);
	}
	
	public String getJsonString(String eu_id){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("eu_id", eu_id));
		params.add(new BasicNameValuePair("userid", User.getUserID()));
		params.add(new BasicNameValuePair("token", User.getUsertoken()));

		String result = NetUtils.httpPostUtil(mcontext, "http://www.quanquan6.com/admin/api/Email.ashx"
				+ "?action=getmailinfo", params);
		
		try {
			JSONObject resultoj = new JSONObject(result);
			if (resultoj.optString("ErrCode").equals("200")) {
				return resultoj.optString("EmailList");
			} else {
				return resultoj.optString("ErrMsg");
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return mcontext.getString(R.string.getdata_error);
		} catch (Exception e) {
			e.printStackTrace();
			return mcontext.getString(R.string.getdata_error);
		}
	}
}
