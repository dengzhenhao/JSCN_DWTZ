package com.websharp.dwtz.activity.user;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityDestroyList;
import com.websharp.dwtz.activity.order.ActivityOrderList;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityCategory extends BaseActivity {

	Spinner sp_butchery;
	Button btn_unqualied, btn_destroy, btn_statistics, btn_destroy_history;
	ArrayAdapter adapterButchery;
	ArrayList<String> listButcheryName = new ArrayList<String>();
	ArrayList<EntityButchery> listButchery = new ArrayList<EntityButchery>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (sp_butchery.getSelectedItemPosition() == -1) {
			Util.createToast(this, "请选择屠宰场,或者您目前还未被分配任何屠宰场.", 3000).show();
			return;
		}
		GlobalData.curButcheryID = listButchery.get(sp_butchery.getSelectedItemPosition()).InnerID;
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.btn_unqualied:
			Util.startActivity(ActivityCategory.this, ActivityOrderList.class, false);
			break;
		case R.id.btn_destroy:
			Util.startActivity(ActivityCategory.this, ActivityDestroyList.class, false);
			break;
		case R.id.btn_destroy_history:
			b.putBoolean("isHistory", true);
			Util.startActivity(ActivityCategory.this, ActivityDestroyList.class, b, false);
			break;
		case R.id.btn_statistics:
			b.putString("title", "查看统计报表");
			b.putInt("ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			b.putString("url", SJECHttpHandler.BASE_URL + "/setting/statistics/mobile_statistics_type.aspx?user_id="
					+ GlobalData.curUser.InnerID);
			// b.putInt("ORIENTATION",
			// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			Util.startActivity(this, ActivityWebview.class, b, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_category);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		sp_butchery = (Spinner) findViewById(R.id.sp_butchery);
		btn_unqualied = (Button) findViewById(R.id.btn_unqualied);
		btn_destroy = (Button) findViewById(R.id.btn_destroy);
		btn_statistics = (Button) findViewById(R.id.btn_statistics);
		btn_destroy_history = (Button) findViewById(R.id.btn_destroy_history);
		btn_unqualied.setOnClickListener(this);
		btn_destroy.setOnClickListener(this);
		btn_statistics.setOnClickListener(this);
		btn_destroy_history.setOnClickListener(this);
		adapterButchery = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listButcheryName);
		adapterButchery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_butchery.setAdapter(adapterButchery);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		if (GlobalData.curUser != null) {
			// if (GlobalData.curUser.Role.equals("JG")) {
			// btn_destroy.setVisibility(View.GONE);
			// btn_destroy_history.setVisibility(View.GONE);
			// btn_unqualied.setVisibility(View.VISIBLE);
			// } else if (GlobalData.curUser.Role.equals("GFSY")) {
			// btn_destroy.setVisibility(View.GONE);
			// btn_destroy_history.setVisibility(View.GONE);
			// btn_unqualied.setVisibility(View.VISIBLE);
			// }else if(GlobalData.curUser.Role.toUpperCase().equals("QYTZGL"))
			// {
			// btn_destroy.setVisibility(View.VISIBLE);
			// btn_destroy_history.setVisibility(View.VISIBLE);
			// btn_unqualied.setVisibility(View.VISIBLE);
			// } else {
			// btn_destroy.setVisibility(View.VISIBLE);
			// btn_destroy_history.setVisibility(View.VISIBLE);
			// btn_unqualied.setVisibility(View.GONE);
			// }

			if (GlobalData.ContainModule("销毁记录表")) {
				btn_destroy.setVisibility(View.VISIBLE);
				btn_destroy_history.setVisibility(View.VISIBLE);
			} else {
				btn_destroy.setVisibility(View.GONE);
				btn_destroy_history.setVisibility(View.GONE);
			}

			if (GlobalData.ContainModule("进场登记表")) {
				btn_unqualied.setVisibility(View.VISIBLE);
			} else {
				btn_unqualied.setVisibility(View.GONE);
			}

			// if (GlobalData.curUser.Role.equals("JG") ||
			// GlobalData.curUser.Role.equals("DJS")
			// || GlobalData.curUser.Role.equals("jdgl")
			// || GlobalData.curUser.Role.toUpperCase().equals("QYTZGL")) {
			// btn_statistics.setVisibility(View.VISIBLE);
			// } else {
			// btn_statistics.setVisibility(View.GONE);
			// }

			if (GlobalData.ContainModule("统计图")) {
				btn_statistics.setVisibility(View.VISIBLE);
			} else {
				btn_statistics.setVisibility(View.GONE);
			}

		}
		new SJECHttpHandler(cbGetButchery, this).getButchery();
	}

	AsyncHttpCallBack cbGetButchery = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					listButchery = gson.fromJson(obj.getString("data"), new TypeToken<ArrayList<EntityButchery>>() {
					}.getType());
					GlobalData.listButcheryByUser = listButchery;
					for (int i = 0; i < listButchery.size(); i++) {
						listButcheryName.add(listButchery.get(i).ButcheryName);
					}
					adapterButchery.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityCategory.this,
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
