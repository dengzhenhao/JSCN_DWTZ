package com.websharp.dwtz.activity.user;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.activity.main.MainActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.order.ActivityOrderList;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityUser;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.code.DescUtil;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;
import com.websharputil.widget.ThumbImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityLogin extends BaseActivity {

	private TextView tv_regist;
	private EditText et_username;
	private EditText et_password;
	private Button btn_login;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_login);
		// Intent intent = new Intent(
		// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);
		
	}

	@Override
	public void init() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

	}

	@Override
	public void bindData() {
		initParam();
//		et_username.setText("13771781982");
//		et_password.setText("123456");
		String prefUser = PrefUtil.getPref(ActivityLogin.this, "user", "");
		if (!prefUser.isEmpty()) {
			 GlobalData.curUser = JSONUtils.fromJson(prefUser,
			 EntityUser.class);
			 et_username.setText(GlobalData.curUser.UserID);
			 et_password.setText(GlobalData.curUser.Password);
			 btn_login.performClick();
			 //Util.startActivity(ActivityLogin.this, MainActivity.class, true);
		}
		// et_username.setText("18934597841");
		// et_password.setText("123456");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			// String username = et_username.getText().toString().trim();
			// GlobalData.curUser = username;
			// if(username.equals("1")){
			// //门卫验货
			// Util.startActivity(ActivityLogin.this, ActivityAddOrder.class,
			// false);
			// }else if(username.equals("2")){
			// //卸货/屠宰
			// Util.startActivity(ActivityLogin.this, ActivityOrderList.class,
			// false);
			// }else if(username.equals("3")){
			// //销毁
			// Util.startActivity(ActivityLogin.this, ActivityOrderList.class,
			// false);
			// }else{
			// Util.startActivity(ActivityLogin.this, ActivityAddOrder.class,
			// false);
			// }

			try {
				AsyncLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					String str = PrefUtil.getPref(ActivityLogin.this, "user",
							"");
					
					PrefUtil.setPref(ActivityLogin.this, "user", obj
							.getJSONObject("data").toString());
					GlobalData.curUser = JSONUtils.fromJson(
							obj.getJSONObject("data").toString(),
							EntityUser.class);
					PrefUtil.setPref(ActivityLogin.this, "lastphone",
							GlobalData.curUser.Telephone.trim()); 
					Util.createToast(
							ActivityLogin.this,R.string.common_login_success,
							3000).show();
					Util.startActivity(ActivityLogin.this, MainActivity.class, true);
				} else {
					Util.createToast(
							ActivityLogin.this,
							obj.optString("desc",
									getString(R.string.common_login_failed)),
							3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	private void AsyncLogin() throws Exception {

		new SJECHttpHandler(cb, this).login(et_username.getText().toString()
				.trim(), et_password.getText().toString().trim());
	}
}
