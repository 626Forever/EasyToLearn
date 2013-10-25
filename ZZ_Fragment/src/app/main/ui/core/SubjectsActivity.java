package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private String subjects[] = new String[] { "��ѧ", "����", "Ӣ��", "����", "��ѧ",
			"����", "����", "����", "��ʷ", "����" };
	private int choose = 0;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_subjects);
		subjectsList = (ListView) findViewById(R.id.main_subjects_list);
		backBtn = (Button) findViewById(R.id.main_subjects_back);
		addBtn = (Button) findViewById(R.id.main_subjects_add);

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
		FileUtility fileModule = MainActivity.fileModule;
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

	public void addItem(String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item_title", title);
		listData.add(map);
		MainActivity.fileModule.createRootSubFolder(title);
		subjectsListAdapter.notifyDataSetChanged();

	}

	public void removeItem(int postion) {
		int size = listData.size();
		if (size > 0 && postion < size) {
			String name = (String) listData.remove(postion).get("item_title");
			subjectsListAdapter.notifyDataSetChanged();
			MainActivity.fileModule.deleteRootSubFolder(name);
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

		backBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		addBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				subjectDialog = new AlertDialog.Builder(SubjectsActivity.this)
						.setTitle("ѡ��Ҫ�����Ŀ�Ŀ")
						.setSingleChoiceItems(subjects, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										choose = which;
									}
								})
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										input = subjects[choose];
										if (input.equals("����")) {
											subjectDialog.dismiss();
											showOtherDialog();
											return;
										}
										for (int i = 0; i < listData.size(); i++) {
											String s = (String) listData.get(i)
													.get("menuText");
											if (input.equals(s)) {
												Toast.makeText(
														SubjectsActivity.this,
														"���Ѿ������ÿ�Ŀ",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
										}
										addItem(input);
									}
								}).setNegativeButton("ȡ��", null).show();
			}
		});
	}

	private void showOtherDialog() {
		edit = new EditText(SubjectsActivity.this);
		otherDialog = new AlertDialog.Builder(SubjectsActivity.this)
				.setTitle("�������Ŀ��")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edit)
				.setPositiveButton("ȷ��",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								input = edit.getText().toString();
								if (input.equals(""))
									Toast.makeText(SubjectsActivity.this,
											"����д��Ŀ����", Toast.LENGTH_SHORT)
											.show();
								for (int i = 0; i < listData.size(); i++) {
									String s = (String) listData.get(i).get(
											"menuText");
									if (input.equals(s)) {
										Toast.makeText(SubjectsActivity.this,
												"���Ѿ������ÿ�Ŀ", Toast.LENGTH_SHORT)
												.show();
										return;
									}
								}
								addItem(input);
							}
						})
				.setNegativeButton("ȡ��",
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
}
