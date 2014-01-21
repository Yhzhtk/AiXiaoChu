package com.yh.aixiaochu;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.yh.aixiaochu.system.EventGetter;
import com.yh.aixiaochu.system.Sendevent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 系统处理
 * @author gudh
 *
 */
public class SystemUtil {
	
	public final static String screenpath = Environment
			.getExternalStorageDirectory().getPath() + "/yhzhtk/";
	
	private static String screenName = null;
	private static byte[] shotCmdBytes  = null;
	private static BitmapFactory.Options options = null;
	
	/**
	 * 初始化数据
	 */
	public static void init(){
		File p = new File(screenpath);
		if(!p.exists()){
			p.mkdirs();
		}
		screenName = screenpath + "screen.png";
		shotCmdBytes = ("/system/bin/screencap -p " + screenName).getBytes();
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		// 初始化事件文件的权限
		try {
			Process sh = Runtime.getRuntime().exec("su", null, null);
			OutputStream os = sh.getOutputStream();
			for(String eventFile : EventGetter.needWriteEventFiles){
				os.write(("chmod 777 " + eventFile).getBytes());
				os.flush();
			}
			os.close();
			sh.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
	}

	/**
	 * 获取屏幕截图
	 * 
	 * @return 截图路径
	 */
	public static String screenCap() {
		try {
			Process sh = Runtime.getRuntime().exec("su", null, null);
			OutputStream os = sh.getOutputStream();
			os.write(shotCmdBytes);
			os.flush();
			os.close();
			sh.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return screenName;
	}

	/**
	 * 单击
	 * @param x
	 * @param y
	 */
	public static boolean click(int x, int y) {
		String[] events = EventGetter.getClickEvents(200, 3);
		return sendEnents(events);
	}

	/**
	 * 拖动
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public static boolean drag(int x1, int y1, int x2, int y2) {
		String[] events = EventGetter.getDragEvents(x1, y1, x2, y2);
		return sendEnents(events);
	}

	/**
	 * 发送事件
	 * 
	 * @param events
	 */
	public static boolean sendEnents(String[] events) {
		// return onProcessEvents(events);
		return Sendevent.onEvent(events);
	}

	/**
	 * 启动进程处理事件
	 * @param events
	 * @return
	 */
	public static boolean onProcessEvents(String[] events){
		try {
			Process suProcess = Runtime.getRuntime().exec("su");  
			DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());  
			for (String event : events) {
				os.writeBytes(event + "\n");
				os.flush();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 测试
	 * @return
	 */
	public static String[] getTest() {
		String[] events = new String[3];
		events[0] = "input keyevent 82"; // MENU
		events[1] = "input keyevent 4"; // Back
		events[2] = "input keyevent 3"; // Home
		return events;
	}
}
