package com.websharp.dwtz.activity.apply;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 添加检疫分销换证的申情
 * 
 * @author dengzh
 * 
 */
public class ActivityAddDsitributionChangeApply extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	
	Button btn_submit;
	Button btn_clear;
	TextView tv_DeliveryNum;
	Spinner sp_breed;// 品种
	EditText et_old_qc_no;
	EditText et_distribution_target_address;
	EditText et_0ld_distribution_count;
//	EditText et_Remark;
	EditText et_distribution_company_name;
	EditText et_breedforelse;
	
	LinearLayout layout_breedforelse;

	String breed = "";
	String[] arrBreed = null;
	ArrayAdapter<String> adapterBreed = null;

	ImageView iv_spinner_distribution_company_name;
	ArrayList<String> listCompanyName = new ArrayList<String>();

	TextView tv_add_detail;
	ArrayList<EntityDistriDetail> listDetail = new ArrayList<ActivityAddDsitributionChangeApply.EntityDistriDetail>();
	TextView tv_LsNo;
	LinearLayout layout_detail;
	ImageView iv_camera;
	String jsonImg="";
	
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constant.ACTION_ADD_DISTRI_DETAIL)) {
				// add detail
				listDetail.add(new EntityDistriDetail(intent.getExtras()
						.getString("target_address"), intent.getExtras()
						.getDouble("count")));
				bindDetail();
				LogUtil.d(listDetail.toString());
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.iv_camera:
			Util.startActivity(ActivityAddDsitributionChangeApply.this, ActivityPicSend.class, false);
			break;
		case R.id.btn_submit:
			String checkResult = checkContent();
			if (!checkResult.isEmpty()) {
				Util.createToast(this, checkResult, 3000).show();
				return;
			}
			Util.createDialog(this, null, R.string.msg_dialog_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AsyncAddDistributionChangeApply();
						}
					}).show();
			break;
		case R.id.btn_clear:
			Util.createDialog(this, null, R.string.msg_dialog_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sp_breed.setSelection(0);
							et_old_qc_no.setText("");
							et_distribution_target_address.setText("");
							et_0ld_distribution_count.setText("");
//							et_Remark.setText("");
						}
					}).show();

			break;
		case R.id.iv_spinner_distribution_company_name:
			showDialogListView(listCompanyName, et_distribution_company_name);
			break;
		case R.id.tv_add_detail:
			Util.startActivity(ActivityAddDsitributionChangeApply.this,
					ActivityAddDsitributionDetail.class, false);
			break;
		}
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		unregisterReceiver(receiver);
		listDetail.clear();
		GlobalData.listImage.clear();
		jsonImg = null;
		System.gc();
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_add_distribution_change_apply);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter(
				Constant.ACTION_ADD_DISTRI_DETAIL);
		registerReceiver(receiver, filter);
		layout_back = (LinearLayout)findViewById(R.id.layout_back);
		tv_title = (TextView)findViewById(R.id.tv_title);
		iv_camera = (ImageView)findViewById(R.id.iv_camera);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		tv_DeliveryNum = (TextView) findViewById(R.id.tv_DeliveryNum);
		sp_breed = (Spinner) findViewById(R.id.sp_breed);
		et_old_qc_no = (EditText) findViewById(R.id.et_old_qc_no);
		et_distribution_target_address = (EditText) findViewById(R.id.et_distribution_target_address);
		et_0ld_distribution_count = (EditText) findViewById(R.id.et_0ld_distribution_count);
//		et_Remark = (EditText) findViewById(R.id.et_Remark);
		et_breedforelse = (EditText)findViewById(R.id.et_breedforelse);
		layout_breedforelse = (LinearLayout)findViewById(R.id.layout_breedforelse);
		et_distribution_company_name = (EditText) findViewById(R.id.et_distribution_company_name);
		iv_spinner_distribution_company_name = (ImageView) findViewById(R.id.iv_spinner_distribution_company_name);
		tv_add_detail = (TextView) findViewById(R.id.tv_add_detail);
		tv_LsNo = (TextView) findViewById(R.id.tv_LsNo);
		layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
		tv_add_detail.setOnClickListener(this);
		iv_spinner_distribution_company_name.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		iv_camera.setOnClickListener(this);
		et_old_qc_no.requestFocus();
	}

	@Override
	public void bindData() {
		tv_title.setText("分销申请");
		if(!GlobalData.curUser.ApplyCompanyName.isEmpty()){
			et_distribution_company_name.setText(GlobalData.curUser.ApplyCompanyName);
			et_distribution_company_name.setEnabled(false);
			iv_spinner_distribution_company_name.setVisibility(View.GONE);
		}
		
		arrBreed = getResources().getStringArray(
				R.array.distribution_apply_breed);
		adapterBreed = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrBreed);

		adapterBreed
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_breed.setAdapter(adapterBreed);
		sp_breed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				arg0.setVisibility(View.VISIBLE);
				breed = arrBreed[position];
				if(position == 5){
					layout_breedforelse.setVisibility(View.VISIBLE); 
				}else{
					layout_breedforelse.setVisibility(View.GONE); 
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		sp_breed.setSelection(0);

		for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
			if (GlobalData.listCommonData.get(i).Type
					.equals(Constant.COMMON_DATA_TYPE_DISTRIBUTION_COMPANY)) {
				listCompanyName.add(GlobalData.listCommonData.get(i).DataValue);
			}
		}
		new SJECHttpHandler(cbGetLsNo, this)
				.getLsNo(Constant.LSNO_PREFIX_DISTRIBUTION);
	}

	private void bindDetail() {
		layout_detail.removeAllViews();
		if (listDetail.size() > 0) {
			layout_detail.setVisibility(View.VISIBLE);
			LayoutInflater mInflater = LayoutInflater.from(this);
			View headerView = mInflater.inflate(
					R.layout.headview_distri_detail, null);
			layout_detail.addView(headerView);
			for (int i = 0; i < listDetail.size(); i++) {
				View convertView = mInflater.inflate(
						R.layout.item_distri_detail, null);
				LinearLayout layout_color = (LinearLayout) convertView
						.findViewById(R.id.layout_color);

				TextView tv_distri_target_address = (TextView) convertView
						.findViewById(R.id.tv_distri_target_address);
				TextView tv_distri_count = (TextView) convertView
						.findViewById(R.id.tv_distri_count);
				ImageView iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				layout_color
						.setBackgroundResource(i % 2 == 0 ? R.drawable.selector_common_layout_trans
								: R.drawable.selector_common_layout_list);
				tv_distri_target_address
						.setText(listDetail.get(i).target_address);
				tv_distri_count.setText(listDetail.get(i).count + "");
				iv_delete.setTag(i);
				iv_delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = ConvertUtil.ParsetStringToInt32(v
								.getTag().toString(), -1);
						if (position != -1) {
							listDetail.remove(position);
							// 因为有一个headview, 所以在removeView的时候,索引要+1
							bindDetail();
						}
					}
				});
				layout_detail.addView(convertView);
			}
		} else {
			layout_detail.setVisibility(View.GONE);
		}
	}

	private String checkContent() {
		if (breed.isEmpty()) {
			return getString(R.string.msg_empty_breed);
		}

		if (getText(et_old_qc_no).isEmpty()) {
			return getString(R.string.msg_empty_old_qc_no);
		}
		
		if (getText(et_0ld_distribution_count).isEmpty()) {
			return getString(R.string.msg_empty_old_distribution_count);
		}
		
		if(sp_breed.getSelectedItemPosition() == 5){
			if (getText(et_breedforelse).isEmpty()) {
				return getString(R.string.msg_empty_breedforelse);
			}
		}
		

//		if (getText(et_distribution_target_address).isEmpty()) {
//			return getString(R.string.msg_empty_distribution_target_address);
//		}
//
//		if (getText(et_distribution_count).isEmpty()) {
//			return getString(R.string.msg_empty_distribution_count);
//		}
		return "";
	}

	private void AsyncAddDistributionChangeApply() {
		JsonObject json = new JsonObject();
		json.addProperty("butchery_id", "");
		json.addProperty("LsNo", getText(tv_LsNo));
		json.addProperty("Distribution_Company", getText(et_distribution_company_name));
		json.addProperty("Breed", breed);
		
//		json.addProperty("Distribution_Target_Address",
//				getText(et_distribution_target_address));
//		json.addProperty("Distribution_Count", getText(et_distribution_count));
		json.addProperty("Old_QC_No", getText(et_old_qc_no));
		json.addProperty("Remark", "");
		json.addProperty("Old_Distribution_Count", getText(et_0ld_distribution_count));
		json.addProperty("BreedForElse", getText(et_breedforelse));
		
		JsonArray jArr = new JsonArray(); 
		for(int i = 0; i<listDetail.size();i++){
			JsonObject jsonDetail = new JsonObject(); 
			jsonDetail.addProperty("Distribution_Count", listDetail.get(i).count+"");
			jsonDetail.addProperty("Distribution_Target_Address", listDetail.get(i).target_address);
			jArr.add(jsonDetail);
		}
		json.add("Distribution_Target", jArr);
		jsonImg = ActivityPicSend.getJsonImg().toString();
		try {
			new SJECHttpHandler(cb, this).addDistributionChangeApply(json
					.toString(),jsonImg);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityAddDsitributionChangeApply.this,
					R.string.msg_control_failed, 3000).show();
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
					Util.createToast(ActivityAddDsitributionChangeApply.this,
							R.string.msg_control_success, 3000).show();
					getApplicationContext().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_APPLY));
					finish();
				} else {
					Util.createToast(
							ActivityAddDsitributionChangeApply.this,
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

	class EntityDistriDetail {
		public String target_address;
		public double count = 0;

		public EntityDistriDetail(String address, double distri_count) {
			this.target_address = address;
			this.count = distri_count;
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
					tv_LsNo.setText(lsNo);
				} else {
					Util.createToast(
							ActivityAddDsitributionChangeApply.this,
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
}
