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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.google.gson.reflect.TypeToken;

public class ActivityViewDestroy extends BaseActivity {

	Button btn_submit;
	Button btn_complete;
	Button btn_choose_unqualied;
	TextView tv_DestroyNum;
	TextView tv_title;
	ImageView iv_camera;
	LinearLayout layout_unqualied_count;

	EditText et_Remark;
	EditText et_DestroyTotalCount;
	EditText et_UnqualiedCount;

	String DestroyNum = "";
	String destroyID = "";
	int position = 0;
	String jsonImg = "";
	boolean isHistory = false;
	private LinearLayout layout_back;

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.btn_complete:
			Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AsyncCompleteDestroy();
						}
					}).show();
			break;
		case R.id.btn_choose_unqualied:
			b.putString("destroyID", destroyID);
			b.putBoolean("isHistory", isHistory);
			Util.startActivity(ActivityViewDestroy.this, ActivityDestroyUnqualiedList.class, b, false);
			break;
		case R.id.iv_camera:
			if (isHistory) {
				b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC, "destroy_unqualied", destroyID,
						GlobalData.curUser.InnerID));
				b.putString("title", "手机端销毁照片");
				Util.startActivity(ActivityViewDestroy.this, ActivityWebview.class, b, false);
			} else {
				Util.startActivity(ActivityViewDestroy.this, ActivityPicSend.class, false);
			}

			break;

		case R.id.layout_back:
			finish();
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
		btn_complete = (Button) findViewById(R.id.btn_complete);
		btn_choose_unqualied = (Button) findViewById(R.id.btn_choose_unqualied);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		btn_submit.setVisibility(View.GONE);
		btn_complete.setVisibility(View.VISIBLE);
		btn_choose_unqualied.setVisibility(View.VISIBLE);
		iv_camera.setVisibility(View.VISIBLE);
		iv_camera.setOnClickListener(this);

		layout_unqualied_count = (LinearLayout) findViewById(R.id.layout_unqualied_count);
		tv_DestroyNum = (TextView) findViewById(R.id.tv_DestroyNum);
		et_Remark = (EditText) findViewById(R.id.et_Remark);
		et_DestroyTotalCount = (EditText) findViewById(R.id.et_DestroyTotalCount);
		et_UnqualiedCount = (EditText) findViewById(R.id.et_UnqualiedCount);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_unqualied_count.setVisibility(View.VISIBLE);
		
		tv_title.setText("处理不合格记录");
		//btn_submit.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
		btn_choose_unqualied.setOnClickListener(this);

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		destroyID = b.getString("destroyID", "");
		position = b.getInt("position", 0);
		isHistory = b.getBoolean("isHistory", false);
		tv_DestroyNum.setText(GlobalData.listDestroy.get(position).DestroyNum);
		et_Remark.setText(GlobalData.listDestroy.get(position).Remark);
		et_Remark.setEnabled(false);
		et_DestroyTotalCount.setText(GlobalData.listDestroy.get(position).TotalCount + "");
		et_DestroyTotalCount.setEnabled(false);
		if (GlobalData.listDestroy.get(position).Add_UserID.equals(GlobalData.curUser.InnerID)
				|| GlobalData.listDestroy.get(position).Update_UserID.equals(GlobalData.curUser.InnerID)
				|| GlobalData.listDestroy.get(position).Add_UserID.equals(GlobalData.curUser.UserID)
				|| GlobalData.listDestroy.get(position).Update_UserID.equals(GlobalData.curUser.UserID)) {
			btn_choose_unqualied.setVisibility(View.VISIBLE);
		} else {
			
			btn_choose_unqualied.setVisibility(View.GONE);
		}

		if (isHistory) {
			btn_complete.setVisibility(View.GONE);
			btn_submit.setVisibility(View.GONE);
			btn_choose_unqualied.setVisibility(View.VISIBLE);
			btn_choose_unqualied.setText("查看销毁的不合格记录");
		}
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
					Util.createToast(ActivityViewDestroy.this, R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_DESTROY));
					finish();
				} else {
					Util.createToast(ActivityViewDestroy.this,
							obj.optString("desc", getString(R.string.common_login_failed)), 3000).show();
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

	AsyncHttpCallBack cbDestroyUnqualiedCount = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					et_UnqualiedCount.setText(obj.getJSONObject("data").optString("UnqualiedCount"));
					
					//如果填 写的不合格数量，与已添加的不合格记录数量相等，那么不显示 按钮 添加不合格记录
					if (ConvertUtil.ParsetStringToInt32(getText(et_DestroyTotalCount), 0) == ConvertUtil
							.ParsetStringToInt32(getText(et_UnqualiedCount), 0)) {
						btn_choose_unqualied.setVisibility(View.GONE);
						return;
					}

				} else {
					Util.createToast(ActivityViewDestroy.this,
							obj.optString("desc", getString(R.string.common_login_failed)), 3000).show();
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

	private void AsyncCompleteDestroy() {

		if (ConvertUtil.ParsetStringToInt32(getText(et_DestroyTotalCount), 0) != ConvertUtil
				.ParsetStringToInt32(getText(et_UnqualiedCount), 0)) {
			Util.createToast(this, "销毁数量与添加的不合格记录数量不符合", Toast.LENGTH_LONG).show();
			return;
		}

		jsonImg = ActivityPicSend.getJsonImg().toString();
		if (jsonImg.isEmpty() || jsonImg.equals("[]")) {
			Util.createToast(this, "请选择销毁照片", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			new SJECHttpHandler(cb, this).getUpdateDestroyStatus(destroyID, jsonImg);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityViewDestroy.this, R.string.msg_control_failed, 3000).show();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new SJECHttpHandler(cbDestroyUnqualiedCount, this).GetUnqualiedCountByDestroyID(destroyID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GlobalData.listImage.clear();
		jsonImg = null;
		System.gc();
	}

}
