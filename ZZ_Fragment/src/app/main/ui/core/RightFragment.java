package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.main.R;
import app.main.ui.media.MediaActivity;
import app.main.ui.memo.MemoCallCode;
import app.main.ui.memo.MemoNewActivity;
import app.main.ui.memo.MemoBrowseActivity;
import app.main.ui.wrong.WrongCallCode;
import app.main.ui.wrong.WrongNewActivity;
import app.main.ui.wrong.WrongListActivity;
import app.main.util.FileUtility;
import app.main.util.StringUtility;

public class RightFragment extends Fragment {
	private MainActivity parent;
	private SimpleAdapter RightListAdapter;
	private ListView RightList;
	private ArrayList<Map<String, Object>> data;
	private String subs[] = new String[] { "学习心得", "错题整理", "课堂笔记" };
	private String root = "";
	private String sub = "";

	public RightFragment() {
	}

	public RightFragment(MainActivity parent) {
		this.parent = parent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_right, null);
		initAdapter();
		RightList = (ListView) view.findViewById(R.id.right_menuList);
		RightList.setAdapter(RightListAdapter);
		RightList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					openLearn(subs[arg2]);
					break;
				case 1:
					openWrong(subs[arg2]);
					break;
				case 2:
					openRecoder(subs[arg2]);
					break;
				}
			}
		});
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void initAdapter() {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < subs.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuIcon", R.drawable.lxqg);
			map.put("menuText", subs[i]);
			data.add(map);
		}
		String[] from = new String[] { "menuIcon", "menuText" };
		int[] to = new int[] { R.id.right_list_icon, R.id.right_list_text };
		RightListAdapter = new SimpleAdapter(parent, data,
				R.layout.main_right_list_item, from, to);
	}

	public void setRootAndSub(String root, String sub) {
		this.root = root;
		this.sub = sub;
	}

	private void openLearn(final String detail) {
		if (root.equals("")) {
			Toast.makeText(parent, "您还没有选择学科", Toast.LENGTH_SHORT).show();
			return;
		}
		if (sub.equals("")) {
			Toast.makeText(parent, "您还没有选择知识条目", Toast.LENGTH_SHORT).show();
			return;
		}
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(root);
		fileModule.createDirectory(sub);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();
		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(parent);
			builder.setMessage("亲，来创建您的心得吧");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getActivity(),
							MemoNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", MemoCallCode.MAIN_CALL_NEW);
					bundle.putString("root", root);
					bundle.putString("sub", sub);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					parent.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			fileModule.reset();
			fileModule.createDirectory(root);
			fileModule.createDirectory(sub);
			fileModule.createDirectory(detail);
			String title = fileModule.getSubFolder().get(0);
			String data = fileModule.readText(title);

			Intent intent = new Intent(getActivity(), MemoBrowseActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("root", root);
			bundle.putString("sub", sub);
			bundle.putString("detail", detail);
			bundle.putString("title", StringUtility.getFileName(title));
			bundle.putString("content", data);
			intent.putExtras(bundle);
			parent.startActivity(intent);
		}

	}

	private void openWrong(final String detail) {
		if (root.equals("")) {
			Toast.makeText(parent, "您还没有选择学科", Toast.LENGTH_SHORT).show();
			return;
		}
		if (sub.equals("")) {
			Toast.makeText(parent, "您还没有选择知识条目", Toast.LENGTH_SHORT).show();
			return;
		}

		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(root);
		fileModule.createDirectory(sub);
		fileModule.createDirectory(detail);
		ArrayList<String> dirs = fileModule.getSubFolder();

		if (dirs.size() == 0) {
			AlertDialog.Builder builder = new Builder(parent);
			builder.setMessage("亲，添加错题吧");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getActivity(),
							WrongNewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("call_code", WrongCallCode.MAIN_CALL_NEW);
					bundle.putString("root", root);
					bundle.putString("sub", sub);
					bundle.putString("detail", detail);
					intent.putExtras(bundle);
					parent.startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} else {
			Intent intent = new Intent(getActivity(), WrongListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("root", root);
			bundle.putString("sub", sub);
			bundle.putString("detail", detail);
			intent.putExtras(bundle);
			parent.startActivity(intent);
		}

	}

	private void openRecoder(final String detail) {
		Intent intent = new Intent(getActivity(), MediaActivity.class);
		parent.startActivity(intent);
	}
}
