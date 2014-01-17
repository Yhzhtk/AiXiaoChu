package com.yh.aixiaochu.system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import android.util.Log;

/**
 * 重写sendevent.c，已完善，字节流与数组顺序相反
 * 
 * @author gudh
 * @data 2014-01-16
 */
public class Sendevent {

	// 使用nid获取字节流
	private static ByteBuffer buffer4 = ByteBuffer.allocate(4);
	private static ByteBuffer buffer2 = ByteBuffer.allocate(2);
	// 存储所有文件
	private static HashMap<String, OutputStream> fileOut = new HashMap<String, OutputStream>(
			10);

	static long start, end;

	/**
	 * event 处理事件
	 * 
	 * @param events
	 */
	public static boolean onEvent(String[] events) {
		start = System.currentTimeMillis();
		try {
			for (String event : events) {
				String[] infos = event.split(" +");
				if (infos[0].equals("sleep") && infos.length == 2) {
					Log.d("SendEvent", event);
					// sleep 则延时
					Long t = Long.parseLong(infos[1]);
					Thread.sleep(t);
					continue;
				} else if (infos[0].equals("sendevent") && infos.length == 5) {
					Log.d("SendEvent", event);
					// 发送事件
					byte[] bytes = getSendArgs(Short.parseShort(infos[2]),
							Short.parseShort(infos[3]),
							Integer.parseInt(infos[4]));
					OutputStream out = getOutputStream(infos[1]);
					out.write(bytes);
					out.flush();
				} else {
					Log.d("SendEvent", event + " NOT RIGHT");
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			end = System.currentTimeMillis();
			Log.i("SendEvent", "time cost:" + (end - start));
		}
		return false;
	}

	/**
	 * 循环发送单击命令测试
	 */
	public static void testClickTop() {
		// run test
		String[] events = new String[9];
		events[0] = "sendevent /dev/input/event1 3 57 0";
		events[1] = "sendevent /dev/input/event1 3 53 " + 300;
		events[2] = "sendevent /dev/input/event1 3 54 " + 4;
		events[3] = "sendevent /dev/input/event1 3 58 46 ";
		events[4] = "sendevent /dev/input/event1 3 50 4";
		events[5] = "sendevent /dev/input/event1 0 2 0";
		events[6] = "sendevent /dev/input/event1 0 0 0";
		events[7] = "sendevent /dev/input/event1 0 2 0";
		events[8] = "sendevent /dev/input/event1 0 0 0";

		onEvent(events);
	}

	/**
	 * 获取发送到命令，结构体的对照
	 * 
	 * @param type
	 * @param code
	 * @param value
	 * @return
	 */
	private static byte[] getSendArgs(short type, short code, int value) {
		byte[] bytes = new byte[16];

		// time
		setTime(bytes);

		// type
		byte[] tbytes = buffer2.putShort(0, type).array();
		bytes[8] = tbytes[1];
		bytes[9] = tbytes[0];

		// code
		byte[] cbytes = buffer2.putShort(0, code).array();
		bytes[10] = cbytes[1];
		bytes[11] = cbytes[0];
		// value

		byte[] vbytes = buffer4.putInt(0, value).array();
		bytes[12] = vbytes[3];
		bytes[13] = vbytes[2];
		bytes[14] = vbytes[1];
		bytes[15] = vbytes[0];

		// for (byte b : bytes) {
		// System.out.print(b + " ");
		// }
		// System.out.println();
		return bytes;
	}

	/**
	 * 设置时间，根据c语言的struct而来
	 * 
	 * @param bytes
	 */
	private static void setTime(byte[] bytes) {
		long seconds = System.currentTimeMillis() / 1000;
		int sec = (int) seconds;
		int microseconds = (int) (System.nanoTime() % 1000000000);
		int mic = microseconds / 1000;

		byte[] sbytes = buffer4.putInt(0, sec).array();
		for (int i = 0; i < 4; i++) {
			bytes[i] = sbytes[i];
		}

		byte[] mbytes = buffer4.putInt(0, mic).array();
		for (int i = 0; i < 4; i++) {
			bytes[i + 4] = mbytes[i];
		}
	}

	/**
	 * 获取文件流
	 * 
	 * @param fileName
	 * @return
	 */
	private static OutputStream getOutputStream(String fileName) {
		OutputStream stream = fileOut.get(fileName);
		if (stream == null) {
			try {
				stream = new FileOutputStream(fileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			fileOut.put(fileName, stream);
		}
		return stream;
	}

	/**
	 * 关闭所有流
	 */
	public static void closeAllStream() {
		for (String key : fileOut.keySet()) {
			try {
				fileOut.get(key).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
