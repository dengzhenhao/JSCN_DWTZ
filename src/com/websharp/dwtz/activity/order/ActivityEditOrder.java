package com.websharp.dwtz.activity.order;

import java.util.ArrayList;
import java.util.Date;

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

import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityEditInvalid;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.activity.photo.ActivityPhotoOrder;
import com.websharp.dwtz.activity.user.ActivityLogin;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityQuarantine;
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

public class ActivityEditOrder extends BaseActivity {

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
	Button btn_unqualied_list;
	ImageView iv_spinner_goodsowner, iv_spinner_processreason, iv_spinner_processcomment, iv_spinner_origin;
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

	int position = 0;

	EntityQuarantine quar;

	boolean isInitObject = false;

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.iv_camera:

			b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC, "regist",
					GlobalData.listQuarantine.get(position).InnerID, GlobalData.curUser.InnerID));
			b.putString("title", "采集监控照片");
			Util.startActivity(ActivityEditOrder.this, ActivityWebview.class, b, false);
			break;
		case R.id.btn_unqualied_list:
			b.putString("QuarantineID", GlobalData.listQuarantine.get(position).InnerID);
			b.putString("DeliveryNum", GlobalData.listQuarantine.get(position).DeliveryNum);
			Util.startActivity(ActivityEditOrder.this, ActivityInvalidList.class, b, false);
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

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityEditOrder.this, R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_QUARANTINE));
					finish();
				} else {
					Util.createToast(ActivityEditOrder.this,
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
		try {
			new SJECHttpHandler(cb, this).editOrder(quar.InnerID, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityEditOrder.this, R.string.msg_control_failed, 3000).show();
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
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_role = (TextView) findViewById(R.id.tv_role);
		tv_staffno = (TextView) findViewById(R.id.tv_staffno);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);

		btn_unqualied_list = (Button) findViewById(R.id.btn_unqualied_list);
		btn_unqualied_list.setVisibility(View.VISIBLE);

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

		iv_spinner_goodsowner.setOnClickListener(this);
		iv_spinner_origin.setOnClickListener(this);
		iv_spinner_processreason.setOnClickListener(this);
		iv_spinner_processcomment.setOnClickListener(this);

		iv_camera.setOnClickListener(this);
		btn_unqualied_list.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_clear.setOnClickListener(this);

		btn_submit.setVisibility(View.GONE);
		btn_clear.setVisibility(View.GONE);

		et_SendTime.setEnabled(false);
		tv_DeliveryNum.setEnabled(false);
		et_GoodsOwner.setEnabled(false);
		et_Origin.setEnabled(false);
		et_QuarantineNum.setEnabled(false);
		et_QuarantineCount.setEnabled(false);
		et_ActualCount.setEnabled(false);
		et_ImmuneTag.setEnabled(false);

		// 瘦肉精检测结果
		et_CheckCount.setEnabled(false);
		et_CheckNegativeCount.setEnabled(false);
		et_CheckPositiveCount.setEnabled(false);

		// 检疫情况
		et_QualiedCount.setEnabled(false);
		et_UnqualiedCount.setEnabled(false);
		et_ProcessReason.setEnabled(false);
		et_ProcessComment.setEnabled(false);

		// 提交情况
		et_OfficalVeterSign.setEnabled(false);
		et_Remark.setEnabled(false);
		// tv_DeliveryNum.requestFocus();

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
				if (isInitObject) {
					for (int i = 0; i < listCityObject.size(); i++) {
						if (quar.origin_city_id.equals(listCityObject.get(i).ID.toString())) {
							sp_city.setSelection(i);
							break;
						}
					}
				} else {
					sp_city.setSelection(0);
				}
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
				if (isInitObject) {
					isInitObject = false;
					for (int i = 0; i < listCountyObject.size(); i++) {
						if (quar.origin_county_id.equals(listCountyObject.get(i).ID.toString())) {
							sp_county.setSelection(i);
							break;
						}
					}
				} else {
					sp_county.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		adapterCounty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCounty);
		adapterCounty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_county.setAdapter(adapterCounty);

		sp_province.setSelection(0);

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

		Bundle b = getIntent().getExtras();
		position = b.getInt("position", 0);
		if (GlobalData.listQuarantine.size() > position) {
			quar = GlobalData.listQuarantine.get(position);
			if (quar != null) {
				et_SendTime.setText(quar.SendTime);
				tv_DeliveryNum.setText(quar.DeliveryNum);
				et_GoodsOwner.setText(quar.GoodsOwner);
				et_Origin.setText(quar.Origin);
				et_QuarantineNum.setText(quar.QuarantineNum);
				et_QuarantineCount.setText(quar.QuarantineCount);
				et_ActualCount.setText(quar.ActualCount);
				et_ImmuneTag.setText(quar.ImmuneTag);
				isInitObject = true;
				for (int i = 0; i < listProvinceObject.size(); i++) {
					if (quar.origin_province_id.equals(listProvinceObject.get(i).ID.toString())) {
						sp_province.setSelection(i);
						break;
					}
				}

				// 瘦肉精检测结果
				et_CheckCount.setText(quar.CheckCount);
				et_CheckNegativeCount.setText(quar.CheckNegativeCount);
				et_CheckPositiveCount.setText(quar.CheckPositiveCount);

				// 检疫情况
				et_QualiedCount.setText(quar.QualiedCount);
				et_UnqualiedCount.setText(quar.UnqualiedCount);
				et_ProcessReason.setText(quar.ProcessReason);
				et_ProcessComment.setText(quar.ProcessComment);

				// 提交情况
				et_OfficalVeterSign.setText(quar.OfficalVeterSign);
				et_Remark.setText(quar.Remark);
				if (quar.Add_UserID.equals(GlobalData.curUser.InnerID)
						|| quar.Add_UserID.equals(GlobalData.curUser.UserID)
						|| quar.Update_UserID.equals(GlobalData.curUser.InnerID)
						|| quar.Update_UserID.equals(GlobalData.curUser.UserID) || quar.Update_UserID.equals("admin")) {
					btn_submit.setVisibility(View.VISIBLE);
					btn_clear.setVisibility(View.VISIBLE);

					// et_SendTime.setEnabled(true);
					tv_DeliveryNum.setEnabled(true);
					et_GoodsOwner.setEnabled(true);
					et_Origin.setEnabled(true);
					et_QuarantineNum.setEnabled(true);
					et_QuarantineCount.setEnabled(true);
					et_ActualCount.setEnabled(true);
					et_ImmuneTag.setEnabled(true);

					// 瘦肉精检测结果
					et_CheckCount.setEnabled(true);
					et_CheckNegativeCount.setEnabled(true);
					et_CheckPositiveCount.setEnabled(true);

					// 检疫情况
					et_QualiedCount.setEnabled(true);
					et_UnqualiedCount.setEnabled(true);
					et_ProcessReason.setEnabled(true);
					et_ProcessComment.setEnabled(true);

					// 提交情况
					et_OfficalVeterSign.setEnabled(true);
					et_Remark.setEnabled(true);
				}
			}
		}
	}

}
