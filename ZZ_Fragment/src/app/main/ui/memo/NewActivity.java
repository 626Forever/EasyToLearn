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
import app.main.util.StringCheck;

public class NewActivity extends Activity {
	private EditText edit_title;
	private EditText edit_content;
	private TextView text_tiem;
	private Button save_btn;
	private Button cancel_btn;
	private String set_date = "";
	private String root;
	private String sub;
	private String detail;
	private Calendar calendar, ca;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_new);
		edit_title = (EditText) findViewById(R.id.memo_new_title);
		edit_content = (EditText) findViewById(R.id.memo_new_content);
		text_tiem = (TextView) findViewById(R.id.memo_new_title_tiem);
		save_btn = (Button) findViewById(R.id.memo_new_save);
		cancel_btn = (Button) findViewById(R.id.memo_new_cancel);

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
		save_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = edit_title.getText().toString();
				if (StringCheck.legalInput(title)) {
					FileUtility fileModule = MainActivity.fileModule;
					fileModule.reset();
					fileModule.createDirectory(root);
					fileModule.createDirectory(sub);
					fileModule.createDirectory(detail);
					String data = edit_content.getText().toString();
					if (fileModule.saveText(data, title)) {
						Toast.makeText(NewActivity.this, "吼吼，保存成功",
								Toast.LENGTH_SHORT).show();
						NewActivity.this.finish();
					}

				}

			}
		});

		cancel_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewActivity.this.finish();
			}
		});
	}

	private void setTime() {
		Calendar.getInstance();

		calendar = Calendar.getInstance();
		ca = Calendar.getInstance();

		// 获取当前的时间
		set_date = new SimpleDateFormat("yyyy/MM/dd  hh:mm").format(new Date());
		text_tiem.setText(set_date);
	}

}
