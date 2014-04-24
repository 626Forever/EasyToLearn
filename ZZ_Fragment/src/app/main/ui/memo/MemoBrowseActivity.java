package app.main.ui.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;

public class MemoBrowseActivity extends Activity {
	private Button backBtn;
	private Button modifyBtn;
	private Button deleteBtn;
	private TextView titleText;
	private TextView contentText;
	private String title;
	private String content;
	private String sub;
	private String item;
	private String detail;

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
		setContentView(R.layout.memo_browse);
		backBtn = (Button) findViewById(R.id.memo_browse_back);
		modifyBtn = (Button) findViewById(R.id.memo_browse_modify);
		deleteBtn = (Button) findViewById(R.id.memo_browse_delete);
		titleText = (TextView) findViewById(R.id.memo_browse_title);
		contentText = (TextView) findViewById(R.id.memo_browse_content);
		fileModule = new FileUtility(this);
		getData();
		titleText.setText(title);
		contentText.setText(content);
		setListener();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// �ڵ�ǰ��activity��ע��㲥
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
	
	private void getData() {
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		detail = bundle.getString("detail");

	}

	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemoBrowseActivity.this.finish();
			}
		});

		modifyBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MemoBrowseActivity.this,
						MemoNewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("call_code", MemoCallCode.BROWSE_CALL_NEW);
				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				bundle.putString("title", title);
				bundle.putString("content", content);
				intent.putExtras(bundle);
				fileModule.reset();
				fileModule.createDirectory(sub);
				fileModule.createDirectory(item);
				fileModule.createDirectory(detail);
				ArrayList<String> dirs = fileModule
						.getSubFolder();
				for (int i = 0; i < dirs.size(); i++) {
					fileModule.deleteFolder(dirs.get(i));
				}
				MemoBrowseActivity.this.startActivity(intent);
				MemoBrowseActivity.this.finish();

			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(
						MemoBrowseActivity.this);
				builder.setMessage("Ҫɾ�������ĵ���?");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								fileModule.reset();
								fileModule.createDirectory(sub);
								fileModule.createDirectory(item);
								fileModule.createDirectory(detail);
								ArrayList<String> dirs = fileModule
										.getSubFolder();
								for (int i = 0; i < dirs.size(); i++) {
									fileModule.deleteFolder(dirs.get(i));
								}
								Toast.makeText(MemoBrowseActivity.this, "ȫɾ����",
										Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								MemoBrowseActivity.this.finish();
							}
						});
				builder.setNegativeButton("ȡ��",
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

	}

}
