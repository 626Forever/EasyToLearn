package app.main.ui.core.alarm;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Toast;
import app.main.R;

public class AlarmReceiver extends BroadcastReceiver {
	Context context;
	AudioManager audio_manager;
	SoundPool soundPool;
	HashMap<Integer, Integer> soundPoolMap;

	@SuppressLint("UseSparseArrays")
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		this.context = context;
		audio_manager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(context, R.raw.alarm, 1));
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// TODO Auto-generated method stub
				playSound(1, 0);
			}
		});
		Toast.makeText(context, "学习时间到啦，休息休息吧！", Toast.LENGTH_LONG).show();
	}

	public void playSound(int sound, int loop) {

		float streamVolumeCurrent = audio_manager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		float streamVolumeMax = audio_manager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;

		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
}
