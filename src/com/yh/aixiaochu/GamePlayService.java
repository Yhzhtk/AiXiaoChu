package com.yh.aixiaochu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
		play = new Play();
		play.stop_flag = false;
		play.start();
		GameUtil.reset();
	}
	
	@Override
	public void onDestroy() {
		Log.i("PlayGame", "onDestroy");
		play.stop_flag = true;
	}
	
	class Play extends Thread{
		
		public boolean stop_flag = false;
		
		
		@Override
		public void run() {
			int res = 0;
			long start;
			long end;
			while(!stop_flag){
				start = System.currentTimeMillis();
				try{
					res = GameUtil.run_time();
				}catch(Exception e){
					e.printStackTrace();
				}
				if(res == 1){
					Log.i("PalyGame", "run ok, sleep 500 ms");
					try {
						Thread.sleep(500);
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
				}else if(res <= -10){
					// 连续不在游戏10次，关闭
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
