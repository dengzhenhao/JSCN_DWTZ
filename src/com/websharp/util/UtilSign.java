package com.websharp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.websharp.dwtz.data.Constant;
import com.websharputil.date.DateUtil;

import android.graphics.Bitmap;


public class UtilSign {

	// 创建签名文件
	public static String createSignFile(Bitmap mSignBitmap) {
		ByteArrayOutputStream baos = null;
		FileOutputStream fos = null;
		File file = null;
		String filenamePng = Constant.SDCARD_IMAGE_DIR + "/"
				+ DateUtil.TimeParseNowToFormatString("yyyyMMddHHmmss")
				+ ".png";
		try {
			file = new File(filenamePng);
			if (!new File(Constant.SDCARD_IMAGE_DIR).exists()) {
				new File(Constant.SDCARD_IMAGE_DIR).mkdirs();
			}
			fos = new FileOutputStream(file);
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] b = baos.toByteArray();
			if (b != null) {
				fos.write(b);
			}
			return filenamePng;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
