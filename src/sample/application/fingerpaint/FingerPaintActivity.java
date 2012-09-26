package sample.application.fingerpaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
// p.131 リスト５
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import android.os.Environment;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;

// p.133 リスト９
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

// p.134 リスト１２
import android.media.MediaScannerConnection;
import android.net.Uri;

// p.137 リスト１６
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// p.78 リスト３４
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

// p.79 リスト３８
import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;






public class FingerPaintActivity extends Activity implements OnTouchListener {
	
	public Canvas canvas;
	public Paint paint;
	public Path path;
	public Bitmap bitmap;
	public Float x1, y1;	// ラッパークラスの、オブジェクトのフロートを使う。
							// public Float x1;
							// public Float y1;
	public Integer w;
	public Integer h;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 親クラスの初期化
		super.onCreate(savedInstanceState);
		// どのビューのレイアウトを使うか
		setContentView(R.layout.main);
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
		
		// findViewByIdはFingerPaintActivityクラスのインスタンスメソッド
		// thisはFingerPaintActivityクラスのインスタンス
		Display disp = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();	// Contextはクラス、WINDOW_SERVICEは定数

		// 二行で表現するときは、以下の通り。
	 // WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	 // Display disp = wm.getDefaultDisplay();

		// 三行で表現するときは、以下の通り。
	 // Object obj = this.getSystemService(Context.WINDOW_SERVICE);
	 // WindowManager wm = (WindowManager)obj;
	 // Display disp = wm.getDefaultDisplay();
		
		w = disp.getWidth();
		h = disp.getHeight();
		
		// Bitmapクラス（Bitmap型）のcreateBitmapメソッド
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// p.133 リスト１０
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// p.133 リスト１１
		switch(item.getItemId()) {
		case R.id.menu_save:
			save();
			break;
		}
		// p.? リスト?
		case R.id.menu_open:
			Intent intent = new Intent(this, FilePicker.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.menu_color_change:
			final String[] items = getResources().getStringArray(R.array.ColorName);
			final int[] colors = getResources().getIntArray(R.array.Color);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTiltle(R.string.menu_color_change);
			ab.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					paint.setColor(colors[item]);
				}
			});
			ab.show();
			break;
		case R.id.menu_new;
			ab = new AlertDialog.Builder(this);
			ab.setTitle(R.string.menu_new);
			ab.setMessage(R.string.confirm_new);
			ab.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					canvas.drawColor(Color.WHITE);
					((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bitmap);
				}
			});
			ab.setNegativeButton(R.string.button_cancel,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			ab.show();
			break;
		}				
		return super.onOptionsItemSelected(item);
	}

	// p.131 リスト６
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
				scanMedia(file.getPath());	// p.135 リスト１４
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("imageNumber", imageNumber);
				editor.commit();
			}
		}
	}
	
	// p.132 リスト７
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

	// p.132 リスト８
	boolean externalMediaChecker() {
		boolean result = false;
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)) result = true;
		return result;
	}
	
	// p.135 リスト１３
	MediaScannerConnection mc;
	
	void scanMedia(final String fp) {
		mc = new MediaScannerConnection(this,
				  new MediaScannerConnection.MediaScannerConnectionClient() {
			public void onScanCompleted(String path, Uri uri) {
				disconnect();
			}
			public void onMediaScannerConnected() {
				scanFile(fp);
			}
		});
		mc.connect();
	}

	// p.135 リスト１５
	void scanFile(String fp) {
		mc.scanFile(fp, "image/png");
	}
	void disconnect() {
		mc.disconnect();
	}
	
	// p.? リスト？
	Bitmap loadImage(String path) {
		boolean landscape = false;
		Bitmap bm;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int oh = options.outHeight;
		int ow = options.outWidth;
		
		if(ow > oh) {
			landscape = true;
			oh = options.outWidth;
			ow = options.outHeight;
		}
		
		options.inJustDecodeBounds = false;
		options.inSampleSize = Math.max(ow/w, oh/h);
		bm = BitmapFactory.decodeFile(path, options);
		
		if(landscape) {
			Matrix matrix = new Matrix();
			matrix.setRotate(90.Of);
			bm = Bitmap.createBitmap(bm, 0, 0,
					bm.getWidth(), bm.getHeight(), matrix, false);
		}
		bm = Bitmap.createScaledBitmap(bm, (int)(w), (int)(w*(((double)oh)/((double)ow))), false);
		
		Bitmap offBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas offCanvas = new Canvas(offBitmap);
		offCanvas.drawBitmap(bm, 0, (h-bm.getHeight())/2, null);
		bm = offBitmap;
		return bm;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			bitmap = loadimage(data.getStringExtra("fn"));
			canvas = new Canvas(bitmap);
			ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
			iv.setImageBitmap(bitmap);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(R.string.title_exit);
			ab.setMessage(R.string.confirm_new);
			ab.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			ab.setNegativeButton(R.string.button_cansel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			ab.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}