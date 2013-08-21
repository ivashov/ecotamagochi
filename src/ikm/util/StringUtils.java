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
