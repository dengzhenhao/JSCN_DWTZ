package com.websharp.dwtz.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.websharp.dwtz.activity.invalid.ActivityAddDestroy;
import com.websharp.dwtz.activity.invalid.ActivityAddInvalid;
import com.websharp.dwtz.activity.invalid.ActivityCheckInvalid;
import com.websharp.dwtz.activity.invalid.ActivityEditInvalid;
import com.websharp.dwtz.activity.invalid.ActivityInvalidList;
import com.websharp.dwtz.dao.EntityQuarantine;
import com.websharp.dwtz.dao.EntityUnqualied;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.AsyncHttpListCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 货单中的不合格品列表
 * 
 * @author dengzh
 * 
 */
public class AdapterDestroyUnqualiedList extends CListView {

	private String destroyID = "";
	private boolean isHistory = false;

	public AdapterDestroyUnqualiedList(PullToRefreshListView lv, Activity activity, String dID, boolean isHistory) {
		super(lv, activity);
		this.isHistory = isHistory;
		this.destroyID = dID;
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		// setHeaderResource(R.layout.headview_invalidlist);
		setListItemResource(R.layout.item_destroy_qualied_list);
	}

	@Override
	public void ensureUi() {
		mPerpage = 20;

		super.setGetMoreResource(R.layout.item_list_getmore, R.id.list_item_getmore_title, "加载更多");
		super.ensureUi();
		mListViewPTR.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
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
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
				Bundle b = new Bundle();
				b.putInt("position", i);
				Util.startActivity(mActivity, ActivityEditInvalid.class, b, false);

			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityUnqualied item = (EntityUnqualied) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.tv_UnqualiedScanCode, item.UnqualiedScanCode, true));
		LVP.add(new CListViewParam(R.id.tv_DeliveryNum, item.DeliveryNum, true));
		if (!isHistory) {
			CListViewParam cvpDelete = new CListViewParam(R.id.iv_delete, null, true);
			LVP.add(cvpDelete);
			
			cvpDelete.setOnclickLinstener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					LogUtil.d("delete" + ((View) v.getParent().getParent()).getTag());
					Util.createDialog(mActivity, null, R.string.msg_dialog_title, R.string.msg_confirm_control, null,
							true, false, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									deleteDestroyUnqualied(
											Integer.parseInt(((View) v.getParent().getParent()).getTag().toString()));
								}
							}).show();
				}
			});
		}else{
			CListViewParam cvpDelete = new CListViewParam(R.id.iv_delete, null, false);
			LVP.add(cvpDelete);
		}

		if (index % 2 == 0) {
			LVP.add(new CListViewParam(R.id.layout_default, null, true));
			LVP.add(new CListViewParam(R.id.layout_color, null, false));
		} else {
			LVP.add(new CListViewParam(R.id.layout_default, null, false));
			LVP.add(new CListViewParam(R.id.layout_color, null, true));
		}
		return LVP;
	}

	private void deleteDestroyUnqualied(int position) {
		new SJECHttpHandler(cbDeleteUnqualied, mActivity).getDeleteDestroyUnqualied(destroyID,
				GlobalData.listUnqualied.get(position).InnerID);
	}

	AsyncHttpCallBack cbDeleteUnqualied = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(mActivity, R.string.msg_control_success, 3000).show();
					refreshListViewStart();
				} else {
					Util.createToast(mActivity,
							obj.optString("desc", mActivity.getString(R.string.common_login_failed)), 3000).show();
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

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).getDestroyUnqualiedList(page + "", mPerpage + "", destroyID);
	}

	AsyncHttpListCallBack<ArrayList<EntityUnqualied>> callback = new AsyncHttpListCallBack<ArrayList<EntityUnqualied>>(
			AdapterDestroyUnqualiedList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityUnqualied>>() {
			}.getType();
		}

		@Override
		public void addItems() {

			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}

				GlobalData.listUnqualied = (ArrayList<EntityUnqualied>) mListItems.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
