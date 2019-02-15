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
import com.websharp.dwtz.adapter.AdapterDestroyList;
import com.websharp.dwtz.adapter.AdapterInvalidList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.common.Util;

public class ActivityDestroyList extends BaseActivity {

	private ImageView iv_add_destroy;
	private PullToRefreshListView lv_destroy;
	private AdapterDestroyList adapterDestroyList;
	private TextView tv_title;
	private boolean isHistory = false;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_REFRESH_LIST_DESTROY)) {
				adapterDestroyList.refreshListViewStart();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_destroy:
			Util.startActivity(ActivityDestroyList.this, ActivityAddDestroy.class, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_destroy_list);
	}

	@Override
	public void init() {
		IntentFilter filter = new IntentFilter(Constant.ACTION_REFRESH_LIST_DESTROY);
		registerReceiver(receiver, filter);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_destroy = (PullToRefreshListView) findViewById(R.id.lv_destroy);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_destroylist, null);
		lv_destroy.getRefreshableView().addHeaderView(headView);
		iv_add_destroy = (ImageView) findViewById(R.id.iv_add_destroy);
		iv_add_destroy.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		if (getIntent().getExtras() != null) {
			isHistory = getIntent().getExtras().getBoolean("isHistory", false);
			if (isHistory) {
				tv_title.setText("历史销毁目录");
				iv_add_destroy.setVisibility(View.GONE);
			}
		}
		adapterDestroyList = new AdapterDestroyList(lv_destroy, this, isHistory);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
