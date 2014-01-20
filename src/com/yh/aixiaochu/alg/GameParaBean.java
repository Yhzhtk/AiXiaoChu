package com.yh.aixiaochu.alg;

import android.util.Log;

/**
 * 游戏区域参数
 * 
 * @author gudh
 * @date 2014-1-20
 */
public class GameParaBean {
	
	int[] start_pos = { 5, 222 }; // 开始的位置
	int[] block_size = { 67, 67 }; // 块大小
	int[] rel_pos = { 33, 28 }; // 相对块头位置
	int[][] colors = { { 255, 255, 255 }, // 白
			{ 164, 130, 213 }, // 紫
			{ 247, 214, 82 }, // 黄
			{ 244, 160, 90 }, // 土
			{ 90, 186, 238 }, // 蓝
			{ 247, 69, 95 }, // 红
			{ 173, 235, 82 } // 绿
	};
	
	String[] colornames = { "ba", "zh", "hu", "tu", "la", "ho", "lv" };
	int[] ax = { 35, 35, 35 }; // 允许的误差
	
	public void print(){
		Log.i("GamePara", "==========start_pos:" + start_pos[0] + " " + start_pos[1]);
		Log.i("GamePara", "==========block_size:" + block_size[0] + " " + block_size[1]);
		Log.i("GamePara", "==========rel_pos:" + rel_pos[0] + " " + rel_pos[1]);
	}
}
