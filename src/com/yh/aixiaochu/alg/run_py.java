package com.yh.aixiaochu.alg;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 算法调用模块
 * @author gudh
 *
 */
public class run_py {
	
	/**
	 * 根据截图数据，获取下一步应该如何消除的步骤
	 * @param img
	 * @return
	 */
	public static List<int[][]> get_step(Bitmap img) {
		// 设置参数
		if(!xiaochu_py.setGamePara(img)){
			Log.i("Para", "para is not set ok");
			return null;
		}
		
		long start = System.currentTimeMillis();
		int[][] mat = xiaochu_py.get_pic_info(img);
		if (mat == null) {
			return null;
		}
		
		List<ResBean> res = xcalg_py.calculate_step(mat);
		if(res == null){
			return null;
		}
		
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
