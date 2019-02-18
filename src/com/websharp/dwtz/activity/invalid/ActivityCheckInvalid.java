package com.websharp.dwtz.activity.invalid;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.photo.ActivityPhotoDestroy;
import com.websharp.dwtz.activity.qr.CaptureActivity;
import com.websharp.dwtz.dao.EntityUnqualied;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityCheckInvalid extends BaseActivity {

	private ImageView iv_camera;
	Button btn_submit;
	EntityUnqualied objUnqualied;

	TextView tv_UnqualiedScanCode;
	TextView tv_SendTime;
	TextView tv_GoodsOwner;
	TextView tv_Origin;
	TextView tv_QuarantineNum;
	TextView tv_ProcessReason;
	TextView tv_ProcessComment;
	TextView tv_ImmuneTag;
	EditText et_Remark;

	ImageView iv_spinner_qr ; 
	private LinearLayout layout_back;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			Util.startActivity(ActivityCheckInvalid.this,
					ActivityPhotoDestroy.class, false);
			break;
		case R.id.btn_submit:
			Util.createDialog(this, null, R.string.msg_dialog_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							AsyncAddDestroy();
						}
					}).show();
			break;

		case R.id.layout_back:
			finish();
			break;
//		case R.id.iv_spinner_qr:
//			 ActivityCheckInvalid.this.startActivityForResult(new Intent(ActivityCheckInvalid.this, CaptureActivity.class), 99);
//			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_check_invalid);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		iv_camera.setOnClickListener(this);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		iv_spinner_qr = (ImageView)findViewById(R.id.iv_spinner_qr);
//		iv_spinner_qr.setOnClickListener(this);
		iv_spinner_qr.setVisibility(View.GONE);
		
		tv_UnqualiedScanCode = (TextView) findViewById(R.id.tv_UnqualiedScanCode);
		tv_SendTime = (TextView) findViewById(R.id.tv_SendTime);
		tv_GoodsOwner = (TextView) findViewById(R.id.tv_GoodsOwner);
		tv_Origin = (TextView) findViewById(R.id.tv_Origin);
		tv_QuarantineNum = (TextView) findViewById(R.id.tv_QuarantineNum);
		tv_ProcessReason = (TextView) findViewById(R.id.tv_ProcessReason);
		tv_ProcessComment = (TextView) findViewById(R.id.tv_ProcessComment);
		tv_ImmuneTag = (TextView) findViewById(R.id.tv_ImmuneTag);
		et_Remark = (EditText) findViewById(R.id.et_Remark);
		

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		int index = b.getInt("index");
		objUnqualied = GlobalData.listUnqualied.get(index);
		// TODO Auto-generated method stub
		tv_UnqualiedScanCode.setText(objUnqualied.UnqualiedScanCode);
		tv_SendTime.setText(objUnqualied.SendTime);
		tv_GoodsOwner.setText(objUnqualied.GoodsOwner);
		tv_Origin.setText(objUnqualied.Origin);
		tv_QuarantineNum.setText(objUnqualied.QuarantineNum);
		tv_ProcessReason.setText(objUnqualied.ProcessReason);
		tv_ProcessComment.setText(objUnqualied.ProcessComment);
		tv_ImmuneTag.setText(objUnqualied.ImmuneTag);
	}
	

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (RESULT_OK == resultCode) {
//			if(requestCode == 99){ 
//				Bundle b = data.getExtras();
//				String qrCode = b.getString("data");
//				//比对
//			}
//		}
//	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityCheckInvalid.this,
							R.string.msg_control_success, 3000).show();
					getApplication().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_LIST_UNQUALIED));
					finish();
				} else {
					Util.createToast(
							ActivityCheckInvalid.this,
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

	private void AsyncAddDestroy() {

		try {
//			new SJECHttpHandler(cb, this).addDestroy(objUnqualied.InnerID,
//					getText(et_Remark));
		} catch (Exception e) {
			e.printStackTrace();
			Util.createToast(ActivityCheckInvalid.this,
					R.string.msg_control_failed, 3000).show();
		}
	}

}
