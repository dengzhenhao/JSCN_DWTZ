package com.websharp.dwtz.activity.main;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.dh.Demo.TestDpsdkCoreActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.websharp.dwtz.R;
import com.websharp.dwtz.R.id;
import com.websharp.dwtz.R.layout;
import com.websharp.dwtz.R.menu;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.activity.apply.ActivityAddDsitributionChangeApply;
import com.websharp.dwtz.activity.apply.ActivityApplyList;
import com.websharp.dwtz.activity.dwtzjysb.ActivityDwtzjysbList;
import com.websharp.dwtz.activity.user.ActivityCategory;
import com.websharp.dwtz.activity.user.ActivityLogin;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.adapter.AdapterMainList;
import com.websharp.dwtz.adapter.AdapterOrderList;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityCommonData;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends BaseActivity {

	private ImageView iv_ycspglxt;
	private ImageView iv_sjxxxt;
	private ImageView iv_fxxt;
	private ImageView iv_sy;
	private ImageView iv_whhcljgxt;
	private ImageView iv_tzc;

	// private ViewFlipper notice_flipper;
	private PullToRefreshListView lv_content;
	private AdapterMainList adapterMainList;
	public RelativeLayout layout_head_title;
	public TextView tv_head_title;
	public TextView tv_notice;
	public TextView tv_login;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				validDownloadStatus(downloadID);
			}
		}
	};

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		if (!new File(Constant.SAVE_PATH).exists()) {
			new File(Constant.SAVE_PATH).mkdirs();
			new File(Constant.SDCARD_ATTACH_DIR).mkdirs();
			new File(Constant.SDCARD_IMAGE_DIR).mkdirs();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// tv_notice = (TextView)findViewById(R.id.tv_content);
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);
		tv_login = (TextView) findViewById(R.id.tv_login);
		iv_tzc = (ImageView) findViewById(R.id.iv_tzc);
		iv_ycspglxt = (ImageView) findViewById(R.id.iv_ycspglxt);
		iv_sjxxxt = (ImageView) findViewById(R.id.iv_sjxxxt);
		iv_fxxt = (ImageView) findViewById(R.id.iv_fxxt);
		iv_sy = (ImageView) findViewById(R.id.iv_sy);
		iv_whhcljgxt = (ImageView) findViewById(R.id.iv_whhcljgxt);
		// notice_flipper = (ViewFlipper)findViewById(R.id.notice_flipper);
		lv_content = (PullToRefreshListView) findViewById(R.id.lv_content);

		// 绑定headview
		LayoutInflater mInflater = LayoutInflater.from(this);
		View headView = mInflater.inflate(R.layout.headview_main, null);

		layout_head_title = (RelativeLayout) headView.findViewById(R.id.layout_head_title);
		tv_head_title = (TextView) headView.findViewById(R.id.tv_head_title);
		tv_notice = (TextView) headView.findViewById(R.id.tv_notice);
		lv_content.getRefreshableView().addHeaderView(headView);

		layout_head_title.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		tv_notice.setOnClickListener(this);
		iv_tzc.setOnClickListener(this);
		iv_ycspglxt.setOnClickListener(this);
		iv_sjxxxt.setOnClickListener(this);
		iv_fxxt.setOnClickListener(this);
		iv_sy.setOnClickListener(this);
		iv_whhcljgxt.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		initParam();
		// ImageView iv_icon = (ImageView)findViewById(R.id.iv_icon);
		adapterMainList = new AdapterMainList(lv_content, this);

		// TODO Auto-generated method stub
		checkUpdateApk(this);
		if (GlobalData.listCommonData.size() == 0) {
			new SJECHttpHandler(cbGetCommonData, MainActivity.this).getCommonData();
		}

		if (GlobalData.listProvince.size() == 0) {
			new SJECHttpHandler(cbLocation, MainActivity.this).GetLocation();
		}

		if (GlobalData.listButchery.size() == 0) {
			new SJECHttpHandler(cbButchery, MainActivity.this).getAllButchery();
		}

		if (new File(Constant.IMAGE_DIR_TAKE_PHOTO).exists()) {
			FileUtil.DeleteDirectory(new File(Constant.IMAGE_DIR_TAKE_PHOTO));
		}
		new SJECHttpHandler(cbGetButchery, this).getButchery();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (GlobalData.curUser == null) {
			tv_login.setText("登录");
		} else {
			tv_login.setText(GlobalData.curUser.UserName + " 注销");
		}
	}

	@Override
	public void onClick(View v) {

		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.iv_tzc:
			if (GlobalData.curUser == null) {
				Util.startActivity(MainActivity.this, ActivityLogin.class, false);
			} else {
				// 监管，销毁，官方兽医
				if (GlobalData.curUser.Role.equals("JG") || GlobalData.curUser.Role.equals("XH")
						|| GlobalData.curUser.Role.equals("GFSY")) {
					Util.startActivity(MainActivity.this, ActivityCategory.class, false);
				} else {
					Util.createToast(MainActivity.this, R.string.msg_dialog_role_failed, 3000).show();
				}
			}
			break;
		case R.id.iv_ycspglxt:
			if (GlobalData.curUser == null) {
				Util.startActivity(MainActivity.this, ActivityLogin.class, false);
			} else {
				if (GlobalData.curUser.Role.equals("JG")) {
					Util.startActivity(MainActivity.this, TestDpsdkCoreActivity.class, false);
				} else {
					Util.createToast(MainActivity.this, R.string.msg_dialog_role_failed, 3000).show();
				}
			}
			break;
		case R.id.iv_fxxt:
			// 分销系统
			if (GlobalData.curUser == null) {
				Util.startActivity(MainActivity.this, ActivityLogin.class, false);
			} else {
				if (GlobalData.curUser.Role.equals("FX")) {
					Util.startActivity(MainActivity.this, ActivityApplyList.class, false);
				} else {
					Util.createToast(MainActivity.this, R.string.msg_dialog_role_failed, 3000).show();
				}
			}
			break;
		case R.id.iv_sjxxxt:
			// 检疫申报
			//Util.startActivity(MainActivity.this, ActivityDwtzjysbList.class, false);
			if (!GlobalData.curUser.Role.equals("JG") && !GlobalData.curUser.Role.equals("GFSY")) {
				Util.startActivity(MainActivity.this, ActivityDwtzjysbList.class, false);
			} else {
				Util.createToast(MainActivity.this, R.string.msg_dialog_role_failed, 3000).show();
			}
			break;
		case R.id.iv_sy:
			// Util.startActivity(MainActivity.this, ActivityLogin.class,
			// false);
			break;
		case R.id.iv_whhcljgxt:
			// Util.startActivity(MainActivity.this, ActivityLogin.class,
			// false);
			break;
		case R.id.layout_head_title:
			b.putString("title", getText(tv_head_title));
			b.putString("url",
					String.format(SJECHttpHandler.URL_PAGE_ARTICLE_CONTENT, layout_head_title.getTag().toString()));
			Util.startActivity(this, ActivityWebview.class, b, false);
			break;
		case R.id.tv_notice:
			b.putString("title", getText(tv_notice));
			b.putString("url", String.format(SJECHttpHandler.URL_PAGE_ARTICLE_CONTENT, tv_notice.getTag().toString()));
			Util.startActivity(this, ActivityWebview.class, b, false);
			break;
		case R.id.tv_login:
			if (GlobalData.curUser == null) {
				Util.startActivity(MainActivity.this, ActivityLogin.class, true);
			} else {

				Editor editor = PrefUtil.getEditor(this);
				editor.clear();
				editor.commit();

				GlobalData.curUser = null;
				tv_login.setText("登录");
				Util.createToast(MainActivity.this, R.string.msg_dialog_cancel_success, 3000).show();
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	/*
	 * 查询版本更新相关
	 */
	public static boolean IS_NEED_UPDATE = false;
	public static String URL_NEW_VERSION = "";
	public static String UPDATE_CONTENT = "";
	DownloadManager downloadmanager = null;

	public void checkUpdateApk(Context context) {
		new SJECHttpHandler(cb, context).checkVersion();
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					JSONObject json = obj.getJSONObject("data");
					int versionCode = json.optInt("VersionCode", 0);
					IS_NEED_UPDATE = AppData.VERSION_CODE < versionCode ? true : false;
					URL_NEW_VERSION = SJECHttpHandler.BASE_URL + json.optString("DownloadUrl");
					UPDATE_CONTENT = json.optString("UpdateContent", "");
					if (IS_NEED_UPDATE) {
						showUpdateApkDialog(MainActivity.this, Constant.DOWNLOAD_APK_NAME, URL_NEW_VERSION,
								UPDATE_CONTENT);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cbGetCommonData = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					GlobalData.listCommonData = gson.fromJson(obj.getString("data"),
							new TypeToken<ArrayList<EntityCommonData>>() {
							}.getType());

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cbLocation = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					ArrayList<EntityLocation> list = gson.fromJson(obj.getString("data"),
							new TypeToken<ArrayList<EntityLocation>>() {
							}.getType());
					GlobalData.listProvince.clear();
					GlobalData.listCity.clear();
					GlobalData.listCounty.clear();
					GlobalData.listProvince.add(new EntityLocation(-1, "--请选择省份--", "", "", -9999, 2));
					GlobalData.listCity.add(new EntityLocation(-1, "--请选择城市--", "", "", -9999, 3));
					GlobalData.listCounty.add(new EntityLocation(-1, "--请选择区/县--", "", "", -9999, 4));
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).LocationLevel == 2) {
							GlobalData.listProvince.add(list.get(i));
						} else if (list.get(i).LocationLevel == 3) {
							GlobalData.listCity.add(list.get(i));
						} else if (list.get(i).LocationLevel == 4) {
							GlobalData.listCounty.add(list.get(i));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cbButchery = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					GlobalData.listButchery = gson.fromJson(obj.getString("data"),
							new TypeToken<ArrayList<EntityButchery>>() {
							}.getType());
					GlobalData.listButchery.add(0, new EntityButchery("", "--请选择--", "", "", "", ""));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	public void showUpdateApkDialog(final Context context, final String savePath, final String url_download,
			final String update_content) {

		StringBuffer sb = new StringBuffer();
		sb.append("更新内容:\n" + update_content);
		Dialog dialog = new AlertDialog.Builder(context).setTitle(R.string.welcome_dialog_version_msg)
				.setMessage(sb.toString())
				.setPositiveButton(R.string.welcome_dialog_version_confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startDownloadApk(context, savePath, url_download);
					}
				}).setNegativeButton(R.string.welcome_dialog_version_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		// }
	}

	public long startDownloadApk(Context context, String savePath, String url_download) {

		if (new File(savePath).exists()) {
			new File(savePath).delete();
		}
		downloadmanager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
		Request req = new DownloadManager.Request(Uri.parse(url_download));
		req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		req.setAllowedOverRoaming(false);
		req.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
		req.setDestinationUri(Uri.fromFile(new File(savePath)));
		long tmp = downloadmanager.enqueue(req);
		return tmp;
	}

	/**
	 * 监听是否下载apk成功,这个方法放在receiver中用来监听 android.intent.action.DOWNLOAD_COMPLETE
	 * android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED
	 * 
	 * @param downloadmanager
	 * @param downloadID
	 * @param context
	 * @param apkPath
	 */
	public void validDownloadStatus(long downloadID) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadID);
		Cursor c = downloadmanager.query(query);
		int status = -1;
		if (c != null && c.moveToFirst()) {
			status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
		c.close();
		if (status == 8) {
			LogUtil.d("%s", "下载成功");
			// 下载成功,调用系统的安装程序
			updateAndInstall();
		}
	}

	/**
	 * 安装/覆盖软件
	 * 
	 * @param context
	 * @param apkPath
	 */
	public void updateAndInstall() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Constant.DOWNLOAD_APK_NAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}


	AsyncHttpCallBack cbGetButchery = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Gson gson = new Gson();
					GlobalData.listButcheryByUser  = gson.fromJson(obj.getString("data"), new TypeToken<ArrayList<EntityButchery>>() {
					}.getType());
				} else {
					Util.createToast(MainActivity.this,
							obj.optString("desc", getString(R.string.msg_failed_getButcheryGroup)), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};
}
