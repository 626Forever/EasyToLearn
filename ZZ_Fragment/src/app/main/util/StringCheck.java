package app.main.util;

public class StringCheck {
	public static boolean legalInput(String input) {
		if (input == null)
			return false;
		String temp = input.trim();
		if (temp.length() == 0)
			return false;
		return true;
	}
}
