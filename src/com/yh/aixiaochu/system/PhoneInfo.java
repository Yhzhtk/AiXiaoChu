package com.yh.aixiaochu.system;

public class PhoneInfo {
	
	public static PHONE usePhone;
	
	public enum PHONE{miOne, huaweiHonor};
	
	/**
	 * 初始化机型
	 * 
	 * @param phone
	 */
	public static void init(PHONE phone){
		// 设置当前的机型
		usePhone = phone;
		
		EventGetter.miOneTouchEvent = "/dev/input/event1";;
		EventGetter.huaweiHonorEvent = "/dev/input/event3";
		switch (usePhone) {
		case miOne:
			EventGetter.needWriteEventFiles = new String[] { EventGetter.miOneTouchEvent };
			break;
		case huaweiHonor:
			EventGetter.needWriteEventFiles = new String[] { EventGetter.huaweiHonorEvent };
			break;
		}
	}
}
