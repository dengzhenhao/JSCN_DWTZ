package com.websharp.dwtz.activity.dwtzjysb;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.adapter.AdapterDwtzjysbList;
import com.websharp.dwtz.adapter.AdapterDwtzjysbListForCheck;
import com.websharp.dwtz.data.Constant;
import com.websharputil.common.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ActivityDwtzjysbListUnCheck extends BaseActivity {

	private ImageView iv_add_order;
	private PullToRefreshListView lv_order;
	private AdapterDwtzjysbListForCheck adapterDwtzjysbList;
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.ACTION_REFRESH_LIST_QUARANTINE)){
				adapterDwtzjysbList.refreshListViewStart();
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.iv_add_order:
			Util.startActivity(ActivityDwtzjysbListUnCheck.this, ActivityAddDwtzjysb.class, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_dwtzjysb_list);
	}

	@Override
	public void init() {
		IntentFilter filter = new IntentFilter(Constant.ACTION_REFRESH_LIST_QUARANTINE);
		registerReceiver(receiver, filter);
		iv_add_order = (ImageView)findViewById(R.id.iv_add_order);
		lv_order = (PullToRefreshListView) findViewById(R.id.lv_order);
		//iv_add_order.setOnClickListener(this);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_dwtzjysb, null);
		lv_order.getRefreshableView().addHeaderView(headView);
	
	}

	@Override
	public void bindData() {
		iv_add_order.setVisibility(View.GONE);
		adapterDwtzjysbList = new AdapterDwtzjysbListForCheck(lv_order, this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
