package app.main.ui.wrong;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.MainActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class WrongBrowseActivity extends Activity {

	private int call_code;

	private String sub;
	private String item;
	private String detail;
	private String name;
	private String title;
	private String content;

	private Bitmap misBmp;
	private Bitmap ansBmp;

	private Button backBtn;
	private Button modifyBtn;
	private Button deleteBtn;
	private Button misBtn;
	private Button ansBtn;
	private Button noteBtn;

	private ImageView misImg;
	private ImageView ansImg;
	private TextView titleText;
	private TextView contentText;

	private RelativeLayout misLayout;
	private RelativeLayout ansLayout;
	private LinearLayout noteLayout;
	private FileUtility fileModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.wrong_browse);

		backBtn = (Button) findViewById(R.id.wrong_browse_back);
		modifyBtn = (Button) findViewById(R.id.wrong_browse_modify);
		deleteBtn = (Button) findViewById(R.id.wrong_browse_delete);
		misBtn = (Button) findViewById(R.id.wrong_broswe_mis_btn);
		ansBtn = (Button) findViewById(R.id.wrong_broswe_ans_btn);
		noteBtn = (Button) findViewById(R.id.wrong_broswe_note_btn);
		misImg = (ImageView) findViewById(R.id.wrong_browse_mis_img);
		ansImg = (ImageView) findViewById(R.id.wrong_browse_ans_img);
		titleText = (TextView) findViewById(R.id.wrong_browse_note_title);
		contentText = (TextView) findViewById(R.id.wrong_browse_note_content);
		misLayout = (RelativeLayout) findViewById(R.id.wrong_browse_mis);
		ansLayout = (RelativeLayout) findViewById(R.id.wrong_browse_ans);
		noteLayout = (LinearLayout) findViewById(R.id.wrong_browse_note);

		misLayout.setVisibility(View.INVISIBLE);
		ansLayout.setVisibility(View.INVISIBLE);
		noteLayout.setVisibility(View.VISIBLE);
		fileModule = new FileUtility(this);
		getFileDirs();
		getData();
		notifyWidgets();
		setListener();
	}

	private void getFileDirs() {
		Bundle bundle = getIntent().getExtras();
		call_code = bundle.getInt("call_code");
		if (call_code == WrongCallCode.LIST_CALL_BROWSE) {
			sub = bundle.getString("sub");
			item = bundle.getString("item");
			detail = bundle.getString("detail");
			name = bundle.getString("name");
		}
	}

	private void getData() {

		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(name);
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			String fileName = dirs.get(i);
			if (fileModule.isTextFile(fileName)) {
				title = StringUtility.getFileName(fileName);
				content = fileModule.readText(fileName);
			} else {
				String fName = StringUtility.getFileName(fileName);
				if (fName.equals(WrongNewActivity.MIS_BITMAP_NAME)) {
					misBmp = fileModule.readBitmap(fileName);
				}
				if (fName.equals(WrongNewActivity.ANS_BITMAP_NAME)) {
					ansBmp = fileModule.readBitmap(fileName);
				}
			}
		}

	}

	private void notifyWidgets() {
		misImg.setImageBitmap(misBmp);
		ansImg.setImageBitmap(ansBmp);
		titleText.setText(title);
		contentText.setText(content);
	}

	private void delete() {

		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(name);
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			fileModule.deleteFile(dirs.get(i));
		}
		fileModule.Rollback();
		fileModule.deleteFile(name);
	}

	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				WrongBrowseActivity.this.finish();
			}
		});
		modifyBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(WrongBrowseActivity.this,
						WrongNewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("call_code", WrongCallCode.BROWSE_CALL_NEW);
				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				bundle.putString("name", name);
				bundle.putString("title", title);
				bundle.putString("content", content);
				intent.putExtras(bundle);
				WrongBrowseActivity.this.startActivity(intent);
				WrongBrowseActivity.this.finish();
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(
						WrongBrowseActivity.this);
				builder.setMessage("要删除您的错题吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								delete();
								Toast.makeText(WrongBrowseActivity.this,
										"全删光了", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								WrongBrowseActivity.this.finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

							}
						});
				builder.create().show();
			}
		});
		misBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				misLayout.setVisibility(View.VISIBLE);
				ansLayout.setVisibility(View.INVISIBLE);
				noteLayout.setVisibility(View.INVISIBLE);
			}
		});
		ansBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				misLayout.setVisibility(View.INVISIBLE);
				ansLayout.setVisibility(View.VISIBLE);
				noteLayout.setVisibility(View.INVISIBLE);
			}
		});
		noteBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				misLayout.setVisibility(View.INVISIBLE);
				ansLayout.setVisibility(View.INVISIBLE);
				noteLayout.setVisibility(View.VISIBLE);
			}
		});
	}

}
