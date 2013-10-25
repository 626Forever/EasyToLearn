package app.main.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
	private String sub = "";
	private ArrayList<Map<String, Object>> listData;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.main_knowledge);
		knowledgeItemList = (ListView) findViewById(R.id.main_knowledge_list);
		backBtn = (Button) findViewById(R.id.main_knowledge_back);
		addBtn = (Button) findViewById(R.id.main_knowledge_add);

		listData = new ArrayList<Map<String, Object>>();
		initAdapter();
		knowledgeItemList.setAdapter(knowledgeItemAdapter);
		getList();

	}

	private void initAdapter() {

		String[] from = new String[] { "item_title" };
		int[] to = new int[] { R.id.main_knowledge_title };
		knowledgeItemAdapter = new SimpleAdapter(KnowledgeItemActivity.this,
				listData, R.layout.main_knowledge_item, from, to);

	}

	private void getList() {
		FileUtility fileModule = MainActivity.fileModule;
		fileModule.reset();
		fileModule.createDirectory(sub);
		ArrayList<String> dirs = fileModule.getSubFolder();
		listData.clear();
		for (int i = 0; i < dirs.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("list_title", dirs.get(i));
			listData.add(item);
		}
	}
}
