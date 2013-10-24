package app.main.ui.wrong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.MainActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class WrongNewActivity extends Activity {
	private static final int RESULT_CAPTURE_MIS_IMAGE = 1;
	private static final int RESULT_CAPTURE_ANS_IMAGE = 2;
	private int call_code;

	public static final String MIS_BITMAP_NAME = "misBtm";
	public static final String ANS_BITMAP_NAME = "ansBtm";
	private static final String TEMP_BITMAP_NAME = "temp";
	private String title = "";
	private String content = "";
	private String root;
	private String sub;
	private String detail;

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

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrong_new);
		headText = (TextView) findViewById(R.id.wrong_new_head_title);
		backBtn = (Button) findViewById(R.id.wrong_new_back);
		saveBtn = (Button) findViewById(R.id.wrong_new_save);
		misBtn = (Button) findViewById(R.id.wrong_new_capture_mis_btn);
		ansBtn = (Button) findViewById(R.id.wrong_new_capture_ans_btn);
		titleEdit = (EditText) findViewById(R.id.wrong_new_title);
		contentEdit = (EditText) findViewById(R.id.wrong_new_content);
		misImage = (ImageView) findViewById(R.id.wrong_new_capture_mis_img);
		ansImage = (ImageView) findViewById(R.id.wrong_new_capture_ans_img);
		getDirsAndBmps();
		setListener();
		notifyWidgets();
	}

	private void notifyWidgets() {
		if (call_code == WrongCallCode.BROWSE_CALL_NEW) {
			headText.setText("修改错题");
			titleEdit.setText(title);
			contentEdit.setText(content);
			misImage.setImageBitmap(misBmp);
			ansImage.setImageBitmap(ansBmp);
		}
		if (call_code == WrongCallCode.LIST_CALL_NEW) {
			headText.setText("新建错题");
		}
	}

	private void getDirsAndBmps() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		call_code = bundle.getInt("call_code");
		if (call_code == WrongCallCode.BROWSE_CALL_NEW) {
			root = bundle.getString("root");
			sub = bundle.getString("sub");
			detail = bundle.getString("detail");
			title = bundle.getString("title");
			content = bundle.getString("content");
			misBmp = intent.getParcelableExtra("misBmp");
			ansBmp = intent.getParcelableExtra("ansBmp");

		}
		if (call_code == WrongCallCode.LIST_CALL_NEW) {
			root = bundle.getString("root");
			sub = bundle.getString("sub");
			detail = bundle.getString("detail");
		}

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
				title = titleEdit.getText().toString();
				content = contentEdit.getText().toString();
				if (misBmp == null || ansBmp == null) {
					Toast.makeText(WrongNewActivity.this, "亲，你还没记录错题或答案呢",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (StringUtility.legalInput(title)) {

					FileUtility fileModule = MainActivity.fileModule;
					fileModule.reset();
					fileModule.createDirectory(root);
					fileModule.createDirectory(sub);
					fileModule.createDirectory(detail);
					fileModule.createDirectory(title);
					if (fileModule.saveText(content, title)
							&& fileModule.savePhoto(misBmp, MIS_BITMAP_NAME)
							&& fileModule.savePhoto(ansBmp, ANS_BITMAP_NAME)) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_CAPTURE_MIS_IMAGE:
			if (resultCode == RESULT_OK) {
				try {
					FileInputStream fis = new FileInputStream(temp);
					misBmp = BitmapFactory.decodeStream(fis);
					temp.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				misImage.setImageBitmap(misBmp);
			}
			break;
		case RESULT_CAPTURE_ANS_IMAGE:
			if (resultCode == RESULT_OK) {
				try {
					FileInputStream fis = new FileInputStream(temp);
					ansBmp = BitmapFactory.decodeStream(fis);
					temp.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				ansImage.setImageBitmap(ansBmp);
			}
			break;

		}
	}

	private void requestCamera(int requestCode) {
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(root);
		fileModule.createDirectory(sub);
		fileModule.createDirectory(detail);
		temp = fileModule.createFileFromName(TEMP_BITMAP_NAME);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
		startActivityForResult(intent, requestCode);
	}
}
