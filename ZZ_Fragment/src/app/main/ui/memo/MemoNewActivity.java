package app.main.ui.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.main.R;
import app.main.ui.core.MainActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class MemoNewActivity extends Activity {
	private TextView headText;
	private EditText titleEdit;
	private EditText contentEdit;
	private Button saveBtn;
	private Button backBtn;
	private String root;
	private String sub;
	private String detail;
	private String title;
	private String content;
	private int call_code;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_new);
		headText = (TextView) findViewById(R.id.memo_new_head_text);
		titleEdit = (EditText) findViewById(R.id.memo_new_title);
		contentEdit = (EditText) findViewById(R.id.memo_new_content);
		saveBtn = (Button) findViewById(R.id.memo_new_save);
		backBtn = (Button) findViewById(R.id.memo_new_back);

		getDirs();
		notifyWidgets();
		setListener();
	}

	private void getDirs() {
		Bundle bundle = getIntent().getExtras();
		call_code = bundle.getInt("call_code");
		if (call_code == MemoCallCode.MAIN_CALL_NEW) {
			root = bundle.getString("root");
			sub = bundle.getString("sub");
			detail = bundle.getString("detail");

		}
		if (call_code == MemoCallCode.BROWSE_CALL_NEW) {
			root = bundle.getString("root");
			sub = bundle.getString("sub");
			detail = bundle.getString("detail");
			title = bundle.getString("title");
			content = bundle.getString("content");
		}
	}

	private void notifyWidgets() {
		if (call_code == MemoCallCode.MAIN_CALL_NEW) {
			headText.setText("新建心得");
		}
		if (call_code == MemoCallCode.BROWSE_CALL_NEW) {
			headText.setText("修改心得");
			titleEdit.setText(title);
			contentEdit.setText(content);
		}
	}

	private void delete() {
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(root);
		fileModule.createDirectory(sub);
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
					FileUtility fileModule = MainActivity.fileModule;
					fileModule.reset();
					fileModule.createDirectory(root);
					fileModule.createDirectory(sub);
					fileModule.createDirectory(detail);
					content = contentEdit.getText().toString();
					if (fileModule.saveText(content, title)) {
						Toast.makeText(MemoNewActivity.this, "吼吼，保存成功",
								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(MemoNewActivity.this,
								MemoBrowseActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("root", root);
						bundle.putString("sub", sub);
						bundle.putString("detail", detail);
						bundle.putString("title", title);
						bundle.putString("content", content);
						intent.putExtras(bundle);
						MemoNewActivity.this.startActivity(intent);
						MemoNewActivity.this.finish();
					}

				} else {
					Toast.makeText(MemoNewActivity.this, "亲，标题不能为空或只打空格哦",
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
