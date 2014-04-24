package app.main.ui.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class MemoNewActivity extends Activity {
	private TextView headText;
	private EditText titleEdit;
	private EditText contentEdit;
	private Button saveBtn;
	private Button backBtn;
	private String sub;
	private String item;
	private String detail;
	private String title;
	private String content;
	private int call_code;
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
		setContentView(R.layout.memo_new);
		headText = (TextView) findViewById(R.id.memo_new_head_text);
		titleEdit = (EditText) findViewById(R.id.memo_new_title);
		contentEdit = (EditText) findViewById(R.id.memo_new_content);
		saveBtn = (Button) findViewById(R.id.memo_new_save);
		backBtn = (Button) findViewById(R.id.memo_new_back);
		fileModule = new FileUtility(this);
		getDirs();
		notifyWidgets();
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
	
	private void getDirs() {
		Bundle bundle = getIntent().getExtras();
		call_code = bundle.getInt("call_code");
		if (call_code == MemoCallCode.MAIN_CALL_NEW) {
			sub = bundle.getString("sub");
			item = bundle.getString("item");
			detail = bundle.getString("detail");

		}
		if (call_code == MemoCallCode.BROWSE_CALL_NEW) {
			sub = bundle.getString("sub");
			item = bundle.getString("item");
			detail = bundle.getString("detail");
			title = bundle.getString("title");
			content = bundle.getString("content");
		}
	}

	private void notifyWidgets() {
		if (call_code == MemoCallCode.MAIN_CALL_NEW) {
			headText.setText(this.getString(R.string.memo_new_title));
		}
		if (call_code == MemoCallCode.BROWSE_CALL_NEW) {
			headText.setText(this.getString(R.string.memo_modify_title));
			titleEdit.setText(title);
			contentEdit.setText(content);
		}
	}

	private void delete() {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			fileModule.deleteFile(dirs.get(i));
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (call_code == MemoCallCode.BROWSE_CALL_NEW) {
					delete();
				}
				title = titleEdit.getText().toString();
				if (StringUtility.legalInput(title)) {

					fileModule.reset();
					fileModule.createDirectory(sub);
					fileModule.createDirectory(item);
					fileModule.createDirectory(detail);
					content = contentEdit.getText().toString();
					if (fileModule.saveText(content, title)) {
						Toast.makeText(MemoNewActivity.this, "��𣬱���ɹ�",
								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(MemoNewActivity.this,
								MemoBrowseActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("sub", sub);
						bundle.putString("item", item);
						bundle.putString("detail", detail);
						bundle.putString("title", title);
						bundle.putString("content", content);
						intent.putExtras(bundle);
						MemoNewActivity.this.startActivity(intent);
						MemoNewActivity.this.finish();
					}

				} else {
					Toast.makeText(MemoNewActivity.this, "�ף����ⲻ��Ϊ�ջ�ֻ��ո�Ŷ",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemoNewActivity.this.finish();
			}
		});
	}

}
