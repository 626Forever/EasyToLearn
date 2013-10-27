package app.main.ui.core;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import app.main.R;

public class MainActivity extends FragmentActivity {
	private Button start;
	private Button help;
	private Button author;
	private Button quit;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_center);
		start = (Button) findViewById(R.id.main_start_btn);
		help = (Button) findViewById(R.id.main_help_btn);
		author = (Button) findViewById(R.id.main_author_btn);
		quit = (Button) findViewById(R.id.main_quit_btn);
		setListener();
	}

	private void setListener() {
		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SubjectsActivity.class);
				startActivity(intent);
			}
		});
		help.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		author.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		quit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
}
