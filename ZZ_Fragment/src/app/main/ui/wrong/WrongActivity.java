package app.main.ui.wrong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import app.main.R;

public class WrongActivity extends Activity {
	ListView wrong_list;
	List<Map<String, Object>> list_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrong_core);
		wrong_list = (ListView) findViewById(R.id.wrong_list);
		list_data = getList();
		initAdapter();
	}

	private boolean initAdapter() {
		String[] from = new String[] { "wrong_item_title", "wrong_item_icon",
				"wrong_item_contect" };
		int[] to = new int[] { R.id.wrong_item_title, R.id.wrong_item_icon,
				R.id.wrong_item_contect };
		SimpleAdapter adapter = new SimpleAdapter(this, list_data,
				R.layout.main_center_list_item, from, to);
		wrong_list.setAdapter(adapter);
		return true;
	}

	public List<Map<String, Object>> getList() {
		Map<String, Object> item1 = new HashMap<String, Object>();
		item1.put("list_title", getString(R.string.title1));
		item1.put("list_image", R.drawable.booklist_menu_about);
		item1.put("list_contect", getString(R.string.test));
		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("list_title", getString(R.string.title1));
		item2.put("list_image", R.drawable.booklist_menu_about);
		item2.put("list_contect", getString(R.string.test));
		Map<String, Object> item3 = new HashMap<String, Object>();
		item3.put("list_title", getString(R.string.title1));
		item3.put("list_image", R.drawable.booklist_menu_about);
		item3.put("list_contect", getString(R.string.test));
		Map<String, Object> item4 = new HashMap<String, Object>();
		item4.put("list_title", getString(R.string.title1));
		item4.put("list_image", R.drawable.booklist_menu_about);
		item4.put("list_contect", getString(R.string.test));
		Map<String, Object> item5 = new HashMap<String, Object>();
		item5.put("list_title", getString(R.string.title1));
		item5.put("list_image", R.drawable.booklist_menu_about);
		item5.put("list_contect", getString(R.string.test));
		Map<String, Object> item6 = new HashMap<String, Object>();
		item6.put("list_title", getString(R.string.title1));
		item6.put("list_image", R.drawable.booklist_menu_about);
		item6.put("list_contect", getString(R.string.test));
		Map<String, Object> item7 = new HashMap<String, Object>();
		item7.put("list_title", getString(R.string.title1));
		item7.put("list_image", R.drawable.p7);
		item7.put("list_contect", getString(R.string.test));
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(item1);
		data.add(item2);
		data.add(item3);
		data.add(item4);
		data.add(item5);
		data.add(item6);
		data.add(item7);

		return data;
	}
}
