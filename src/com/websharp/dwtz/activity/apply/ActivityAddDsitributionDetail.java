package com.websharp.dwtz.activity.apply;

import java.util.ArrayList;

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
import com.websharp.dwtz.activity.order.ActivityAddOrder;
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
public class ActivityAddDsitributionDetail extends BaseActivity {

	Button btn_submit;
	Button btn_clear;

	EditText et_distribution_target_address;
	EditText et_distribution_count;

	ImageView iv_spinner_distribution_target_address;
	ArrayList<String> listTargetAddress = new ArrayList<String>();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
							// AsyncAddDistributionChangeApply();
							Intent intent = new Intent(
									Constant.ACTION_ADD_DISTRI_DETAIL);
							intent.putExtra("target_address",
									getText(et_distribution_target_address));
							intent.putExtra("count", ConvertUtil
									.ParsetStringToDouble(
											getText(et_distribution_count), 0));
							getApplicationContext().sendBroadcast(intent);
							finish();
						}
					}).show();
			break;
		case R.id.btn_clear:
			Util.createDialog(this, null, R.string.msg_dialog_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							et_distribution_target_address.setText("");
							et_distribution_count.setText("");
						}
					}).show();

			break;
		case R.id.iv_spinner_distribution_target_address:
			showDialogListView(listTargetAddress,
					et_distribution_target_address);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_add_distribution_detail);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		et_distribution_target_address = (EditText) findViewById(R.id.et_distribution_target_address);
		et_distribution_count = (EditText) findViewById(R.id.et_distribution_count);
		iv_spinner_distribution_target_address = (ImageView)findViewById(R.id.iv_spinner_distribution_target_address);
		iv_spinner_distribution_target_address.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		
	}

	@Override
	public void bindData() {

		for (int i = 0; i < GlobalData.listCommonData.size(); i++) {
			if (GlobalData.listCommonData.get(i).Type
					.equals(Constant.COMMON_DATA_TYPE_DISTRIBUTION_ADDRESS)) {
				listTargetAddress
						.add(GlobalData.listCommonData.get(i).DataValue);
			}
		}
	}

	private String checkContent() {

		if (getText(et_distribution_target_address).isEmpty()) {
			return getString(R.string.msg_empty_distribution_target_address);
		}

		if (getText(et_distribution_count).isEmpty()) {
			return getString(R.string.msg_empty_distribution_count);
		}
		return "";
	}
}
