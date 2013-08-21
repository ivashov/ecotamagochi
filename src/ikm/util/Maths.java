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

import java.util.Random;

public class Maths {
	public static int clamp(int x, int a, int b) {
		if (a > b)
			return x;
		
		if (x < a)
			return a;
		else if (x > b)
			return b;
		else
			return x;
	}
	
	public static int sign(int x) {
		if (x > 0)
			return 1;
		else  if (x < 0)
			return -1;
		else return 0;
	}
	
	public static int posCenter(int screenSize, int objectSize) {
		return screenSize / 2 - objectSize / 2;
	}

	public static int posObject(int screenSize, int objectSize, int count, int idx) {
		int interval = (screenSize - objectSize * count) / (count + 1);
		return (interval + objectSize) * (idx + 1) - objectSize;
	}
	
	public static boolean pointInRect(int x, int y, int rx, int ry, int w, int h) {
		return x >= rx && y >= ry && x <= rx + w && y <= ry + h;
	}
	
	public static void shuffle(int[] arr, Random r) {
		for (int i = arr.length - 1; i >= 1; i--) {
			int j = r.nextInt(i + 1);
			int t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	
	public static final int dist2(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}
	
	private Maths(){}
}
