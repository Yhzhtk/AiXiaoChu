package com.yh.aixiaochu.system;

/**
 * 获取事件
 * 
 * @author gudh
 * @data 2014-1-21
 */
public class EventGetter {
	// 事件文件
	public static String miOneTouchEvent;
	public static String huaweiHonorEvent;
	// 需要赋予写权限的文件
	public static String[] needWriteEventFiles;

	/**
	 * 根据设置获取拖拽事件
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static String[] getDragEvents(int x1, int y1, int x2, int y2) {
		switch (PhoneInfo.usePhone) {
		case miOne:
			return getMiOneDragEvents(x1, y1, x2, y2);
		case huaweiHonor:
			return getHuaweiHonorDragEvents(x1, y1, x2, y2);
		}
		return null;
	}
	
	/**
	 * 根据设置获取单击事件
	 * @param x
	 * @param y
	 * @return
	 */
	public static String[] getClickEvents(int x, int y){
		switch (PhoneInfo.usePhone) {
		case miOne:
			return getMiOneClickEvents(x, y);
		case huaweiHonor:
			return getHuaweiHonorClickEvents(x, y);
		}
		return null;
	}
	
	/**
	 * 小米1s拖动步骤
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static String[] getMiOneDragEvents(int x1, int y1, int x2, int y2) {
		String[] events = new String[17];
		// 第一点
		events[0] = "sendevent " + miOneTouchEvent + " 3 57 0";
		events[1] = "sendevent " + miOneTouchEvent + " 3 53 " + x1;
		events[2] = "sendevent " + miOneTouchEvent + " 3 54 " + y1;
		events[3] = "sendevent " + miOneTouchEvent + " 3 58 31";
		events[4] = "sendevent " + miOneTouchEvent + " 3 50 2";
		events[5] = "sendevent " + miOneTouchEvent + " 0 2 0";
		events[6] = "sendevent " + miOneTouchEvent + " 0 0 0";
		// 延时
		events[7] = "sleep 100";
		// 第二点
		events[8] = "sendevent " + miOneTouchEvent + " 3 57 0";
		events[9] = "sendevent " + miOneTouchEvent + " 3 53 " + x2;
		events[10] = "sendevent " + miOneTouchEvent + " 3 54 " + y2;
		events[11] = "sendevent " + miOneTouchEvent + " 3 58 31";
		events[12] = "sendevent " + miOneTouchEvent + " 3 50 2";
		events[13] = "sendevent " + miOneTouchEvent + " 0 2 0";
		events[14] = "sendevent " + miOneTouchEvent + " 0 0 0";
		// 确认
		events[15] = "sendevent " + miOneTouchEvent + " 0 2 0";
		events[16] = "sendevent " + miOneTouchEvent + " 0 0 0";
		return events;
	}
	
	/**
	 * 小米1s单击步骤
	 * @param x
	 * @param y
	 * @return
	 */
	public static String[] getMiOneClickEvents(int x, int y) {
		String[] events = new String[9];
		events[0] = "sendevent " + miOneTouchEvent + " 3 57 0";
		events[1] = "sendevent " + miOneTouchEvent + " 3 53 " + x;
		events[2] = "sendevent " + miOneTouchEvent + " 3 54 " + y;
		events[3] = "sendevent " + miOneTouchEvent + " 3 58 46 ";
		events[4] = "sendevent " + miOneTouchEvent + " 3 50 4";
		events[5] = "sendevent " + miOneTouchEvent + " 0 2 0";
		events[6] = "sendevent " + miOneTouchEvent + " 0 0 0";
		events[7] = "sendevent " + miOneTouchEvent + " 0 2 0";
		events[8] = "sendevent " + miOneTouchEvent + " 0 0 0";
		return events;
	}
	
	/**
	 * 华为荣耀拖动步骤
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static String[] getHuaweiHonorDragEvents(int x1, int y1, int x2, int y2) {
		String[] events = new String[14];
		events[0] = "sendevent " + huaweiHonorEvent + " 1 330 1";
		events[1] = "sendevent " + huaweiHonorEvent + " 3 57 59";
		// 第一点
		events[2] = "sendevent " + huaweiHonorEvent + " 3 53 " + x1;
		events[3] = "sendevent " + huaweiHonorEvent + " 3 54 " + y1;
		events[4] = "sendevent " + huaweiHonorEvent + " 3 58 58";
		events[5] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		// 延时
		events[6] = "/sleep 5";
		// 第二点
		events[7] = "sendevent " + huaweiHonorEvent + " 3 53 " + x2;
		events[8] = "sendevent " + huaweiHonorEvent + " 3 54 " + y2;
		events[9] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		// 确认
		events[10] = "sendevent " + huaweiHonorEvent + " 3 57 -1"; //ffffffff
		events[11] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		events[12] = "sendevent " + huaweiHonorEvent + " 1 330 0";
		events[13] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		return events;
	}
	
	/**
	 * 华为荣耀单击步骤
	 * @param x
	 * @param y
	 * @return
	 */
	public static String[] getHuaweiHonorClickEvents(int x, int y) {
		String[] events = new String[10];
		events[0] = "sendevent " + huaweiHonorEvent + " 1 330 1";
		events[1] = "sendevent " + huaweiHonorEvent + " 3 57 58";
		events[2] = "sendevent " + huaweiHonorEvent + " 3 53 " + x;
		events[3] = "sendevent " + huaweiHonorEvent + " 3 54 " + y;
		events[4] = "sendevent " + huaweiHonorEvent + " 3 58 58";
		events[5] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		events[6] = "sendevent " + huaweiHonorEvent + " 3 57 " + Integer.MIN_VALUE; //ffffffff
		events[7] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		events[8] = "sendevent " + huaweiHonorEvent + " 1 330 0";
		events[9] = "sendevent " + huaweiHonorEvent + " 0 0 0";
		System.out.println("log huawei honor click");
		return events;
	}
	
	
	
}
