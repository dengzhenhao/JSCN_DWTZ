package com.websharp.dwtz.activity.invalid;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharputil.common.Util;

public class ActivityMeatManList extends BaseActivity {

	Button btn_submit;
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_submit:
			Util.startActivity(ActivityMeatManList.this, ActivityInvalidList.class, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_meatman_list );
	}

	@Override
	public void init() {
		btn_submit  = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void bindData() {

	}

}
