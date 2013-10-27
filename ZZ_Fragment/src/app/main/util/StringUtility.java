package app.main.util;

public class StringUtility {
	public static boolean legalInput(String input) {
		if (input == null)
			return false;
		String temp = input.trim();
		if (temp.length() == 0)
			return false;
		return true;
	}

	public static String getFileName(String file) {
		String strs[] = file.split("\\.");

		return strs[0];
	}
}
