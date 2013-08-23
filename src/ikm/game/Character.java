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

public class Character {
	public static final int EAT_DECREASE_SPEED = Settings.getEntry("eat_decrease_speed");
	public static final int MAX_VALUE = Settings.getEntry("max_value");
	public static final int INITIAL_VALUE = Settings.getEntry("initial_value");
	public static final int EAT_STARVATION = Settings.getEntry("eat_starvation");
	public static final int EAT_STARVATION_HEALTH_EFFECT = Settings.getEntry("eat_starvation_health_effect");
	public static final int EAT_STARVATION_MOOD_EFFECT = Settings.getEntry("eat_starvation_mood_effect");

	public static final int TRASH_MOOD_EFFECT = Settings.getEntry("trash_mood_effect");
	public static final int TRASH_HEALTH_EFFECT = Settings.getEntry("trash_health_effect");
	
	private int hp = INITIAL_VALUE;
	private int mood = INITIAL_VALUE;
	private int hunger = INITIAL_VALUE;
	
	private int hpDelta = 0;
	private World world;
	
	public Character(World world) {
		this.world = world;
	}

	public void update(long time) {
		if (isDead())
			return;

		// 1. Decrease hunger by EAT_DECREASE_SPEED
		hunger -= EAT_DECREASE_SPEED * time;
		
		// 2. Decrease mood and health if hunger too low
		if (hunger < EAT_STARVATION) {
			changeHp(-EAT_STARVATION_HEALTH_EFFECT  * (int) time);
			changeMood(-EAT_STARVATION_MOOD_EFFECT * (int) time);
		}
		
		// 3. Change health
		changeHp(hpDelta * (int) time);
		
		// 4. Decrease health change speed by module
		if (Math.abs(hpDelta) < 10)
			hpDelta = 0;
		else
			hpDelta -= Maths.sign(hpDelta) * 3 * (int) time;
		
		// 5. Trash in world decreased mood and health
		if (world.getTrash() > 10) {
			int hpChange = (int) time * world.getTrash() * TRASH_HEALTH_EFFECT / 100;
			int moodChange = (int) time * world.getTrash() * TRASH_MOOD_EFFECT / 100;
			changeHp(-hpChange);
			changeMood(-moodChange);
		}
	}

	public int getHp() {
		return hp;
	}
	
	public int getHunger() {
		return hunger;
	}
	
	public int getMood() {
		return mood;
	}
	
	public void changeMood(int d) {
		mood = Maths.clamp(mood + d, 0, MAX_VALUE);
	}

	public void changeHunger(int d) {
		hunger = Maths.clamp(hunger + d, 0, MAX_VALUE);
	}
	
	public void changeHp(int d) {
		hp = Maths.clamp(hp + d, 0, MAX_VALUE);
	}
	
	public void changeHpDelta(int d) {
		hpDelta += d;
	}
	
	public void save(ByteArray arr) {
		arr.writeInt(hp);
		arr.writeInt(hunger);
		arr.writeInt(mood);
		arr.writeInt(hpDelta);
	}
	
	public void load(ByteArray arr) {
		hp = arr.readInt();
		hunger = arr.readInt();
		mood = arr.readInt();
		hpDelta = arr.readInt();
	}
	
	public final boolean isDead() {
		return hp == 0;
	}
}
