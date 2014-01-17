package com.yh.aixiaochu;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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
	
	/**
	 * 单击事件文件，不同的手机可能不一样
	 */
	public static String eventFile = "/dev/input/event1";
	
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
			os.write(("chmod 777 " + eventFile).getBytes());
			os.flush();
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
		String[] events = getClickEvents(200, 3);
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
		String[] events = getDragEvents(x1, y1, x2, y2);
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

	/**
	 * 拖动步骤，不同的手机可能不一致
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private static String[] getDragEvents(int x1, int y1, int x2, int y2) {
		String[] events = new String[17];
		// 第一点
		events[0] = "sendevent /dev/input/event1 3 57 0";
		events[1] = "sendevent /dev/input/event1 3 53 " + x1;
		events[2] = "sendevent /dev/input/event1 3 54 " + y1;
		events[3] = "sendevent /dev/input/event1 3 58 31";
		events[4] = "sendevent /dev/input/event1 3 50 2";
		events[5] = "sendevent /dev/input/event1 0 2 0";
		events[6] = "sendevent /dev/input/event1 0 0 0";
		// 延时
		events[7] = "sleep 100";
		// 第二点
		events[8] = "sendevent /dev/input/event1 3 57 0";
		events[9] = "sendevent /dev/input/event1 3 53 " + x2;
		events[10] = "sendevent /dev/input/event1 3 54 " + y2;
		events[11] = "sendevent /dev/input/event1 3 58 31";
		events[12] = "sendevent /dev/input/event1 3 50 2";
		events[13] = "sendevent /dev/input/event1 0 2 0";
		events[14] = "sendevent /dev/input/event1 0 0 0";
		// 确认
		events[15] = "sendevent /dev/input/event1 0 2 0";
		events[16] = "sendevent /dev/input/event1 0 0 0";
		return events;
	}
	
	/**
	 * 单击步骤，不同的手机可能不一致
	 * @param x
	 * @param y
	 * @return
	 */
	private static String[] getClickEvents(int x, int y) {
		String[] events = new String[9];
		events[0] = "sendevent /dev/input/event1 3 57 0";
		events[1] = "sendevent /dev/input/event1 3 53 " + x;
		events[2] = "sendevent /dev/input/event1 3 54 " + y;
		events[3] = "sendevent /dev/input/event1 3 58 46 ";
		events[4] = "sendevent /dev/input/event1 3 50 4";
		events[5] = "sendevent /dev/input/event1 0 2 0";
		events[6] = "sendevent /dev/input/event1 0 0 0";
		events[7] = "sendevent /dev/input/event1 0 2 0";
		events[8] = "sendevent /dev/input/event1 0 0 0";
		return events;
	}
}
