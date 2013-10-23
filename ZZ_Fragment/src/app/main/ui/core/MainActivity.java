package app.main.ui.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
		setContentView(R.layout.main_core);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.main_sliding_menu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.main_left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.main_right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.main_center_frame, null));
		initFileModel();
		initWidgets();

	}

	private void initWidgets() {
		t = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment(this);
		rightFragment = new RightFragment(this);
		t.replace(R.id.left_frame, leftFragment);
		t.replace(R.id.right_frame, rightFragment);

		centerFragment = new SampleListFragment(this);
		t.replace(R.id.main_center_frame, centerFragment);
		t.commit();
	}

	private void initFileModel() {
		fileModule = new FileUtility();
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
