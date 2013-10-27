package app.main.ui.media;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import app.main.R;

public class MediaActivity extends Activity {
	private final static int RESULT_CAPTURE_VIDEO = 0;
	private final static int RESULT_CAPTURE_RECORDER = 1;
	private Button videoBtn;
	private Button recoderBtn;
	private String sub;
	private String item;
	private String detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.media_core);
		videoBtn = (Button) findViewById(R.id.button1);
		recoderBtn = (Button) findViewById(R.id.button2);
		getData();
		setListener();
	}

	private void getData() {
		Bundle bundle = getIntent().getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		detail = bundle.getString("detail");

	}

	private void setListener() {
		videoBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
			}
		});
		recoderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
				startActivityForResult(intent, RESULT_CAPTURE_RECORDER);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode == RESULT_CAPTURE_VIDEO) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					Toast toast = Toast.makeText(this, "视频已保存在:" + path,
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();

					Uri uri = Uri.parse(path);
					// 调用系统自带的播放器
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "video/mp4");
					startActivity(intent);
				}
			}
			if (requestCode == RESULT_CAPTURE_RECORDER) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					Toast toast = Toast.makeText(this, "音频已保存在:" + path,
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
					Uri uri = Uri.parse(path);
					// 调用系统自带的播放器
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "audio/mp3");

					startActivity(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
