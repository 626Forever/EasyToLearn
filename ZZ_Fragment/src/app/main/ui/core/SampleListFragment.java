package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.main.R;
import app.main.util.FileUtility;

public class SampleListFragment extends ListFragment {
	private MainActivity parent;
	private ImageView iv_left;
	private ImageView iv_right;
	private SimpleAdapter adapter;
	private AlertDialog addDialog;
	private EditText edit;
	private String input;
	private String rootDir = "";
	private ArrayList<Map<String, Object>> data;

	public SampleListFragment(MainActivity parent) {
		this.parent = parent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.main_center, null);
		iv_left = (ImageView) mView.findViewById(R.id.main_center_left_image);
		iv_right = (ImageView) mView.findViewById(R.id.main_center_right_image);
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		data = new ArrayList<Map<String, Object>>();
		getList();
		String[] from = new String[] { "list_title", "list_image",
				"list_contect" };
		int[] to = new int[] { R.id.main_center_list_title,
				R.id.main_center_list_icon, R.id.main_center_list_contect };
		adapter = new SimpleAdapter(parent, data,
				R.layout.main_center_list_item, from, to);

		setListAdapter(adapter);

		iv_left.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				((MainActivity) getActivity()).showLeft();
			}
		});

		iv_right.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (rootDir.equals("")) {
					Toast.makeText(parent, "请选择学习科目", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				edit = new EditText(parent);
				addDialog = new AlertDialog.Builder(parent)
						.setTitle("请输入科目名")
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
											Toast.makeText(parent,
													"请正确填写要建立的学习条目",
													Toast.LENGTH_SHORT).show();
											return;
										}
										for (int i = 0; i < data.size(); i++) {
											String s = (String) data.get(i)
													.get("list_title");
											if (input.equals(s)) {
												Toast.makeText(parent,
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		parent.showRight();
		parent.setRightListRootDir(rootDir,
				(String) data.get(position).get("list_title"));

	}

	public void getList() {
		if (rootDir.equals("")) {
			data.clear();
			return;
		}
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(rootDir);
		ArrayList<String> dirs = fileModule.getSubFolder();
		data.clear();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("list_title", dirs.get(i));
			item.put("list_image", R.drawable.booklist_menu_about);
			item.put("list_contect", getString(R.string.test));
			data.add(item);
		}
	}

	public void setRootDirAndNofiy(String dir) {
		this.rootDir = dir;
		this.getList();
		this.adapter.notifyDataSetChanged();
	}

	public void addItem(String title) {
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(rootDir);
		fileModule.createPreSubFolder(title);
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("list_title", title);
		item.put("list_image", R.drawable.booklist_menu_about);
		item.put("list_contect", getString(R.string.test));
		data.add(item);
		adapter.notifyDataSetChanged();
	}
}
