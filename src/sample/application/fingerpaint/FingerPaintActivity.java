package sample.application.fingerpaint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
// p.131 ���X�g�T
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import android.os.Environment;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;

// p.133 ���X�g�X
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;



public class FingerPaintActivity extends Activity implements OnTouchListener {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// p.133 ���X�g�P�O
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// p.133 ���X�g�P�P
		switch(item.getItemId()) {
		case R.id.menu_save:
			save();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public Canvas canvas;
	public Paint paint;
	public Path path;
	public Bitmap bitmap;
	public Float x1, y1;	// ���b�p�[�N���X�́A�I�u�W�F�N�g�̃t���[�g���g���B
		// public Float x1;
		// public Float y1;
	public Integer w;
	public Integer h;

	public void onCreate(Bundle savedInstanceState) {
		// �e�N���X�̏�����
		super.onCreate(savedInstanceState);
		// �ǂ̃r���[�̃��C�A�E�g���g����
		setContentView(R.layout.main);
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
		
		// findViewById��FingerPaintActivity�N���X�̃C���X�^���X���\�b�h
		// this��FingerPaintActivity�N���X�̃C���X�^���X
		Display disp = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();	// Context�̓N���X�AWINDOW_SERVICE�͒萔

		// ��s�ŕ\������Ƃ��́A�ȉ��̒ʂ�B
	 // WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	 // Display disp = wm.getDefaultDisplay();

		// �O�s�ŕ\������Ƃ��́A�ȉ��̒ʂ�B
	 // Object obj = this.getSystemService(Context.WINDOW_SERVICE);
	 // WindowManager wm = (WindowManager)obj;
	 // Display disp = wm.getDefaultDisplay();
		
		w = disp.getWidth();
		h = disp.getHeight();
		// Bitmap�N���X�iBitmap�^�j��createBitmap���\�b�h
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		paint = new Paint();
		path = new Path();
		canvas = new Canvas(bitmap);
		
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		canvas.drawColor(Color.WHITE);
		iv.setImageBitmap(bitmap);
		iv.setOnTouchListener(this);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.reset();
			path.moveTo(x, y);
			x1 = x;
			y1 = y;
			break;
		case MotionEvent.ACTION_MOVE:
			path.quadTo(x1, y1, x, y);
			x1 = x;
			y1 = y;
			canvas.drawPath(path, paint);
			path.reset();
			path.moveTo(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if(x == x1 && y == y1) y1 = y1 + 1;
			path.quadTo(x1, y1, x, y);
			canvas.drawPath(path, paint);
			path.reset();
			break;
			
		}
		ImageView iv = (ImageView)this.findViewById(R.id.imageView1);
		iv.setImageBitmap(bitmap);
		
		return true;
	}
	
	// p.131 ���X�g�U
	void save() {
		SharedPreferences prefs = getSharedPreferences("FingerPaintPreferences", MODE_PRIVATE);
		int imageNumber = prefs.getInt("imageNumber", 1);
		File file = null;
		
		if(externalMediaChecker()) {
			DecimalFormat form = new DecimalFormat("0000");
			String path = Environment.getExternalStorageDirectory() + "/mypaint/";
			File outDir = new File(path);
			if(!outDir.exists()) outDir.mkdir();
			
			do {
				file = new File(path + "img" + form.format(imageNumber) + ".png");
				imageNumber++;
			} while(file.exists());
			if(writeImage(file)) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("imageNumber", imageNumber);
				editor.commit();
			}
		}
	}
	
	// p.132 ���X�g�V
	boolean writeImage(File file) {
		try {
			FileOutputStream fo = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fo);
			fo.flush();
			fo.close();
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	// p.132 ���X�g�W
	boolean externalMediaChecker() {
		boolean result = false;
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)) result = true;
		return result;
	}
}
