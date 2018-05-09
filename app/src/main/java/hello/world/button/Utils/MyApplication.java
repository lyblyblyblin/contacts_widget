package hello.world.button.Utils;

import android.app.Application;
import android.content.Intent;

/**
 * 全局Application
 */
public class MyApplication extends Application {
	private static MyApplication instance;

	/**
	 * 获取Application
	 * 
	 * @return BaseApplication
	 */
	public synchronized static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	/**
	 * 获取上下文
	 * 
	 * @return getInstance()
	 */
	public static MyApplication getContext() {
		return getInstance();
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

}
