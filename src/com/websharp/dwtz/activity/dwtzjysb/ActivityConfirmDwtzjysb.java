package com.websharp.dwtz.activity.dwtzjysb;

import java.io.File;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import de.greenrobot.dao.query.WhereCondition;

import com.google.gson.JsonObject;
import com.timepicker.AbstractWheelTextAdapter;
import com.timepicker.ArrayWheelAdapter;
import com.timepicker.NumericWheelAdapter;
import com.timepicker.OnWheelClickedListener;
import com.timepicker.OnWheelChangedListener;
import com.timepicker.OnWheelScrollListener;
import com.timepicker.WheelView;
import com.websharp.async.AsyncDownloadFile;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityEditInvalid;
import com.websharp.dwtz.activity.photo.ActivityPhotoOrder;
import com.websharp.dwtz.activity.pic.ActivityPicSend;
import com.websharp.dwtz.activity.user.ActivityLogin;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApply;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityUser;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharp.util.UtilSign;
import com.websharp.widget.WriteDialogListener;
import com.websharp.widget.WritePadDialog;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.file.FileUtil;
import com.websharputil.json.JSONUtils;

public class ActivityConfirmDwtzjysb extends BaseActivity {

	private ImageView iv_camera;
	LinearLayout layout_sbrmc;
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
	TextView tv_do_check_time;
	EditText et_do_check_address;
	EditText et_reject_reason;
	EditText et_confirm_user_name;

	CheckBox cbx_read_warn;

	RadioButton rb_special_channelIn_yes;
	RadioButton rb_special_channelIn_no;
	RadioButton rb_animal_health_yes;
	RadioButton rb_animal_health_no;
	RadioButton rb_animal_ear_tag_yes;
	RadioButton rb_animal_ear_tag_no;
	RadioButton rg_apply_confirm_yes;
	RadioButton rg_apply_confirm_no;
	RadioGroup rg_apply_confirm;

	LinearLayout layout_do_check_time_picker;
	LinearLayout layout_do_check_time;
	LinearLayout layout_do_check_address;
	LinearLayout layout_reject_reason;

	Spinner sp_butchery;

	Button tv_view_signature;
	boolean isReWriteSignature = false;

	Button btn_submit, btn_load_data;

	LinearLayout layout_jcsl, layout_kcsl;

	WheelView wv_day_do_check_time;
	DayArrayAdapter dayAdapter_start;

	ArrayAdapter adapterButchery;
	private String curImgPath = "";

	ImageView iv_spinner_apply_user_name;
	ArrayList<String> listGoodsowner = new ArrayList<String>();
	ArrayList<String> listItem = new ArrayList<String>();
	ArrayList<String> listButcheryID = new ArrayList<String>();

	String[] arrMinutes = null;
	EntityAnimalSlaughterImmuneApply curApply = null;

	LinearLayout layout_downloading;
	TextView tv_downloading;
	Bitmap mSignBitmap;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(Constant.ACTION_OPEN_ATTACH)) {
				Bundle b = intent.getExtras();
				String url = b.getString("url", "");
				handlerFileUrl(url, ActivityConfirmDwtzjysb.this, layout_downloading, tv_downloading);
			}
		}
	};

	public static void handlerFileUrl(String url, Context context, LinearLayout layout_downloading,
			TextView tv_downloading) {

		String fileName = FileUtil.getFileNameFromUrl(url);
		String fileNameAbs = Constant.SDCARD_ATTACH_DIR + fileName + ".tmp";
		if (new File(fileNameAbs).exists()) {
			openFile(fileNameAbs, context);
		} else {
			LogUtil.d(url);
			download(url, fileName, context, layout_downloading, tv_downloading);
		}
	}

	public static void openFile(String fileNameAbs, Context context) {

		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(new File(fileNameAbs)),
				FileUtil.getMIMEType(fileNameAbs.replace(".tmp", "")));
		// intent.putExtra(Intent.EXTRA_MIME_TYPES,
		// v.getTag().toString());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, "打开方式"));
	}

	public static void download(String url, String fileName, final Context context,
			final LinearLayout layout_downloading, final TextView tv_downloading) {
		final AsyncDownloadFile down = new AsyncDownloadFile();
		down.fileName = fileName + ".tmp";
		down.fileUrl = url;
		down.savePath = Constant.SDCARD_ATTACH_DIR;

		down.setDownloadListener(new AsyncDownloadFile.DownloadListener() {

			@Override
			public void DownloadProgress(int progress) {
				LogUtil.d(progress + "");
				try {
					tv_downloading
							.setText(context.getResources().getString(R.string.common_downloading, progress + ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void DownloadComplete(boolean result) {
				LogUtil.d(result + "");
				try {
					layout_downloading.setVisibility(View.GONE);
					openFile(down.savePath + down.fileName, context);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void DownloadCancel() {

			}

			@Override
			public void DownlaodPre() {
				try {
					tv_downloading.setText(context.getResources().getString(R.string.common_downloading, "0"));
					layout_downloading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		down.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

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

		case R.id.tv_do_signature:
			doSignature();
			break;
		case R.id.tv_view_signature:
			if (tv_view_signature.getTag() != null) {
				if (isReWriteSignature) {
					Intent intent = new Intent(this, ActivityViewImage.class);
					intent.setData(Uri.parse(v.getTag().toString()));
					startActivity(intent);
				} else {
					Intent intentOpenAttach = new Intent();
					Bundle bAttach = new Bundle();
					bAttach.putString("url", v.getTag().toString());
					intentOpenAttach.putExtras(bAttach);
					intentOpenAttach.setAction(Constant.ACTION_OPEN_ATTACH);
					getApplicationContext().sendBroadcast(intentOpenAttach);
				}
			}
			break;
		case R.id.tv_do_check_time:
			if (layout_do_check_time_picker.getVisibility() == View.VISIBLE) {
				layout_do_check_time_picker.setVisibility(View.GONE);
			} else {
				layout_do_check_time_picker.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_confirm_dwtzjysb);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		iv_camera.setVisibility(View.GONE);

		btn_submit = (Button) findViewById(R.id.btn_submit);

		layout_sbrmc = (LinearLayout) findViewById(R.id.layout_sbrmc);
		btn_load_data = (Button) findViewById(R.id.btn_load_data);
		btn_load_data.setVisibility(View.GONE);

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
		et_reject_reason = (EditText)findViewById(R.id.et_reject_reason);
		et_confirm_user_name = (EditText)findViewById(R.id.et_confirm_user_name);

		tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
		tv_start_trans_time = (TextView) findViewById(R.id.tv_start_trans_time);
		tv_do_check_time = (TextView) findViewById(R.id.tv_do_check_time);
		et_do_check_address = (EditText)findViewById(R.id.et_do_check_address);

		layout_do_check_time_picker = (LinearLayout) findViewById(R.id.layout_do_check_time_picker);
		layout_do_check_time = (LinearLayout) findViewById(R.id.layout_do_check_time);
		layout_do_check_address = (LinearLayout) findViewById(R.id.layout_do_check_address);
		layout_reject_reason = (LinearLayout) findViewById(R.id.layout_reject_reason);

		cbx_read_warn = (CheckBox) findViewById(R.id.cbx_read_warn);
		layout_jcsl = (LinearLayout) findViewById(R.id.layout_jcsl);
		layout_kcsl = (LinearLayout) findViewById(R.id.layout_kcsl);
		layout_jcsl.setVisibility(View.GONE);
		layout_kcsl.setVisibility(View.GONE);

		rb_special_channelIn_yes = (RadioButton) findViewById(R.id.rb_special_channelIn_yes);
		rb_special_channelIn_no = (RadioButton) findViewById(R.id.rb_special_channelIn_no);
		rb_animal_health_yes = (RadioButton) findViewById(R.id.rb_animal_health_yes);
		rb_animal_health_no = (RadioButton) findViewById(R.id.rb_animal_health_no);
		rb_animal_ear_tag_yes = (RadioButton) findViewById(R.id.rb_animal_ear_tag_yes);
		rb_animal_ear_tag_no = (RadioButton) findViewById(R.id.rb_animal_ear_tag_no);

		rg_apply_confirm = (RadioGroup) findViewById(R.id.rg_apply_confirm);
		rg_apply_confirm_yes = (RadioButton) findViewById(R.id.rg_apply_confirm_yes);
		rg_apply_confirm_no = (RadioButton) findViewById(R.id.rg_apply_confirm_no);

		tv_view_signature = (Button) findViewById(R.id.tv_view_signature);

		sp_butchery = (Spinner) findViewById(R.id.sp_butchery);

		iv_spinner_apply_user_name = (ImageView) findViewById(R.id.iv_spinner_apply_user_name);
		iv_spinner_apply_user_name.setVisibility(View.GONE);

		layout_downloading = (LinearLayout) findViewById(R.id.layout_downloading);
		tv_downloading = (TextView) findViewById(R.id.tv_downloading);

		btn_submit.setOnClickListener(this);
		tv_view_signature.setOnClickListener(this);
		tv_do_check_time.setOnClickListener(this);

		sp_butchery.setEnabled(false);
		et_apply_user_name.setEnabled(false);
		et_inspection_staff.setEnabled(false);
		et_inspection_staff_telephone.setEnabled(false);
		et_animal_type.setEnabled(false);
		et_quar_count.setEnabled(false);
		et_rest_count.setEnabled(false);
		et_animal_count.setEnabled(false);
		et_Origin.setEnabled(false);
		et_entranceImmune_num.setEnabled(false);
		et_target_address.setEnabled(false);
		rb_special_channelIn_yes.setEnabled(false);
		rb_special_channelIn_no.setEnabled(false);
		rb_animal_health_yes.setEnabled(false);
		rb_animal_health_no.setEnabled(false);
		rb_animal_ear_tag_yes.setEnabled(false);
		rb_animal_ear_tag_no.setEnabled(false);
		cbx_read_warn.setEnabled(false);

		rg_apply_confirm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				if (radioButtonId == R.id.rg_apply_confirm_yes) {
					layout_do_check_time.setVisibility(View.VISIBLE);
					layout_do_check_address.setVisibility(View.VISIBLE);
					layout_reject_reason.setVisibility(View.GONE);
				} else if (radioButtonId == R.id.rg_apply_confirm_no) {
					layout_do_check_time.setVisibility(View.GONE);
					layout_do_check_address.setVisibility(View.GONE);
					layout_reject_reason.setVisibility(View.VISIBLE);
				}
			}
		});

		LayoutParams lp = (LayoutParams) layout_sbrmc.getLayoutParams();
		lp.height = ConvertUtil.dip2px(this, 40);
		layout_sbrmc.setLayoutParams(lp);
	}

	private String checkContent() {
		String result = "";

		if (rg_apply_confirm_yes.isChecked()) {
			if (getText(tv_do_check_time).isEmpty()) {
				return "实施检疫时间不能为空";
			}
			
			if (getText(et_do_check_address).isEmpty()) {
				return "实施检疫地点不能为空";
			}
		}else{
			if (getText(et_reject_reason).isEmpty()) {
				return "不受理理由不能为空";
			}
		}
		
		if (getText(et_confirm_user_name).isEmpty()) {
			return "经办人不能为空";
		}

		return result;
	}

	@Override
	public void bindData() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_OPEN_ATTACH);
		registerReceiver(receiver, filter);

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

		int position = getIntent().getExtras().getInt("position", -1);
		if (position != -1) {
			curApply = GlobalData.listAnimalSlaughterImmuneApply.get(position);

			et_apply_user_name.setText(curApply.apply_user_name);
			tv_apply_time
					.setText(curApply.apply_time.replaceAll("/", "-").substring(0, curApply.apply_time.length() - 3));
			et_inspection_staff.setText(curApply.inspection_staff);
			et_inspection_staff_telephone.setText(curApply.inspection_staff_telephone);
			et_animal_type.setText(curApply.animal_type);
			et_animal_count.setText(curApply.animal_count);
			et_Origin.setText(curApply.animal_origin);
			et_entranceImmune_num.setText(curApply.entrance_immune_num);
			tv_view_signature.setTag(SJECHttpHandler.BASE_URL + curApply.signature_src);
			isReWriteSignature = false;
			if (curApply.is_special_channel_in.equals("1")) {
				rb_special_channelIn_yes.setChecked(true);
			} else {
				rb_special_channelIn_no.setChecked(true);
			}
			if (curApply.clinic_health.equals("1")) {
				rb_animal_health_yes.setChecked(true);
			} else {
				rb_animal_health_no.setChecked(true);
			}

			if (curApply.is_animal_ear_tag.equals("1")) {
				rb_animal_ear_tag_yes.setChecked(true);
			} else {
				rb_animal_ear_tag_no.setChecked(true);
			}
			et_target_address.setText(curApply.target_address);
			tv_start_trans_time.setText(curApply.start_trans_time.replaceAll("/", "-").substring(0,
					curApply.start_trans_time.length() - 3));

			if (curApply.is_read_warn.equals("1")) {
				cbx_read_warn.setChecked(true);
			}
			for (int i = 0; i < listButcheryID.size(); i++) {
				if (curApply.butchery_id.equals(listButcheryID.get(i))) {
					sp_butchery.setSelection(i);
					GlobalData.curButcheryID = curApply.butchery_id;
					break;
				}
			}
		}

		if (arrMinutes == null) {
			arrMinutes = new String[60];
			for (int i = 0; i < 60; i++) {
				arrMinutes[i] = (i > 9 ? i : ("0" + i)) + "分";
			}
		}

		initDateDoCheckTime();
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
					Util.createToast(ActivityConfirmDwtzjysb.this, R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_QUARANTINE));
					finish();
				} else {
					Util.createToast(ActivityConfirmDwtzjysb.this,
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
					Util.createToast(ActivityConfirmDwtzjysb.this, obj.optString("desc", "加载失败"), 3000).show();
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

		JSONObject jobj = new JSONObject();
		try {
			jobj.put("innerid", curApply.innerid);
			jobj.put("confirm_result", rg_apply_confirm_yes.isChecked()?"true":"false");
			jobj.put("do_check_time", getText(tv_do_check_time));
			jobj.put("do_check_address", getText(et_do_check_address));
			jobj.put("reject_reason", getText(et_reject_reason));
			jobj.put("confirm_usr_name",getText(et_confirm_user_name));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String str = jobj.toString();
		try {
			new SJECHttpHandler(cb, this).ConfirmAnimalSlaughterImmuneApply(str);
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityConfirmDwtzjysb.this, R.string.msg_control_failed, 3000).show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void doSignature() {
		WritePadDialog mWritePadDialog = new WritePadDialog(ActivityConfirmDwtzjysb.this, new WriteDialogListener() {
			@Override
			public void onPaintDone(Object object) {
				isReWriteSignature = true;
				String filePath = UtilSign.createSignFile((Bitmap) object);
				tv_view_signature.setTag(filePath);
				curImgPath = filePath;
				tv_view_signature.setVisibility(View.VISIBLE);
			}
		});
		mWritePadDialog.show();
		super.SetDialogFullScreen(mWritePadDialog);
	}

	private void initDateDoCheckTime() {

		// 当前时间
		// set current time
		Calendar calendar = Calendar.getInstance(Locale.CHINA);

		wv_day_do_check_time = (WheelView) findViewById(R.id.wv_day_do_check_time);
		dayAdapter_start = new DayArrayAdapter(this, calendar);
		wv_day_do_check_time.setViewAdapter(dayAdapter_start);
		wv_day_do_check_time.setCurrentItem(10);
		wv_day_do_check_time.setDrawShadows(false);

		wv_day_do_check_time.addClickingListener(new OnWheelClickedListener() {

			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {

			}
		});

		wv_day_do_check_time.addScrollingListener(new OnWheelScrollListener() {

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
		newCalendar.roll(Calendar.DAY_OF_YEAR, -10 + wv_day_do_check_time.getCurrentItem());
		String str = new SimpleDateFormat("yyyy-MM-dd").format(newCalendar.getTime());
		tv_do_check_time.setText(str);
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
}
