package com.websharp.dwtz.activity.dwtzjysb;

import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;


public class ActivityViewImage extends BaseActivity {

	ImageView iv_view;
	Uri uri;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_view_image);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_view = (ImageView) findViewById(R.id.iv_view);
	}

	@Override
	public void bindData() {
		uri = this.getIntent().getData();
		Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
		iv_view.setImageBitmap(bitmap);
	}

}
