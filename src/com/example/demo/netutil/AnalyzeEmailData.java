package com.example.demo.netutil;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.example.demo.R;
import com.example.demo.data.NetResultData;
import com.example.demo.data.UserInforData;
import com.example.demo.util.MyUrl;
import com.example.demo.util.NetUtils;


public class AnalyzeEmailData {
	private Context mcontext;
	public UserInforData User;
	
	public AnalyzeEmailData(Context context) {
		mcontext = context;
		User = new UserInforData(context);
	}


	public String Login(String sc_id, String username, String password,
			String vercode) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sc_id", sc_id));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("vercode", vercode));

		String result = NetUtils.httpPostUtil(mcontext, MyUrl.IF_HOST_URL
				+ MyUrl.IFLoginSignin, params);
		try {
			JSONObject resultoj = new JSONObject(result);
			if (resultoj.optString("code").equals("1")) {
				JSONObject oj = resultoj.getJSONObject("data");
				String UserID = oj.optString("userID");
				String token = oj.optString("token");
				User.setUserImageUrl(oj.optString("photo"));
				User.setUserRealName(oj.optString("realname"));
				User.setUserStatus(oj.optString("role_name"));
				User.setUserID(UserID);
				User.setIsreg(true);
				User.setUsertoken(token);
				User.setUserType(oj.optString("role_type"));
				User.setUserRoleId(oj.optString("role_id"));
                User.setUnauthedInstructor(oj.optInt("unauthed_instructor"));
				return null;
			} else {
				return resultoj.optString("msg");
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return mcontext.getString(R.string.getdata_error);
		} catch (Exception e) {
			e.printStackTrace();
			return mcontext.getString(R.string.getdata_error);
		}
	}
	
	/**
	 * 查看用户短信额度是否充足
	 * @date 15-7-14
	 * */
	public NetResultData GetSMSLimitEnough(String studentIds, String comGroupIds, String orgIds){
		
		NetResultData netData = new NetResultData();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("if", MyUrl.GET_MY_INBOX_LIST));
		params.add(new BasicNameValuePair("token", User.getUsertoken()));
		params.add(new BasicNameValuePair("roleid", User.getUserRoleId()));
		params.add(new BasicNameValuePair("roletype", User.getUserType()));
		
		params.add(new BasicNameValuePair("student", studentIds));
		params.add(new BasicNameValuePair("comgroup", comGroupIds));
		params.add(new BasicNameValuePair("org", orgIds));

		String resultString = NetUtils.httpPostUtil(mcontext, MyUrl.EMAIL_URL, params);
		try {
			JSONObject resultObject = new JSONObject(resultString);
			
			netData.setCode(resultObject.optInt("code"));
			netData.setMessage(resultObject.optString("msg"));
			netData.setDataString(resultObject.optString("data"));
			
			if(netData.getCode() == 1){
				JSONObject dataObject = resultObject.optJSONObject("data");
				netData.setDataObject(dataObject.optInt("result"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return netData;
	}
}
