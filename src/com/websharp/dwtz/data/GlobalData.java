package com.websharp.dwtz.data;

import java.util.ArrayList;

import com.websharp.dwtz.activity.pic.EntityPicture;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApply;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityCommonData;
import com.websharp.dwtz.dao.EntityDestroy;
import com.websharp.dwtz.dao.EntityDistributionApply;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityQuarantine;
import com.websharp.dwtz.dao.EntitySlaughter;
import com.websharp.dwtz.dao.EntityUnqualied;
import com.websharp.dwtz.dao.EntityUser;


public class GlobalData {

	public static void clear() {
		curButcheryID = "";
		curUser = null;
		listButchery.clear();
		listCommonData.clear();
		listQuarantine.clear();
		listUnqualied.clear();
		listDestroy.clear();
		listDestroyUnqualied.clear();
		listApply.clear();
		listImage.clear();
		listButcheryByUser.clear();
		System.gc();
	}
	
	public static String curButcheryID = "";

	public static EntityUser curUser =null;
	

	public static ArrayList<EntityLocation> listProvince = new ArrayList<EntityLocation>();
	
	public static ArrayList<EntityLocation> listCity = new ArrayList<EntityLocation>();
	
	public static ArrayList<EntityLocation> listCounty = new ArrayList<EntityLocation>();
	
	public static ArrayList<EntityButchery> listButchery = new ArrayList<EntityButchery>();
	
	public static ArrayList<EntityButchery> listButcheryByUser = new ArrayList<EntityButchery>();
	
	/**
	 *通用数据列表
	 */
	public static ArrayList<EntityCommonData> listCommonData  = new ArrayList<EntityCommonData>();
	
	
	/**
	 * 送货单列表
	 */
	public static ArrayList<EntityQuarantine> listQuarantine = new ArrayList<EntityQuarantine>();
	

	public static ArrayList<EntityAnimalSlaughterImmuneApply> listAnimalSlaughterImmuneApply = new ArrayList<EntityAnimalSlaughterImmuneApply>();
	
	/**
	 * 不合格记录表
	 */
	public static ArrayList<EntityUnqualied> listUnqualied = new ArrayList<EntityUnqualied>();
	
	public static ArrayList<EntityDestroy>  listDestroy= new ArrayList<EntityDestroy>();
	
	public static ArrayList<EntityUnqualied> listDestroyUnqualied = new ArrayList<EntityUnqualied>();
	
	public static ArrayList<EntityDistributionApply> listApply= new ArrayList<EntityDistributionApply>();
	
	public static ArrayList<EntityPicture> listImage = new ArrayList<EntityPicture>();
}
