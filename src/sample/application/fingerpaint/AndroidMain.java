package sample.application.fingerpaint;

import android.app.Activity;

public class AndroidMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
//		MemopadActivity activity = getMainActivity();
//		activity.onCreate(null);
//	}
//
//	public MemopadActivity getMainActivity() {
//		return new MemopadActivity();
//	}
//}
		
		Activity activity = getMainActivity();
//		activity.onCreate(null);
	}

	public static Activity getMainActivity() {
		Activity act = new MemopadActivity();
		return new Activity();
	}
}
