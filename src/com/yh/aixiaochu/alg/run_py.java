package com.yh.aixiaochu.alg;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class run_py {
	
	public static List<int[][]> get_step(Bitmap img) {
		
		long start = System.currentTimeMillis();
		int[][] mat = xiaochu_py.get_pic_info(img);
		if (mat == null) {
			return null;
		}
		
		List<ResBean> res = xcalg_py.calculate_step(mat);
		for (ResBean r : res) {
			r.print();
		}
		Log.i("S", "---------");
		res = xcalg_py.get_optimal(res);
		List<int[][]> pos = new ArrayList<int[][]>();
		for (ResBean r : res) {
			r.print();
			// 坐标和序号是反的
			int[] m = xiaochu_py.get_rc_pos(r.start);
			int[] n = xiaochu_py.get_rc_pos(r.end);
			Log.i("Res", new StringBuffer().append(m[0]).append(" ").append(m[1]).append("    ").append(m[0]).append(" ").append(m[1]).toString());
			pos.add(new int[][] { m, n });
		}
		
		long end = System.currentTimeMillis();
		Log.i("Time", "GetStep Time: " + (end - start));
		return pos;
	}
}
