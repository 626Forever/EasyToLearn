package app.main.ui.wrong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.MainActivity;
import app.main.ui.memo.MemoNewActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class WrongNewActivity extends Activity {
	private static final int RESULT_CAPTURE_MIS_IMAGE = 1;// �����requestCode
	private static final int RESULT_CAPTURE_ANS_IMAGE = 2;// �����requestCode
	public static final String MIS_BITMAP_NAME = "misBtm";
	public static final String ANS_BITMAP_NAME = "ansBtm";
	private String title;
	private String content;
	private String root;
	private String sub;
	private String detail;
	
	private Button backBtn;
	private Button saveBtn;
	private Button misBtn;
	private Button ansBtn;
	private EditText titleEdit;
	private EditText contentEdit;
	private ImageView misImage;
	private ImageView ansImage;
	private Bitmap misBitmap = null;
	private Bitmap ansBitmap = null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrong_new);
		backBtn = (Button) findViewById(R.id.wrong_new_back);
		saveBtn = (Button) findViewById(R.id.wrong_new_save);
		misBtn = (Button) findViewById(R.id.wrong_new_capture_mis_btn);
		ansBtn = (Button) findViewById(R.id.wrong_new_capture_ans_btn);
		titleEdit = (EditText) findViewById(R.id.wrong_new_title);
		contentEdit = (EditText) findViewById(R.id.wrong_new_content);
		misImage = (ImageView) findViewById(R.id.wrong_new_capture_mis_img);
		ansImage = (ImageView) findViewById(R.id.wrong_new_capture_ans_img);
		getFileDir();
		setListener();
	}

	private void getFileDir() {
		Bundle bundle = getIntent().getExtras();
		root = bundle.getString("root");
		sub = bundle.getString("sub");
		detail = bundle.getString("detail");
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
				if (misBitmap == null || ansBitmap == null) {
					Toast.makeText(WrongNewActivity.this, "�ף��㻹û��¼��������",
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
							&& fileModule.savePhoto(misBitmap, MIS_BITMAP_NAME)
							&& fileModule.savePhoto(ansBitmap, ANS_BITMAP_NAME)) {
						Toast.makeText(WrongNewActivity.this, "��𣬱���ɹ�",
								Toast.LENGTH_SHORT).show();
						WrongNewActivity.this.finish();

					}
				} else {

					Toast.makeText(WrongNewActivity.this, "�ף����ⲻ��Ϊ�ջ�ֻ��ո�Ŷ",
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
				Bundle bundle = data.getExtras();
				misBitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
				misImage.setImageBitmap(misBitmap);
			}
			break;
		case RESULT_CAPTURE_ANS_IMAGE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				ansBitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
				ansImage.setImageBitmap(ansBitmap);
			}
			break;

		}
	}

	private void requestCamera(int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, requestCode);
	}
}
