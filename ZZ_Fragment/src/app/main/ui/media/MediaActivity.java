package app.main.ui.media;

import android.app.Activity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_core);
		videoBtn = (Button) findViewById(R.id.button1);
		recoderBtn = (Button) findViewById(R.id.button2);
		setListener();
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
			if (resultCode == Activity.RESULT_OK) {

				if (requestCode == RESULT_CAPTURE_VIDEO) {
					String path = data.getData().toString();

					Toast toast = Toast.makeText(this, "视频已保存在:" + path,
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
