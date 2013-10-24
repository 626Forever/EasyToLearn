package app.main.ui.memo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
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
	private EditText editTitle;
	private EditText editContent;
	private TextView textTiem;
	private Button saveBtn;
	private Button backBtn;
	private String setDate = "";
	private String root;
	private String sub;
	private String detail;
	private Calendar calendar, ca;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_new);
		editTitle = (EditText) findViewById(R.id.memo_new_title);
		editContent = (EditText) findViewById(R.id.memo_new_content);
		textTiem = (TextView) findViewById(R.id.memo_new_title_tiem);
		saveBtn = (Button) findViewById(R.id.memo_new_save);
		backBtn = (Button) findViewById(R.id.memo_new_back);

		getDirs();
		setTime();
		setListener();
	}

	private void getDirs() {
		Bundle bundle = getIntent().getExtras();
		root = bundle.getString("root");
		sub = bundle.getString("sub");
		detail = bundle.getString("detail");
	}

	private void setListener() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = editTitle.getText().toString();
				if (StringUtility.legalInput(title)) {
					FileUtility fileModule = MainActivity.fileModule;
					fileModule.reset();
					fileModule.createDirectory(root);
					fileModule.createDirectory(sub);
					fileModule.createDirectory(detail);
					String data = editContent.getText().toString();
					if (fileModule.saveText(data, title)) {
						Toast.makeText(MemoNewActivity.this, "吼吼，保存成功",
								Toast.LENGTH_SHORT).show();
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

	private void setTime() {
		Calendar.getInstance();

		calendar = Calendar.getInstance();
		ca = Calendar.getInstance();

		// 获取当前的时间
		setDate = new SimpleDateFormat("yyyy/MM/dd  hh:mm").format(new Date());
		textTiem.setText(setDate);
	}

}
