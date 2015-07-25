package com.example.demo.client;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.data.UserInforData;
import com.example.demo.netutil.AnalyzeEmailData;

public class LoginActivity extends Activity {

	private String strname, strpass, straction = "Login", TAG = "TEST", result, versionName, packageNames;
	private EditText username, password;
	private ProgressDialog progressDialog;

	private List<NameValuePair> params;
	private UserInforData User, AnotherUser;
	TextView versionTev, registerTev, forgetPassTev, schoolTev;
	int versionCode;
	private Button SignButton;
	//	private Button SelectSchoolBtn;
	private RelativeLayout stuSChoolLayout;
	private TextView selectSchoolText;
	private LinearLayout selectSchoolLayout;
	
	private boolean LoginOfSelect = false;
	private final int LOGIN_IN_SUCCESS = 1;
	private final int LOGIN_IN_ERROR = 0;
	private final int ENTER_APP = 7;
	String SchoolID = "", schoolName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		User = new UserInforData(this);
		AnotherUser = new UserInforData(this);
		
		if (User.isIsreg()) {
			//直接进入应用的主界面
			IndexCreate();
		} else {
			LoginCreate();
		}
	}

	private String flag;

	private void LoginCreate() {
		setContentView(R.layout.activity_login);
		SignButton = (Button) findViewById(R.id.signin_button);
		selectSchoolText = (TextView) findViewById(R.id.selectSchoolText);
		selectSchoolLayout = (LinearLayout)findViewById(R.id.selectSchoolLayout);
		username = (EditText) findViewById(R.id.stuIDEdit);
		password = (EditText) findViewById(R.id.passEdit);
		schoolTev = (TextView) findViewById(R.id.SchoolTev);
		forgetPassTev = (TextView) findViewById(R.id.ForgetPass_Tev);
		registerTev = (TextView) findViewById(R.id.register_Tev);
		stuSChoolLayout = (RelativeLayout) findViewById(R.id.stuSChoolLayout);
		
		if(LoginOfSelect){
   		   stuSChoolLayout.setVisibility(View.VISIBLE);
   		   selectSchoolText.setText("邮箱手机号登录");
   		   schoolTev.setText(schoolName);
   		   ((View)findViewById(R.id.SchoolPutin)).setVisibility(View.VISIBLE);
   		   ((View)findViewById(R.id.Schoolnormal)).setVisibility(View.GONE);
   	    }
   	    else{
   		   stuSChoolLayout.setVisibility(View.GONE);
   		   selectSchoolText.setText("选择学校登录");
   		   ((View)findViewById(R.id.SchoolPutin)).setVisibility(View.GONE);
   		   ((View)findViewById(R.id.Schoolnormal)).setVisibility(View.VISIBLE);
   	    }
		
		User = new UserInforData(this);
		SignButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = new ProgressDialog(LoginActivity.this);
				progressDialog.setMessage("登录中...");
				progressDialog.setCancelable(true);
				progressDialog.show();
				
				strname = username.getText().toString();
				strpass = password.getText().toString();
				new Thread() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						AnalyzeEmailData net = new AnalyzeEmailData(LoginActivity.this);
						flag = net.Login(SchoolID, strname, strpass, "");
						if (flag == null) {
							if (!net.User.getUserID().equals(User.getUserID())) {
								AnotherUser = net.User;
							}
							net.User.SaveUserData();
							handler.sendEmptyMessage(LOGIN_IN_SUCCESS);
						} else {
							handler.sendEmptyMessage(LOGIN_IN_ERROR);
						}
					}
				}.start();

			}
		});
		
		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if ("".equals(s)) {
					((View) findViewById(R.id.stuIDnormal)).setVisibility(View.VISIBLE);
					((View) findViewById(R.id.stuIDPutin)).setVisibility(View.GONE);
				} else {
					((View) findViewById(R.id.stuIDPutin)).setVisibility(View.VISIBLE);
					((View) findViewById(R.id.stuIDnormal)).setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if ("".equals(s)) {
					((View) findViewById(R.id.passNormal)).setVisibility(View.VISIBLE);
					((View) findViewById(R.id.passPutin)).setVisibility(View.GONE);
				} else {
					((View) findViewById(R.id.passPutin)).setVisibility(View.VISIBLE);
					((View) findViewById(R.id.passNormal)).setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
	}


	private void IndexCreate() {
		Intent intent=new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing() == true) {
				progressDialog.dismiss();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			
			switch (msg.what) {
			case LOGIN_IN_ERROR:
				builder.setTitle("提示");
				builder.setPositiveButton("确定", null);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage(flag);
				if(!LoginActivity.this.isFinishing()){
					builder.show();
				}
				break;
			case LOGIN_IN_SUCCESS:
				Toast.makeText(getApplicationContext(), getString(R.string.login_suc), Toast.LENGTH_SHORT).show();
			case ENTER_APP:
				Intent intent = new Intent();

				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
				break;
			}
		}
	};

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (progressDialog != null && progressDialog.isShowing() == true) {
			progressDialog.dismiss();
		}
		super.onDestroy();
	}
}
