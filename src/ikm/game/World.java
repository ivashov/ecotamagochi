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

package ikm.game;

import ikm.util.ByteArray;

public class World {
	private int trashCount;
	public void addTrash(int count) {
		trashCount += count;
		if (trashCount < 0)
			trashCount = 0;
	}
	
	public void save(ByteArray arr) {
		arr.writeInt(trashCount);
	}
	
	public void load(ByteArray arr) {
		trashCount = arr.readInt();
	}

	public int getTrash() {
		return trashCount;
	}
}
