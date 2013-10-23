package app.main.ui.memo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import app.main.R;

public class SeeActivity extends Activity {
	private TextView title_browse;
	private TextView content_browse;
	private String name;
	private String data;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_browse);
		title_browse = (TextView) findViewById(R.id.memo_browse_title);
		content_browse = (TextView) findViewById(R.id.memo_browse_content);
		getData();
		title_browse.setText(name);
		content_browse.setText(data);

	}

	private void getData() {
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("name");
		data = bundle.getString("data");
	}

}
