package app.main.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import app.main.R;

/**
 * 文件处理类,包含文件目录的创建，文件的创建删除，文件的读写，文件的保存等功能。
 * 
 * @author hzy
 * 
 */
public class FileUtility {
	private static String FUCTION_ROOT_DIR = "MySyllabus";
	private static String FUCTION_LEARN_DIR = "学习心得";
	private static String FUCTION_WRONG_DIR = "错题整理";
	private static String FUCTION_MEDIA_DIR = "课堂笔记";

	private String rootPath;
	/**
	 * SDCard存储根目录。
	 */
	private String SDPath;
	/**
	 * 当前目录
	 */
	private String previousPath;

	/**
	 * 创建项目存储目录
	 * 
	 */
	public FileUtility(Context context) {
		FUCTION_ROOT_DIR = context.getString(R.string.file_root);
		FUCTION_LEARN_DIR = context.getString(R.string.file_learn_folder);
		FUCTION_WRONG_DIR = context.getString(R.string.file_wrong_folder);
		FUCTION_MEDIA_DIR = context.getString(R.string.file_media_folder);

		SDPath = Environment.getExternalStorageDirectory() + File.separator;
		rootPath = SDPath + FUCTION_ROOT_DIR + File.separator;
		previousPath = rootPath;
		/**
		 * 新建项目存储根目录
		 */
		File file = new File(previousPath);
		file.mkdir();
	}

	public void reset() {
		previousPath = rootPath;
	}

	/**
	 * 得到存储根目录
	 * 
	 * @return 项目存储根目录
	 */
	public String getPath() {
		return this.previousPath;
	}

	/**
	 * 通过文件目录和文件名判断文件是否存在。
	 * 
	 * @param dir
	 *            文件目录
	 * @param name
	 *            文件名
	 * @return -true 文件已存在 -false 文件不存在
	 */
	@SuppressWarnings("unused")
	private boolean isFileExist(String dir, String name) {
		File file = new File(previousPath + dir + File.separator + name);
		return file.exists();
	}

	/**
	 * 给定目录名创建文件目录
	 * 
	 * @param dirname
	 *            目录名
	 * @return 创建目录文件
	 */
	public File createDirectory(String dirname) {
		previousPath = previousPath + dirname;
		File dir = new File(previousPath);
		dir.mkdir();
		previousPath = previousPath + File.separator;
		return dir;
	}

	public File createRootSubFolder(String subName) {
		this.reset();
		File file = createDirectory(subName);
		return file;
	}

	public File createPreSubFolder(String subName) {
		File file = createDirectory(subName);
		createDirectory(FUCTION_LEARN_DIR);
		Rollback();
		createDirectory(FUCTION_WRONG_DIR);
		Rollback();
		createDirectory(FUCTION_MEDIA_DIR);
		return file;
	}

	/**
	 * 给定目录和文件名，创建文件
	 * 
	 * @param name
	 *            文件名
	 * @return 创建文件
	 */
	public File createFileFromName(String name) {
		File file = new File(previousPath + name);
		try {
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 回滚当前路径的上一层
	 */
	public void Rollback() {
		String dirs[] = previousPath.split(File.separator);
		int len = dirs.length;
		String dir = dirs[0];
		for (int i = 1; i < len - 1; i++) {
			dir = dir + File.separator + dirs[i] + File.separator;
		}
		previousPath = dir;
	}

	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 * 删除根目录的一个子层文件夹
	 * 
	 * @param folderName
	 */
	public void deleteFolder(String folderName) {
		File file = new File(previousPath + folderName);
		RecursionDeleteFile(file);
	}

	public void deleteFile(String path) {
		File file = new File(path);
		RecursionDeleteFile(file);
	}

	public ArrayList<String> getSubFolder() {
		ArrayList<String> ml = new ArrayList<String>();
		ml.clear();
		File file = new File(previousPath);
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			ml.add(f.getName());
		}
		return ml;
	}

	/**
	 * 读取文件内容,得到文件读取流
	 * 
	 * @param file
	 *            读取的文件
	 * @return 文件读入流
	 */

	public InputStream readFileSDCard(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			return is;
		}

	}

	/**
	 * 根据提供的文件读入流，将内容写入新文件
	 * 
	 * 
	 * @param name
	 *            新文件的文件名
	 * @param is
	 *            旧文件的度入流
	 * @return 新文件
	 */
	public File writeFileToSdCard(String name, InputStream is) {
		File file = null;
		OutputStream os = null;

		try {
			// this.createDirectory(dir); // 创建新文件所在目录。
			file = this.createFileFromName(name); // 创建新文件
			os = new FileOutputStream(file);

			/**
			 * 写入新文件内容
			 */
			int length = is.available();
			byte buffer[] = new byte[length];
			int temp = 0;
			while ((temp = is.read(buffer)) != -1) {
				os.write(buffer, 0, temp);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
					os = null;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}

		}

		return file;

	}

	/**
	 * 保存照片到本地存储目录中
	 * 
	 * @param bitmap
	 *            照片在内存中的保存对象
	 * 
	 * @return -true照片存储成功 -false照片存储失败
	 */
	@SuppressWarnings("static-access")
	public boolean savePhoto(Bitmap bitmap, String name) {
		/**
		 * 根据当前系统时间生成照片名称
		 */
		String picture_name = name + ".jpg";

		File file = createFileFromName(picture_name);// 创建照片文件

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			/** 将照片数据输入至输出流中，输出到文件中 */
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
					bos = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}

		}
		return true;

	}

	/**
	 * 将原录音音频保存到项目存储目录中
	 * 
	 * @param origionPath
	 *            原录音音频路径
	 * @param audioDirName
	 *            新录音音频存录音存储失败
	 */
	@SuppressWarnings("static-access")
	public boolean saveAudio(String origionPath) {
		/**
		 * 根据当前系统时间生成录音名称
		 */
		String audio_name = new DateFormat().format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".3gpp";

		System.out.println(audio_name);
		Log.d("test", audio_name);
		InputStream is = readFileSDCard(new File(origionPath));// 创建原录音读入流

		/**
		 * 根据原有录音流，写入新建录音文件中
		 */
		if (is != null) {

			File file = writeFileToSdCard(audio_name, is);
			if (file != null) {
				System.out.println(file.getAbsolutePath());
				Log.d("test", file.getAbsolutePath());
				return true;
			} else
				return false;
		} else
			return false;

	}

	public boolean saveText(String data, String title) {
		String name = title + ".txt";
		File file = createFileFromName(name);
		BufferedWriter outStream = null;
		try {
			outStream = new BufferedWriter(new FileWriter(file));
			outStream.write(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (outStream != null) {
					outStream.flush();
					outStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	public String readText(String fileName) {
		File file = new File(previousPath + fileName);
		BufferedReader reader = null;
		String data = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp;
			data = reader.readLine();
			while ((temp = reader.readLine()) != null) {
				data = data + "\n" + temp;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return data;
	}

	public Bitmap readBitmap(String fileName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(previousPath + fileName,
				options);
		return bitmap;
	}

	public ArrayList<String> getImageList(String dir) {
		ArrayList<String> ml = new ArrayList<String>();
		ml.clear();
		File file = new File(dir);
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (isImageFile(f.getPath()))
				ml.add(f.getPath());
		}
		return ml;
	}

	public ArrayList<String> getAudioList(String dir) {
		ArrayList<String> ml = new ArrayList<String>();
		ml.clear();
		File file = new File(dir);
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (isAudioFile(f.getPath()))
				ml.add(f.getPath());
		}
		return ml;
	}

	/**
	 * 检测手机平板SDCard是否可用
	 * 
	 * @return -true SDCard可用 -false SDCard不可用
	 */
	public boolean isSDCardUseful() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isImageFile(String name) {
		boolean b = false;
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("jpg") || end.equals("png") || end.equals("gif")
				|| end.equals("jpeg") || end.equals("bmp"))
			b = true;
		return b;
	}

	public boolean isAudioFile(String name) {
		boolean b = false;
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("3gpp") || end.equals("mp4") || end.equals("mp3")
				|| end.equals("wma"))
			b = true;
		return b;
	}

	public boolean isTextFile(String name) {
		boolean b = false;
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("txt"))
			b = true;
		return b;
	}
}
