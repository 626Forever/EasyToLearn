package app.main.ui.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class RecorderActivity extends Activity {
	private final static int RESULT_CAPTURE_RECORDER = 0;
	private Button backBtn;
	private Button addBtn;
	private ListView recorderList;
	private SimpleAdapter recorderListAdapter;
	private ArrayList<Map<String, Object>> listData;
	private String sub = "";
	private String item = "";
	private String detail = "";
	private String type = "";
	FileUtility fileModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.media_recorder_list);

		recorderList = (ListView) findViewById(R.id.media_recorder_list);
		backBtn = (Button) findViewById(R.id.media_recorder_list_back);
		addBtn = (Button) findViewById(R.id.media_recorder_list_add);
		listData = new ArrayList<Map<String, Object>>();
		fileModule = new FileUtility(this);
		initAdapter();
		getFileDir();
		getList();
		setListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == RESULT_CAPTURE_RECORDER) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					saveRecorder(path);
				} else {
					Toast toast = Toast.makeText(this, "没录上。。。。",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				RecorderActivity.this.finish();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestRecorder();
			}
		});
		recorderList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String name = (String) listData.get(arg2).get(
						"media_video_item_title");
				String path = fileModule.readText(name + ".txt");
				showRecorder(path);

			}
		});
		recorderList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int loc = arg2;
				AlertDialog.Builder builder = new Builder(RecorderActivity.this);
				builder.setMessage("要删除这个录音?");
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								removeItem(loc);
							}

						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	private void getFileDir() {
		Bundle bundle = getIntent().getExtras();
		sub = bundle.getString("sub");
		item = bundle.getString("item");
		detail = bundle.getString("detail");
		type = bundle.getString("type");
	}

	private void removeItem(int postion) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(type);
		ArrayList<String> dirs = fileModule.getSubFolder();
		int size = listData.size();
		if (size > 0 && postion < size) {
			listData.remove(postion).get("item_title");
			fileModule.deleteFolder(dirs.get(postion));
			recorderListAdapter.notifyDataSetChanged();
		}
	}

	private void initAdapter() {
		String[] from = new String[] { "media_recorder_item_title" };
		int[] to = new int[] { R.id.media_recorder_item_title };
		recorderListAdapter = new SimpleAdapter(this, listData,
				R.layout.media_recorder_list_item, from, to);
		recorderList.setAdapter(recorderListAdapter);
	}

	private void getList() {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		fileModule.createDirectory(type);
		ArrayList<String> dirs = fileModule.getSubFolder();
		listData.clear();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("media_recorder_item_title",
					StringUtility.getFileName(dirs.get(i)));
			listData.add(item);
		}
		recorderListAdapter.notifyDataSetChanged();
	}

	private void requestRecorder() {
		Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
		startActivityForResult(intent, RESULT_CAPTURE_RECORDER);
	}

	private void showRecorder(String path) {
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "audio/mp3");
		startActivity(intent);
	}

	private void saveRecorder(final String path) {

		final EditText edit = new EditText(RecorderActivity.this);
		@SuppressWarnings("unused")
		AlertDialog otherDialog = new AlertDialog.Builder(RecorderActivity.this)
				.setTitle("亲，给录音命名吧")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edit)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String input = edit.getText().toString();
								if (input.equals(""))
									Toast.makeText(RecorderActivity.this,
											"名字不能是空的哦，请重新填写",
											Toast.LENGTH_SHORT).show();
								else {
									fileModule.reset();
									fileModule.createDirectory(sub);
									fileModule.createDirectory(item);
									fileModule.createDirectory(detail);
									fileModule.createDirectory(type);
									fileModule.saveText(path, input);
									Toast toast = Toast.makeText(
											RecorderActivity.this, "视频已保存",
											Toast.LENGTH_LONG);
									toast.setGravity(Gravity.BOTTOM, 0, 0);
									toast.show();
									Map<String, Object> item = new HashMap<String, Object>();
									item.put("media_video_item_title", input);
									listData.add(item);
									getList();
									dialog.dismiss();
								}

							}
						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}
}
