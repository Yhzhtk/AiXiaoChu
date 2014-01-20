package com.yh.aixiaochu;

import android.util.Log;

/**
 * 玩游戏的现场
 * @author gudh
 * 通过线程来完成循环截图模拟操作
 */
public class GamePlay extends Thread{
	
	// 是否停止的标志
	public boolean stop_flag = false;
	
	public void stopPlay(){
		stop_flag = true;
	}
	
	@Override
	public void run() {
		int res = 0;
		long start;
		long end;
		
		int zeroTimes = 0;
		
		while(!stop_flag){
			start = System.currentTimeMillis();
			try{
				// 运行一次识别模拟，返回结果
				res = GameUtil.run_time();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(res == -1){
				zeroTimes++;
			} else{
				zeroTimes = 0;
			}
			
			if(zeroTimes >= 30){
				// 连续不在游戏30次，关闭
				stop_flag = true;
				Log.i("PlayGame", "not game over 30, over game");
			}
			
			// 对返回的结果进行不同的延时处理
			if(res == 1){
				Log.i("PalyGame", "run ok, sleep 500 ms");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else if(res == 0){
				Log.i("PalyGame", "run false, sleep 5 ms");
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else if(res <= -30){
				// 连续不在游戏30次，关闭
				stop_flag = true;
				Log.i("PlayGame", "not game over 30, over game");
			}else{
				Log.i("PalyGame", "not in game, sleep 1000 ms, now is " + res);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			end = System.currentTimeMillis();
			Log.i("Time", "One Time Use:" + (end - start));
		}
	}
}