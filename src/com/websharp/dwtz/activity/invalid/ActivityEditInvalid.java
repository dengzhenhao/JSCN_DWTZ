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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	
	LinearLayout layout_processreason;
	LinearLayout layout_bhg_weight;

	RadioButton rb_invalid_type_zq;
	RadioButton rb_invalid_type_zh;
	RadioButton rb_invalid_type_bhg;
	RadioGroup rg_invalid_type;

	EditText et_bhg_weight;
	
	ArrayAdapter adapterButcheryGroup;
	ArrayList<String> listButcheryGroupName = new ArrayList<String>();
	ArrayList<EntityButcheryGroup> listButcheryGroup = new ArrayList<EntityButcheryGroup>();
	private LinearLayout layout_back;
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			Bundle b = new Bundle();
			b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC, "resolve",
					GlobalData.listUnqualied.get(position).InnerID, GlobalData.curUser.InnerID));
			b.putString("title", "采集监控照片");
			Util.startActivity(ActivityEditInvalid.this, ActivityWebview.class, b, false);
			break;
		case R.id.layout_back:
			finish();
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

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
		adapterButcheryGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				listButcheryGroupName);
		adapterButcheryGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_butchery_group.setAdapter(adapterButcheryGroup);
		et_UnqualiedScanCode.setEnabled(false);
		et_UnqualiedScanCode_2.setEnabled(false);
		et_ProcessReason.setEnabled(false);
		et_ProcessComment.setEnabled(false);
		et_Remark.setEnabled(false);
		
		rg_invalid_type = (RadioGroup) findViewById(R.id.rg_invalid_type);
		rb_invalid_type_zq = (RadioButton) findViewById(R.id.rb_invalid_type_zq);
		rb_invalid_type_zh = (RadioButton) findViewById(R.id.rb_invalid_type_zh);
		rb_invalid_type_bhg =  (RadioButton) findViewById(R.id.rb_invalid_type_bhg);

		layout_processreason = (LinearLayout) findViewById(R.id.layout_processreason);
		layout_bhg_weight = (LinearLayout) findViewById(R.id.layout_bhg_weight);
		et_bhg_weight = (EditText) findViewById(R.id.et_bhg_weight);
		
		et_bhg_weight.setEnabled(false);
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
						listButcheryGroupName.add(listButcheryGroup.get(i).GroupName);
						if (listButcheryGroup.get(i).InnerID
								.equals(GlobalData.listUnqualied.get(position).ButcheryGroupID)) {
							selectPosition = i;
						}
					}
					adapterButcheryGroup.notifyDataSetChanged();

					tv_DeliveryNum.setText(GlobalData.listUnqualied.get(position).DeliveryNum);
					et_UnqualiedScanCode.setText(GlobalData.listUnqualied.get(position).UnqualiedScanCode);
					et_UnqualiedScanCode_2.setText(GlobalData.listUnqualied.get(position).UnqualiedScanCode_2);
					et_ProcessReason.setText(GlobalData.listUnqualied.get(position).ProcessReason);
					et_ProcessComment.setText(GlobalData.listUnqualied.get(position).ProcessComment);
					et_Remark.setText(GlobalData.listUnqualied.get(position).Remark);
					sp_butchery_group.setSelection(selectPosition);
					et_bhg_weight.setText(GlobalData.listUnqualied.get(position).Weight);
					if(GlobalData.listUnqualied.get(position).Type.equals("0")){
						rb_invalid_type_zq.setChecked(true);
						layout_processreason.setVisibility(View.VISIBLE);
					}else 	if(GlobalData.listUnqualied.get(position).Type.equals("1")){
						rb_invalid_type_zh.setChecked(true);
						layout_processreason.setVisibility(View.VISIBLE);
					}else 	if(GlobalData.listUnqualied.get(position).Type.equals("2")){
						rb_invalid_type_bhg.setChecked(true);
						layout_bhg_weight.setVisibility(View.VISIBLE);
					}
					
				} else {
					Util.createToast(ActivityEditInvalid.this,
							obj.optString("desc", getString(R.string.msg_failed_getButcheryGroup)), 3000).show();
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
