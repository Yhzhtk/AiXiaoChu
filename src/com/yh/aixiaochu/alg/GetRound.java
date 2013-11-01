package com.yh.aixiaochu.alg;

import java.util.ArrayList;
import java.util.List;

/**
 * 一轮一轮的操作
 * @author gudh
 *
 */
public class GetRound {
	
	public static int get_color(int[][] mat, int[] pos) {
		// '获取颜色信息//'
		if (pos[0] < 0 || pos[1] < 0) {
			return -1;
		}
		if (pos[0] >= mat.length || pos[1] >= mat[0].length) {
			return -1;
		}
		return mat[pos[0]][pos[1]];
	}

	public static List<ResBean> get_avail(int[][] mat, List<ResBean> arounds,
			int[] compare_pos) {
		// '获取可用的移动信息//'
		List<ResBean> res = new ArrayList<ResBean>();
		int color = get_color(mat, compare_pos);
		for (ResBean roun : arounds) {
			int c1 = get_color(mat, roun.end);
			if ((c1 != -1) && (c1 == color)) {
				res.add(roun);
			}
		}
		return res;
	}
	
	public static List<ResBean> get_middle(int[] pos1, int[] pos2, int[][] mat) {
		// '''获取两个相隔的情况下，可能的第三个的移动信息'''
		int[] p1 = pos1;
		int[] p2 = pos2;
		ResBean bean = null;
		List<ResBean> arounds = new ArrayList<ResBean>();
		if (pos1[1] == pos2[1]) { // 竖排
			// p1是小的那个
			if (p1[0] > p2[0]) {
				p1 = pos2;
				p2 = pos1;
			}
			bean = new ResBean(p1[0] + 1, p1[1], p1[0] + 1, p1[1] - 1);
			bean.addZhanyong(p2[0], p1[1]);
			bean.addZhanyong(p1[0] + 1, p1[1] - 1);
			arounds.add(bean);

			bean = new ResBean(p1[0] + 1, p1[1], p1[0] + 1, p1[1] + 1);
			bean.addZhanyong(p2[0], p1[1]);
			bean.addZhanyong(p1[0] + 1, p1[1] + 1);
			arounds.add(bean);
		} else if (pos1[0] == pos2[0]) { // 横排
			if (p1[1] > p2[1]) {
				p1 = pos2;
				p2 = pos1;
			}
			bean = new ResBean(p1[0], p1[1] + 1, p1[0] - 1, p1[1] + 1);
			bean.addZhanyong(p1[0], p1[1]);
			bean.addZhanyong(p1[0], p1[1] + 1);
			bean.addZhanyong(p2[0], p2[1]);
			arounds.add(bean);

			bean = new ResBean(p1[0], p1[1] + 1, p1[0] + 1, p1[1] + 1);
			bean.addZhanyong(p1[0], p1[1]);
			bean.addZhanyong(p1[0] + 1, p1[1] + 1);
			bean.addZhanyong(p2[0], p2[1]);
			arounds.add(bean);
		}
		return get_avail(mat, arounds, pos1);
	}

	public static List<ResBean> get_arround(int[] pos1, int[] pos2, int[][] mat) {
		// '''获取两个相连的情况下，可能的第三个的移动信息返回结果为(移动位置1， 移动位置2， 受影响的行)'''
		int[] p1 = pos1;
		int[] p2 = pos2;
		ResBean bean = null;
		List<ResBean> arounds = new ArrayList<ResBean>();
		int[] size = new int[2];
		size[0] = mat.length;
		size[1] = mat[0].length;
		if (pos1[1] == pos2[1]) { // 竖排
			// p1是小的那个
			if (p1[0] > p2[0]) {
				p1 = pos2;
				p2 = pos1;
			}
			// 上边
			if (p1[0] > 0) {
				bean = new ResBean(p1[0] - 1, p1[1], p1[0] - 1, p1[1] - 1);
				bean.addZhanyong(p2[0], p1[1]);
				bean.addZhanyong(p1[0] - 1, p1[1] - 1);
				arounds.add(bean);

				bean = new ResBean(p1[0] - 1, p1[1], p1[0] - 1, p1[1] + 1);
				bean.addZhanyong(p2[0], p1[1]);
				bean.addZhanyong(p1[0] - 1, p1[1] + 1);
				arounds.add(bean);

				bean = new ResBean(p1[0] - 1, p1[1], p1[0] - 2, p1[1]);
				bean.addZhanyong(p2[0], p1[1]);
				arounds.add(bean);
			}
			// 下边
			if (p2[0] < size[0] - 1) {
				bean = new ResBean(p2[0] + 1, p2[1], p2[0] + 1, p2[1] - 1);
				bean.addZhanyong(p2[0] + 1, p1[1]);
				bean.addZhanyong(p2[0] + 1, p1[1] - 1);
				arounds.add(bean);

				bean = new ResBean(p2[0] + 1, p2[1], p2[0] + 1, p2[1] + 1);
				bean.addZhanyong(p2[0] + 1, p1[1]);
				bean.addZhanyong(p2[0] + 1, p1[1] + 1);
				arounds.add(bean);

				bean = new ResBean(p2[0] + 1, p2[1], p2[0] + 2, p2[1]);
				bean.addZhanyong(p2[0] + 2, p1[1]);
				arounds.add(bean);

			}
		} else if (pos1[0] == pos2[0]) { // 横排
			if (p1[1] > p2[1]) {
				p1 = pos2;
				p2 = pos1;
			}
			// 左边
			if (p1[1] > 0) {
				bean = new ResBean(p1[0], p1[1] - 1, p1[0] - 1, p1[1] - 1);
				bean.addZhanyong(p1[0], p1[1] - 1);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				arounds.add(bean);

				bean = new ResBean(p1[0], p1[1] - 1, p1[0] + 1, p1[1] - 1);
				bean.addZhanyong(p1[0] + 1, p1[1] - 1);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				arounds.add(bean);

				bean = new ResBean(p1[0], p1[1] - 1, p1[0], p1[1] - 2);
				bean.addZhanyong(p1[0], p1[1] - 2);
				bean.addZhanyong(p1[0], p1[1] - 1);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				arounds.add(bean);
			}
			// 右边
			if (p2[1] < size[1] - 1) {
				bean = new ResBean(p2[0], p2[1] + 1, p2[0] - 1, p2[1] + 1);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				bean.addZhanyong(p1[0], p2[1] + 1);
				arounds.add(bean);

				bean = new ResBean(p2[0], p2[1] + 1, p2[0] + 1, p2[1] + 1);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				bean.addZhanyong(p1[0] + 1, p2[1] + 1);
				arounds.add(bean);

				bean = new ResBean(p2[0], p2[1] + 1, p2[0], p2[1] + 2);
				bean.addZhanyong(p1[0], p1[1]);
				bean.addZhanyong(p1[0], p2[1]);
				bean.addZhanyong(p1[0], p2[1] + 1);
				bean.addZhanyong(p1[0], p2[1] + 2);
				arounds.add(bean);

			}
		}
		return get_avail(mat, arounds, pos1);
	}
}
