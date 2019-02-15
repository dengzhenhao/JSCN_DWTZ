package com.websharp.dwtz.activity.pic;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.websharp.dwtz.R;
import com.websharp.dwtz.activity.BaseActivity;
import com.websharp.dwtz.data.Constant;
import com.websharp.dwtz.data.GlobalData;
import com.websharp.dwtz.http.AsyncHttpCallBack;
import com.websharp.dwtz.http.SJECHttpHandler;
import com.websharputil.common.AppUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.file.FileUtil;
import com.websharputil.image.ImageUtil;

/**
 * 通用的手机拍照上传图片
 * @author dengzh
 *
 */
public class ActivityPicSendCommon extends BaseActivity {
	private final int PIC_WIDTH = 150;
	private final int PIC_HEIGHT= 150;
	ContentResolver resolver;
	Uri uri;
	// ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	

	SharedPreferences sharedPreferences;
	public String innerID, type;

	public String topic_content = null;
	String str;

	private static String TEMP_IMG_FILE = "dwtz_tmp.jpg";
	private static String TEMP_IMG_PATH = Constant.IMAGE_DIR_TAKE_PHOTO
			+ TEMP_IMG_FILE;
	private static String PREFIX_IMG = "dwtz_name.jpg";
	private AdapterPic adapterPic;
	private GridView gv_pic;
	private LinearLayout loading;
	EntityPicture epAdd = new EntityPicture(false, "", null, null);

	private String classID = "";

	private TextView tv_title;
	private LinearLayout layout_back;
	private Button btn_submit;
	private String json_img = "";
	private int widthScreen = 0;
	private int widthGridViewItem = 0;
	
	private String workorder_id = "";
	
	private StringBuilder sb = new StringBuilder();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_submit:
			uploadPics(ActivityPicSendCommon.this);
			
			break;
		} 
	}
	
	public StringBuilder getJsonImg(){
		if (GlobalData.listImage.size() ==1 && ! GlobalData.listImage.get(0).isImage) {
			return new StringBuilder();
		} 
		
		StringBuilder sb = new StringBuilder();
		JSONArray image_jsonArray = new JSONArray();
		Bitmap bitmap = null;
		for (int i = 0; i < GlobalData.listImage.size(); i++) {
			if (!GlobalData.listImage.get(i).isImage) {
				continue;
			}
			JSONObject obj = new JSONObject();
			
			System.err.println("bitmapbytearr2:"
					+ GlobalData.listImage.get(i).byteArr.length);
	
			String topic_image = new String(Base64.encodeBase64(GlobalData.listImage
					.get(i).byteArr));
		
			System.gc();
			try {
				obj.put("data", topic_image);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			image_jsonArray.put(obj);
		}

		sb.append(image_jsonArray.toString());
		return sb;
	}
	
	public void uploadPics(final Context context){
		if (GlobalData.listImage.size() ==1 && ! GlobalData.listImage.get(0).isImage) {
			Util.createToast(context, "请至少选择一张图片", 3000).show(); 
			return;
		} 
		
		 sb = getJsonImg();

		new SJECHttpHandler(new AsyncHttpCallBack() {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				Util.createToast(context,
						R.string.msg_control_success, 3000).show();
				finish();
			}

			@Override
			public void onFailure(String message) {
				super.onFailure(message);
				Util.createToast(context, message, 3000)
						.show(); 
			}
		}, context).uploadPics(
				innerID, sb.toString(),type);
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pic_send);

	}

	@Override
	public void init() {
		//workorder_id = getIntent().getExtras().getString("workorder_id", "");
		
		gv_pic = (GridView) findViewById(R.id.gv_pic);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		loading = (LinearLayout) findViewById(R.id.loading);
		btn_submit.setOnClickListener(this);
		tv_title.setText("上传照片");
		layout_back.setOnClickListener(this);
		if(GlobalData.listImage.size() == 0){
			GlobalData.listImage.add(epAdd);
		}
		adapterPic = new AdapterPic(this);
		gv_pic.setAdapter(adapterPic);
	}

	@Override
	public void bindData() {
		innerID = getIntent().getExtras().getString("innerID", "");
		type = getIntent().getExtras().getString("pic_type","");
		widthScreen = AppUtil.getScreenSize(this).widthPixels;
		widthGridViewItem = (widthScreen - ConvertUtil.dip2px(this, 5) * 4) / 3;
		// TODO Auto-generated method stub
		gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.d(arg2 + "");
				if (!GlobalData.listImage.get(arg2).isImage) {
					TakePhoto();
				}
			}
		});
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sb = null;
		GlobalData.listImage.clear();
		System.gc();
		getApplicationContext().sendBroadcast(new Intent("refresh_web"));
	}

	private void TakePhoto() {
		File f = new File(TEMP_IMG_PATH);
		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		new AlertDialog.Builder(ActivityPicSendCommon.this)
				.setTitle("图片来源...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent openAlbumIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(openAlbumIntent, 999);
					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机

						Uri imageUri = Uri.fromFile(new File(
								Constant.IMAGE_DIR_TAKE_PHOTO, TEMP_IMG_FILE));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, 888);
					}
				}).show();

	}

	/**
	 * 处理相册中选取的照片
	 * 
	 * @param iv
	 * @param data
	 */
	private void handlerPhoto(Intent data) {
		Bitmap bitmap_thumb = null;
		Bitmap bitmap = null;
		if (data != null) {
			resolver = getContentResolver();
			uri = data.getData();
			String path = FileUtil.getAbsoluteImagePathFromUri(this, uri);
			new AsyncLoadImage().execute(path);
		}
	}

	/**
	 * 处理拍摄的照片
	 * 
	 * @param iv
	 */
	private void handlerCamera() {
		if (new File(TEMP_IMG_PATH).exists()) {
			String targetFileName = Constant.IMAGE_DIR_TAKE_PHOTO
					+ PREFIX_IMG.replace("name", DateUtil
							.TimeParseNowToFormatString("yyyyMMddHHmmssSSS"));
			new File(TEMP_IMG_PATH).renameTo(new File(targetFileName));
			//FileUtil.FileCopy(new File(TEMP_IMG_PATH), new File(targetFileName));
			new AsyncLoadImage().execute(targetFileName);
			
		}
	}

	class AsyncLoadImage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String path = params[0];
				EntityPicture ep = new EntityPicture(true, path,
						getThumbImage(path), compressImage(path));
				GlobalData.listImage.remove(epAdd);
				GlobalData.listImage.add(ep);
				if (GlobalData.listImage.size() < 9) {
					GlobalData.listImage.add(epAdd);
				}
			} catch (Exception e) {
				e.printStackTrace();
//				Util.createToast(ActivityPicSend.this,
//						R.string.msg_error_pic_send_take, 3000).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loading.setVisibility(View.GONE);
			adapterPic.notifyDataSetChanged();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 888:
			// 拍照
			handlerCamera();
			break;
		case 999:
			// 相册
			handlerPhoto(data);
			break;
		default:
			break;
		}
		adapterPic.notifyDataSetChanged();
	}

	class ViewHolder {
		private ImageView iv_pic;
		private ImageView iv_delete;
		private TextView tv_thumb_take_pic;
	}

	class AdapterPic extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context ctx;

		public AdapterPic(Context context) {
			this.ctx = context;
		}

		@Override
		public int getCount() {
			return GlobalData.listImage.size();
		}

		@Override
		public Object getItem(int position) {
			return GlobalData.listImage.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			try {
				if (mInflater == null) {
					mInflater = LayoutInflater.from(ctx);
				}
				if (GlobalData.listImage.get(position).isImage) {
					convertView = mInflater.inflate(R.layout.item_grid_img,
							null);
					holder = new ViewHolder();
					holder.iv_pic = (ImageView) convertView
							.findViewById(R.id.iv_thumb);
					holder.iv_delete = (ImageView) convertView
							.findViewById(R.id.iv_delete);
					LayoutParams lp = holder.iv_pic.getLayoutParams();
					lp.width = widthGridViewItem;
					lp.height = widthGridViewItem;
					holder.iv_pic.setLayoutParams(lp);
				} else {
					convertView = mInflater.inflate(
							R.layout.item_grid_take_pic, null);
					holder = new ViewHolder();
					holder.tv_thumb_take_pic = (TextView) convertView
							.findViewById(R.id.tv_thumb_take_pic);
					LayoutParams lp = holder.tv_thumb_take_pic
							.getLayoutParams();
					lp.width = widthGridViewItem;
					lp.height = widthGridViewItem;
					holder.tv_thumb_take_pic.setLayoutParams(lp);
				}

				if (GlobalData.listImage.get(position).isImage) {
					holder.iv_pic.setImageBitmap(ImageUtil.toRoundCorner(
							GlobalData.listImage.get(position).bitmap_thumb, 5));
					BitmapDrawable drawable = (BitmapDrawable) getResources()
							.getDrawable(R.drawable.delete);
					holder.iv_delete.setImageBitmap(ImageUtil.toRoundCorner(
							drawable, 5).getBitmap());
					holder.iv_delete.setTag(position + "");
					holder.iv_delete
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									GlobalData.listImage.remove(Integer.parseInt(v
											.getTag().toString()));
									System.gc();
									adapterPic.notifyDataSetChanged();
								}

							});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

	}

	

	private Bitmap getThumbImage(String path) {
		LogUtil.d(path);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 20;
		Bitmap image = BitmapFactory.decodeFile(path, newOpts);
		Bitmap bitmap_thumb = ThumbnailUtils.extractThumbnail(image, 200, 200);
		try {
			image.recycle();
			image = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap_thumb;
	}

	/**
	 * 压缩图片后返回byte[]数组
	 * 
	 * @param path
	 * @return
	 */
	private byte[] compressImage(String path) {

		int targetSize = 1280;
		int size = 165 * 1024;
		int quality = 90;
		float scale = 0.90f;

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		 newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		// newOpts.inPreferQualityOverSpeed= false;
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, newOpts);
		int imgWidth = newOpts.outWidth;
		int imgHeight = newOpts.outHeight;
		if ((imgWidth > imgHeight && imgWidth >= targetSize * 2)
				|| (imgHeight > imgWidth && imgHeight >= targetSize * 2)) {
			if (imgWidth > imgHeight) {
				newOpts.inSampleSize = 2;
			} else {
				newOpts.inSampleSize = 2;
			}
		}
		newOpts.inJustDecodeBounds = false;
		Bitmap image = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, quality, out);
		float zoom = (float) Math.sqrt(size / (float) out.toByteArray().length);
		Matrix matrix = new Matrix();
		matrix.setScale(zoom, zoom);
		Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
				image.getHeight(), matrix, true);
		out.reset();
		result.compress(Bitmap.CompressFormat.JPEG, quality, out);
		while (out.toByteArray().length > size) {
			matrix.setScale(scale, scale);
			result = Bitmap.createBitmap(result, 0, 0, result.getWidth(),
					result.getHeight(), matrix, true);
			out.reset();
			result.compress(Bitmap.CompressFormat.JPEG, quality, out);
		}
		byte[] arr = out.toByteArray();
		try {
			image.recycle();
			image = null;
			result.recycle();
			result = null;
			out.close();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	


}
