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

import ikm.GameState.Clickable;
import ikm.game.Food;
import ikm.util.Maths;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class FoodSelector implements Clickable {
	public interface FoodListener {
		void foodSelected(Food food);
	}
	
	public static final int HEIGHT = 32;
	
	private int y;
	private boolean hidden = true;
	private Vector foods;
	private int width;
	private FoodListener listener;
	
	public FoodSelector(int y, int width) {
		this.y = y;
		this.width = width;
	}
	
	public void setFood(Vector foods) {
		this.foods = foods;
		hidden = false;
	}
	
	public void paint(Graphics g) {
		if (hidden || foods == null)
			return;
				
		int foodCount = foods.size();
		for (int i = 0; i < foodCount; i++) {
			Food food = (Food) foods.elementAt(i);
			Sprite foodSprite = (Sprite) food.getData();
			
			foodSprite.setPosition(Maths.posObject(width, HEIGHT, foodCount, i), y);
			foodSprite.paint(g);
		}
	}
	
	public void setListener(FoodListener listener) {
		this.listener = listener;
	}
	
	public boolean clicked(int x, int y) {
		if (hidden || foods == null)
			return false;
		
		int foodCount = foods.size();
		for (int i = 0; i < foodCount; i++) {
			Food food = (Food) foods.elementAt(i);
			Sprite foodSprite = (Sprite) food.getData();
			
			int sx = foodSprite.getX();
			int sy = foodSprite.getY();
			int sw = foodSprite.getWidth();
			int sh = foodSprite.getHeight();
			
			if (Maths.pointInRect(x, y, sx, sy, sw, sh)) {
				hidden = true;
				foods = null;
				
				if (listener != null)
					listener.foodSelected(food);
				
				return true;
			}
		}	
		return false;
	}
}
