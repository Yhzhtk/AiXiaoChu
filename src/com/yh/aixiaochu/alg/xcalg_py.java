package com.yh.aixiaochu.alg;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消除算法，计算步骤
 * @author gudh
 *
 */
public class xcalg_py {

	public static List<ResBean> calculate_step(int[][] mat) {
		// '计算下一步的所有可行情况//'
		int ws = mat.length;
		int hs = mat[0].length;

		List<ResBean> res = new ArrayList<ResBean>();
		// 从下往上，从右往左
		for (int j = 0; j < hs; j++) {
			int[] last = { -1, -1 };
			int[] last2 = { -1, -1 };
			for (int i = 0; i < ws; i++) {
				if (last[0] != -1 && last[1] != -1) {
					int m1 = mat[last[0]][last[1]];
					int m2 = mat[i][j];
					if (m1 == m2) {
						// 两边
						List<ResBean> a = GetRound.get_arround( new int[] { i, j }, last, mat);
						res.addAll(a);
					} else if (last2[0] != -1 && last2[1] != -1) {
						// 中间
						int m0 = mat[last2[0]][last2[1]];
						if (m0 == m2) {
							List<ResBean> a = GetRound.get_middle( new int[] { i, j }, last2,mat);
							res.addAll(a);
						}
					}
					last2 = last;
					last = new int[] { i, j };
				} else {
					last2 = last;
					last = new int[] { i, j };
				}
			}
		}
		// 从右往左，从下往上
		for (int i = 0; i < ws; i++) {
			int[] last = { -1, -1 };
			int[] last2 = { -1, -1 };
			for (int j = 0; j < hs; j++) {
				if (last[0] != -1 && last[1] != -1) {
					int m1 = mat[last[0]][last[1]];
					int m2 = mat[i][j];
					if (m1 == m2) {
						// 两边
						List<ResBean> a = GetRound.get_arround( new int[] { i, j }, last, mat);
						res.addAll(a);
					} else if (last2[0] != -1 && last2[1] != -1) {
						// 中间
						int m0 = mat[last2[0]][last2[1]];
						if (m0 == m2) {
							List<ResBean> a = GetRound.get_middle( new int[] { i, j }, last2,mat);
							res.addAll(a);
						}
					}
					last2 = last;
					last = new int[] { i, j };
				} else {
					last2 = last;
					last = new int[] { i, j };
				}
			}
		}
		if (res.size() > 20) {
			Log.i("Result", "PIC is ! ok, result count is bigger return no result "
							+ res.size());
			res = null;
		}
		return res;
	}

	public static boolean is_down_ok(HashMap<Integer, Integer> ys,
			ResBean needlocs) {
		// '是否在下面，如果在下面可以多个运行//'
		for (int[] loc : needlocs.zhanyong) {
			if (ys.containsKey(loc[1]) && ys.get(loc[1]) >= loc[0]) {
				return false;
			}
		}
		return true;
	}

	public static void add_ys(HashMap<Integer, Integer> ys, ResBean needlocs) {
		// '添加合并,将相同y更大的x存入//'
		for (int[] loc : needlocs.zhanyong) {
			if (ys.containsKey(loc[1])) {
				if (ys.get(loc[1]) < loc[0]) {
					ys.put(loc[1], loc[0]);
				}
			} else {
				ys.put(loc[1], loc[0]);
			}
		}
	}

	@SuppressLint("UseSparseArrays")
	public static List<ResBean> get_optimal(List<ResBean> avails) {
		// '获取一张图最优的步骤集，有顺序的//'
		List<ResBean> res = new ArrayList<ResBean>();
		HashMap<Integer, Integer> ys = new HashMap<Integer, Integer>();
		// 先添加同含多个的
		// for (ResBean ava : avails){
		// if (avails.(ava) > 1){
		// if (!is_down_ok(ys, ava[2])){
		// continue
		// res.append(ava[0 : 2])
		// add_ys(ys, ava[2])
		// }
		// }
		for (ResBean ava : avails) {
			// 如果x小于范围，则跳过
			if (!is_down_ok(ys, ava)) {
				continue;
			}
			res.add(ava);
			add_ys(ys, ava);
		}
		return res;
	}

}
