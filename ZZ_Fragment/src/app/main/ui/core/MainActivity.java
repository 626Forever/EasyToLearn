package app.main.ui.core;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import app.main.R;

public class MainActivity extends FragmentActivity {

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_center);

		Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
		startActivity(intent);

	}

}
