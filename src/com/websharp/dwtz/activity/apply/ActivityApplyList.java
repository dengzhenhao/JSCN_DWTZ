package com.websharp.dwtz.activity.apply;

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
import com.websharp.dwtz.adapter.AdapterApplyList;
import com.websharp.dwtz.adapter.AdapterDestroyList;
import com.websharp.dwtz.adapter.AdapterInvalidList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.common.Util;

public class ActivityApplyList extends BaseActivity {

	private ImageView iv_add_apply;
	private PullToRefreshListView lv_apply;
	private AdapterApplyList adapterApplyList;	

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constant.ACTION_REFRESH_LIST_APPLY)) {
				adapterApplyList.refreshListViewStart();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iv_add_apply:
				Util.startActivity(ActivityApplyList.this, ActivityAddDsitributionChangeApply .class, 
						false);
				break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_apply_list);
	}

	@Override
	public void init() {
		IntentFilter filter = new IntentFilter(
				Constant.ACTION_REFRESH_LIST_APPLY);
		registerReceiver(receiver, filter);
		lv_apply = (PullToRefreshListView) findViewById(R.id.lv_apply);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_apply_list, null);
		lv_apply.getRefreshableView().addHeaderView(headView);
		iv_add_apply = (ImageView)findViewById(R.id.iv_add_apply);
		iv_add_apply.setOnClickListener(this);
		
	}

	@Override
	public void bindData() {
		adapterApplyList = new AdapterApplyList(lv_apply, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
