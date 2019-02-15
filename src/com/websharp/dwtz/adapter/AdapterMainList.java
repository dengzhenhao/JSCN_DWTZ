package com.websharp.dwtz.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.activity.main.MainActivity;
import com.websharp.dwtz.activity.order.ActivityAddOrder;
import com.websharp.dwtz.activity.web.ActivityWebview;
import com.websharp.dwtz.dao.EntityArticle;
import com.websharp.dwtz.dao.EntityQuarantine;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.AsyncHttpListCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 
 * 
 * @author dengzh
 * 
 */
public class AdapterMainList extends CListView {

	public AdapterMainList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_news);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.item_list_getmore,
				R.id.list_item_getmore_title, "加载更多");
		super.ensureUi();
		mListViewPTR
				.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (actionType == IDLE && !mAdapter.isNotMore) {
							// 如果并没有在加载过程中,可以加载更多
							getmoreListViewStart();
						}
					}
				});
		// super.setGetMoreClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// getmoreListViewStart();
		// }
		// });

		super.setItemOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开新闻详情页
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
				EntityArticle article = (EntityArticle)mListItems.get(i);
				Bundle b = new Bundle();
				b.putString("title", article.Title);
				b.putString("url", String.format(SJECHttpHandler.URL_PAGE_ARTICLE_CONTENT, article.InnerID));
				Util.startActivity(mActivity, ActivityWebview.class, b, false);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final EntityArticle item = (EntityArticle) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		CListViewParam avatarLVP = new CListViewParam(R.id.iv_title, R.drawable.default_pic,
				true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(item.CutPath);
		LVP.add(avatarLVP);
		LVP.add(new CListViewParam(R.id.tv_title, item.Title, true));
		LVP.add(new CListViewParam(R.id.tv_content, item.ArticleContent, true));
		LVP.add(new CListViewParam(R.id.tv_publishtime, item.PublishTime, true));

		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).GetDongTaiNews(page + "",
				mPerpage + "");
		//LogUtil.d("actiontype:%s", actionType+"");
		if(actionType == REFRESH || actionType == INIT){
			new SJECHttpHandler(cbNotice, mActivity).GetNotice();
			new SJECHttpHandler(cbTopAD, mActivity).GetTopAD();
		}
	}
	
	AsyncHttpCallBack cbNotice = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
						JSONArray jArray = obj.getJSONArray("data");
						if(jArray.length()>0){
							JSONObject jsonData = jArray.getJSONObject(0);
							((MainActivity)mActivity).tv_notice.setTag(jsonData.optString("InnerID", ""));
							((MainActivity)mActivity).tv_notice.setText(jsonData.optString("Title", "--"));
						}
				} else {
					Util.createToast(
							mActivity,
							obj.optString("desc",
									mActivity.getString(R.string.common_login_failed)),
							3000).show();
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
	
	
	AsyncHttpCallBack cbTopAD = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					JSONArray jArray = obj.getJSONArray("data");
					if(jArray.length()>0){
						JSONObject jsonData = jArray.getJSONObject(0);
						((MainActivity)mActivity).layout_head_title.setTag(jsonData.optString("InnerID", ""));
						((MainActivity)mActivity).tv_head_title.setText(jsonData.optString("Title", "--"));
					}
				} else {
					Util.createToast(
							mActivity,
							obj.optString("desc",
									mActivity.getString(R.string.common_login_failed)),
							3000).show();
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

	AsyncHttpListCallBack<ArrayList<EntityArticle>> callback = new AsyncHttpListCallBack<ArrayList<EntityArticle>>(
			AdapterMainList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityArticle>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}

			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
