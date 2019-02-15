package com.websharp.dwtz.activity.photo;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharputil.common.Util;

public class ActivityPhotoOrder extends BaseActivity {

	Button btn_submit;
	LinearLayout layout_back;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			Util.createToast(ActivityPhotoOrder.this, "操作成功!", 3000).show();
			finish();
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_photo_order);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		layout_back  = (LinearLayout)findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub

	}

}
