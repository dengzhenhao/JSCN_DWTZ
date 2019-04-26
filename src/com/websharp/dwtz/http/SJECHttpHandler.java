package com.websharp.dwtz.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.client.utils.URLEncodedUtils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;

import com.loopj.android.http.RequestParams;
import com.websharp.dwtz.data.GlobalData;
import com.websharputil.code.DescUtil;
import com.websharputil.code.CodeUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.image.ImageUtil;
import com.websharputil.json.JSONUtils;

public class SJECHttpHandler extends BaseHttpHandler {

	public static final String CLIENT = "android";

	/**
	 * 接口中加密的密钥
	 */
	public final static String ENCRYPT_KEY = "ButcherTracer_API";

	/**
	 * 服务器地址
	 */
//	public static final String BASE_URL = "http://192.168.0.16:9898";
	 public static final String BASE_URL = "http://153.37.221.130:8800";

	public static String URL_PAGE_ARTICLE_CONTENT = BASE_URL + "/client/article.aspx?articleID=%s";

	/**
	 * 检查更新
	 */
	public static final String URL_CHECK_VERSION = "/handlers/version/CheckVersion.ashx";

	/**
	 * 登录接口
	 */
	public static final String URL_LOGIN = "/handlers/user/Login.ashx";

	public static final String URL_GET_MODULE_BY_USER_ID = "/handlers/user/GetUserModuleByUserID.ashx";

	/**
	 * 城市列表
	 */
	public static final String URL_LIST_LOCATION = "/handlers/business/GetLocation.ashx";

	/**
	 * 获防通用数据
	 */
	public static final String URL_GET_COMMON_DATA = "/handlers/business/GetCommonData.ashx";

	/**
	 * 新增工单
	 */
	public static final String URL_ADD_ORDER = "/handlers/business/AddQuarantine.ashx";
	public static final String URL_EDIT_ORDER = "/handlers/business/EditQuarantine.ashx";

	/**
	 * 工单列表
	 */
	public static final String URL_LIST_ORDER = "/handlers/business/GetQuarantineListByButcheryID.ashx";

	/**
	 * 新增不合格记录
	 */
	public static final String URL_ADD_UNQUALIED = "/handlers/business/AddUnqualied.ashx";

	/**
	 * 不合格记录列表
	 */
	public static final String URL_LIST_UNQUALIED = "/handlers/business/GetUnqualiedList.ashx";

	/**
	 * 新增销毁记录
	 */
	public static final String URL_ADD_DESTROY = "/handlers/business/AddDestroy.ashx";
	public static final String URL_LIST_DESTROY = "/handlers/business/GetDestroyListByButcheryID.ashx";
	public static final String URL_LIST_DESTROY_UNQUALIED = "/handlers/business/GetDestroyUnqualiedList.ashx";
	public static final String URL_DELETE_DESTROY_UNQUALIED = "/handlers/business/DeleteDestroyUnqualied.ashx";
	public static final String URL_ADD_DESTROY_UNQUALIED = "/handlers/business/AddDestroyUnqualied.ashx";
	public static final String URL_GetUnqualiedListByButcheryID = "/handlers/business/GetUnqualiedListByButcheryID.ashx";
	public static final String URL_UpdateDestroyStatus = "/handlers/business/UpdateDestroyStatus.ashx";
	public static final String URL_ADD_DESTROY_UNQUALIED_BY_QR = "/handlers/business/AddDestroyUnqualiedByQr.ashx";

	public static final String URL_ADD_DISTRIBUTION_CHANGE_APPLY = "/handlers/apply/AddDistributionChangeApply.ashx";
	public static final String URL_GET_APPLY_LIST_BY_USER_ID = "/handlers/apply/GetApplyListByUserID.ashx";

	/**
	 * 动态新闻
	 */
	public static final String URL_NEWS = "/handlers/article/QueryArticle.ashx";

	/**
	 * 动态
	 */
	public static final String URL_NOTICE = "/handlers/article/Notification.ashx";

	/**
	 * 首页顶部的新闻
	 */
	public static final String URL_TOPAD = "/handlers/article/TopAD.ashx";

	/**
	 * 获取流水号
	 */
	public static final String URL_GET_LS_NO = "/handlers/business/GetLsNo.ashx";

	/**
	 * 获取屠宰场下面的屠宰小组
	 */
	public static final String URL_GET_BUTCHERY_GROUP = "/handlers/business/GetButcheryGroup.ashx";

	/**
	 * 获取所有屠宰场
	 */
	public static final String URL_GET_BUTCHERY = "/handlers/business/GetButchery.ashx";
	public static final String URL_GET_ALL_BUTCHERY = "/handlers/business/GetAllButchery.ashx";

	public static String URL_TAKE_PIC = BASE_URL + "/client/takePic.aspx?type=%s&innerID=%s&user_id=%s";

	/**
	 * 上传图片
	 */
	public static final String URL_UPDATE_WORKORDER_PICS = "/handlers/upload/uploadPics.ashx";

	/**
	 * 查询销毁关联的不合格记录数量
	 */
	public static final String URL_GetUnqualiedCountByDestroyID = "/handlers/business/GetUnqualiedCountByDestroyID.ashx";

	/**
	 * 动物屠宰检疫申报
	 */
	public static final String URL_LIST_ANIMAL_SLAUGHTER_IMMUNE = "/handlers/animal_slaughter_immune_apply/GetAnimalSlaughterImmuneApplyByUserID.ashx";

	/**
	 * 检疫申报页面，加载申报人的数据
	 */
	public static final String URL_LIST_ANIMAL_SLAUGHTER_IMMUNE_APPLY_DATA = "/handlers/animal_slaughter_immune_apply/GetAnimalSlaughterImmuneApplyerData.ashx";

	public static final String URL_ADD_ANIMAL_SLAUGHTER_IMMUNE_APPLY = "/handlers/animal_slaughter_immune_apply/AddAnimalSlaughterImmuneApply.ashx";

	public SJECHttpHandler(AsyncHttpCallBack callback, Context context) {
		super(callback, context);

		// if (GlobalData.curUser == null) {
		// String prefUser = PrefUtil.getPref(context, "user", "");
		// if (!prefUser.isEmpty()) {
		// GlobalData.curUser = JSONUtils.fromJson(prefUser,
		// EntityUser.class);
		// }
		// }
	}

	/**
	 * 得到签名字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String GetSignature(String... params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length != 0) {
			for (int i = 0; i < params.length; i++) {

				sb = sb.append(params[i]);

			}
		}
		sb = sb.append(CLIENT).append(ENCRYPT_KEY);
		String str = CodeUtil.MD5(sb.toString()).substring(8, 24);
		return str;
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 *            登录名
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void login(String userName, String password) throws Exception {
		RequestParams params = new RequestParams();
		params.add("telephone", URLEncoder.encode(userName, "utf-8"));
		params.add("password", password);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(userName, password));
		new AsyncHttpUtil().post(BASE_URL + URL_LOGIN, params, handler);
	}

	public void checkVersion() {
		RequestParams params = new RequestParams();
		params.add("client", CLIENT);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_CHECK_VERSION, params, handler);
	}

	/**
	 * 添加工单
	 * 
	 * @param jsonObj
	 * @throws Exception
	 */
	public void addOrder(String jsonObj, String json_img) throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_ADD_ORDER, params, handler);
	}

	public void editOrder(String innerid, String jsonObj) throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("innerid", innerid);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, innerid, jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_EDIT_ORDER, params, handler);
	}

	/**
	 * 添加不合格记录
	 * 
	 * @param jsonObj
	 * @throws Exception
	 */
	public void addUnqualied(String jsonObj, String quarantineId, String json_img) throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("quarantine_id", quarantineId);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, quarantineId, jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_ADD_UNQUALIED, params, handler);
	}

	public void addDestroy(String jsonObj) throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_ADD_DESTROY, params, handler);
	}

	/**
	 * 分页获取送货单列表
	 * 
	 * @param page
	 * @param pageSize
	 * @throws Exception
	 */
	public void GetQuarantineListByButcheryID(String page, String pageSize) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("butchery_id", GlobalData.curButcheryID);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, GlobalData.curButcheryID, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_ORDER, params, handler);
	}

	/**
	 * 分页查询销毁记录
	 * 
	 * @param page
	 * @param pageSize
	 */
	public void GetDestroyListByButcheryID(String page, String pageSize, boolean isHistory) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("butchery_id", GlobalData.curButcheryID);
		params.add("isHistory", isHistory ? "1" : "0");
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, GlobalData.curButcheryID, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_DESTROY, params, handler);
	}

	/**
	 * 分页获取不合格记录表
	 * 
	 * @param page
	 * @param pageSize
	 * @throws Exception
	 */
	public void GetUnqualiedListByButcheryID(String page, String pageSize, String quarantineId) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("quarantineID", quarantineId);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, quarantineId, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_UNQUALIED, params, handler);
	}

	/**
	 * 添加分销换证的申请
	 * 
	 * @param jsonObj
	 * @throws Exception
	 */
	public void addDistributionChangeApply(String jsonObj, String json_img) throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_ADD_DISTRIBUTION_CHANGE_APPLY, params, handler);
	}

	/**
	 * 获取动态新闻
	 * 
	 * @throws Exception
	 */
	public void GetDongTaiNews(String page, String pageSize) {
		RequestParams params = new RequestParams();
		// params.add("user_id", GlobalData.curUser.InnerID);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_NEWS, params, handler);
	}

	/**
	 * 顶部新闻
	 */
	public void GetTopAD() {
		RequestParams params = new RequestParams();
		// params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_TOPAD, params, handler);
	}

	/**
	 * 通知
	 */
	public void GetNotice() {
		RequestParams params = new RequestParams();
		// params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_NOTICE, params, handler);
	}

	/**
	 * 按前缀类型获取流水号
	 * 
	 * @param prefix
	 *            前缀类型分别为 HD:货单流水号 BHG:不合格记录流水号 XH:销毁记录流水号
	 */
	public void getLsNo(String prefix) {
		RequestParams params = new RequestParams();
		params.add("prefix", prefix);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(prefix));
		new AsyncHttpUtil().get(BASE_URL + URL_GET_LS_NO, params, handler);
	}

	/**
	 * 获取屠宰小组
	 * 
	 * @param quarantineID
	 */
	public void getButcheryGroup(String quarantineID) {
		RequestParams params = new RequestParams();
		params.add("quarantineID", quarantineID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(quarantineID));
		new AsyncHttpUtil().get(BASE_URL + URL_GET_BUTCHERY_GROUP, params, handler);
	}

	/**
	 * 获取屠宰场
	 * 
	 * @param quarantineID
	 */
	public void getButchery() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_GET_BUTCHERY, params, handler);
	}

	public void getAllButchery() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_GET_ALL_BUTCHERY, params, handler);
	}

	public void getDestroyUnqualiedList(String page, String pageSize, String destroy_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_DESTROY_UNQUALIED, params, handler);
	}

	public void getDeleteDestroyUnqualied(String destroy_id, String unqualied_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("unqualied_id", unqualied_id);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id, unqualied_id));
		new AsyncHttpUtil().get(BASE_URL + URL_DELETE_DESTROY_UNQUALIED, params, handler);
	}

	public void addDestroyUnqualied(String destroy_id, String unqualied_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("unqualied_id", unqualied_id);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id, unqualied_id));
		new AsyncHttpUtil().get(BASE_URL + URL_ADD_DESTROY_UNQUALIED, params, handler);
	}

	public void getUnqualiedListByButcheryID(String page, String pageSize) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("butchery_id", GlobalData.curButcheryID);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, GlobalData.curButcheryID, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_GetUnqualiedListByButcheryID, params, handler);
	}

	public void getUpdateDestroyStatus(String destroy_id, String json_img) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id));
		new AsyncHttpUtil().post(BASE_URL + URL_UpdateDestroyStatus, params, handler);
	}

	public void getCommonData() {
		MyRequestParams params = MyRequestParams.getInstance();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_GET_COMMON_DATA, params, handler);
	}

	public void uploadPics(String workorder_id, String json_img, String pic_type) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("pic_type", pic_type);
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, workorder_id, pic_type));
		new AsyncHttpUtil().post(BASE_URL + URL_UPDATE_WORKORDER_PICS, params, handler);
	}

	public void getApplyListByUserID(String page, String pageSize) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_GET_APPLY_LIST_BY_USER_ID, params, handler);
	}

	public void addDestroyUnqualiedByQr(String destroy_id, String qrcode) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("qrcode", qrcode);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id, qrcode));
		new AsyncHttpUtil().get(BASE_URL + URL_ADD_DESTROY_UNQUALIED_BY_QR, params, handler);
	}

	public void GetUnqualiedCountByDestroyID(String destroy_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("destroy_id", destroy_id);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, destroy_id));
		new AsyncHttpUtil().get(BASE_URL + URL_GetUnqualiedCountByDestroyID, params, handler);
	}

	public void GetLocation() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_LOCATION, params, handler);
	}

	public void GetSlaughterListByUserID(String page, String pageSize) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("page", page);
		params.add("pageSize", pageSize);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, page, pageSize));
		new AsyncHttpUtil().get(BASE_URL + URL_LIST_ANIMAL_SLAUGHTER_IMMUNE, params, handler);
	}

	public void GetAnimalSlaughterImmuneApplyData(String butchery_id, String apply_user_name) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("butchery_id", butchery_id);
		params.add("apply_user_name", apply_user_name);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, butchery_id, apply_user_name));
		new AsyncHttpUtil().post(BASE_URL + URL_LIST_ANIMAL_SLAUGHTER_IMMUNE_APPLY_DATA, params, handler);
	}

	public void addAnimalSlaughterImmuneApply(String jsonObj, String imgPath) throws Exception {

		String topic_image = "";
		if (!imgPath.isEmpty() && imgPath != null) {
			topic_image = new String(
					org.apache.commons.codec.binary.Base64.encodeBase64(ImageUtil.GetByteFromFile(imgPath)));
		}

		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("jsonObj", URLEncoder.encode(jsonObj, "utf-8"));
		params.add("sign_img", topic_image);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID, jsonObj));
		new AsyncHttpUtil().post(BASE_URL + URL_ADD_ANIMAL_SLAUGHTER_IMMUNE_APPLY, params, handler);
	}

	public void GetUserModuleByUserID() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_GET_MODULE_BY_USER_ID, params, handler);
	}
}
