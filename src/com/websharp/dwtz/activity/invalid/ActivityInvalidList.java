package com.websharp.dwtz.activity.invalid;

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
import com.websharp.dwtz.adapter.AdapterInvalidList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.common.Util;

public class ActivityInvalidList extends BaseActivity {

	private ImageView iv_add_invalid;
	private PullToRefreshListView lv_invalid;
	private AdapterInvalidList adapterInvalidList;
	private String quarantineID= "";
	private String deliveryNum = "";

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constant.ACTION_REFRESH_LIST_UNQUALIED)) {
				adapterInvalidList.refreshListViewStart();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iv_add_invalid:
				Bundle b = new Bundle();
				b.putString("QuarantineID",quarantineID);
				b.putString("DeliveryNum",deliveryNum);
				Util.startActivity(ActivityInvalidList.this, ActivityAddInvalid.class, b,
						false);
				break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_invalid_list);
	}

	@Override
	public void init() {
		IntentFilter filter = new IntentFilter(
				Constant.ACTION_REFRESH_LIST_UNQUALIED);
		registerReceiver(receiver, filter);
		lv_invalid = (PullToRefreshListView) findViewById(R.id.lv_invalid);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_invalidlist, null);
		lv_invalid.getRefreshableView().addHeaderView(headView);
		iv_add_invalid = (ImageView)findViewById(R.id.iv_add_invalid);
		iv_add_invalid.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		quarantineID = b.getString("QuarantineID", "");
		deliveryNum = b.getString("DeliveryNum", "");
		GlobalData.listUnqualied.clear();
		adapterInvalidList = new AdapterInvalidList(lv_invalid, this,quarantineID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
