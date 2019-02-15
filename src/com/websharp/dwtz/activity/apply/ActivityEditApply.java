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
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityDistributionApplyTarget;
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
public class ActivityEditApply extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	
	// Button btn_submit;
	// Button btn_clear;
//	TextView tv_DeliveryNum;
	// Spinner sp_breed;// 品种
	TextView tv_breed;
	TextView tv_old_qc_no;
	// TextView tv_distribution_target_address;
//	TextView tv_distribution_count;
	// EditText et_Remark;
	TextView tv_distribution_company_name;
	TextView tv_0ld_distribution_count;

	String breed = "";
	String[] arrBreed = null;
	ArrayAdapter<String> adapterBreed = null;

	// ImageView iv_spinner_distribution_company_name;
	// ArrayList<String> listCompanyName = new ArrayList<String>();

	// TextView tv_add_detail;
	ArrayList<EntityDistributionApplyTarget> listDetail = new ArrayList<EntityDistributionApplyTarget>();
	TextView tv_LsNo;
	LinearLayout layout_detail;
	ImageView iv_camera;
	TextView tv_Status;
	int position = 0;

	// BroadcastReceiver receiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// if (intent.getAction().equals(Constant.ACTION_ADD_DISTRI_DETAIL)) {
	// // add detail
	// listDetail.add(new EntityDistriDetail(intent.getExtras()
	// .getString("target_address"), intent.getExtras()
	// .getDouble("count")));
	// bindDetail();
	// LogUtil.d(listDetail.toString());
	// }
	// }
	// };

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			Bundle b = new Bundle();
			b.putString("url", String.format(SJECHttpHandler.URL_TAKE_PIC,
					"apply", GlobalData.listApply.get(position).InnerID,GlobalData.curUser.InnerID)); 
			b.putString("title", "分销照片");
			Util.startActivity(ActivityEditApply.this, ActivityWebview.class, b,
					false);
			break;
		case R.id.layout_back:
			finish();
			break;
		// case R.id.iv_spinner_distribution_company_name:
		// showDialogListView(listCompanyName, et_distribution_company_name);
		// break;
		// case R.id.tv_add_detail:
		// Util.startActivity(ActivityEditApply.this,
		// ActivityAddDsitributionDetail.class, false);
		// break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// unregisterReceiver(receiver);
		listDetail.clear();
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_edit_apply);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// IntentFilter filter = new IntentFilter(
		// Constant.ACTION_ADD_DISTRI_DETAIL);
		// registerReceiver(receiver, filter);
		layout_back = (LinearLayout)findViewById(R.id.layout_back);
		tv_title = (TextView)findViewById(R.id.tv_title);
		// btn_submit = (Button) findViewById(R.id.btn_submit);
		// btn_clear = (Button) findViewById(R.id.btn_clear);
//		tv_DeliveryNum = (TextView) findViewById(R.id.tv_DeliveryNum);
		// sp_breed = (Spinner) findViewById(R.id.sp_breed);
		tv_breed = (TextView)findViewById(R.id.tv_breed);
		tv_old_qc_no = (TextView) findViewById(R.id.tv_old_qc_no);
		// tv_distribution_target_address = (TextView)
		// findViewById(R.id.tv_distribution_company_name);
		// tv_distribution_count = (TextView)
		// findViewById(R.id.tv_distribution_count);
		// et_Remark = (EditText) findViewById(R.id.et_Remark);
		tv_distribution_company_name = (TextView) findViewById(R.id.tv_distribution_company_name);
		// iv_spinner_distribution_company_name = (ImageView)
		// findViewById(R.id.iv_spinner_distribution_company_name);
		// tv_add_detail = (TextView) findViewById(R.id.tv_add_detail);
		tv_LsNo = (TextView) findViewById(R.id.tv_LsNo);
		layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
		tv_Status = (TextView) findViewById(R.id.tv_Status);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		tv_0ld_distribution_count  = (TextView)findViewById(R.id.tv_0ld_distribution_count);
		iv_camera.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		// tv_add_detail.setOnClickListener(this);
		// iv_spinner_distribution_company_name.setOnClickListener(this);
		// btn_submit.setOnClickListener(this);
		// btn_clear.setOnClickListener(this);
		// et_old_qc_no.requestFocus();
	}

	@Override
	public void bindData() {
		position = getIntent().getExtras().getInt("position", -1);
		
		tv_title.setText("分销申请");
		tv_Status
				.setText(GlobalData.listApply.get(position).Status
						.equals("Init") ? "待审核" : (GlobalData.listApply
						.get(position).Status.equals("Pass") ? "通过" : "否决"));
		tv_LsNo.setText(GlobalData.listApply.get(position).Distribution_LsNo);
		tv_distribution_company_name.setText(GlobalData.listApply.get(position).Distribution_Company);
		tv_breed.setText(GlobalData.listApply.get(position).Breed+GlobalData.listApply.get(position).BreedForElse);
		tv_old_qc_no.setText(GlobalData.listApply.get(position).Old_QC_No);
		tv_0ld_distribution_count.setText(GlobalData.listApply.get(position).Old_Distribution_Count);
		bindDetail();
		// arrBreed = getResources().getStringArray(
		// R.array.distribution_apply_breed);
		// adapterBreed = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, arrBreed);
		//
		// adapterBreed
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// sp_breed.setAdapter(adapterBreed);
		// sp_breed.setOnItemSelectedListener(new
		// AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// arg0.setVisibility(View.VISIBLE);
		// breed = arrBreed[position];
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });
		// sp_breed.setSelection(0);

		// for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
		// if (GlobalData.listCommonData.get(i).Type
		// .equals(Constant.COMMON_DATA_TYPE_DISTRIBUTION_COMPANY)) {
		// listCompanyName.add(GlobalData.listCommonData.get(i).DataValue);
		// }
		// }
		// new SJECHttpHandler(cbGetLsNo, this)
		// .getLsNo(Constant.LSNO_PREFIX_DISTRIBUTION);
	}

	private void bindDetail() {
		
		listDetail = GlobalData.listApply.get(position).target_list; 
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
						.setText(listDetail.get(i).Distribution_Target_Address);
				tv_distri_count.setText(listDetail.get(i).Distribution_Count
						+ "");
				iv_delete.setVisibility(View.INVISIBLE);
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

	// private String checkContent() {
	// if (breed.isEmpty()) {
	// return getString(R.string.msg_empty_breed);
	// }
	//
	// // if (getText(et_old_qc_no).isEmpty()) {
	// // return getString(R.string.msg_empty_old_qc_no);
	// // }
	//
	// // if (getText(et_distribution_target_address).isEmpty()) {
	// // return getString(R.string.msg_empty_distribution_target_address);
	// // }
	// //
	// // if (getText(et_distribution_count).isEmpty()) {
	// // return getString(R.string.msg_empty_distribution_count);
	// // }
	// return "";
	// }

	// AsyncHttpCallBack cb = new AsyncHttpCallBack() {
	//
	// @Override
	// public void onSuccess(String response) {
	//
	// super.onSuccess(response);
	// LogUtil.d("%s", response);
	// JSONObject obj;
	// try {
	// obj = new JSONObject(response);
	//
	// if (obj.optString("result").equals("true")) {
	// Util.createToast(ActivityEditApply.this,
	// R.string.msg_control_success, 3000).show();
	// finish();
	// } else {
	// Util.createToast(
	// ActivityEditApply.this,
	// obj.optString("desc",
	// getString(R.string.common_login_failed)),
	// 3000).show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(String message) {
	// super.onFailure(message);
	// LogUtil.d("%s", message);
	// }
	//
	// };

	// AsyncHttpCallBack cbGetLsNo = new AsyncHttpCallBack() {
	//
	// @Override
	// public void onSuccess(String response) {
	//
	// super.onSuccess(response);
	// LogUtil.d("%s", response);
	// JSONObject obj;
	// try {
	// obj = new JSONObject(response);
	//
	// if (obj.optString("result").equals("true")) {
	// JSONObject objData = obj.optJSONObject("data");
	// String lsNo = objData.optString("lsno", "failed");
	// tv_LsNo.setText(lsNo);
	// } else {
	// Util.createToast(
	// ActivityEditApply.this,
	// obj.optString("desc",
	// getString(R.string.msg_failed_getlsno)),
	// 3000).show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(String message) {
	// super.onFailure(message);
	// LogUtil.d("%s", message);
	// }
	//
	// };
}
