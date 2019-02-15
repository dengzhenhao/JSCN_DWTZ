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
import com.websharp.dwtz.adapter.AdapterDestroyUnqualiedChooseList;
import com.websharp.dwtz.adapter.AdapterDestroyUnqualiedList;
import com.websharp.dwtz.adapter.AdapterInvalidList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.common.Util;

public class ActivityDestroyUnqualiedChooseList extends BaseActivity {

//	private ImageView iv_add_invalid;
	private PullToRefreshListView lv_invalid;
	private AdapterDestroyUnqualiedChooseList  adapterDestroyUnqualiedChooseList;
	private String destroyID= "";

	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iv_add_invalid:
				Util.startActivity(ActivityDestroyUnqualiedChooseList.this, ActivityAddInvalid.class,
						false);
				break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_destroy_unqualied_choose_list);
	}

	@Override
	public void init() {
		
		lv_invalid = (PullToRefreshListView) findViewById(R.id.lv_invalid);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_destroy_qualied_list, null);
		lv_invalid.getRefreshableView().addHeaderView(headView);
//		iv_add_invalid = (ImageView)findViewById(R.id.iv_add_invalid);
//		iv_add_invalid.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		destroyID = b.getString("destroyID", "");
		adapterDestroyUnqualiedChooseList = new AdapterDestroyUnqualiedChooseList(lv_invalid, this,destroyID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getApplicationContext().sendBroadcast(new Intent(Constant.ACTION_REFRESH_LIST_UNQUALIED));
	}

}
