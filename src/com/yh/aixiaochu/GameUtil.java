package com.yh.aixiaochu;

import java.io.IOException;
import java.util.List;

import com.yh.aixiaochu.alg.run_py;
import com.yh.aixiaochu.system.PhoneInfo;
import com.yh.aixiaochu.system.Screenshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 爱消除游戏调用入口
 * @author gudh
 * 
 */
public class GameUtil {
	
	private static int gameTrueTimes = 0;
	
	public static void reset(){
		gameTrueTimes = 0;
	}
	
	/**
	 * 进行一次扫描点击
	 * 
	 * @return
	 */
	public static int run_time() {

		// 屏幕截图
		Bitmap bm = getScreenBitmap();
		
		if(!isGamePic(bm)){
			gameTrueTimes--;
			Log.d("Judge", "pic is not game screen, now false time is " + gameTrueTimes);
			return gameTrueTimes;
		}
		
		// 获取步骤
		List<int[][]> steps = run_py.get_step(bm);

		if(steps != null){
			// drag步骤
			boolean res = goSteps(steps, 0);
			if(res){
				gameTrueTimes = 1;
			} else{
				gameTrueTimes = 0;
			}
		} else{
			gameTrueTimes--;
		}
		return gameTrueTimes;
	}

	/**
	 * 获取屏幕截图的bitmap
	 * 
	 * @return
	 */
	public static Bitmap getScreenBitmap() {
		switch(PhoneInfo.usePhone){
		case miOne:
			return getScreenBitmapByFile();
		case huaweiHonor:
			return getScreenBitmapByProcess(); 
		}
		return null;
	}
	
	/**
	 * 通过读取文件命令截图，效率更高
	 * 
	 * @return
	 */
	public static Bitmap getScreenBitmapByFile() {
		long s = System.currentTimeMillis();
		Bitmap bm = null;
		try {
			bm = Screenshot.getScreenBitmap();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		long e = System.currentTimeMillis();
		Log.i("Time", "getScreenBitmap use " + (e - s));
		return bm;
	}

	/**
	 * 通过执行screencap命令截图
	 * 
	 * @return
	 */
	public static Bitmap getScreenBitmapByProcess() {
		long s = System.currentTimeMillis();
		String path = SystemUtil.screenCap();
		Log.d("Screen", "screencap " + path);
		Bitmap bm = BitmapFactory.decodeFile(path);
		long e = System.currentTimeMillis();
		Log.i("Time", "getScreenBitmap use " + (e - s));
		return bm;
	}

	/**
	 * 判断是否进入游戏状态，四个角都是0
	 * @param bm
	 * @return
	 */
	private static boolean isGamePic(Bitmap bm){
		return true;
//		int w = bm.getWidth();
//		int h = bm.getHeight();
//		int[][] ps = {{0, 0}, {0, h - 1}, {w - 1, 0}, {w - 1, h - 1}};
//		for(int[] p : ps){
//			// 全黑色
//			if(bm.getPixel(p[0], p[1]) != 0xff000000){
//				return false;
//			}
//		}
//		return true;
	}
	
	/**
	 * 按步骤走
	 * 
	 * @param steps
	 * @param s
	 *            步骤间延时
	 * @return
	 */
	private static boolean goSteps(List<int[][]> steps, int s) {
		boolean res = false;
		for (int[][] step : steps) {
			try {
				res |= SystemUtil.drag(step[0][0], step[0][1], step[1][0], step[1][1]);
				if (s > 0) {
					Thread.sleep(s);
				}
			} catch (Exception e) {
				res = false;
				e.printStackTrace();
			}
		}
		return res;
	}
}
