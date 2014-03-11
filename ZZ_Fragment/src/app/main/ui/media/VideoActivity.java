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
import android.provider.MediaStore;
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

public class VideoActivity extends Activity {
	private final static int RESULT_CAPTURE_VIDEO = 0;
	private Button backBtn;
	private Button addBtn;
	private ListView videoList;
	private SimpleAdapter videoListAdapter;
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
		setContentView(R.layout.media_video_list);

		videoList = (ListView) findViewById(R.id.media_video_list);
		backBtn = (Button) findViewById(R.id.media_video_list_back);
		addBtn = (Button) findViewById(R.id.media_video_list_add);
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
			if (requestCode == RESULT_CAPTURE_VIDEO) {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getData().toString();
					saveVideo(path);

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
				VideoActivity.this.finish();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestVideo();
			}
		});
		videoList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String name = (String) listData.get(arg2).get(
						"media_video_item_title");
				String path = fileModule.readText(name + ".txt");
				showVideo(path);

			}
		});
		videoList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int loc = arg2;
				AlertDialog.Builder builder = new Builder(VideoActivity.this);
				builder.setMessage("要删除这个录像?");
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
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
			videoListAdapter.notifyDataSetChanged();
		}
	}

	private void initAdapter() {
		String[] from = new String[] { "media_video_item_title" };
		int[] to = new int[] { R.id.media_video_item_title };
		videoListAdapter = new SimpleAdapter(this, listData,
				R.layout.media_video_list_item, from, to);
		videoList.setAdapter(videoListAdapter);
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
			item.put("media_video_item_title",
					StringUtility.getFileName(dirs.get(i)));
			listData.add(item);
		}
		videoListAdapter.notifyDataSetChanged();
	}

	private void requestVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
	}

	private void showVideo(String path) {
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}

	private void saveVideo(final String path) {

		final EditText edit = new EditText(VideoActivity.this);
		@SuppressWarnings("unused")
		AlertDialog otherDialog = new AlertDialog.Builder(VideoActivity.this)
				.setTitle("亲，给录像命名吧")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edit)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String input = edit.getText().toString();
								if (input.equals(""))
									Toast.makeText(VideoActivity.this,
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
											VideoActivity.this, "视频已保存",
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
