package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import app.main.R;
import app.main.url.SlidingMenu;
import app.main.util.FileUtility;

public class MainActivity extends FragmentActivity {

	private SlidingMenu mSlidingMenu;
	private LeftFragment leftFragment;
	private RightFragment rightFragment;
	private SampleListFragment centerFragment;
	private FragmentTransaction t;
	public static FileUtility fileModule;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_center);
		fileModule = new FileUtility();
		Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
		startActivity(intent);

	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	public void setCenterListRootDir(String dir) {
		centerFragment.setRootDirAndNofiy(dir);
	}

	public void setRightListRootDir(String root, String sub) {
		rightFragment.setRootAndSub(root, sub);
	}
}
