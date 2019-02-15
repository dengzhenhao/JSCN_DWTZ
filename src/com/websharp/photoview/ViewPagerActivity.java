/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.websharp.photoview;

import java.io.File;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.data.Constant;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerActivity extends BaseActivity {

	AsyncImageLoaderScrollImage asyncLoad = new AsyncImageLoaderScrollImage();
	private ViewPager mViewPager;
	private String[] arrImageUrls ;
	private RelativeLayout layout_scroll_img;
	private TextView tv_loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scroll_image);
		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(getResources().getColor(android.R.color.black));
		layout_scroll_img = (RelativeLayout) findViewById(R.id.layout_scroll_img);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
	
		layout_scroll_img.addView(mViewPager);

		Bundle b = getIntent().getExtras();
		int index = 0;
		try {
			index =Integer.parseInt( b.getString("index","0"));
			JSONArray arr = new JSONArray(b.getString("pics", "[]"));
			if (arr != null) {
				arrImageUrls = new String[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					arrImageUrls[i] = obj.getString("pic");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
		
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return arrImageUrls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {

			final PhotoView photoView = new PhotoView(container.getContext());
			// photoView.setImageResource(sDrawables[position]);
			Bitmap bitmap = asyncLoad.loadDrawable(arrImageUrls[position],
					new AsyncImageLoaderScrollImage.ImageCallback() {

						@Override
						public void imageLoaded(Bitmap imageDrawable,
								String imageUrl) {
							try { 
								photoView.setImageBitmap(imageDrawable);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, false);
			if (bitmap == null) {
				photoView.setImageResource(R.drawable.icon);
			} else {
				photoView.setImageBitmap(bitmap);
			}
			// photoView.setImageURI(Uri.parse(urls[position]));
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	
	protected void clearData() {
		try {
			mViewPager.removeAllViews();
			mViewPager = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		
	}

}
