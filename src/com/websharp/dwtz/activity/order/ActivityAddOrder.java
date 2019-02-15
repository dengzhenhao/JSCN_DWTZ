package com.websharp.dwtz.activity.order;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityEditInvalid;
import com.websharp.dwtz.activity.photo.ActivityPhotoOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.activity.user.ActivityLogin;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityUser;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.json.JSONUtils;

public class ActivityAddOrder extends BaseActivity {

	private ImageView iv_camera;
	private ImageView iv_setting;
	private TextView tv_username;
	private TextView tv_role;
	private TextView tv_staffno;

	// 进场生猪情况
	EditText et_SendTime;
	TextView tv_DeliveryNum;
	EditText et_GoodsOwner;
	EditText et_Origin;
	EditText et_QuarantineNum;
	EditText et_QuarantineCount;
	EditText et_ActualCount;
	EditText et_ImmuneTag;

	Spinner sp_province, sp_city, sp_county;
	ArrayAdapter<String> adapterProvince = null;
	ArrayAdapter<String> adapterCity = null;
	ArrayAdapter<String> adapterCounty = null;

	// 瘦肉精检测结果
	EditText et_CheckCount;
	EditText et_CheckNegativeCount;
	EditText et_CheckPositiveCount;

	// 检疫情况
	EditText et_QualiedCount;
	EditText et_UnqualiedCount;
	EditText et_ProcessReason;
	EditText et_ProcessComment;

	// 提交情况
	EditText et_OfficalVeterSign;
	EditText et_Remark;

	Button btn_submit;
	Button btn_clear;

	ImageView iv_spinner_goodsowner, iv_spinner_processreason, iv_spinner_processcomment,iv_spinner_origin;
	// Spinner sp_goodsowner, sp_origin, sp_processreason, sp_processcomment;
	ArrayList<String> listGoodsowner = new ArrayList<String>();
	ArrayList<String> listOrigin = new ArrayList<String>();
	ArrayList<String> listProcessReason = new ArrayList<String>();
	ArrayList<String> listProcessComment = new ArrayList<String>();
	ArrayList<String> listProvince = new ArrayList<String>();
	ArrayList<String> listCity = new ArrayList<String>();
	ArrayList<String> listCounty = new ArrayList<String>();

	ArrayList<EntityLocation> listProvinceObject = new ArrayList<EntityLocation>();
	ArrayList<EntityLocation> listCityObject = new ArrayList<EntityLocation>();
	ArrayList<EntityLocation> listCountyObject = new ArrayList<EntityLocation>();

	String jsonImg = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			// Bundle b = new Bundle();
			// b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC,
			// "regist", getText(tv_DeliveryNum),GlobalData.curUser.InnerID));
			// b.putString("title", "采集监控照片");
			// Util.startActivity(ActivityAddOrder.this, ActivityWebview.class,
			// b,
			// false);
			Util.startActivity(ActivityAddOrder.this, ActivityPicSend.class, false);
			break;
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

							AsyncAddOrder();
						}
					}).show();

			break;
		case R.id.btn_clear:
			clearContent();
			break;
		case R.id.iv_spinner_goodsowner:
			showDialogListView(listGoodsowner, et_GoodsOwner);
			break;
		case R.id.iv_spinner_origin:
			showDialogListView(listOrigin, et_Origin);
			break;
		case R.id.iv_spinner_processreason:
			showDialogListView(listProcessReason, et_ProcessReason);
			break;
		case R.id.iv_spinner_processcomment:
			showDialogListView(listProcessComment, et_ProcessComment);
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_order);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		// iv_camera.setVisibility(View.GONE);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_role = (TextView) findViewById(R.id.tv_role);
		tv_staffno = (TextView) findViewById(R.id.tv_staffno);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);

		// 进场生猪情况
		et_SendTime = (EditText) findViewById(R.id.et_SendTime);
		tv_DeliveryNum = (TextView) findViewById(R.id.et_DeliveryNum);
		et_GoodsOwner = (EditText) findViewById(R.id.et_GoodsOwner);
		et_Origin = (EditText) findViewById(R.id.et_Origin);
		et_QuarantineNum = (EditText) findViewById(R.id.et_QuarantineNum);
		et_QuarantineCount = (EditText) findViewById(R.id.et_QuarantineCount);
		et_ActualCount = (EditText) findViewById(R.id.et_ActualCount);
		et_ImmuneTag = (EditText) findViewById(R.id.et_ImmuneTag);

		sp_province = (Spinner) findViewById(R.id.sp_province);
		sp_city = (Spinner) findViewById(R.id.sp_city);
		sp_county = (Spinner) findViewById(R.id.sp_county);

		// 瘦肉精检测结果
		et_CheckCount = (EditText) findViewById(R.id.et_CheckCount);
		et_CheckNegativeCount = (EditText) findViewById(R.id.et_CheckNegativeCount);
		et_CheckPositiveCount = (EditText) findViewById(R.id.et_CheckPositiveCount);

		// 检疫情况
		et_QualiedCount = (EditText) findViewById(R.id.et_QualiedCount);
		et_UnqualiedCount = (EditText) findViewById(R.id.et_UnqualiedCount);
		et_ProcessReason = (EditText) findViewById(R.id.et_ProcessReason);
		et_ProcessComment = (EditText) findViewById(R.id.et_ProcessComment);

		// 提交情况
		et_OfficalVeterSign = (EditText) findViewById(R.id.et_OfficalVeterSign);
		et_Remark = (EditText) findViewById(R.id.et_Remark);

		iv_spinner_goodsowner = (ImageView) findViewById(R.id.iv_spinner_goodsowner);

		iv_spinner_origin = (ImageView) findViewById(R.id.iv_spinner_origin);

		iv_spinner_processreason = (ImageView) findViewById(R.id.iv_spinner_processreason);

		iv_spinner_processcomment = (ImageView) findViewById(R.id.iv_spinner_processcomment);

		Button btn_unqualied_list = (Button) findViewById(R.id.btn_unqualied_list);
		btn_unqualied_list.setVisibility(View.GONE);

		iv_camera.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		iv_spinner_goodsowner.setOnClickListener(this);
		iv_spinner_origin.setOnClickListener(this);
		iv_spinner_processreason.setOnClickListener(this);
		iv_spinner_processcomment.setOnClickListener(this);
		// tv_DeliveryNum.requestFocus();

	}

	private String checkContent() {
		String result = "";

		if (getText(tv_DeliveryNum).isEmpty()) {
			return getString(R.string.msg_empty_DeliveryNum);
		}

		if (getText(et_GoodsOwner).isEmpty()) {
			return getString(R.string.msg_empty_GoodsOwner);
		}

		if (getText(et_Origin).isEmpty()) {
			return getString(R.string.msg_empty_Origin);
		}

		if (sp_province.getSelectedItemPosition() == 0) {
			return "请选择省份";
		}

		if (sp_city.getSelectedItemPosition() == 0) {
			return "请选择城市";
		}

		if (sp_county.getSelectedItemPosition() == 0) {
			return "请选择区/县";
		}

		if (getText(et_QuarantineNum).isEmpty()) {
			return getString(R.string.msg_empty_QuarantineNum);
		}
		if (getText(et_QuarantineCount).isEmpty()) {
			return getString(R.string.msg_empty_QuarantineCount);
		}
		if (getText(et_ActualCount).isEmpty()) {
			return getString(R.string.msg_empty_ActualCount);
		}
		if (getText(et_ImmuneTag).isEmpty()) {
			return getString(R.string.msg_empty_ImmuneTag);
		}

		// 瘦肉精检测结果
		if (getText(et_CheckCount).isEmpty()) {
			return getString(R.string.msg_empty_CheckCount);
		}
		if (getText(et_CheckNegativeCount).isEmpty()) {
			return getString(R.string.msg_empty_CheckNegativeCount);
		}
		if (getText(et_CheckPositiveCount).isEmpty()) {
			return getString(R.string.msg_empty_CheckPositiveCount);
		}

		// 检疫情况
		if (getText(et_QualiedCount).isEmpty()) {
			return getString(R.string.msg_empty_QualiedCount);
		}
		if (getText(et_UnqualiedCount).isEmpty()) {
			return getString(R.string.msg_empty_UnqualiedCount);
		}
		// if (getText(et_ProcessReason).isEmpty()) {
		//
		// }
		// if (getText(et_ProcessComment).isEmpty()) {
		//
		// }

		// 提交情况
		if (getText(et_OfficalVeterSign).isEmpty()) {
			return getString(R.string.msg_empty_OfficalVeterSign);
		}
		// if (getText(et_Remark).isEmpty()) {
		//
		// }

		return result;
	}

	private void clearContent() {
		// et_DeliveryNum.setText("");
		et_GoodsOwner.setText("");
		et_Origin.setText("");
		et_QuarantineNum.setText("");
		et_QuarantineCount.setText("");
		et_ActualCount.setText("");
		et_ImmuneTag.setText("");

		// 瘦肉精检测结果
		et_CheckCount.setText("");
		et_CheckNegativeCount.setText("");
		et_CheckPositiveCount.setText("");

		// 检疫情况
		et_QualiedCount.setText("");
		et_UnqualiedCount.setText("");
		et_ProcessReason.setText("");
		et_ProcessComment.setText("");

		// 提交情况
		et_OfficalVeterSign.setText("");
		et_Remark.setText("");
	}

	@Override
	public void bindData() {
		for (int i = 0; i < GlobalData.listProvince.size(); i++) {
			listProvince.add(GlobalData.listProvince.get(i).Name);
			listProvinceObject.add(GlobalData.listProvince.get(i));
		}

		adapterProvince = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProvince);
		adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_province.setAdapter(adapterProvince);
		sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogUtil.d("sp_provincesp_provincesp_provincesp_province");
				adapterCity.clear();
				listCity.clear();
				listCityObject.clear();
				int parentID = listProvinceObject.get(position).ID;
				listCity = new ArrayList<String>();
				for (int i = 0; i < GlobalData.listCity.size(); i++) {
					if (GlobalData.listCity.get(i).ParentID == parentID || GlobalData.listCity.get(i).ID == -1) {
						listCity.add(GlobalData.listCity.get(i).Name);
						listCityObject.add(GlobalData.listCity.get(i));
					}
				}
				adapterCity.addAll(listCity);
				adapterCity.notifyDataSetChanged();
				sp_city.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		adapterCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCity);
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_city.setAdapter(adapterCity);
		sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogUtil.d("sp_citysp_citysp_city");
				adapterCounty.clear();
				listCounty.clear();
				listCountyObject.clear();
				int parentID = listCityObject.get(position).ID;
				listCounty = new ArrayList<String>();
				for (int i = 0; i < GlobalData.listCounty.size(); i++) {
					if (GlobalData.listCounty.get(i).ParentID == parentID || GlobalData.listCounty.get(i).ID == -1) {
						listCounty.add(GlobalData.listCounty.get(i).Name);
						listCountyObject.add(GlobalData.listCounty.get(i));
					}
				}
				adapterCounty.addAll(listCounty);
				adapterCounty.notifyDataSetChanged();
				sp_county.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		adapterCounty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCounty);
		adapterCounty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_county.setAdapter(adapterCounty);

		sp_province.setSelection(0);

		new SJECHttpHandler(cbGetLsNo, this).getLsNo(Constant.LSNO_PREFIX_HD);
		et_SendTime.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
		for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
			if (!GlobalData.listCommonData.get(i).ButcheryID.equals(GlobalData.curButcheryID))
				continue;
			if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_GOODSOWNER)) {
				listGoodsowner.add(GlobalData.listCommonData.get(i).DataValue);
			} else if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_ORIGIN)) {
				listOrigin.add(GlobalData.listCommonData.get(i).DataValue);
			} else if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_PROCESS_REASON)) {
				listProcessReason.add(GlobalData.listCommonData.get(i).DataValue);
			} else if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_PROCESS_COMMENT)) {
				listProcessComment.add(GlobalData.listCommonData.get(i).DataValue);
			}
		}
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
					tv_DeliveryNum.setText(lsNo);
				} else {
					Util.createToast(ActivityAddOrder.this,
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

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityAddOrder.this, R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_QUARANTINE));
					finish();
				} else {
					Util.createToast(ActivityAddOrder.this,
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

	private void AsyncAddOrder() {

		JsonObject json = new JsonObject();

		json.addProperty("ButcheryID", GlobalData.curButcheryID);
		json.addProperty("SendTime", getText(et_SendTime));
		json.addProperty("DeliveryNum", getText(tv_DeliveryNum));
		json.addProperty("GoodsOwner", getText(et_GoodsOwner));
		 json.addProperty("Origin", getText(et_Origin));
		json.addProperty("origin_province_id", listProvinceObject.get(sp_province.getSelectedItemPosition()).ID);
		json.addProperty("origin_city_id", listCityObject.get(sp_city.getSelectedItemPosition()).ID);
		json.addProperty("origin_county_id", listCountyObject.get(sp_county.getSelectedItemPosition()).ID);
		json.addProperty("QuarantineNum", getText(et_QuarantineNum));
		json.addProperty("QuarantineCount", getText(et_QuarantineCount));
		json.addProperty("ActualCount", getText(et_ActualCount));
		json.addProperty("ImmuneTag", getText(et_ImmuneTag));

		json.addProperty("CheckCount", getText(et_CheckCount));
		json.addProperty("CheckNegativeCount", getText(et_CheckNegativeCount));
		json.addProperty("CheckPositiveCount", getText(et_CheckPositiveCount));

		json.addProperty("QualiedCount", getText(et_QualiedCount));
		json.addProperty("UnqualiedCount", getText(et_UnqualiedCount));
		json.addProperty("ProcessReason", getText(et_ProcessReason));
		json.addProperty("ProcessComment", getText(et_ProcessComment));

		json.addProperty("OfficalVeterSign", getText(et_OfficalVeterSign));
		json.addProperty("Remark", getText(et_Remark));
		jsonImg = new ActivityPicSend().getJsonImg().toString();
		try {
			new SJECHttpHandler(cb, this).addOrder(json.toString(), jsonImg);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityAddOrder.this, R.string.msg_control_failed, 3000).show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GlobalData.listImage.clear();
		jsonImg = null;
	}
}
