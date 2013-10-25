package app.main.ui.core;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import app.main.R;
import app.main.ui.media.MediaActivity;
import app.main.ui.memo.MemoBrowseActivity;
import app.main.ui.memo.MemoCallCode;
import app.main.ui.memo.MemoNewActivity;
import app.main.ui.wrong.WrongCallCode;
import app.main.ui.wrong.WrongListActivity;
import app.main.ui.wrong.WrongNewActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class KnowledgeChooseActivity extends Activity {
	private Button learnBtn;
	private Button wrongBtn;
	private Button mediaBtn;

	private String subs[] = new String[] { "学习心得", "错题整理", "课堂笔记" };
	private String sub;
	private String item;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_knowledge_choose);
		learnBtn = (Button) findViewById(R.id.main_knowledge_learn_btn);
		wrongBtn = (Button) findViewById(R.id.main_knowledge_wrong_btn);
		mediaBtn = (Button) findViewById(R.id.main_knowledge_media_btn);
		initWidgets();
		getDirs();
		setListener();
	}

	private void getDirs() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
	}

	private void initWidgets() {
		learnBtn.setText(subs[0]);
		wrongBtn.setText(subs[1]);
		mediaBtn.setText(subs[2]);
	}

	private void setListener() {
		learnBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openLearn(subs[0]);
			}
		});
		wrongBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openWrong(subs[1]);
			}
		});
		mediaBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openRecoder(subs[2]);
			}
		});
	}

	private void openLearn(final String detail) {

		FileUtility fileModule = new FileUtility();
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();
		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(
					KnowledgeChooseActivity.this);
			builder.setMessage("亲，来创建您的心得吧");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(KnowledgeChooseActivity.this,
							MemoNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", MemoCallCode.MAIN_CALL_NEW);
					bundle.putString("sub", sub);
					bundle.putString("item", item);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					KnowledgeChooseActivity.this.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			fileModule.reset();
			fileModule.createDirectory(sub);
			fileModule.createDirectory(item);
			fileModule.createDirectory(detail);
			String title = fileModule.getSubFolder().get(0);
			String data = fileModule.readText(title);

			Intent intent = new Intent(KnowledgeChooseActivity.this,
					MemoBrowseActivity.class);
			Bundle bundle = new Bundle();

			bundle.putString("sub", sub);
			bundle.putString("item", item);
			bundle.putString("detail", detail);
			bundle.putString("title", StringUtility.getFileName(title));
			bundle.putString("content", data);
			intent.putExtras(bundle);
			KnowledgeChooseActivity.this.startActivity(intent);
		}

	}

	private void openWrong(final String detail) {

		FileUtility fileModule = new FileUtility();
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();

		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(
					KnowledgeChooseActivity.this);
			builder.setMessage("亲，添加错题吧");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(KnowledgeChooseActivity.this,
							WrongNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", WrongCallCode.MAIN_CALL_NEW);
					bundle.putString("sub", sub);
					bundle.putString("item", item);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					KnowledgeChooseActivity.this.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			Intent intent = new Intent(KnowledgeChooseActivity.this,
					WrongListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sub", sub);
			bundle.putString("item", item);
			bundle.putString("detail", detail);
			intent.putExtras(bundle);
			KnowledgeChooseActivity.this.startActivity(intent);
		}

	}

	private void openRecoder(final String detail) {
		Intent intent = new Intent(KnowledgeChooseActivity.this,
				MediaActivity.class);
		KnowledgeChooseActivity.this.startActivity(intent);
	}
}
