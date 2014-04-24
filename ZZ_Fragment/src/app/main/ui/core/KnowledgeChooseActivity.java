package app.main.ui.core;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import app.main.R;
import app.main.ui.media.VideoActivity;
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

	private String subs[];
	private String sub;
	private String item;
	private String type_video;
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
		setContentView(R.layout.main_knowledge_choose);
		learnBtn = (Button) findViewById(R.id.main_knowledge_learn_btn);
		wrongBtn = (Button) findViewById(R.id.main_knowledge_wrong_btn);
		mediaBtn = (Button) findViewById(R.id.main_knowledge_media_btn);
		fileModule = new FileUtility(this);
		subs = new String[] { this.getString(R.string.file_learn_folder),
				this.getString(R.string.file_wrong_folder),
				this.getString(R.string.file_media_folder) };

		initWidgets();
		getDirs();
		setListener();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(this.broadcastReceiver);
	}

	private void getDirs() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		type_video = getString(R.string.file_media_video);
		getString(R.string.file_media_recorder);
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
				openMedia(subs[2]);
			}
		});
	}

	private void openLearn(final String detail) {
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

	private void openMedia(final String detail) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		Intent intent = new Intent(KnowledgeChooseActivity.this,
				VideoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("sub", sub);
		bundle.putString("item", item);
		bundle.putString("detail", detail);
		bundle.putString("type", type_video);
		intent.putExtras(bundle);
		KnowledgeChooseActivity.this.startActivity(intent);
		// AlertDialog.Builder builder = new
		// Builder(KnowledgeChooseActivity.this);
		// builder.setMessage("亲，请选择录像还是录音");
		// builder.setTitle("提示");
		// builder.setPositiveButton("录像", new OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent(KnowledgeChooseActivity.this,
		// VideoActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString("sub", sub);
		// bundle.putString("item", item);
		// bundle.putString("detail", detail);
		// bundle.putString("type", type_video);
		// intent.putExtras(bundle);
		// KnowledgeChooseActivity.this.startActivity(intent);
		// dialog.dismiss();
		// }
		//
		// });
		// builder.setNegativeButton("录音", new OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent(KnowledgeChooseActivity.this,
		// RecorderActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString("sub", sub);
		// bundle.putString("item", item);
		// bundle.putString("detail", detail);
		// bundle.putString("type", type_recorder);
		// intent.putExtras(bundle);
		// KnowledgeChooseActivity.this.startActivity(intent);
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
	}

}
