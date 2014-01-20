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
	
	public static GamePlay play = null;
	
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
		play = new GamePlay();
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
}
