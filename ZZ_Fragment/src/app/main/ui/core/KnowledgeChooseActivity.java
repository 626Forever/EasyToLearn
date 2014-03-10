package app.main.ui.core;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import app.main.R;
import app.main.ui.media.RecorderActivity;
import app.main.ui.media.VideoActivity;
import app.main.ui.memo.MemoBrowseActivity;
import app.main.ui.memo.MemoCallCode;
import app.main.ui.memo.MemoNewActivity;
import app.main.ui.wrong.WrongCallCode;
import app.main.ui.wrong.WrongListActivity;
import app.main.ui.wrong.WrongNewActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class KnowledgeChooseActivity extends Activity {
	private final static int RESULT_CAPTURE_VIDEO = 0;
	private final static int RESULT_CAPTURE_RECORDER = 1;

	private Button learnBtn;
	private Button wrongBtn;
	private Button mediaBtn;

	private String subs[];
	private String sub;
	private String item;
	private String type_video;
	private String type_recorder;
	private FileUtility fileModule;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_knowledge_choose);
		learnBtn = (Button) findViewById(R.id.main_knowledge_learn_btn);
		wrongBtn = (Button) findViewById(R.id.main_knowledge_wrong_btn);
		mediaBtn = (Button) findViewById(R.id.main_knowledge_media_btn);
		fileModule = new FileUtility(this);
		subs = new String[] { this.getString(R.string.file_learn_folder),
				this.getString(R.string.file_wrong_folder),
				this.getString(R.string.file_media_folder) };

		initWidgets();
		getDirs();
		setListener();
	}

	private void getDirs() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		type_video = getString(R.string.file_media_video);
		type_recorder = getString(R.string.file_media_recorder);
	}

	private void initWidgets() {
		learnBtn.setText(subs[0]);
		wrongBtn.setText(subs[1]);
		mediaBtn.setText(subs[2]);
	}

	private void setListener() {
		learnBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openLearn(subs[0]);
			}
		});
		wrongBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openWrong(subs[1]);
			}
		});
		mediaBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openMedia(subs[2]);
			}
		});
	}

	private void openLearn(final String detail) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();
		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(
					KnowledgeChooseActivity.this);
			builder.setMessage("�ף������������ĵð�");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(KnowledgeChooseActivity.this,
							MemoNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", MemoCallCode.MAIN_CALL_NEW);
					bundle.putString("sub", sub);
					bundle.putString("item", item);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					KnowledgeChooseActivity.this.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			fileModule.reset();
			fileModule.createDirectory(sub);
			fileModule.createDirectory(item);
			fileModule.createDirectory(detail);
			String title = fileModule.getSubFolder().get(0);
			String data = fileModule.readText(title);

			Intent intent = new Intent(KnowledgeChooseActivity.this,
					MemoBrowseActivity.class);
			Bundle bundle = new Bundle();

			bundle.putString("sub", sub);
			bundle.putString("item", item);
			bundle.putString("detail", detail);
			bundle.putString("title", StringUtility.getFileName(title));
			bundle.putString("content", data);
			intent.putExtras(bundle);
			KnowledgeChooseActivity.this.startActivity(intent);
		}

	}

	private void openWrong(final String detail) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();

		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(
					KnowledgeChooseActivity.this);
			builder.setMessage("�ף���Ӵ����");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(KnowledgeChooseActivity.this,
							WrongNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", WrongCallCode.MAIN_CALL_NEW);
					bundle.putString("sub", sub);
					bundle.putString("item", item);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					KnowledgeChooseActivity.this.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			Intent intent = new Intent(KnowledgeChooseActivity.this,
					WrongListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sub", sub);
			bundle.putString("item", item);
			bundle.putString("detail", detail);
			intent.putExtras(bundle);
			KnowledgeChooseActivity.this.startActivity(intent);
		}

	}

	private void openMedia(final String detail) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		AlertDialog.Builder builder = new Builder(KnowledgeChooseActivity.this);
		builder.setMessage("�ף���ѡ��¼����¼��");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("¼��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(KnowledgeChooseActivity.this,
						VideoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				bundle.putString("type", type_video);
				intent.putExtras(bundle);
				KnowledgeChooseActivity.this.startActivity(intent);
				dialog.dismiss();
			}

		});
		builder.setNegativeButton("¼��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(KnowledgeChooseActivity.this,
						RecorderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				bundle.putString("type", type_recorder);
				intent.putExtras(bundle);
				KnowledgeChooseActivity.this.startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void requestVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
	}

	private void requestRecoder() {
		Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
		startActivityForResult(intent, RESULT_CAPTURE_RECORDER);
	}

	private void showVideo(String path) {

		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}

	private void showRecoder(String path) {
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "audio/mp3");
		startActivity(intent);
	}

	private void saveVideo(String path) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(subs[2]);
		fileModule.saveText(path, getString(R.string.file_media_video));
	}

	private void saveRecoder(String path) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(subs[2]);
		fileModule.saveText(path, getString(R.string.file_media_recorder));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode == RESULT_CAPTURE_VIDEO) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					Toast toast = Toast.makeText(this, "��Ƶ�ѱ���",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
					saveVideo(path);
				} else {
					Toast toast = Toast.makeText(this, "û¼�ϡ�������",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
				}
			}
			if (requestCode == RESULT_CAPTURE_RECORDER) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					Toast toast = Toast.makeText(this, "��Ƶ�ѱ���",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
					saveRecoder(path);
				} else {

					Toast toast = Toast.makeText(this, "û¼�ϡ�������",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);
	}

}
