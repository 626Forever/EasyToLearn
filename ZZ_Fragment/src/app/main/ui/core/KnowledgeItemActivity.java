package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;

public class KnowledgeItemActivity extends Activity {
	private EditText edit;
	private Button backBtn;
	private Button addBtn;
	private ListView knowledgeItemList;
	private SimpleAdapter knowledgeItemAdapter;
	private AlertDialog addDialog;

	private String input;
	private String sub;
	private String item;
	private ArrayList<Map<String, Object>> listData;
	FileUtility fileModule;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_knowledge);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		knowledgeItemList = (ListView) findViewById(R.id.main_knowledge_list);
		backBtn = (Button) findViewById(R.id.main_knowledge_back);
		addBtn = (Button) findViewById(R.id.main_knowledge_add);
		fileModule = new FileUtility(this);
		listData = new ArrayList<Map<String, Object>>();
		initAdapter();
		knowledgeItemList.setAdapter(knowledgeItemAdapter);
		getDirs();
		getData();
		setListener();
	}

	private void initAdapter() {

		String[] from = new String[] { "item_title" };
		int[] to = new int[] { R.id.main_knowledge_title };
		knowledgeItemAdapter = new SimpleAdapter(KnowledgeItemActivity.this,
				listData, R.layout.main_knowledge_item, from, to);

	}

	private void getDirs() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		sub = bundle.getString("sub");
	}

	private void getData() {
		fileModule.reset();
		fileModule.createDirectory(sub);
		ArrayList<String> dirs = fileModule.getSubFolder();
		listData.clear();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("item_title", dirs.get(i));
			listData.add(item);
		}
		knowledgeItemAdapter.notifyDataSetChanged();
	}

	private void addItem(String title) {
		fileModule.reset();
		fileModule.createDirectory(sub);
		fileModule.createPreSubFolder(title);
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("list_title", title);
		listData.add(item);
		getData();
	}

	private void setListener() {
		knowledgeItemList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				item = (String) listData.get(arg2).get("item_title");
				openKnowledgeChoose(sub, item);
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				KnowledgeItemActivity.this.finish();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				edit = new EditText(KnowledgeItemActivity.this);
				addDialog = new AlertDialog.Builder(KnowledgeItemActivity.this)
						.setTitle("请输入学习条目")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(edit)
						.setPositiveButton(
								"确定",
								new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										input = edit.getText().toString();
										if (input.equals("")) {
											Toast.makeText(
													KnowledgeItemActivity.this,
													"请正确填写要建立的学习条目",
													Toast.LENGTH_SHORT).show();
											return;
										}
										for (int i = 0; i < listData.size(); i++) {
											String s = (String) listData.get(i)
													.get("item_title");
											if (input.equals(s)) {
												Toast.makeText(
														KnowledgeItemActivity.this,
														"您已经建立该条目",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
										}
										addItem(input);
									}
								})
						.setNegativeButton(
								"取消",
								new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										addDialog.dismiss();
									}
								}).show();
			}
		});
	}

	private void openKnowledgeChoose(String sub, String item) {
		Intent intent = new Intent(KnowledgeItemActivity.this,
				KnowledgeChooseActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("sub", sub);
		bundle.putString("item", item);
		intent.putExtras(bundle);
		KnowledgeItemActivity.this.startActivity(intent);
	}
}
