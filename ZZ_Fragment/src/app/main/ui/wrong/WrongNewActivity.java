package app.main.ui.wrong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class WrongNewActivity extends Activity {
	private static final int RESULT_CAPTURE_MIS_IMAGE = 1;
	private static final int RESULT_CAPTURE_ANS_IMAGE = 2;
	private int call_code;

	public static String MIS_BITMAP_NAME;
	public static String ANS_BITMAP_NAME;
	private static String TEMP_BITMAP_NAME;

	private String sub;
	private String item;
	private String detail;
	private String title = "";
	private String content = "";

	private TextView headText;
	private Button backBtn;
	private Button saveBtn;
	private Button misBtn;
	private Button ansBtn;
	private EditText titleEdit;
	private EditText contentEdit;
	private ImageView misImage;
	private ImageView ansImage;

	private Bitmap misBmp = null;
	private Bitmap ansBmp = null;

	private File temp;
	private FileUtility fileModule;
	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.wrong_new);
		headText = (TextView) findViewById(R.id.wrong_new_head_text);
		backBtn = (Button) findViewById(R.id.wrong_new_back);
		saveBtn = (Button) findViewById(R.id.wrong_new_save);
		misBtn = (Button) findViewById(R.id.wrong_new_capture_mis_btn);
		ansBtn = (Button) findViewById(R.id.wrong_new_capture_ans_btn);
		titleEdit = (EditText) findViewById(R.id.wrong_new_title);
		contentEdit = (EditText) findViewById(R.id.wrong_new_content);
		misImage = (ImageView) findViewById(R.id.wrong_new_capture_mis_img);
		ansImage = (ImageView) findViewById(R.id.wrong_new_capture_ans_img);
		fileModule = new FileUtility(this);
		MIS_BITMAP_NAME = this.getString(R.string.file_wrong_misBmp);
		ANS_BITMAP_NAME = this.getString(R.string.file_wrong_ansBmp);
		TEMP_BITMAP_NAME = this.getString(R.string.file_wrong_temp);

		getDirsAndBmps();
		setListener();
		notifyWidgets();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// 在当前的activity中注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("ExitApp");
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_CAPTURE_MIS_IMAGE:
			if (resultCode == RESULT_OK) {
				if (misBmp != null && !misBmp.isRecycled())
					misBmp.recycle();
				try {
					FileInputStream fis = new FileInputStream(temp);
					misBmp = BitmapFactory.decodeStream(fis);
					temp.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					Toast.makeText(WrongNewActivity.this, "额，没拍成功。。",
							Toast.LENGTH_SHORT).show();
				}
	
				misImage.setImageBitmap(misBmp);
			}
			break;
		case RESULT_CAPTURE_ANS_IMAGE:
			if (resultCode == RESULT_OK) {
				if (ansBmp != null && !ansBmp.isRecycled())
					ansBmp.recycle();
				try {
					FileInputStream fis = new FileInputStream(temp);
					ansBmp = BitmapFactory.decodeStream(fis);
					temp.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					Toast.makeText(WrongNewActivity.this, "额，没拍成功。。",
							Toast.LENGTH_SHORT).show();
				}
	
				ansImage.setImageBitmap(ansBmp);
			}
			break;
	
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(this.broadcastReceiver);
		if (misBmp != null && !misBmp.isRecycled())
			misBmp.recycle();
		if (ansBmp != null && !ansBmp.isRecycled())
			ansBmp.recycle();
	}

	@SuppressWarnings("deprecation")
	private void notifyWidgets() {
		if (call_code == WrongCallCode.BROWSE_CALL_NEW) {
			headText.setText(this.getString(R.string.wrong_modify_title));
			titleEdit.setText(title);
			contentEdit.setText(content);
			misImage.setImageBitmap(misBmp);
			ansImage.setImageBitmap(ansBmp);
		}
		if (call_code == WrongCallCode.LIST_CALL_NEW) {
			headText.setText(this.getString(R.string.wrong_new_title));
		}
		WindowManager mWindowManager = (WindowManager) getApplication()
				.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				screenWidth / 3, screenHeight / 4);
		mParams.gravity = Gravity.CENTER_HORIZONTAL;
		misImage.setLayoutParams(mParams);
		ansImage.setLayoutParams(mParams);
	}

	private void getDirsAndBmps() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		call_code = bundle.getInt("call_code");
		if (call_code == WrongCallCode.BROWSE_CALL_NEW) {

			sub = bundle.getString("sub");
			item = bundle.getString("item");
			detail = bundle.getString("detail");
			title = bundle.getString("title");
			content = bundle.getString("content");
			fileModule.reset();
			fileModule.createDirectory(sub);
			fileModule.createDirectory(item);
			fileModule.createDirectory(detail);
			fileModule.createDirectory(title);
			ArrayList<String> dirs = fileModule.getSubFolder();
			for (int i = 0; i < dirs.size(); i++) {
				String fileName = dirs.get(i);
				String fName = StringUtility.getFileName(fileName);
				if (fName.equals(MIS_BITMAP_NAME)) {
					misBmp = fileModule.readBitmap(fileName);
				}
				if (fName.equals(ANS_BITMAP_NAME)) {
					ansBmp = fileModule.readBitmap(fileName);
				}

			}
		}
		if (call_code == WrongCallCode.LIST_CALL_NEW
				|| call_code == WrongCallCode.MAIN_CALL_NEW) {

			sub = bundle.getString("sub");
			item = bundle.getString("item");
			detail = bundle.getString("detail");
		}

	}

	private boolean save() {

		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(title);
		if (fileModule.saveText(content, title)
				&& fileModule.savePhoto(misBmp, MIS_BITMAP_NAME)
				&& fileModule.savePhoto(ansBmp, ANS_BITMAP_NAME)) {
			return true;
		}
		return false;
	}

	private void delete() {

		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(title);
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			fileModule.deleteFile(dirs.get(i));
		}
		fileModule.Rollback();
		fileModule.deleteFile(title);
	}

	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				WrongNewActivity.this.finish();
			}
		});
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (call_code == WrongCallCode.BROWSE_CALL_NEW) {
					delete();
				}
				title = titleEdit.getText().toString();
				content = contentEdit.getText().toString();
				if (misBmp == null || ansBmp == null) {
					Toast.makeText(WrongNewActivity.this, "亲，你还没记录错题或答案呢",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (StringUtility.legalInput(title)) {

					if (save()) {
						Toast.makeText(WrongNewActivity.this, "吼吼，保存成功",
								Toast.LENGTH_SHORT).show();
						WrongNewActivity.this.finish();
					}
				} else {

					Toast.makeText(WrongNewActivity.this, "亲，标题不能为空或只打空格哦",
							Toast.LENGTH_LONG).show();
					return;
				}
			}
		});
		misBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestCamera(RESULT_CAPTURE_MIS_IMAGE);
			}
		});

		ansBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestCamera(RESULT_CAPTURE_ANS_IMAGE);
			}
		});
	}

	private void requestCamera(int requestCode) {
		temp = new File(Environment.getExternalStorageDirectory()
				+ File.separator + TEMP_BITMAP_NAME);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
		WrongNewActivity.this.startActivityForResult(intent, requestCode);
	}

}
