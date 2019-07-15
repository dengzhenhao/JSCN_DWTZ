package com.websharp.dwtz.adapter;

import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.dwtzjysb.ActivityConfirmDwtzjysb;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApply;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpListCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 动物屠宰检疫申报列表
 * 
 * @author dengzh
 * 
 */
public class AdapterDwtzjysbListForCheck extends CListView {

	public AdapterDwtzjysbListForCheck(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		//setHeaderResource(R.layout.headview_orderlist);
		setListItemResource(R.layout.item_dwtzjysb);
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
				// 打开设备详情页
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
//				Bundle b = new Bundle();
//				b.putString("QuarantineID",
//						GlobalData.listQuarantine.get(i).InnerID);
//				b.putString("DeliveryNum",
//						GlobalData.listQuarantine.get(i).DeliveryNum);
//				Util.startActivity(mActivity, ActivityInvalidList.class, b,
//						false);
				Bundle b = new Bundle();
				b.putInt("position",i);
				Util.startActivity(mActivity, ActivityConfirmDwtzjysb.class, b,
						false);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityAnimalSlaughterImmuneApply item = (EntityAnimalSlaughterImmuneApply) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.tv_status, item.confirm_status.equals("0") ? "待审核":(item.confirm_status.equals("1")? "通过":"被否决"), true));
		LVP.add(new CListViewParam(R.id.tv_user, item.apply_user_name, true));
		LVP.add(new CListViewParam(R.id.tv_time, item.apply_time, true));
		LVP.add(new CListViewParam(R.id.tv_phone, item.inspection_staff_telephone, true));
		String sendTime = "";
		
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
		new SJECHttpHandler(callback, mActivity).GetAnimalSlaughterImmuneApplyUnCheck();
	}

	AsyncHttpListCallBack<ArrayList<EntityAnimalSlaughterImmuneApply>> callback = new AsyncHttpListCallBack<ArrayList<EntityAnimalSlaughterImmuneApply>>(
			AdapterDwtzjysbListForCheck.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityAnimalSlaughterImmuneApply>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				
				GlobalData.listAnimalSlaughterImmuneApply = (ArrayList<EntityAnimalSlaughterImmuneApply>) mListItems
						.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
