package com.yh.aixiaochu.alg;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ResBean {
	
	int[] start;
	int[] end;
	List<int[]> zhanyong;
	
	public ResBean(int[] start, int[] end, List<int[]> zhanyong) {
		this.start = start;
		this.end = end;
		this.zhanyong = zhanyong;
	}
	
	public ResBean(int startx, int starty, int endx, int endy) {
		this.start = new int[2];
		this.start[0] = startx;
		this.start[1] = starty;
		this.end = new int[2];
		this.end[0] = endx;
		this.end[1] = endy;
		this.zhanyong = new ArrayList<int[]>();
	}
	
	public void addZhanyong(int x, int y){
		int[] m = new int[2];
		m[0] = x;
		m[1] = y;
		this.zhanyong.add(m);
	}
	
	public void print(){
		Log.i("ResBean", new StringBuffer().append(start[0]).append(" ").append(this.start[1]).append("    ").append(this.end[0]).append(" ").append(this.end[1]).toString());
	}
	
	public static boolean equals(ResBean b1, ResBean b2){
		if(b1.start == b2.start){
			if(b1.end == b2.end){
				return true;
			}
		}
		return false;
	}
}
