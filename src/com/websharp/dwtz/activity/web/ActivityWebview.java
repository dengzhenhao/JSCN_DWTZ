package com.websharp.dwtz.activity.web;

import java.io.IOException;

import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.pic.ActivityPicSendCommon;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharp.photoview.ViewPagerActivity;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityWebview extends BaseActivity {

	private FrameLayout frameLayout = null;
	private FrameLayout layout_play = null;
	private WebChromeClient chromeClient = null;
	private View myView = null;
	private WebChromeClient.CustomViewCallback myCallBack = null;
	private LinearLayout layout_back;

	private WebView webView;
	private RelativeLayout layout_relative;
	private TextView tv_title;
	private String biaoti;

	String url = "";
	int ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	boolean isPlaying = false;
	private LinearLayout layout_loading;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("refresh_web")) {
				LogUtil.d(intent.getAction());
				webView.loadUrl(url);
			}
		}
	};

	private void initWebview() {
		webView.setWebChromeClient(new MyChromeClient());
		WebSettings mWebSettings = webView.getSettings();

		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setDefaultTextEncodingName("gbk");

		mWebSettings.setSaveFormData(true);
		mWebSettings.setPluginState(PluginState.ON);
		mWebSettings.setRenderPriority(RenderPriority.HIGH);
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebSettings.setDomStorageEnabled(true);
		mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		mWebSettings.setAppCachePath(appCacheDir);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setAppCacheEnabled(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		if (width <= 480) {
			mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		}
		// wv.setScrollBarStyle(0);
		webView.setHapticFeedbackEnabled(false);
		webView.setDrawingCacheEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(), "Android");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				layout_loading.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				layout_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			close();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void close() {
		try {
			layout_relative.removeView(webView);
			webView.removeAllViews();
			webView.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}

	class WebAppInterface {

		@JavascriptInterface
		public String toString() {
			return "injectedObject";
		}

		@JavascriptInterface
		public void OpenResource(String param) {
			LogUtil.d(param);
			String[] arr = param.trim().split("\\|\\|");
			if (arr[0].trim().equals("take_pic")) {
				try {

					Bundle b = new Bundle();
					b.putString("innerID", arr[1]);
					b.putString("pic_type", arr[2]);
					Util.startActivity(ActivityWebview.this, ActivityPicSendCommon.class, b, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (myView == null) {
			super.onBackPressed();
		} else {
			chromeClient.onHideCustomView();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		webView.onResume();
	}

	public class MyChromeClient extends WebChromeClient {

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (myView != null) {
				callback.onCustomViewHidden();
				return;
			}
			frameLayout.removeView(webView);
			// frameLayout.addView(view);
			layout_play.setVisibility(View.VISIBLE);
			layout_play.addView(view);

			myView = view;
			myCallBack = callback;
		}

		@Override
		public void onHideCustomView() {
			if (myView == null) {
				return;
			}
			// frameLayout.removeView(myView);
			layout_play.removeView(myView);
			layout_play.setVisibility(View.GONE);
			myView = null;
			frameLayout.addView(webView);
			myCallBack.onCustomViewHidden();
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return super.onConsoleMessage(consoleMessage);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			close();
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_webview);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter("refresh_web");
		registerReceiver(receiver, filter);
		webView = (WebView) findViewById(R.id.lbnr_webview);
		frameLayout = (FrameLayout) findViewById(R.id.layout_wv);
		layout_play = (FrameLayout) findViewById(R.id.layout_play);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_loading = (LinearLayout) findViewById(R.id.layout_loading);
		layout_back.setOnClickListener(this);
		initWebview();

		Bundle bundle = this.getIntent().getExtras();
		String title = bundle.getString("title");
		url = bundle.getString("url");
//		ORIENTATION = bundle.getInt("ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		if (ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//			
//			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		} else {
//
//			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		LogUtil.d("url:" + url);
		biaoti = title;
		tv_title.setText(title);
		webView.loadUrl(url);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub

	}
	
	
	
	
}
