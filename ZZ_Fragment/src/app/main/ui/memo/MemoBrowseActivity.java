package app.main.ui.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.MainActivity;
import app.main.util.FileUtility;

public class MemoBrowseActivity extends Activity {
	private Button backBtn;
	private Button modifyBtn;
	private Button deleteBtn;
	private TextView titleText;
	private TextView contentText;
	private TextView timeText;
	private String name;
	private String content;
	private String root;
	private String sub;
	private String detail;
	private boolean modify = false;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_browse);
		backBtn = (Button) findViewById(R.id.memo_browse_back);
		modifyBtn = (Button) findViewById(R.id.memo_browse_modify);
		deleteBtn = (Button) findViewById(R.id.memo_browse_delete);
		timeText = (TextView) findViewById(R.id.memo_browse_title_tiem);
		titleText = (TextView) findViewById(R.id.memo_browse_title);
		contentText = (TextView) findViewById(R.id.memo_browse_content);
		getData();
		titleText.setText(name);
		contentText.setText(content);
		setListener();
	}

	private void getData() {
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("name");
		content = bundle.getString("content");
		root = bundle.getString("root");
		sub = bundle.getString("sub");
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
				modiftBtnChangeState();

			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(
						MemoBrowseActivity.this);
				builder.setMessage("要删除您的心得吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								FileUtility fileModule = MainActivity.fileModule;
								fileModule.reset();
								fileModule.createDirectory(root);
								fileModule.createDirectory(sub);
								fileModule.createDirectory(detail);
								ArrayList<String> dirs = fileModule
										.getSubFolder();
								for (int i = 0; i < dirs.size(); i++) {
									fileModule.deleteFile(dirs.get(i));
								}
								Toast.makeText(MemoBrowseActivity.this, "全删光了",
										Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								MemoBrowseActivity.this.finish();
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

	}

	private void modiftBtnChangeState() {
		if (!modify) {
			modify = true;
			modifyBtn.setText("保存");

		} else {
			modify = false;
			modifyBtn.setText("修改");
		}
	}
}
