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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import ikm.Log;
import ikm.Settings;
import ikm.util.ByteArray;

public class Game {
	public final static int STEP_RATE = Settings.getEntry("step_rate"); // Seconds per step
	public final static int ADAPTATION_DECREASE_RATE = Settings.getEntry("adaptation_decrease_rate");
	
	private World world = new World();
	private Character character = new Character(world);

	private Hashtable foodStorage = new Hashtable();
	
	private long step = 0;
	private long foodAdaptationDecreaseTime = ADAPTATION_DECREASE_RATE;
	
	public void step() {
		if (character.isDead())
			return;
		
		step++;
		character.update(STEP_RATE);
		
		foodAdaptationDecreaseTime -= STEP_RATE;
		if (foodAdaptationDecreaseTime < 0) {
			foodAdaptationDecreaseTime += ADAPTATION_DECREASE_RATE;

			for (Enumeration en = foodStorage.elements(); en.hasMoreElements();) {
				((Food) en.nextElement()).decreaseAdaptation();
			}
		}
		
		Log.setStep(step);
		Log.log("Hp " + character.getHp() + "; Hunger " + character.getHunger() + "; Mood " + character.getMood() + "; trash=" + world.getTrash());
	}
	
	public Food getFood(String name) {
		return (Food) foodStorage.get(name);
	}
	
	public void createFood(String name, int hunger, int litter, int health, int mood) {
		foodStorage.put(name, new Food(name, hunger, litter, health, mood));

		Log.log("Food created: " + name + " litter=" + litter + "; health=" + health
				+ "; hunger" + hunger + "; mood=" + mood);
	}
	
	public void addFood(String name, int count) {
		Food food = getFood(name);
		food.increaseCount(count);
	}
	
	public Character getCharacter() {
		return character;
	}

	public World getWorld() {
		return world;
	}
	
	public Vector getAvailableFood() {
		Vector ret = new Vector();
		
		for (Enumeration en = foodStorage.elements(); en.hasMoreElements();) {
			ret.addElement(en.nextElement());
		}
		
		return ret;
	}
	
	public void saveFoodStorage(ByteArray arr) {
		arr.writeInt(foodStorage.size());
		for (Enumeration en = foodStorage.elements(); en.hasMoreElements();) {
			Food food = (Food) en.nextElement();
			food.save(arr);
		}
	}
	
	public void saveGame(ByteArray arr) {
		arr.writeLong(step);
		arr.writeLong(foodAdaptationDecreaseTime);
	}
	
	public void loadGame(ByteArray arr) {
		step = arr.readLong();
		foodAdaptationDecreaseTime = arr.readLong();
	}
	
	public void loadFoodStorage(ByteArray arr) {
		int count = arr.readInt();
		for (int i = 0; i < count; i++) {
			Food food = getFood(arr.readString());
			food.load(arr);
		}
	}
}
