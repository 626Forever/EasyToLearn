package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;

public class LeftFragment extends Fragment {
	private MainActivity parent;
	private SimpleAdapter LeftListAdapter;
	private ListView LeftList;
	private Button LeftButton;
	private EditText edit;
	private AlertDialog otherDialog;
	private AlertDialog subjectDialog;
	private ArrayList<Map<String, Object>> listItems;
	private String input;
	private String subjects[] = new String[] { "数学", "语文", "英语", "物理", "化学",
			"生物", "政治", "地理", "历史", "其他" };
	private int choose = 0;

	public LeftFragment() {

	}

	public LeftFragment(MainActivity parent) {
		this.parent = parent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_left, null);
		LeftList = (ListView) view.findViewById(R.id.leftList);
		initAdapter();
		getData();
		LeftList.setAdapter(LeftListAdapter);
		LeftList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String sub = (String) listItems.get(arg2).get("menuText");
				parent.setCenterListRootDir(sub);
			}
		});

		LeftButton = (Button) view.findViewById(R.id.leftButton);
		LeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				subjectDialog = new AlertDialog.Builder(parent)
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
										for (int i = 0; i < listItems.size(); i++) {
											String s = (String) listItems
													.get(i).get("menuText");
											if (input.equals(s)) {
												Toast.makeText(parent,
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
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initAdapter() {
		listItems = new ArrayList<Map<String, Object>>();
		String[] from = new String[] { "menuIcon", "menuText" };
		int[] to = new int[] { R.id.left_list_item_icon,
				R.id.left_list_item_title };
		LeftListAdapter = new SimpleAdapter(parent, listItems,
				R.layout.main_left_list_item, from, to);

	}

	private void getData() {
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		ArrayList<String> dirs = fileModule.getSubFolder();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuIcon", R.drawable.main_left_item_icon);
			map.put("menuText", dirs.get(i));
			listItems.add(map);

		}

	}

	public void addItem(String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuIcon", R.drawable.main_left_item_icon);
		map.put("menuText", title);
		listItems.add(map);
		MainActivity.fileModule.createRootSubFolder(title);
		LeftListAdapter.notifyDataSetChanged();

	}

	public void removeItem(int postion) {
		int size = listItems.size();
		if (size > 0 && postion < size) {
			String name = (String) listItems.remove(postion).get("menuText");
			LeftListAdapter.notifyDataSetChanged();
			MainActivity.fileModule.deleteRootSubFolder(name);
		}
	}

	private void showOtherDialog() {
		edit = new EditText(parent);
		otherDialog = new AlertDialog.Builder(parent)
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
									Toast.makeText(parent, "请填写科目名称",
											Toast.LENGTH_SHORT).show();
								for (int i = 0; i < listItems.size(); i++) {
									String s = (String) listItems.get(i).get(
											"menuText");
									if (input.equals(s)) {
										Toast.makeText(parent, "您已经建立该科目",
												Toast.LENGTH_SHORT).show();
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

}
