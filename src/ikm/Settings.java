/* 
 * This file is part of Ecotamagochi.
 * Copyright (C) 2013, Ivashov Kirill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
