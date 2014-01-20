package com.yh.aixiaochu.alg;

import android.graphics.Bitmap;
import android.util.Log;


/**
 * 识别原始图片算法
 * @author gudh
 *
 */
public class xiaochu_py {

	public static GameParaBean para = new GameParaBean();

	public static int[] get_pos(int i, int j) {
		// //'获取块内判断的点//'
		int[] xy = new int[2];
		xy[0] = para.start_pos[0] + i * para.block_size[0] + para.rel_pos[0];
		xy[1] = para.start_pos[1] + j * para.block_size[1] + para.rel_pos[1];
		return xy;
	}

	public static int[] get_rc_pos(int[] rc) {
		// '获取rc的点，注意横纵是反的//'
		int[] xy = new int[2];
		xy[0] = para.start_pos[0] + rc[1] * para.block_size[0] + para.rel_pos[0];
		xy[1] = para.start_pos[1] + rc[0] * para.block_size[1] + para.rel_pos[1];
		return xy;
	}

	public static int[] get_block(int i, int j) {
		// '获取块的区域//'
		int[] xywh = new int[4];
		xywh[0] = para.start_pos[0] + i * para.block_size[0];
		xywh[1] = para.start_pos[1] + j * para.block_size[1];
		xywh[2] = xywh[0] + para.block_size[0];
		xywh[3] = xywh[1] + para.block_size[1];
		return xywh;
	}

	public static boolean similar_color(int[] p, int[] color) {
		// '判断是否是相似//'
		// print p, color
		for (int i = 0; i < 3; i++) {
			if (Math.abs(p[i] - color[i]) >= para.ax[i]) {
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
			if (similar_color(color, para.colors[index])) {
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
			log("Blank", "blank is " +  blank_c + ", return None");
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
					sb.append(para.colornames[n] + " ");
				}
			}
			sb.append("\n");
		}
		log("MAT", sb.toString());
	}
	
	static void log(String t, String c){
		System.out.println(t + " " + c);
	}
	
	
	private static boolean isSetPara = false;
	/**
	 * 计算屏幕尺寸参数
	 * 
	 * @param bitmap
	 */
	public static boolean setGamePara(Bitmap bitmap) {
		if(!isSetPara){
			GameParaBean tmp = GetParaUtil.getGameParaBean(bitmap);
			if(tmp != null){
				para = tmp;
				para.print();
				Log.i("SetGamePara", "success");
				isSetPara = true;
			}else{
				Log.i("SetGamePara", "failure");
			}
		}
		return isSetPara;
		// get_pic_info(img);
	}
}
