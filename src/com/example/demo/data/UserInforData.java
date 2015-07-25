package com.example.demo.data;

import java.util.ArrayList;
import java.util.List;

//import com.quanquanle.client.R;
//import com.quanquanle.client.utils.QuanQuanConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInforData {
    public static final int USER_AUTHED = 0;
    public static final int USER_UNAUTHED = 1;
	private Context mcontext;

	private boolean isreg;
	private String UserID;
	private String Usertoken;
	private String UserRealName;
	private String UserImageUrl;
	private String UserStatus;
	private String UserDetail;
	private String SchoolID="1";
	private String SchoolName="北京理工大学";
	private String UserType;
	private String UserRoleId;
	private String UserNickName;
	private String Signature;
    private int unauthedInstructor;
	/*-----------获取详情的返回数据---------------*/
	// private String LoginName; //登录名字
	// private String UserNickName;//昵称
	// //此处为：姓名，UserRealName
	// private String UserSex;
	// private String UserBirth;
	// private String UserVolk;//民族
	// private String UserPhone;
	// private String UserEmail;
	// private String UserPolitical;//政治面貌
	// //此处为：头像，UserImageUrl
	/*-----------获取详情的返回数据---------------*/


	// private String UserClass="";//班级
	// private String UserStuID="";//学号

//	private List<UserInforContentData> UserInforContentList;
//
//	public List<UserInforContentData> getUserInforContentList() {
//		
//		UserInforContentList = new ArrayList<UserInforContentData>();
//		
////		if (UserInforContentList.size() != 0) {
////			UserInforContentList.clear();
////		}
//
//		String stringDetail = getUserDetail();
//		String stringArray[] = stringDetail.split(", ");
//
//		for (int i = 0; i < stringArray.length; i++) {
//			String temp[] = stringArray[i].split("：");
//			// UserInforItemList.add(stringArray[i]);
//			UserInforContentData data = new UserInforContentData();
//			data.setsInforTitle(temp[0]);
//			if (temp.length == 2) {
//				data.setsInforDetail(temp[1]);
//			} else {
//				data.setsInforDetail(" ");
//			}
//
//			String titleString = data.getsInforTitle();
////			if (titleString.equals("昵称") || titleString.equals("手机")
////					|| titleString.equals("邮箱")) {
////				data.setEditType(1);
////			} else if (titleString.equals("生日")) {
////				data.setEditType(2);
////			} else {
////				data.setEditType(0);
////			}
//			
//			if (titleString.equals("昵称")) {
//				data.setEditType(QuanQuanConstants.OWNER_INFOR_EDIT_TYPE_EDITABLE_NICKNAME);
//			} else if (titleString
//					.equals("生日")) {
//				data.setEditType(QuanQuanConstants.OWNER_INFOR_EDIT_TYPE_EDITABLE_BIRTHDAY);
//			} else if (titleString
//					.equals("手机")) {
//				data.setEditType(QuanQuanConstants.OWNER_INFOR_EDIT_TYPE_EDITABLE_PHONE);
//			} else if (titleString
//					.equals("邮箱")) {
//				data.setEditType(QuanQuanConstants.OWNER_INFOR_EDIT_TYPE_EDITABLE_EMAIL);
//			} else {
//				data.setEditType(QuanQuanConstants.OWNER_INFOR_EDIT_TYPE_UNEDITABLE);
//			}
//			
//			UserInforContentList.add(data);
//		}
//		return UserInforContentList;
//	}
//	
//	public String UserInforContentListToString(List<UserInforContentData> UserInforContentList)
//	{
//		String UserDetailString = "";
//		for(int i = 0; i < UserInforContentList.size(); i ++){
//			UserDetailString += UserInforContentList.get(i).getsInforTitle() + "：" + UserInforContentList.get(i).getsInforDetail() + ", ";
//		}
//		UserDetailString = UserDetailString.substring(0, UserDetailString.length() - 2);
//		
//		return UserDetailString;
//	}
			

	public Context getMcontext() {
		return mcontext;
	}

	public void setMcontext(Context mcontext) {
		this.mcontext = mcontext;
	}

	public boolean isreg() {
		if (Usertoken.equals(""))
			return false;
		else
			return true;
	}

	public void logoff() {
		Usertoken = "";
		UserRealName = "";// 真实姓名
		UserImageUrl = "";
		UserStatus = "";
		UserDetail = "";
		UserType = "";
		UserRoleId = "";
		UserNickName = "";
	}

	public UserInforData(Context context) {

		SharedPreferences sp = context.getSharedPreferences("Userdata",
				context.MODE_APPEND);
		UserID = sp.getString("userID", "");
		Usertoken = sp.getString("usertoken", "");

		UserRealName = sp.getString("userRealName", "");
		UserImageUrl = sp.getString("userImageUrl", "");
		UserStatus = sp.getString("userStatus", "");
		UserDetail = sp.getString("userDetail", "");
		isreg = sp.getBoolean("isreg", false);
		UserType = sp.getString("userType", "");
		UserRoleId = sp.getString("UserRoleId", "");
		UserNickName = sp.getString("UserNickName", "");
		Signature = sp.getString("Signature", "");
		mcontext = context;
	}

	public void SaveUserData() {
		SharedPreferences sp = mcontext.getSharedPreferences("Userdata",
				mcontext.MODE_APPEND);
		sp.edit().clear().commit();
		Editor spEd = sp.edit();
		spEd.putString("userID", UserID);
		spEd.putString("usertoken", Usertoken);
		spEd.putString("userRealName", UserRealName);
		spEd.putString("userImageUrl", UserImageUrl);
		spEd.putString("userStatus", UserStatus);
		spEd.putString("userDetail", UserDetail);
		spEd.putBoolean("isreg", isreg);
		spEd.putString("userType", UserType);
		spEd.putString("UserRoleId", UserRoleId);
		spEd.putString("UserNickName", UserNickName);
		spEd.putString("Signature", Signature);
		spEd.commit();
	}

	public void SaveUserID() {
		SharedPreferences sp = mcontext.getSharedPreferences("Userdata",
				mcontext.MODE_APPEND);
		sp.edit().clear().commit();
		Editor spEd = sp.edit();
		// UserID = "hzt";
		// Usertoken = "bbb";
		spEd.putString("userID", UserID);
		spEd.putString("Usertoken", Usertoken);
		spEd.putBoolean("isreg", isreg);
		spEd.putString("userType", UserType);
		spEd.putString("UserRoleId", UserRoleId);
		spEd.commit();
	}

	public String getUsertoken() {
		return Usertoken;
	}

	public void setUsertoken(String usertoken) {
		Usertoken = usertoken;
	}

	public String getUserID() {
		return UserID;
	}

	public String getUserImageUrl() {
		return UserImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		UserImageUrl = userImageUrl;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserRealName() {
		return UserRealName;
	}

	public void setUserRealName(String userRealName) {
		UserRealName = userRealName;
	}

	public String getUserStatus() {
		return UserStatus;
	}

	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}

	public String getUserDetail() {
		return UserDetail;
	}

	public void setUserDetail(String userDetail) {
		UserDetail = userDetail;
	}

	public boolean isIsreg() {
		return isreg;
	}

	public void setIsreg(boolean isreg) {
		this.isreg = isreg;
	}

	public String getSchoolID() {
		return SchoolID;
	}

	public void setSchoolID(String schoolID) {
		SchoolID = schoolID;
	}

	public String getSchoolName() {
		return SchoolName;
	}

	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

//	public void setUserInforContentList(
//			List<UserInforContentData> userInforContentList) {
//		UserInforContentList = userInforContentList;
//	}

	public String getUserRoleId() {
		return UserRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		UserRoleId = userRoleId;
	}

	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}
	
	public String getUidRoleidType()
	{
		return UserID + "_" + UserRoleId + "_" + UserType;
	}

    public int getUnauthedInstructor() {
        return unauthedInstructor;
    }

    public void setUnauthedInstructor(int unauthedInstructor) {
        this.unauthedInstructor = unauthedInstructor;
    }
}
