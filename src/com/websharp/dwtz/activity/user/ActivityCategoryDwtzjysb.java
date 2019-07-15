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
import com.websharp.dwtz.activity.dwtzjysb.ActivityDwtzjysbList;
import com.websharp.dwtz.activity.dwtzjysb.ActivityDwtzjysbListUnCheck;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityDestroyList;
import com.websharp.dwtz.activity.main.MainActivity;
import com.websharp.dwtz.activity.order.ActivityOrderList;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityCategoryDwtzjysb extends BaseActivity {

	Button btn_dwtzjysb_list, btn_dwtzjysb_list_confirm;

	@Override
	public void onClick(View v) {
	
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.btn_dwtzjysb_list:	
			Util.startActivity(ActivityCategoryDwtzjysb.this, ActivityDwtzjysbList.class, false);
			break;
		case R.id.btn_dwtzjysb_list_confirm:
			Util.startActivity(ActivityCategoryDwtzjysb.this, ActivityDwtzjysbListUnCheck.class, false);
			break;
		
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_category_dwtzjysb);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btn_dwtzjysb_list = (Button) findViewById(R.id.btn_dwtzjysb_list);
		btn_dwtzjysb_list_confirm = (Button) findViewById(R.id.btn_dwtzjysb_list_confirm);
	
		btn_dwtzjysb_list.setOnClickListener(this);
		btn_dwtzjysb_list_confirm.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		if (GlobalData.curUser.Role.toLowerCase().equals("jysb")
				|| GlobalData.curUser.Role.toLowerCase().equals("gfsy") ) {
			btn_dwtzjysb_list.setVisibility(View.GONE);
			btn_dwtzjysb_list_confirm.setVisibility(View.VISIBLE);
		}else{
			btn_dwtzjysb_list.setVisibility(View.VISIBLE);
			btn_dwtzjysb_list_confirm.setVisibility(View.GONE);
		}
	}


}
