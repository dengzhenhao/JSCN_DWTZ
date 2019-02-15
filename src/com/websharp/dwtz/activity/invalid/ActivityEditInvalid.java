package com.websharp.dwtz.activity.invalid;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 
 * @author dengzh
 * 
 */
public class ActivityEditInvalid extends BaseActivity {
	Button btn_submit;
	Button btn_clear;
	ImageView iv_camera;
	TextView tv_DeliveryNum;
	EditText et_UnqualiedScanCode;
	EditText et_UnqualiedScanCode_2;
	EditText et_ProcessReason;
	EditText et_ProcessComment;
	EditText et_Remark;
	Spinner sp_butchery_group;
	int position = 0;
	ArrayAdapter adapterButcheryGroup;
	ArrayList<String> listButcheryGroupName = new ArrayList<String>();
	ArrayList<EntityButcheryGroup> listButcheryGroup = new ArrayList<EntityButcheryGroup>();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			Bundle b = new Bundle();
			b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC,
					"resolve", GlobalData.listUnqualied.get(position).InnerID,
					GlobalData.curUser.InnerID));
			b.putString("title", "采集监控照片");
			Util.startActivity(ActivityEditInvalid.this, ActivityWebview.class,
					b, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_add_invalid);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		tv_DeliveryNum = (TextView) findViewById(R.id.tv_DeliveryNum);
		et_UnqualiedScanCode = (EditText) findViewById(R.id.et_UnqualiedScanCode);
		et_UnqualiedScanCode_2 = (EditText) findViewById(R.id.et_UnqualiedScanCode_2);
		et_ProcessReason = (EditText) findViewById(R.id.et_ProcessReason);
		et_ProcessComment = (EditText) findViewById(R.id.et_ProcessComment);
		et_Remark = (EditText) findViewById(R.id.et_Remark);
		sp_butchery_group = (Spinner) findViewById(R.id.sp_butchery_group);
		btn_submit.setVisibility(View.GONE);
		btn_clear.setVisibility(View.GONE);
		iv_camera.setVisibility(View.VISIBLE);
		iv_camera.setOnClickListener(this);

		adapterButcheryGroup = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listButcheryGroupName);
		adapterButcheryGroup
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_butchery_group.setAdapter(adapterButcheryGroup);
		et_UnqualiedScanCode.setEnabled(false);
		et_UnqualiedScanCode_2.setEnabled(false);
		et_ProcessReason.setEnabled(false);
		et_ProcessComment.setEnabled(false);
		et_Remark.setEnabled(false);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		position = b.getInt("position", 0);
		getInitData();
	}

	public void getInitData() {
		new SJECHttpHandler(cbGetButcheryGroup, this)
				.getButcheryGroup(GlobalData.listUnqualied.get(position).QuarantineId);
	}

	AsyncHttpCallBack cbGetButcheryGroup = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					listButcheryGroup = gson.fromJson(obj.getString("data"),
							new TypeToken<ArrayList<EntityButcheryGroup>>() {
							}.getType());
					int selectPosition = 0;
					for (int i = 0; i < listButcheryGroup.size(); i++) {
						listButcheryGroupName
								.add(listButcheryGroup.get(i).GroupName);
						if (listButcheryGroup.get(i).InnerID
								.equals(GlobalData.listUnqualied.get(position).ButcheryGroupID)) {
							selectPosition = i;
						}
					}
					adapterButcheryGroup.notifyDataSetChanged();

					tv_DeliveryNum.setText(GlobalData.listUnqualied
							.get(position).DeliveryNum);
					et_UnqualiedScanCode.setText(GlobalData.listUnqualied
							.get(position).UnqualiedScanCode);
					et_UnqualiedScanCode_2.setText(GlobalData.listUnqualied
							.get(position).UnqualiedScanCode_2);
					et_ProcessReason.setText(GlobalData.listUnqualied
							.get(position).ProcessReason);
					et_ProcessComment.setText(GlobalData.listUnqualied
							.get(position).ProcessComment);
					et_Remark
							.setText(GlobalData.listUnqualied.get(position).Remark);
					sp_butchery_group.setSelection(selectPosition);
				} else {
					Util.createToast(
							ActivityEditInvalid.this,
							obj.optString(
									"desc",
									getString(R.string.msg_failed_getButcheryGroup)),
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
}
