package ikm;

import ikm.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

public class Translation {
	public static Hashtable trans = new Hashtable();
	
	public static void loadTranslation(Reader reader) throws IOException {
		String line = null;
		while ((line = StringUtils.readLine(reader)) != null) {
			String[] arr = StringUtils.split(line, ':');
			if (arr.length < 2) {
				continue;
			}
			
			trans.put(arr[0], arr[1]);
		}
	}
	
	public static String tr(String key) {
		String str = (String) trans.get(key);
		if (str == null)
			return "<no data>";
		else
			return str;
	}
}
