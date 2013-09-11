package com.yh.aixiaochu.alg;

import android.graphics.Bitmap;
import android.util.Log;

public class xiaochu_py {

	static int[] start_pos = { 5, 222 }; // 开始的位置
	static int[] block_size = { 67, 67 }; // 块大小
	static int[] rel_pos = { 33, 28 }; // 相对块头位置
	static int[][] colors = { { 255, 255, 255 }, // 白
			{ 164, 130, 213 }, // 紫
			{ 247, 214, 82 }, // 黄
			{ 244, 160, 90 }, // 土
			{ 90, 186, 238 }, // 蓝
			{ 247, 69, 95 }, // 红
			{ 173, 235, 82 } // 绿
	};
	static String[] colornames = { "ba", "zh", "hu", "tu", "la", "ho", "lv" };
	static int[] ax = { 35, 35, 35 }; // 允许的误差

	public static int[] get_pos(int i, int j) {
		// //'获取块内判断的点//'
		int[] xy = new int[2];
		xy[0] = start_pos[0] + i * block_size[0] + rel_pos[0];
		xy[1] = start_pos[1] + j * block_size[1] + rel_pos[1];
		return xy;
	}

	public static int[] get_rc_pos(int[] rc) {
		// '获取rc的点，注意横纵是反的//'
		int[] xy = new int[2];
		xy[0] = start_pos[0] + rc[1] * block_size[0] + rel_pos[0];
		xy[1] = start_pos[1] + rc[0] * block_size[1] + rel_pos[1];
		return xy;
	}

	public static int[] get_block(int i, int j) {
		// '获取块的区域//'
		int[] xywh = new int[4];
		xywh[0] = start_pos[0] + i * block_size[0];
		xywh[1] = start_pos[1] + j * block_size[1];
		xywh[2] = xywh[0] + block_size[0];
		xywh[3] = xywh[1] + block_size[1];
		return xywh;
	}

	public static boolean similar_color(int[] p, int[] color) {
		// '判断是否是相似//'
		// print p, color
		for (int i = 0; i < 3; i++) {
			if (Math.abs(p[i] - color[i]) >= ax[i]) {
				return false;
			}
		}
		return true;
	}

	public static int get_color(Bitmap img, int i, int j) {
		// //'获取像素点的颜色//'
		int[] p = get_pos(i, j);

		int rgb = img.getPixel(p[0], p[1]);
		int[] color = new int[3];
		color[0] = (rgb >> 16) & 0xFF;
		color[1] = (rgb >> 8) & 0xFF;
		color[2] = rgb & 0xFF;

		for (int index = 0; index < 7; index++) {
			if (similar_color(color, colors[index])) {
				return index;
			}
		}
		return -1;
	}

	public static int[][] get_pic_info(Bitmap img) {
		// //'获取像素矩阵//'
		int[][] mat = new int[7][7];
		int blank_c = 0;
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 7; i++) {
				int c = get_color(img, i, j);
				mat[j][i] = c;
				if (c == -1) {
					blank_c += 1;
				}
			}
		}
		print_mat(mat);
		if (blank_c > 7) {
			Log.i("Blank", "blank is " +  blank_c + ", return None");
			mat = null;
		}
		return mat;
	}

	public static void print_mat(int[][] mat) {
		// '输出结果矩阵//'
		StringBuffer sb = new StringBuffer();
		sb.append(". | 0  1  2  3  4  5  6\n");
		int i = 0;
		for (int[] m : mat) {
			sb.append(i + " | ");
			i += 1;
			for (int n : m) {
				if (n < 0) {
					sb.append("No ");
				} else {
					sb.append(colornames[n] + " ");
				}
			}
			sb.append("\n");
		}
		Log.i("MAT", sb.toString());
	}
}
