package com.websharp.dwtz.activity.dwtzjysb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import de.greenrobot.dao.query.WhereCondition;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.timepicker.AbstractWheelTextAdapter;
import com.timepicker.ArrayWheelAdapter;
import com.timepicker.NumericWheelAdapter;
import com.timepicker.OnWheelClickedListener;
import com.timepicker.OnWheelChangedListener;
import com.timepicker.OnWheelScrollListener;
import com.timepicker.WheelView;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityEditInvalid;
import com.websharp.dwtz.activity.photo.ActivityPhotoOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.activity.user.ActivityLogin;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApply;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityUser;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharp.util.UtilSign;
import com.websharp.widget.WriteDialogListener;
import com.websharp.widget.WritePadDialog;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.json.JSONUtils;

public class ActivityAddDwtzjysb extends BaseActivity {

	private ImageView iv_camera;

	// 进场生猪情况
	EditText et_apply_user_name;
	EditText et_inspection_staff;
	EditText et_inspection_staff_telephone;
	EditText et_animal_type;
	EditText et_quar_count;
	EditText et_rest_count;
	EditText et_animal_count;
	EditText et_Origin;
	EditText et_entranceImmune_num;
	EditText et_target_address;

	TextView tv_apply_time;
	TextView tv_start_trans_time;

	CheckBox cbx_read_warn;

	RadioButton rb_special_channelIn_yes;
	RadioButton rb_special_channelIn_no;
	RadioButton rb_animal_health_yes;
	RadioButton rb_animal_health_no;
	RadioButton rb_animal_ear_tag_yes;
	RadioButton rb_animal_ear_tag_no;

	Spinner sp_butchery;

	Button tv_view_signature, tv_do_signature;

	Button btn_submit, btn_load_data;

	LinearLayout layout_apply_time, layout_start_trans_time;

	WheelView wv_hour_apply_time;
	WheelView wv_min_apply_time;
	WheelView wv_day_apply_time;
	NumericWheelAdapter hourAdapter_start;
	ArrayWheelAdapter<String> minAdapter_start;
	DayArrayAdapter dayAdapter_start;

	WheelView wv_hour_start_trans_time;
	WheelView wv_min_start_trans_time;
	WheelView wv_day_start_trans_time;
	NumericWheelAdapter hourAdapter_end;
	ArrayWheelAdapter<String> minAdapter_end;
	DayArrayAdapter dayAdapter_end;

	ArrayAdapter adapterButchery;
	private String curApplyTime = "";
	private String curStartTransTime = "";
	private String curImgPath = "";

	ImageView iv_spinner_apply_user_name;
	ArrayList<String> listGoodsowner = new ArrayList<String>();
	ArrayList<String> listItem = new ArrayList<String>();
	ArrayList<String> listButcheryID= new ArrayList<String>();

	String[] arrMinutes = null;

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

							AsyncAddOrder();
						}
					}).show();

			break;
		case R.id.iv_spinner_apply_user_name:
			showDialogListView(listGoodsowner, et_apply_user_name);
			break;
		case R.id.tv_apply_time:
			layout_start_trans_time.setVisibility(View.GONE);
			if (layout_apply_time.getVisibility() == View.VISIBLE) {
				layout_apply_time.setVisibility(View.GONE);
			} else {
				layout_apply_time.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.tv_start_trans_time:
			layout_apply_time.setVisibility(View.GONE);
			if (layout_start_trans_time.getVisibility() == View.VISIBLE) {
				layout_start_trans_time.setVisibility(View.GONE);
			} else {
				layout_start_trans_time.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_load_data:
			new SJECHttpHandler(cbLoadApplyUserData, ActivityAddDwtzjysb.this)
					.GetAnimalSlaughterImmuneApplyData(GlobalData.curButcheryID, getText(et_apply_user_name));
			break;
		case R.id.tv_do_signature:
			doSignature();
			break;
		case R.id.tv_view_signature:
			if (tv_view_signature.getTag() != null) {
				Intent intent = new Intent(this, ActivityViewImage.class);
				intent.setData(Uri.parse(v.getTag().toString()));
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_dwtzjysb);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		iv_camera.setVisibility(View.GONE);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_load_data = (Button) findViewById(R.id.btn_load_data);

		et_apply_user_name = (EditText) findViewById(R.id.et_apply_user_name);
		et_inspection_staff = (EditText) findViewById(R.id.et_inspection_staff);
		et_inspection_staff_telephone = (EditText) findViewById(R.id.et_inspection_staff_telephone);
		et_animal_type = (EditText) findViewById(R.id.et_animal_type);
		et_quar_count = (EditText) findViewById(R.id.et_quar_count);
		et_rest_count = (EditText) findViewById(R.id.et_rest_count);
		et_animal_count = (EditText) findViewById(R.id.et_animal_count);
		et_Origin = (EditText) findViewById(R.id.et_Origin);
		et_entranceImmune_num = (EditText) findViewById(R.id.et_entranceImmune_num);
		et_target_address = (EditText) findViewById(R.id.et_target_address);

		tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
		tv_start_trans_time = (TextView) findViewById(R.id.tv_start_trans_time);

		cbx_read_warn = (CheckBox) findViewById(R.id.cbx_read_warn);

		rb_special_channelIn_yes = (RadioButton) findViewById(R.id.rb_special_channelIn_yes);
		rb_special_channelIn_no = (RadioButton) findViewById(R.id.rb_special_channelIn_no);
		rb_animal_health_yes = (RadioButton) findViewById(R.id.rb_animal_health_yes);
		rb_animal_health_no = (RadioButton) findViewById(R.id.rb_animal_health_no);
		rb_animal_ear_tag_yes = (RadioButton) findViewById(R.id.rb_animal_ear_tag_yes);
		rb_animal_ear_tag_no = (RadioButton) findViewById(R.id.rb_animal_ear_tag_no);

		tv_view_signature = (Button) findViewById(R.id.tv_view_signature);
		tv_do_signature = (Button) findViewById(R.id.tv_do_signature);

		sp_butchery = (Spinner) findViewById(R.id.sp_butchery);

		iv_spinner_apply_user_name = (ImageView) findViewById(R.id.iv_spinner_apply_user_name);

		layout_apply_time = (LinearLayout) findViewById(R.id.layout_apply_time);
		layout_start_trans_time = (LinearLayout) findViewById(R.id.layout_start_trans_time);

		btn_submit.setOnClickListener(this);
		btn_load_data.setOnClickListener(this);
		tv_apply_time.setOnClickListener(this);
		tv_start_trans_time.setOnClickListener(this);
		iv_spinner_apply_user_name.setOnClickListener(this);
		tv_do_signature.setOnClickListener(this);
		tv_view_signature.setOnClickListener(this);
	}

	private String checkContent() {
		String result = "";

		if (listButcheryID.get(sp_butchery.getSelectedItemPosition()).isEmpty()) {
			return "所属屠宰场不能为空";
		}

		if (getText(et_apply_user_name).isEmpty()) {
			return "申报人名称不能为空";
		}

		if (getText(tv_apply_time).isEmpty()) {
			return "报检时间不能为空";
		}

		if (getText(et_inspection_staff).isEmpty()) {
			return "报检员姓名不能为空";
		}

		if (getText(et_inspection_staff_telephone).isEmpty()) {
			return "联系电话不能为空";
		}

		if (getText(et_animal_type).isEmpty()) {
			return "动物种类不能为空";
		}

		if (getText(et_quar_count).isEmpty()) {
			return "进场数量不能为空";
		}

		if (getText(et_rest_count).isEmpty()) {
			return "库存数量不能为空";
		}

		if (getText(et_animal_count).isEmpty()) {
			return "总数量不能为空";
		}

		if (getText(et_Origin).isEmpty()) {
			return "动物产地不能为空";
		}

		if (getText(et_entranceImmune_num).isEmpty()) {
			return "入场检疫证明编号不能为空";
		}

		if (getText(et_target_address).isEmpty()) {
			return "到达地点不能为空";
		}

		if (getText(tv_start_trans_time).isEmpty()) {
			return "启运时间不能为空";
		}

		if (!cbx_read_warn.isChecked()) {
			return "注意事项请阅读并确认";
		}

		if (curImgPath.isEmpty()) {
			return "请报检员签名";
		}
		return result;
	}

	private void clearContent() {

	}

	@Override
	public void bindData() {
		String userButcheryID = "";
		if (GlobalData.listButcheryByUser.size() > 0) {
			userButcheryID = GlobalData.listButcheryByUser.get(0).InnerID;
		}
		
		for (int i = 0; i < GlobalData.listButchery.size(); i++) {
			if (userButcheryID.equals(GlobalData.listButchery.get(i).InnerID)) {
				listItem.add(GlobalData.listButchery.get(i).ButcheryName);
				listButcheryID.add(GlobalData.listButchery.get(i).InnerID);
			}
		}

		adapterButchery = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItem);
		adapterButchery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_butchery.setAdapter(adapterButchery);
		sp_butchery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				GlobalData.curButcheryID = listButcheryID.get(position);
				listGoodsowner.clear();
				for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
					if (!GlobalData.listCommonData.get(i).ButcheryID.equals(GlobalData.curButcheryID))
						continue;
					if (GlobalData.listCommonData.get(i).Type.equals(Constant.COMMON_DATA_TYPE_GOODSOWNER)) {
						listGoodsowner.add(GlobalData.listCommonData.get(i).DataValue);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		for (int i = 0; i < listButcheryID.size(); i++) {
			if (userButcheryID.equals(listButcheryID.get(i))) {
				sp_butchery.setSelection(i);
				GlobalData.curButcheryID = userButcheryID;
				break;
			}
		}
		
		if (arrMinutes == null) {
			arrMinutes = new String[60];
			for (int i = 0; i < 60; i++) {
				arrMinutes[i] = (i > 9 ? i : ("0" + i)) + "分";
			}
		}
		initDatePickerApplyTime();
		initDatePickerStartTransTime();
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
					Util.createToast(ActivityAddDwtzjysb.this, R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_QUARANTINE));
					finish();
				} else {
					Util.createToast(ActivityAddDwtzjysb.this,
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

	AsyncHttpCallBack cbLoadApplyUserData = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					JSONObject json = obj.getJSONObject("data");
					et_quar_count.setText(json.optString("TotalCount", "0"));
					et_animal_count.setText(json.optString("AnimalCount", "0"));
					et_Origin.setText(json.optString("origin", ""));
					et_entranceImmune_num.setText(json.optString("entrance_immune_num", ""));
					et_rest_count.setText(json.optString("rest_count", "0"));
					et_inspection_staff.setText(json.optString("inspection_staff", ""));
					et_inspection_staff_telephone.setText(json.optString("inspection_staff_telephone", ""));
				} else {
					Util.createToast(ActivityAddDwtzjysb.this, obj.optString("desc", "加载失败"), 3000).show();
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

		EntityAnimalSlaughterImmuneApply apply = new EntityAnimalSlaughterImmuneApply();
		apply.innerid = "";
		apply.apply_user_name = getText(et_apply_user_name);
		apply.apply_time = getText(tv_apply_time) + ":00";
		apply.inspection_staff = getText(et_inspection_staff);
		apply.inspection_staff_telephone = getText(et_inspection_staff_telephone);
		apply.animal_type = getText(et_animal_type);
		apply.animal_count = getText(et_animal_count);
		apply.animal_origin = getText(et_Origin);
		apply.entrance_immune_num = getText(et_entranceImmune_num);
		apply.is_special_channel_in = rb_special_channelIn_yes.isChecked() ? "1" : "0";
		apply.clinic_health = rb_animal_health_yes.isChecked() ? "1" : "0";
		apply.is_animal_ear_tag = rb_animal_ear_tag_yes.isChecked() ? "1" : "0";
		apply.target_address = getText(et_target_address);
		apply.start_trans_time = getText(tv_start_trans_time) + ":00";
		apply.is_read_warn = cbx_read_warn.isChecked() ? "1" : "0";
		apply.butchery_id = GlobalData.curButcheryID;

		String str = JSONUtils.toJson2(apply, null, null);
		try {
			new SJECHttpHandler(cb, this).addAnimalSlaughterImmuneApply(str, curImgPath);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityAddDwtzjysb.this, R.string.msg_control_failed, 3000).show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initDatePickerApplyTime() {
		wv_hour_apply_time = (WheelView) findViewById(R.id.wv_hour_apply_time);
		hourAdapter_start = new NumericWheelAdapter(this, 0, 23, "%02d点");
		hourAdapter_start.setItemResource(R.layout.wheel_text_hour);
		hourAdapter_start.setItemTextResource(R.id.text);
		wv_hour_apply_time.setViewAdapter(hourAdapter_start);
		wv_hour_apply_time.setCyclic(true);

		// 分钟
		wv_min_apply_time = (WheelView) findViewById(R.id.wv_min_apply_time);
		// （min ,max ,format）
		// NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59,
		// "%02d");
		minAdapter_start = new ArrayWheelAdapter<String>(this, arrMinutes);

		minAdapter_start.setItemResource(R.layout.wheel_text_min);
		minAdapter_start.setItemTextResource(R.id.text);
		wv_min_apply_time.setViewAdapter(minAdapter_start);
		wv_min_apply_time.setCyclic(true);
		// 当前时间
		// set current time
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		wv_hour_apply_time.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		wv_min_apply_time.setCurrentItem(calendar.get(Calendar.MINUTE));

		wv_day_apply_time = (WheelView) findViewById(R.id.wv_day_apply_time);
		dayAdapter_start = new DayArrayAdapter(this, calendar);
		wv_day_apply_time.setViewAdapter(dayAdapter_start);
		wv_day_apply_time.setCurrentItem(10);
		wv_hour_apply_time.setDrawShadows(false);
		wv_min_apply_time.setDrawShadows(false);
		wv_day_apply_time.setDrawShadows(false);

		wv_day_apply_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				// TODO Auto-generated method stub

			}
		});

		wv_hour_apply_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});

		wv_min_apply_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});

		wv_day_apply_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetApplyTime();
			}
		});

		wv_hour_apply_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetApplyTime();
			}
		});

		wv_min_apply_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetApplyTime();
			}
		});
		GetApplyTime();
	}

	private void GetApplyTime() {
		Calendar newCalendar = (Calendar) Calendar.getInstance(Locale.CHINA).clone();
		newCalendar.roll(Calendar.DAY_OF_YEAR, -10 + wv_day_apply_time.getCurrentItem());
		String str = new SimpleDateFormat("yyyy-MM-dd").format(newCalendar.getTime());
		curApplyTime = new SimpleDateFormat("yyyy-MM-dd").format(newCalendar.getTime());
		str += " " + hourAdapter_start.getItemText(wv_hour_apply_time.getCurrentItem()).toString().replace("点", ":");
		str += minAdapter_start.getItemText(wv_min_apply_time.getCurrentItem()).toString().replace("分", "");
		tv_apply_time.setText(str);
		curApplyTime += " "
				+ hourAdapter_start.getItemText(wv_hour_apply_time.getCurrentItem()).toString().replace("点", ":")
				+ minAdapter_start.getItemText(wv_min_apply_time.getCurrentItem()).toString().replace("分", ":00");
		System.err.println(curApplyTime);
	}

	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		// Count of days to be shown
		private final int daysCount = 100;

		// Calendar
		Calendar calendar;

		/**
		 * Constructor
		 */
		protected DayArrayAdapter(Context context, Calendar calendar) {
			super(context, R.layout.time2_day, NO_RESOURCE);
			this.calendar = calendar;
			setItemTextResource(R.id.time2_monthday);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			// int day = -daysCount / 2 + index;
			int day = -10 + index;
			Calendar newCalendar = (Calendar) calendar.clone();
			newCalendar.roll(Calendar.DAY_OF_YEAR, day);

			View view = super.getItem(index, cachedView, parent);
			TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
			// if (day == 0) {
			// weekday.setText("");
			// } else {
			java.text.DateFormat format = new SimpleDateFormat("EEE");
			weekday.setText(format.format(newCalendar.getTime()));
			// }
			weekday.setText("");

			TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
			// if (day == 0) {
			// monthday.setText("Today");
			// monthday.setTextColor(0xFF0000F0);
			// } else {
			format = new SimpleDateFormat("MM月dd日 EEEE");
			monthday.setText(format.format(newCalendar.getTime()));
			monthday.setTextColor(0xFF111111);
			// }

			return view;
		}

		@Override
		public int getItemsCount() {
			return daysCount + 1;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}
	}

	private void initDatePickerStartTransTime() {
		wv_hour_start_trans_time = (WheelView) findViewById(R.id.wv_hour_start_trans_time);
		hourAdapter_end = new NumericWheelAdapter(this, 0, 23, "%02d点");
		hourAdapter_end.setItemResource(R.layout.wheel_text_hour);
		hourAdapter_end.setItemTextResource(R.id.text);
		wv_hour_start_trans_time.setViewAdapter(hourAdapter_end);
		wv_hour_start_trans_time.setCyclic(true);

		// 分钟
		wv_min_start_trans_time = (WheelView) findViewById(R.id.wv_min_start_trans_time);
		// （min ,max ,format）
		// NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59,
		// "%02d");
		minAdapter_end = new ArrayWheelAdapter<String>(this, arrMinutes);

		minAdapter_end.setItemResource(R.layout.wheel_text_min);
		minAdapter_end.setItemTextResource(R.id.text);
		wv_min_start_trans_time.setViewAdapter(minAdapter_end);
		wv_min_start_trans_time.setCyclic(true);
		// 当前时间
		// set current time
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		wv_hour_start_trans_time.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		wv_min_start_trans_time.setCurrentItem(calendar.get(Calendar.MINUTE));

		wv_day_start_trans_time = (WheelView) findViewById(R.id.wv_day_start_trans_time);
		dayAdapter_end = new DayArrayAdapter(this, calendar);
		wv_day_start_trans_time.setViewAdapter(dayAdapter_end);
		wv_day_start_trans_time.setCurrentItem(10);
		wv_hour_start_trans_time.setDrawShadows(false);
		wv_min_start_trans_time.setDrawShadows(false);
		wv_day_start_trans_time.setDrawShadows(false);

		wv_day_start_trans_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});

		wv_hour_start_trans_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});

		wv_min_start_trans_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});

		wv_day_start_trans_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetStartTransTime();
			}
		});

		wv_hour_start_trans_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetStartTransTime();
			}
		});

		wv_min_start_trans_time.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				GetStartTransTime();
			}
		});
		GetStartTransTime();
	}

	private void GetStartTransTime() {
		Calendar newCalendar = (Calendar) Calendar.getInstance(Locale.CHINA).clone();
		newCalendar.roll(Calendar.DAY_OF_YEAR, -10 + wv_day_start_trans_time.getCurrentItem());
		String str = new SimpleDateFormat("yyyy-MM-dd").format(newCalendar.getTime());
		curStartTransTime = new SimpleDateFormat("yyyy-MM-dd").format(newCalendar.getTime());
		str += " "
				+ hourAdapter_end.getItemText(wv_hour_start_trans_time.getCurrentItem()).toString().replace("点", ":");
		str += minAdapter_end.getItemText(wv_min_start_trans_time.getCurrentItem()).toString().replace("分", "");
		tv_start_trans_time.setText(str);
		curStartTransTime += " "
				+ hourAdapter_end.getItemText(wv_hour_start_trans_time.getCurrentItem()).toString().replace("点", ":")
				+ minAdapter_end.getItemText(wv_min_start_trans_time.getCurrentItem()).toString().replace("分", ":00");
		System.err.println(curStartTransTime);
	}

	private void doSignature() {
		WritePadDialog mWritePadDialog = new WritePadDialog(ActivityAddDwtzjysb.this, new WriteDialogListener() {
			@Override
			public void onPaintDone(Object object) {
				String filePath = UtilSign.createSignFile((Bitmap) object);
				tv_view_signature.setTag(filePath);
				curImgPath = filePath;
				tv_view_signature.setVisibility(View.VISIBLE);
			}
		});
		mWritePadDialog.show();
		super.SetDialogFullScreen(mWritePadDialog);
	}
}
