package com.websharp.dwtz.activity.invalid;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.order.ActivityOrderList;
import com.websharp.dwtz.activity.qr.CaptureActivity;
import com.websharp.dwtz.adapter.AdapterDestroyUnqualiedList;
import com.websharp.dwtz.adapter.AdapterInvalidList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityDestroyUnqualiedList extends BaseActivity {

	private TextView tv_title;
	private ImageView iv_add_invalid, iv_add_invalid_qr;
	private PullToRefreshListView lv_invalid;
	private AdapterDestroyUnqualiedList adapterDestroyUnqualiedList;
	private String destroyID = "";
	private boolean isHistory = false;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_REFRESH_LIST_UNQUALIED)) {
				adapterDestroyUnqualiedList.initListViewStart();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_invalid_qr:
			// Bundle b = new Bundle();
			// b.putString("destroyID",destroyID);
			// Util.startActivity(ActivityDestroyUnqualiedList.this,
			// ActivityDestroyUnqualiedChooseList.class,b,
			// false);
			ActivityDestroyUnqualiedList.this
					.startActivityForResult(new Intent(ActivityDestroyUnqualiedList.this, CaptureActivity.class), 100);
			break;
		case R.id.iv_add_invalid:
			Bundle b = new Bundle();
			b.putString("destroyID", destroyID);
			Util.startActivity(ActivityDestroyUnqualiedList.this, ActivityDestroyUnqualiedChooseList.class, b, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_destroy_unqualied_list);
	}

	@Override
	public void init() {
		IntentFilter filter = new IntentFilter(Constant.ACTION_REFRESH_LIST_UNQUALIED);
		registerReceiver(receiver, filter);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_invalid = (PullToRefreshListView) findViewById(R.id.lv_invalid);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_destroy_qualied_list, null);
		lv_invalid.getRefreshableView().addHeaderView(headView);
		iv_add_invalid = (ImageView) findViewById(R.id.iv_add_invalid);
		iv_add_invalid.setOnClickListener(this);
		iv_add_invalid_qr = (ImageView) findViewById(R.id.iv_add_invalid_qr);
		iv_add_invalid_qr.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		destroyID = b.getString("destroyID", "");
		isHistory = b.getBoolean("isHistory", false);
		GlobalData.listUnqualied.clear();
		adapterDestroyUnqualiedList = new AdapterDestroyUnqualiedList(lv_invalid, this, destroyID, isHistory);
		if (isHistory) {
			tv_title.setText("销毁的不合格记录");
			iv_add_invalid.setVisibility(View.GONE);
			iv_add_invalid_qr.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			if (requestCode == 100) {
				Bundle b = data.getExtras();
				String qrCode = b.getString("data");
				// 添加到列表中,调用接口
				new SJECHttpHandler(cbAddUnqualied, this).addDestroyUnqualiedByQr(destroyID, qrCode);
			}
		}
	}

	AsyncHttpCallBack cbAddUnqualied = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("result").equals("true")) {
					Util.createToast(ActivityDestroyUnqualiedList.this, R.string.msg_control_success, 3000).show();
					adapterDestroyUnqualiedList.initListViewStart();
				} else {
					Util.createToast(ActivityDestroyUnqualiedList.this, obj.optString("desc", ""), 3000).show();
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
