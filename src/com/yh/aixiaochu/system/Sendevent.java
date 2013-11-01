package com.yh.aixiaochu.system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * 重写sendevent.c，未完善，字节流还不正确，需要改进
 * @author gudh
 * 
 */
public class Sendevent {

	// 使用nid获取字节流
	private static ByteBuffer buffer4 = ByteBuffer.allocate(4);
	private static ByteBuffer buffer2 = ByteBuffer.allocate(2);

	/**
	 * 根据命令行参数获取字节数组
	 * @param arg
	 * @return
	 */
	public static byte[] getSendArgs(String arg) {
		String[] infos = arg.split(" +");
		System.out.println(arg);
		return getSendArgs(Short.parseShort(infos[2]),
				Short.parseShort(infos[3]), Integer.parseInt(infos[4]));
	}

	/**
	 * 获取发送到命令，结构体的对照
	 * @param type
	 * @param code
	 * @param value
	 * @return
	 */
	public static byte[] getSendArgs(short type, short code, int value) {
		byte[] bytes = new byte[16];

		// time
		setTime(bytes);

		// type
		byte[] tbytes = buffer2.putShort(0, type).array();
		bytes[8] = tbytes[0];
		bytes[9] = tbytes[1];

		// code
		byte[] cbytes = buffer2.putShort(0, code).array();
		bytes[10] = cbytes[0];
		bytes[11] = cbytes[1];
		// value

		byte[] vbytes = buffer4.putInt(0, value).array();
		bytes[12] = vbytes[0];
		bytes[13] = vbytes[1];
		bytes[14] = vbytes[2];
		bytes[15] = vbytes[3];

		for (byte b : bytes) {
			System.out.print(b + " ");
		}
		System.out.println();
		return bytes;
	}

	/**
	 * 设置时间，根据c语言的struct而来
	 * 
	 * @param bytes
	 */
	public static void setTime(byte[] bytes) {
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
	 * 循环发送单击命令测试
	 */
	public static void TestClick() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			// run test
			long start = System.currentTimeMillis();
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
			
			try {
				RandomAccessFile out = new RandomAccessFile("/dev/input/event1", "rw");
				for (String event : events) {
					byte[] bytes = getSendArgs(event);
					//out.seek(0);
					out.write(bytes);
					
				}
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			long end = System.currentTimeMillis();
			System.out.println("use time:" + (end - start));
		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
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

		try {
			OutputStream out = new FileOutputStream("D:/a");
			for (String event : events) {
				byte[] bytes = getSendArgs(event);
				out.write(bytes);
				out.flush();
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("use time:" + (end - start));
	}
}
