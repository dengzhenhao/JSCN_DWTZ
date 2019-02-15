package com.websharp.dwtz.adapter;

import java.util.ArrayList;

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
import com.websharp.dwtz.activity.apply.ActivityAddDsitributionChangeApply;
import com.websharp.dwtz.activity.apply.ActivityEditApply;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityCheckInvalid;
import com.websharp.dwtz.activity.invalid.ActivityDestroyUnqualiedList;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.activity.invalid.ActivityViewDestroy;
import com.websharp.dwtz.dao.EntityDestroy;
import com.websharp.dwtz.dao.EntityDistributionApply;
import com.websharp.dwtz.dao.EntityQuarantine;
import com.websharp.dwtz.dao.EntityUnqualied;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpListCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

/**
 * 分销申请列表
 * 
 * @author dengzh
 * 
 */
public class AdapterApplyList extends CListView {

	public AdapterApplyList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		// setHeaderResource(R.layout.headview_invalidlist);
		setListItemResource(R.layout.item_apply);
	}

	@Override
	public void ensureUi() {
		mPerpage = 20;

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
				// // 打开设备详情页
				int i = (Integer) v.getTag();
				// LogUtil.d("%s", "点击" + i);
				 Bundle b = new Bundle();
				 //b.putString("applyID",GlobalData.listApply.get(i).InnerID);
				 b.putInt("position",i);
				 Util.startActivity(mActivity, ActivityEditApply.class,b,
				 false);
 
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityDistributionApply item = (EntityDistributionApply) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.tv_LsNo, item.Distribution_LsNo, true));
		LVP.add(new CListViewParam(R.id.tv_Status,
				item.Status.equals("Init") ? "待审核" : (item.Status
						.equals("Pass") ? "通过" : "否决"), true));

		LVP.add(new CListViewParam(R.id.tv_AddTime, DateUtil
				.TimeParseStringToFormatString(item.Add_Time,
						"yyyy-MM-dd HH:mm"), true));

		if (index % 2 == 0) {
			LVP.add(new CListViewParam(R.id.layout_default, null, true));
			LVP.add(new CListViewParam(R.id.layout_color, null, false));
		} else {
			LVP.add(new CListViewParam(R.id.layout_default, null, false));
			LVP.add(new CListViewParam(R.id.layout_color, null, true));
		}
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).getApplyListByUserID(
				page + "", mPerpage + "");
	}

	AsyncHttpListCallBack<ArrayList<EntityDistributionApply>> callback = new AsyncHttpListCallBack<ArrayList<EntityDistributionApply>>(
			AdapterApplyList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityDistributionApply>>() {
			}.getType();
		}

		@Override
		public void addItems() {

			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
					
				}

				GlobalData.listApply = (ArrayList<EntityDistributionApply>) mListItems
						.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
