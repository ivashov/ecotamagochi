package ikm;

import ikm.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;

public class Settings {
	private Settings(){}
	
	public static final Hashtable settings = new Hashtable();
	
	public static void loadSettings(Reader reader) throws IOException {		
		String line = null;
		while ((line = StringUtils.readLine(reader)) != null) {
			String[] tokens = StringUtils.split(line, ' ');
			if (tokens.length < 3 || !tokens[1].equals("=")) {
				Log.err("Settings: wrong config line \"" + line + "\"");
				continue;
			}
			
			try {
				int value = Integer.parseInt(tokens[2]);
				settings.put(tokens[0], new Integer(value));
			} catch (NumberFormatException ex) {
				Log.err("Settings: wrong value \"" + tokens[2] + "\"");
			}
		}
		
		dump();
	}
	
	private static void dump() {
		Enumeration en = settings.keys();
		
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			Log.log("Settings: " + key + " = " + ((Integer) settings.get(key)).intValue()); 
		}
	}
	
	public static int getEntry(String key) {
		Integer ret = (Integer) settings.get(key);
		if (ret == null)
			throw new RuntimeException("No such entry in config: " + key);
			
		return ret.intValue();
	}
}
