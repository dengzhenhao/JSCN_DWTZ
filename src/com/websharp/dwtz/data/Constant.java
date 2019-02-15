package com.websharp.dwtz.data;

import android.os.Environment;

public class Constant {

	public static final String APP_NAME = "JSCN_DWTZ";

	public static final String ROOT_SRC = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String SAVE_PATH = ROOT_SRC + "/ JSCN_DWTZ/"; // 本地存放资源路径
	public static final String SDCARD_ATTACH_DIR = SAVE_PATH+"attach/";//附件下载存放路径 
	public static final String SDCARD_IMAGE_DIR = SAVE_PATH + "image/";// 图片存放
	public static final String DOWNLOAD_APK_NAME = SAVE_PATH + "JSCN_DWTZ.apk";
	public static final String IMAGE_DIR_TAKE_PHOTO = SAVE_PATH + "image_take/";

	/**
	 * 注册成功
	 */
	public static final String ACTION_BROADCAST_REGIST_SUCCESS = "regist_success";

	/**
	 * 刷新货单列表
	 */
	public static final String ACTION_REFRESH_LIST_QUARANTINE = "refresh_quarantine";

	/**
	 * 刷新不合格记录表
	 */
	public static final String ACTION_REFRESH_LIST_UNQUALIED = "refresh_unqualied";
	
	/**
	 * 刷新销毁记录表
	 */
	public static final String ACTION_REFRESH_LIST_DESTROY = "refresh_destroy";
	
	/**
	 * 刷新分销申请记录表
	 */
	public static final String ACTION_REFRESH_LIST_APPLY = "refresh_apply";
	
	/**
	 * 添加分销明细记录
	 */
	public static final String ACTION_ADD_DISTRI_DETAIL = "add_distri_detail";
	

	public static final String ACTION_OPEN_ATTACH = "open_attach";

	/**
	 * 货单流水前缀
	 */
	public static final String LSNO_PREFIX_HD = "HD"; 
	/**
	 * 不合格记录单流水前缀 
	 */
	public static final String LSNO_PREFIX_BHG = "BHG"; 
	/**
	 * 销毁记录流水前缀 
	 */
	public static final String LSNO_PREFIX_XH = "XH"; 
	/**
	 * 分销申请流水前缀
	 */
	public static final String LSNO_PREFIX_DISTRIBUTION = "FX"; 
	
	
	 public static final String COMMON_DATA_TYPE_GOODSOWNER = "goods_owner";
     
     public   static final  String  COMMON_DATA_TYPE_ORIGIN = "origin";
 
     public   static final  String  COMMON_DATA_TYPE_PROCESS_REASON = "process_reason";
   
     public   static final  String  COMMON_DATA_TYPE_PROCESS_COMMENT = "process_comment";
    
     public   static final  String  COMMON_DATA_TYPE_DISTRIBUTION_COMPANY = "distribution_company";
     
     public   static final  String  COMMON_DATA_TYPE_DISTRIBUTION_ADDRESS = "distribution_target_address";
	
}
