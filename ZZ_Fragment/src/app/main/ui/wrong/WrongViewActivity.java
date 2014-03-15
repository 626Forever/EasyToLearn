package app.main.ui.wrong;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import app.main.R;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class WrongViewActivity extends Activity {
	public static String MIS_BITMAP_NAME;
	public static String ANS_BITMAP_NAME;
	private int screenWidth;
	private int screenHeight;
	private int view_type;

	private String sub;
	private String item;
	private String detail;
	private String name;
	private String file_name;

	private ImageView iv;
	private Bitmap src;
	private Matrix curMatrix;
	private Matrix savedMatrix;

	private FileUtility fileModule;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics disManager = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(disManager);
		screenWidth = disManager.widthPixels;
		screenHeight = disManager.heightPixels;
		this.setContentView(R.layout.wrong_image_browse);
		fileModule = new FileUtility(this);
		iv = (ImageView) findViewById(R.id.wrong_image_view);

		MIS_BITMAP_NAME = this.getString(R.string.file_wrong_misBmp);
		ANS_BITMAP_NAME = this.getString(R.string.file_wrong_ansBmp);

		Intent intent = getIntent();
		Bundle bundle = getIntent().getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		detail = bundle.getString("detail");
		name = bundle.getString("name");
		file_name = bundle.getString("file_name");
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(name);
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			String fileName = dirs.get(i);
			String fName = StringUtility.getFileName(fileName);
			if (file_name.equals(fName)) {
				src = fileModule.readBitmap(fileName);
			}

		}
		iv.setImageBitmap(src);

		curMatrix = new Matrix();
		savedMatrix = new Matrix();

		final float scale = (float) screenWidth / src.getWidth();

		curMatrix.setScale(scale, scale);
		curMatrix
				.postTranslate(0, (screenHeight - src.getHeight() * scale) / 2);

		iv.setImageMatrix(curMatrix);
		iv.setOnTouchListener(new myTouchListener(scale));

	}

	class myTouchListener implements OnTouchListener {
		private float now_dist;
		private float last_dist;
		private float image_width = screenWidth;
		private float image_height;
		private float pre_image_width = screenWidth;
		private float pre_image_height;
		/** ��ק������ͼƬ���Ͻǵľ��� */
		private float drag_offset_x;
		private float drag_offset_y;

		private PointF centerScreenPoint;
		private PointF lastOffsetPoint;
		private PointF centerImagePoint;

		private boolean init = false;
		/** ��ʾ��ǰͼƬ�����ƶ� */
		private boolean move = false;

		public myTouchListener(float scale) {
			image_height = src.getHeight() * scale;
			pre_image_height = image_height;
			centerScreenPoint = new PointF(screenWidth / 2, screenHeight / 2);
			lastOffsetPoint = new PointF(0, (screenHeight - src.getHeight()
					* scale) / 2);
			centerImagePoint = new PointF(screenWidth / 2, src.getHeight()
					* scale / 2);
		}

		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			int point_num = event.getPointerCount();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (point_num > 1 && !init) {
					float dist_x = event.getX(0) - event.getX(1);
					float dist_y = event.getY(0) - event.getY(1);
					last_dist = (float) Math
							.sqrt((double) (dist_x * dist_x + dist_y * dist_y));
					init = true;

				} else {
					if (init && point_num == 1) {
						float image_lx = lastOffsetPoint.x;
						float image_ly = lastOffsetPoint.y;
						float image_rx = image_lx + image_width;
						float image_ry = image_ly + image_height;
						float ex = event.getX();
						float ey = event.getY();
						/** ��������ͼƬ�� */
						if (ex >= image_lx && ex <= image_rx && ey >= image_ly
								&& ey <= image_ry) {
							savedMatrix.set(curMatrix);
							drag_offset_x = ex - lastOffsetPoint.x;
							drag_offset_y = ey - lastOffsetPoint.y;
							move = true;
						}
					}
				}
				break;
			case MotionEvent.ACTION_POINTER_2_DOWN:
				if (!init) {
					float dist_x = event.getX(0) - event.getX(1);
					float dist_y = event.getY(0) - event.getY(1);
					last_dist = (float) Math
							.sqrt((double) (dist_x * dist_x + dist_y * dist_y));
					init = true;
				} else {

				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (point_num == 1 && init && move) {
					int nowx = (int) event.getX();
					int nowy = (int) event.getY();
					curMatrix.postTranslate(-lastOffsetPoint.x,
							-lastOffsetPoint.y);
					lastOffsetPoint.x = nowx - drag_offset_x;
					lastOffsetPoint.y = nowy - drag_offset_y;
					curMatrix.postTranslate(lastOffsetPoint.x,
							lastOffsetPoint.y);
				}

				break;
			case MotionEvent.ACTION_UP:
				move = false;
				break;
			}

			if (point_num > 1 && init) {
				float save_now = now_dist;
				float save_pre = last_dist;
				last_dist = now_dist;
				float dist_x = event.getX(0) - event.getX(1);
				float dist_y = event.getY(0) - event.getY(1);
				now_dist = (float) Math.sqrt((double) (dist_x * dist_x + dist_y
						* dist_y));
				if (Math.abs(now_dist - last_dist) < 20f) {
					now_dist = save_now;
					last_dist = save_pre;
				} else {
					pre_image_width = image_width;
					pre_image_height = image_height;
					savedMatrix.set(curMatrix);
					curMatrix.postTranslate(-lastOffsetPoint.x,
							-lastOffsetPoint.y);
					if (now_dist > last_dist) {
						image_width *= 1.1f;
						image_height *= 1.1f;
						if (image_width > 2.5 * screenWidth) {
							image_width = pre_image_width;
							image_height = pre_image_height;
							curMatrix.set(savedMatrix);
							iv.setImageMatrix(curMatrix);
							return true;
						}
						curMatrix.preScale(1.1f, 1.1f);
						centerImagePoint.x = centerImagePoint.x * 1.1f;
						centerImagePoint.y = centerImagePoint.y * 1.1f;

					} else {
						image_width *= 0.9f;
						image_height *= 0.9f;
						if (image_width < 0.5 * screenWidth) {
							image_width = pre_image_width;
							image_height = pre_image_height;
							curMatrix.set(savedMatrix);
							iv.setImageMatrix(curMatrix);
							return true;
						}
						curMatrix.preScale(0.9f, 0.9f);
						centerImagePoint.x = centerImagePoint.x * 0.9f;
						centerImagePoint.y = centerImagePoint.y * 0.9f;

					}

					curMatrix.postTranslate(centerScreenPoint.x
							- centerImagePoint.x, centerScreenPoint.y
							- centerImagePoint.y);
					lastOffsetPoint.x = centerScreenPoint.x
							- centerImagePoint.x;
					lastOffsetPoint.y = centerScreenPoint.y
							- centerImagePoint.y;
				}
			}
			iv.setImageMatrix(curMatrix);
			return true;
		}
	}
}
