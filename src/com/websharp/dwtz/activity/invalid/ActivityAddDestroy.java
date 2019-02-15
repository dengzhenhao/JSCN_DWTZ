package com.websharp.dwtz.activity.invalid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.google.gson.reflect.TypeToken;

public class ActivityAddDestroy extends BaseActivity {

	Button btn_submit;
	TextView tv_DestroyNum;

	EditText et_Remark;
	EditText et_DestroyTotalCount;

	String DestroyNum = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			Util.createDialog(this, null, R.string.msg_dialog_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AsyncAddDestroy();
						}
					}).show();
			break;
		
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_add_destroy);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btn_submit = (Button) findViewById(R.id.btn_submit);
		tv_DestroyNum =(TextView)findViewById(R.id.tv_DestroyNum);
		et_Remark = (EditText) findViewById(R.id.et_Remark);
		et_DestroyTotalCount = (EditText)findViewById(R.id.et_DestroyTotalCount);
		btn_submit.setOnClickListener(this);

	}

	@Override
	public void bindData() {
		
		getInitData();
	}

	public void getInitData() {
		new SJECHttpHandler(cbGetLsNo, this).getLsNo(Constant.LSNO_PREFIX_XH);
	}

	AsyncHttpCallBack cbGetLsNo = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					JSONObject objData = obj.optJSONObject("data");
					String lsNo = objData.optString("lsno", "failed");
					tv_DestroyNum.setText(lsNo);
				} else {
					Util.createToast(
							ActivityAddDestroy.this,
							obj.optString("desc",
									getString(R.string.msg_failed_getlsno)),
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
	


	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityAddDestroy.this,
							R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_LIST_DESTROY));
					finish();
				} else {
					Util.createToast(
							ActivityAddDestroy.this,
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

	

	private void AsyncAddDestroy() {
 
		JsonObject json = new JsonObject();
		json.addProperty("DestroyNum", getText(tv_DestroyNum));
		json.addProperty("Remark", getText(et_Remark));
		json.addProperty("ButcheryID", GlobalData.curButcheryID);
		json.addProperty("TotalCount", getText(et_DestroyTotalCount));
		try {
			new SJECHttpHandler(cb, this).addDestroy(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityAddDestroy.this,
					R.string.msg_control_failed, 3000).show();
		}
	}

}
