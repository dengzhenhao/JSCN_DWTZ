package com.websharp.dwtz.activity.invalid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.apply.ActivityAddDsitributionChangeApply;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.activity.qr.CaptureActivity;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.google.gson.reflect.TypeToken;

public class ActivityAddInvalid extends BaseActivity {

	Button btn_submit;
	Button btn_clear;
	Button btn_gen_scan_code;
	TextView tv_DeliveryNum;
	EditText et_UnqualiedScanCode;
	EditText et_UnqualiedScanCode_2;
	EditText et_ProcessReason;
	EditText et_ProcessComment;
	EditText et_Remark;
	Spinner sp_butchery_group;
	ImageView iv_camera;

	LinearLayout layout_processreason;
	LinearLayout layout_bhg_weight;

	RadioButton rb_invalid_type_zq;
	RadioButton rb_invalid_type_zh;
	RadioButton rb_invalid_type_bhg;
	RadioGroup rg_invalid_type;

	EditText et_bhg_weight;

	String QuarantineID = "";
	String DeliveryNum = "";
	ArrayAdapter adapterButcheryGroup;
	ArrayList<String> listButcheryGroupName = new ArrayList<String>();
	ArrayList<EntityButcheryGroup> listButcheryGroup = new ArrayList<EntityButcheryGroup>();

	ImageView iv_spinner_processreason, iv_spinner_processcomment;
	ArrayList<String> listProcessReason = new ArrayList<String>();
	ArrayList<String> listProcessComment = new ArrayList<String>();
	String jsonImg = "";

	ImageView iv_spinner_qr;
	ImageView iv_spinner_qr_2;
	private LinearLayout layout_back;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			String checkResult = checkContent();
			if (!checkResult.isEmpty()) {
				Util.createToast(this, checkResult, 3000).show();
				return;
			}
			Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AsyncAddInvalid();
						}
					}).show();
			break;
		case R.id.btn_clear:
			Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							et_ProcessComment.setText("");
							et_ProcessReason.setText("");
							et_Remark.setText("");
							et_UnqualiedScanCode.setText("");
						}
					}).show();

			break;
		case R.id.iv_spinner_processreason:
			showDialogListView(listProcessReason, et_ProcessReason);
			break;
		case R.id.iv_spinner_processcomment:
			showDialogListView(listProcessComment, et_ProcessComment);
			break;
		case R.id.iv_camera:
			Util.startActivity(ActivityAddInvalid.this, ActivityPicSend.class, false);
			break;
		case R.id.iv_spinner_qr:
			ActivityAddInvalid.this.startActivityForResult(new Intent(ActivityAddInvalid.this, CaptureActivity.class),
					99);
			break;
		case R.id.iv_spinner_qr_2:
			ActivityAddInvalid.this.startActivityForResult(new Intent(ActivityAddInvalid.this, CaptureActivity.class),
					100);
			break;
		case R.id.btn_gen_scan_code:
			new SJECHttpHandler(cbGetLsNo, this).getLsNo(Constant.LSNO_PREFIX_BHG);
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
		btn_gen_scan_code = (Button) findViewById(R.id.btn_gen_scan_code);
		tv_DeliveryNum = (TextView) findViewById(R.id.tv_DeliveryNum);
		et_UnqualiedScanCode = (EditText) findViewById(R.id.et_UnqualiedScanCode);
		et_UnqualiedScanCode_2 = (EditText) findViewById(R.id.et_UnqualiedScanCode_2);
		et_ProcessReason = (EditText) findViewById(R.id.et_ProcessReason);
		et_ProcessComment = (EditText) findViewById(R.id.et_ProcessComment);
		et_Remark = (EditText) findViewById(R.id.et_Remark);
		sp_butchery_group = (Spinner) findViewById(R.id.sp_butchery_group);
		iv_spinner_processreason = (ImageView) findViewById(R.id.iv_spinner_processreason);
		iv_spinner_processcomment = (ImageView) findViewById(R.id.iv_spinner_processcomment);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		iv_spinner_qr = (ImageView) findViewById(R.id.iv_spinner_qr);
		iv_spinner_qr_2 = (ImageView) findViewById(R.id.iv_spinner_qr_2);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);

		rg_invalid_type = (RadioGroup) findViewById(R.id.rg_invalid_type);
		rb_invalid_type_zq = (RadioButton) findViewById(R.id.rb_invalid_type_zq);
		rb_invalid_type_zh = (RadioButton) findViewById(R.id.rb_invalid_type_zh);
		rb_invalid_type_bhg =  (RadioButton) findViewById(R.id.rb_invalid_type_bhg);

		layout_processreason = (LinearLayout) findViewById(R.id.layout_processreason);
		layout_bhg_weight = (LinearLayout) findViewById(R.id.layout_bhg_weight);
		et_bhg_weight = (EditText) findViewById(R.id.et_bhg_weight);

		layout_back.setOnClickListener(this);
		iv_spinner_qr.setOnClickListener(this);
		iv_spinner_qr_2.setOnClickListener(this);
		iv_camera.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_gen_scan_code.setOnClickListener(this);
		iv_spinner_processreason.setOnClickListener(this);
		iv_spinner_processcomment.setOnClickListener(this);

		rg_invalid_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_invalid_type_zh || checkedId == R.id.rb_invalid_type_zq) {
					layout_processreason.setVisibility(View.VISIBLE);
					layout_bhg_weight.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_invalid_type_bhg) {
					layout_processreason.setVisibility(View.GONE);
					layout_bhg_weight.setVisibility(View.VISIBLE);
				}
			}
		});

		adapterButcheryGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				listButcheryGroupName);
		adapterButcheryGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_butchery_group.setAdapter(adapterButcheryGroup);

		et_ProcessReason.requestFocus();

	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		QuarantineID = b.getString("QuarantineID", "");
		DeliveryNum = b.getString("DeliveryNum", "");
		tv_DeliveryNum.setText(DeliveryNum);
		for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
			if (!GlobalData.listCommonData.get(i).ButcheryID.equals(GlobalData.curButcheryID))
				continue;
			if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_PROCESS_REASON)) {
				listProcessReason.add(GlobalData.listCommonData.get(i).DataValue);
			} else if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_PROCESS_COMMENT)) {
				listProcessComment.add(GlobalData.listCommonData.get(i).DataValue);
			}
		}

		getInitData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GlobalData.listImage.clear();
		jsonImg = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			if (requestCode == 99) {
				Bundle b = data.getExtras();
				String qrCode = b.getString("data");
				et_UnqualiedScanCode.setText(qrCode);
			} else if (requestCode == 100) {
				Bundle b = data.getExtras();
				String qrCode = b.getString("data");
				et_UnqualiedScanCode_2.setText(qrCode);
			}
		}
	}

	public void getInitData() {
		// new SJECHttpHandler(cbGetLsNo,
		// this).getLsNo(Constant.LSNO_PREFIX_BHG);
		new SJECHttpHandler(cbGetButcheryGroup, this).getButcheryGroup(QuarantineID);
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
					et_UnqualiedScanCode.setText(lsNo);
				} else {
					Util.createToast(ActivityAddInvalid.this,
							obj.optString("desc", getString(R.string.msg_failed_getlsno)), 3000).show();
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

					for (int i = 0; i < listButcheryGroup.size(); i++) {
						listButcheryGroupName.add(listButcheryGroup.get(i).GroupName);
					}
					adapterButcheryGroup.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityAddInvalid.this,
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

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityAddInvalid.this, R.string.msg_control_success_continue_operator, 3000)
							.show();
					et_UnqualiedScanCode.setText("");
					et_ProcessComment.setText("");
					et_ProcessReason.setText("");
					et_Remark.setText("");
					et_UnqualiedScanCode_2.setText("");
					et_bhg_weight.setText("");
					rb_invalid_type_zq.setChecked(false);
					rb_invalid_type_zh.setChecked(false);
					rb_invalid_type_bhg.setChecked(false);
					layout_processreason.setVisibility(View.GONE);
					layout_bhg_weight.setVisibility(View.GONE);
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_UNQUALIED));
					// finish();
				} else {
					Util.createToast(ActivityAddInvalid.this,
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

	private String checkContent() {

		// if (getText(et_UnqualiedScanCode).isEmpty()) {
		// return getString(R.string.msg_empty_UnqualiedScanCode);
		// }

		if (GlobalData.listImage.size() == 1 && !GlobalData.listImage.get(0).isImage) {
			return getString(R.string.msg_empty_image_size_0);
		}

		if (sp_butchery_group.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			return "请选择一个屠宰小组";
		}

		if (rg_invalid_type.getCheckedRadioButtonId() == -1) {
			return "请选择不合格记录类型";
		}

		if (rg_invalid_type.getCheckedRadioButtonId() == R.id.rb_invalid_type_zh
				|| rg_invalid_type.getCheckedRadioButtonId() == R.id.rb_invalid_type_zq) {
			if (getText(et_ProcessReason).isEmpty()) {
				return getString(R.string.msg_empty_ProcessReason);
			}
		}else if(rg_invalid_type.getCheckedRadioButtonId() == R.id.rb_invalid_type_bhg){
			if (getText(et_bhg_weight).isEmpty() || ConvertUtil.ParsetStringToDouble(getText(et_bhg_weight),0) == 0) {
				return "不合格品重量不能为空并且不能为0";
			}
		}
		
		if (getText(et_ProcessComment).isEmpty()) {
			return getString(R.string.msg_empty_ProcessComment);
		}

		return "";
	}

	private void AsyncAddInvalid() {

		JsonObject json = new JsonObject();
		json.addProperty("ProcessComment", getText(et_ProcessComment));
		json.addProperty("ProcessReason", getText(et_ProcessReason));
		json.addProperty("UnqualiedScanCode", getText(et_UnqualiedScanCode));
		json.addProperty("UnqualiedScanCode_2", getText(et_UnqualiedScanCode_2));
		json.addProperty("ButcheryGroupID", listButcheryGroup.get(sp_butchery_group.getSelectedItemPosition()).InnerID);
		json.addProperty("Remark", getText(et_Remark));
		json.addProperty("Weight", getText(et_bhg_weight));
		if (rg_invalid_type.getCheckedRadioButtonId() == rb_invalid_type_zq.getId()) {
			json.addProperty("Type", 0);
		} else if (rg_invalid_type.getCheckedRadioButtonId() == rb_invalid_type_zh.getId()) {
			json.addProperty("Type", 1);
		} else {
			json.addProperty("Type", 2);
		}

		jsonImg = ActivityPicSend.getJsonImg().toString();
		try {
			new SJECHttpHandler(cb, this).addUnqualied(json.toString(), QuarantineID, jsonImg);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityAddInvalid.this, R.string.msg_control_failed, 3000).show();
		}
	}

}
