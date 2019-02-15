package com.websharp.dwtz.activity.dwtzjysb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.activity.invalid.ActivityMeatManList;
import com.websharp.dwtz.adapter.AdapterDwtzjysbList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.common.Util;

public class ActivityDwtzjysbList extends BaseActivity {

	private ImageView iv_add_order;
	private PullToRefreshListView lv_order;
	private AdapterDwtzjysbList adapterDwtzjysbList;
	
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
			Util.startActivity(ActivityDwtzjysbList.this, ActivityAddDwtzjysb.class, false);
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
		iv_add_order.setOnClickListener(this);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_dwtzjysb, null);
		lv_order.getRefreshableView().addHeaderView(headView);
	
	}

	@Override
	public void bindData() {
		adapterDwtzjysbList = new AdapterDwtzjysbList(lv_order, this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
