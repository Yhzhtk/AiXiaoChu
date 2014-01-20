package com.yh.aixiaochu.alg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 获取不同机器的适配参数
 * 
 * @author gudh
 * @date 2014-1-20
 */
public class GetParaUtil {

	static int width = 480;
	static int height = 853;
	
	static int startWidth = 0;
	static int startHeight = height / 5;
	
	static int blockCount = 7;
	
	private static float SIMILAR = 15;
	private static final float MAXYRADIO = 0.5f;
	private static final float MAXXRADIO = 0.7f;
	
	/**
	 * 从一张图片获取参数
	 * 
	 * @param img
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public static GameParaBean getGameParaBean(Bitmap img) {
		saveBitmap(img, "/sdcard/sc.png");
		width = img.getWidth();
		height = img.getHeight();
		startHeight = height / 5;
		
		return analyRect1(img);
	}
	
	/**
	 * 仅图像分析startY, startX靠计算
	 * 
	 * @param img
	 * @param para
	 * @return 
	 */
	public static GameParaBean analyRect1(Bitmap img) {
		int startY = 222;
		if(img.getHeight() == 854){
			startY = 222;
		} else if(img.getHeight() == 1280){
			startY = 333;
		}
		
		if(startY >= height / 2){
			Log.i("SetGamePara", "startY not right:" + startY);
			return null;
		}
		int yu = width % blockCount + blockCount;
		int startX = yu / 2;
		int blockSize = (width - yu) / blockCount;
		int rx = (int) ((float)33 / 67 * blockSize);
		int ry = (int) ((float)28 / 67 * blockSize);
		
		GameParaBean para = new GameParaBean();
		para.start_pos = new int[]{startX, 222};
		para.block_size = new int[]{blockSize, blockSize};
		para.rel_pos = new int[]{rx, ry};
		
		return para;
	}

	/**
	 * 全部分析方法获取游戏区域
	 * 
	 * @param img
	 * @param para
	 */
	public static void analyRect(Bitmap img, GameParaBean para) {
		int startY = analyStartY(img);
		int startX = analyStartX(img, 1, startY - 5, startY + 5);
		int blockX = analyStartX(img, startX + 1, startY - 5, startY + 5);
		System.out.println("StartY" + startY);
		System.out.println("StartX" + startX);
		System.out.println("BlockX" + blockX);
			
	}
	
	/**
	 * 分析开始的Y位置
	 * @param img
	 * @return
	 */
	private static int analyStartY(Bitmap img){
		int y = startHeight;
		out:
		for (y = startHeight; y < height - 1; y ++) {
			int similarTimes = 0; // 连续相似次数
			for (int x = startWidth; x < width; x ++) {
				int rgb1 = getRGBAdd(img, x, y);
				int rgb2 = getRGBAdd(img, x, y + 1);
				if(isSimilar(rgb1, rgb2)){
					similarTimes++;
				}
				if(x > 0 && x % 100 == 0){
					float sRadio = similarTimes / (float)(x - startWidth);
					if(sRadio > MAXYRADIO){
						continue out;
					}
				}
			}
			float sRadio = similarTimes / (float)(width - startWidth);
			if(sRadio <= MAXYRADIO){
				break;
			}
		}
		return y;
	}
	
	/**
	 * 分析开始的X位置，还有问题
	 * @param img
	 * @param xMin
	 * @param yMin
	 * @param yMax
	 * @return
	 */
	private static int analyStartX(Bitmap img, int xMin, int yMin, int yMax){
		int x = xMin;
		for (x = xMin; x < width - 1; x ++) {
			int similarTimes = 0; // 连续相似次数
			for (int y = yMin; y < yMax; y++) {
				int rgb1 = getRGBAdd(img, x, y);
				int rgb2 = getRGBAdd(img, x + 1, y);
				if(isSimilar(rgb1, rgb2)){
					similarTimes++;
				}
			}
			float sRadio = similarTimes / (float)(yMax - yMin);
			if(sRadio <= MAXXRADIO){
				break;
			}
		}
		return x;
	}
	
	/**
	 * 判断两个RGB和是否一致
	 * 
	 * @param rgb1
	 * @param rgb2
	 * @param num
	 * @return
	 */
	private static boolean isSimilar(int rgb1, int rgb2){
		boolean res = Math.abs(rgb2 - rgb1) < SIMILAR; 
		return res;
	}
	
	/**
	 * 获取像素点x,y的rgb值相加
	 * @param img
	 * @param x
	 * @param y
	 * @return
	 */
	private static int getRGBAdd(Bitmap img, int x, int y){
		int value = 0;
		int rgb = img.getPixel(x, y);
		value += (rgb >> 16) & 0xFF;
		value += (rgb >> 8) & 0xFF;
		value += rgb & 0xFF;
		return value;
	}
	
	private static void saveBitmap(Bitmap bitmap, String fileName) {
		File myCaptureFile = new File(fileName);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
