package app.main.ui.wrong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import app.main.R;
import app.main.ui.core.KnowledgeItemActivity;
import app.main.ui.core.MainActivity;
import app.main.util.FileUtility;

public class WrongListActivity extends Activity {
	private final static int ADD_NEW_ITEM = 0;
	private final static int BROWSE_ITEM = 1;
	private Button backBtn;
	private Button addBtn;
	private ListView wrongList;
	private SimpleAdapter wrongListAdapter;
	private ArrayList<Map<String, Object>> listData;
	private String sub = "";
	private String item = "";
	private String detail = "";
	FileUtility fileModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.wrong_list);

		wrongList = (ListView) findViewById(R.id.wrong_list);
		backBtn = (Button) findViewById(R.id.wrong_list_back);
		addBtn = (Button) findViewById(R.id.wrong_list_add);
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
		switch (requestCode) {
		case ADD_NEW_ITEM:
			this.getList();
			break;
		case BROWSE_ITEM:
			this.getList();
			break;
		}

	}

	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				WrongListActivity.this.finish();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(WrongListActivity.this,
						WrongNewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("call_code", WrongCallCode.LIST_CALL_NEW);

				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				intent.putExtras(bundle);
				WrongListActivity.this.startActivityForResult(intent,
						ADD_NEW_ITEM);
			}
		});
		wrongList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String name = (String) listData.get(arg2).get(
						"wrong_item_title");
				Intent intent = new Intent(WrongListActivity.this,
						WrongBrowseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("call_code", WrongCallCode.LIST_CALL_BROWSE);

				bundle.putString("sub", sub);
				bundle.putString("item", item);
				bundle.putString("detail", detail);
				bundle.putString("name", name);
				intent.putExtras(bundle);
				WrongListActivity.this.startActivityForResult(intent,
						BROWSE_ITEM);

			}
		});
		wrongList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int loc = arg2;
				AlertDialog.Builder builder = new Builder(
						WrongListActivity.this);
				builder.setMessage("要删除这个错题?");
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
	}

	private void removeItem(int postion) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();

		int size = listData.size();
		if (size > 0 && postion < size) {
			listData.remove(postion).get("item_title");
			fileModule.deleteFolder(dirs.get(postion));
			wrongListAdapter.notifyDataSetChanged();
		}
	}

	private void initAdapter() {
		String[] from = new String[] { "wrong_item_title" };
		int[] to = new int[] { R.id.wrong_item_title };
		wrongListAdapter = new SimpleAdapter(this, listData,
				R.layout.wrong_list_item, from, to);
		wrongList.setAdapter(wrongListAdapter);
	}

	private void getList() {

		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createDirectory(item);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();
		listData.clear();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("wrong_item_title", dirs.get(i));
			listData.add(item);
		}
		wrongListAdapter.notifyDataSetChanged();
	}
}
