package com.yh.aixiaochu.system;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * 通过读取文件 /dev/graphics/fb0获取屏幕截图
 * 截图获取bitmap只需要100ms左右
 * 
 * @author gudh
 * @data 2014-1-17
 */
public class Screenshot {

	final static String FB0FILE1 = "/dev/graphics/fb0";
	final static String FB0FILE2 = "/dev/fb0";

	static File fbFile;
	static FileInputStream graphics = null;
	static int screenWidth = 480; // 屏幕宽（像素，如：480px）
	static int screenHeight = 800; // 屏幕高（像素，如：800p）
	static byte[] piex;

	/**
	 * 初始化基本信息
	 * 
	 * @param context
	 */
	public static void init(Activity context) {
		fbFile = new File(FB0FILE1);
		if (!fbFile.exists()) {
			File nFile = new File(FB0FILE2);
			if (nFile.exists()) {
				fbFile = nFile;
			}
		}
		// 初始化事件文件的权限
		try {
			Process sh = Runtime.getRuntime().exec("su", null, null);
			OutputStream os = sh.getOutputStream();
			os.write(("chmod 777 " + fbFile.getAbsolutePath()).getBytes());
			os.flush();
			os.close();
			sh.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		DisplayMetrics dm = new DisplayMetrics();
		Display display = context.getWindowManager().getDefaultDisplay();
		display.getMetrics(dm);
		screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
		screenHeight = dm.heightPixels; // 屏幕高（像素，如：800p）

		PixelFormat pixelFormat = new PixelFormat();
		PixelFormat.getPixelFormatInfo(PixelFormat.RGBA_8888, pixelFormat);
		int deepth = pixelFormat.bytesPerPixel; // 位深
		piex = new byte[screenHeight * screenWidth * deepth]; // 像素
	}

	/**
	 * 测试截图
	 */
	@SuppressLint("SdCardPath")
	public static void testShot() {
		long start = System.currentTimeMillis();
		try {
			Bitmap bm = getScreenBitmap();
			saveMyBitmap(bm, "/sdcard/" + System.currentTimeMillis() + ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		Log.i("Screenshot", "time cost:" + (end - start));
	}

	/**
	 * 保存bitmap到文件
	 * 
	 * @param bitmap
	 * @param bitName
	 * @throws IOException
	 */
	public static void saveMyBitmap(Bitmap bitmap, String bitName)
			throws IOException {
		File f = new File(bitName);
		f.createNewFile();
		FileOutputStream fOut = new FileOutputStream(f);

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		fOut.flush();
		fOut.close();
	}

	/**
	 * 获取当前屏幕截图，一定要先init
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized static Bitmap getScreenBitmap() throws IOException {
		try {
			graphics = new FileInputStream(fbFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		DataInputStream dStream = new DataInputStream(graphics);
		dStream.readFully(piex);
		dStream.close();

		int[] colors = new int[screenHeight * screenWidth];
		// 将rgb转为色值
		for (int m = 0; m < colors.length; m++) {
			int r = (piex[m * 4] & 0xFF);
			int g = (piex[m * 4 + 1] & 0xFF);
			int b = (piex[m * 4 + 2] & 0xFF);
			int a = (piex[m * 4 + 3] & 0xFF);
			colors[m] = (a << 24) + (r << 16) + (g << 8) + b;
		}

		return Bitmap.createBitmap(colors, screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
	}
}
