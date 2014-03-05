package app.main.ui.help;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import app.main.R;

public class AuthorActivity extends Activity {
	TextView infoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_author_layout);
		infoText = (TextView) findViewById(R.id.author_text);
		String h = readAuthor();
		infoText.setText(h);

	}

	public String readAuthor() {
		String fileName = "author.txt"; // ÎÄ¼þÃû×Ö
		String res = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "GB2312");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);
	}
}
