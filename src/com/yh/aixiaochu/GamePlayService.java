package com.yh.aixiaochu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 玩游戏的Service
 * @author gudh
 *
 */
public class GamePlayService extends Service {
	
	public static Play play = null;
	
	public GamePlayService() {
		Log.i("PlayGame", "PlayGame()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("PlayGame", "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Log.i("PlayGame", "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.i("PlayGame", "onStart");
		//Sendevent.TestClick();
		play = new Play();
		play.stop_flag = false;
		play.start();
		GameUtil.reset();
	}
	
	@Override
	public void onDestroy() {
		Log.i("PlayGame", "onDestroy");
		if(play != null){
			play.stop_flag = true;
		}
	}
	
	/**
	 * 玩游戏的现场
	 * @author gudh
	 * 通过线程来完成循环截图模拟操作
	 */
	class Play extends Thread{
		
		// 是否停止的标志
		public boolean stop_flag = false;
		
		@Override
		public void run() {
			int res = 0;
			long start;
			long end;
			
			while(!stop_flag){
				start = System.currentTimeMillis();
				try{
					// 运行一次识别模拟，返回结果
					res = GameUtil.run_time();
				}catch(Exception e){
					e.printStackTrace();
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
				}else if(res <= -60){
					// 连续不在游戏60次，关闭
					stop_flag = true;
					Log.i("PlayGame", "not game over 10, over game");
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
}
