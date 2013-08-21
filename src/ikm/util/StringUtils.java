package ikm.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

public class StringUtils {
	private StringUtils(){}
	
	private static Vector arr = new Vector();
	public static String[] split(String str, char d) {
		String[] ret = null;
		synchronized(arr) {
			arr.setSize(0);
			
			int last = 0;
			boolean lastSpace = true;
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
			
				if (c == d && !lastSpace) {
					arr.addElement(str.substring(last, i));
					lastSpace = true;
				} else if (c != d && lastSpace) {
					last = i;
					lastSpace = false;
				}
			}
			
			if (!lastSpace)
				arr.addElement(str.substring(last, str.length()));
			
			if (!arr.isEmpty()) {
				ret = new String[arr.size()];
				arr.copyInto(ret);
			}
		}
		return ret;
	}
	
	public static String readLine(Reader reader) throws IOException {
		StringBuffer line = new StringBuffer();
		int c = reader.read();
		while (c != -1 && c != '\n') {
			if (c != '\r')
				line.append((char)c);
			c = reader.read();
		}
		
		if (line.length() == 0)
			return null;
		else
			return line.toString();
	}
}
