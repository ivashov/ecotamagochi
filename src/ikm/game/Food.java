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

import ikm.Settings;
import ikm.util.ByteArray;
import ikm.util.Maths;

public class Food {
	public static final int ADAPTATION_RATE = Settings.getEntry("adaptation_rate");
	public static final int ADAPTATION_MAX = Settings.getEntry("adaptation_max");
	
	private String name;
	
	private int hungerEffect;
	private int healthEffect;
	private int moodEffect;
	private int trash;
	private Object data;
	
	private int adaptation = 0;
	private int foodCount = 1;
	
	public Food(String name, int hunger, int trash_, int health, int mood) {
		this.name = name;
		
		hungerEffect = hunger;
		trash = trash_;
		healthEffect = health;
		moodEffect = mood;
	}
	
	public void increaseCount(int count) {
		foodCount += count;
	}

	public void feed(Character character, World world) {
		if (foodCount == 0 || character.isDead())
			return;
		//foodCount--;
		
		world.addTrash(trash);
		
		character.changeHpDelta(healthEffect);
		character.changeHunger(hungerEffect);
		character.changeMood(moodEffect * (ADAPTATION_MAX - adaptation) * ADAPTATION_RATE / ADAPTATION_MAX);
		adaptation = Maths.clamp(adaptation + 1, 0, ADAPTATION_MAX);
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public void decreaseAdaptation() {
		adaptation = Maths.clamp(adaptation - 1, 0, ADAPTATION_MAX);
	}
	
	public void save(ByteArray arr) {
		arr.writeString(name);
		arr.writeInt(adaptation);
		arr.writeInt(foodCount);
	}
	
	public void load(ByteArray arr) {
		adaptation = arr.readInt();
		foodCount = arr.readInt();
	}
}
