package app.main.ui.core;

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
import android.view.KeyEvent;
import android.view.View;
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

public class SubjectsActivity extends Activity {
	private EditText edit;
	private Button backBtn;
	private Button addBtn;
	private ListView subjectsList;
	private AlertDialog otherDialog;
	private AlertDialog subjectDialog;

	private SimpleAdapter subjectsListAdapter;
	private ArrayList<Map<String, Object>> listData;
	private String input;
	private String subjects[] = new String[] { "数学", "语文", "英语", "物理", "化学",
			"生物", "政治", "地理", "历史", "其他" };
	private FileUtility fileModule;
	private int choose = 0;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_subjects);
		subjectsList = (ListView) findViewById(R.id.main_subjects_list);
		backBtn = (Button) findViewById(R.id.main_subjects_back);
		addBtn = (Button) findViewById(R.id.main_subjects_add);
		fileModule = new FileUtility(this);
		listData = new ArrayList<Map<String, Object>>();
		initAdapter();
		subjectsList.setAdapter(subjectsListAdapter);
		getData();
		setListener();
	}

	private void initAdapter() {

		String[] from = new String[] { "item_title" };
		int[] to = new int[] { R.id.main_subject_title };
		subjectsListAdapter = new SimpleAdapter(SubjectsActivity.this,
				listData, R.layout.main_subjects_item, from, to);

	}

	private void getData() {

		fileModule.reset();
		listData.clear();
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("item_title", dirs.get(i));
			listData.add(map);

		}
		subjectsListAdapter.notifyDataSetChanged();
	}

	private void addItem(String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item_title", title);
		listData.add(map);
		fileModule.createRootSubFolder(title);
		getData();
	}

	private void removeItem(int postion) {
		fileModule.reset();
		ArrayList<String> dirs = fileModule.getSubFolder();

		int size = listData.size();
		if (size > 0 && postion < size) {
			listData.remove(postion).get("item_title");
			fileModule.deleteFolder(dirs.get(postion));
			subjectsListAdapter.notifyDataSetChanged();
		}
	}

	private void setListener() {
		subjectsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String sub = (String) listData.get(arg2).get("item_title");
				openKnowledgeItem(sub);

			}
		});
		subjectsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int loc = arg2;
				AlertDialog.Builder builder = new Builder(SubjectsActivity.this);
				builder.setMessage("要删除这个科目吗?");
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

		backBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				SubjectsActivity.this.setResult(RESULT_OK);
				SubjectsActivity.this.finish();
			}
		});

		addBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				subjectDialog = new AlertDialog.Builder(SubjectsActivity.this)
						.setTitle("选择要建立的科目")
						.setSingleChoiceItems(subjects, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										choose = which;
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										input = subjects[choose];
										if (input.equals("其他")) {
											subjectDialog.dismiss();
											showOtherDialog();
											return;
										}
										for (int i = 0; i < listData.size(); i++) {
											String s = (String) listData.get(i)
													.get("item_title");
											if (input.equals(s)) {
												Toast.makeText(
														SubjectsActivity.this,
														"您已经建立该科目",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
										}
										addItem(input);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}

	private void showOtherDialog() {
		edit = new EditText(SubjectsActivity.this);
		otherDialog = new AlertDialog.Builder(SubjectsActivity.this)
				.setTitle("请输入科目名")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edit)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								input = edit.getText().toString();
								if (input.equals(""))
									Toast.makeText(SubjectsActivity.this,
											"请填写科目名称", Toast.LENGTH_SHORT)
											.show();
								for (int i = 0; i < listData.size(); i++) {
									String s = (String) listData.get(i).get(
											"item_title");
									if (input.equals(s)) {
										Toast.makeText(SubjectsActivity.this,
												"您已经建立该科目", Toast.LENGTH_SHORT)
												.show();
										return;
									}
								}
								addItem(input);
							}
						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								otherDialog.dismiss();
							}
						}).show();
	}

	private void openKnowledgeItem(String sub) {
		Intent intent = new Intent(SubjectsActivity.this,
				KnowledgeItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("sub", sub);
		intent.putExtras(bundle);
		SubjectsActivity.this.startActivity(intent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SubjectsActivity.this.setResult(RESULT_OK);
			SubjectsActivity.this.finish();
		}
		return false;
	}
}
