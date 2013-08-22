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
import ikm.GameState.Dragable;
import ikm.game.Food;
import ikm.util.Maths;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class FoodSelector implements Clickable, Dragable {
	public interface FoodListener {
		void foodSelected(Food food);
	}
	
	public static final int HEIGHT = 50;
	public static final int ITEM_WIDTH = 50;
	public static final int ITEM_INTERVAL = 10;
	
	private int y;
	private boolean hidden = true;
	private Vector foods;
	private int screenWidth;
	private int totalWidth;
	private FoodListener listener;
	
	private int renderX = 0;
	private boolean dragging = false;
	private int cx, cy;
	
	public FoodSelector(int y, int width) {
		this.y = y;
		this.screenWidth = width;
	}
	
	public void setFood(Vector foods) {
		this.foods = foods;
		hidden = false;
		totalWidth = foods.size() * (ITEM_WIDTH + ITEM_INTERVAL);
	}
	
	public void paint(Graphics g) {
		if (hidden || foods == null)
			return;
				
		int foodCount = foods.size();
		for (int i = 0; i < foodCount; i++) {
			Food food = (Food) foods.elementAt(i);
			Sprite foodSprite = (Sprite) food.getData();
			foodSprite.setPosition(renderX + i * (ITEM_WIDTH + ITEM_INTERVAL), y);
			foodSprite.paint(g);
		}
	}
	
	public void setListener(FoodListener listener) {
		this.listener = listener;
	}
	
	public boolean clicked(int xx, int yy) {
		if (hidden || foods == null)
			return false;
		
		if (yy < y || yy > y + HEIGHT)
			return false;
		
		cx = xx;
		cy = yy;
		
		return true;
	}
	
	public boolean draged(int xx, int yy) {
		if (hidden || foods == null)
			return false;
		
		if (yy < y || yy > y + HEIGHT)
			return false;
				
		dragging = true;
		renderX += (xx - cx);
		cx = xx;
		
		renderX = Maths.clamp(renderX, -totalWidth + screenWidth, 0);
		return true;
	}
	
	public boolean released(int xx, int yy) {
		if (hidden || foods == null)
			return false;
		
		if (yy < y || yy > y + HEIGHT)
			return false;
		
		
		if (!dragging) {
			int foodCount = foods.size();
			for (int i = 0; i < foodCount; i++) {
				Food food = (Food) foods.elementAt(i);
				Sprite foodSprite = (Sprite) food.getData();
				
				int sx = foodSprite.getX();
				int sy = foodSprite.getY();
				int sw = foodSprite.getWidth();
				int sh = foodSprite.getHeight();
				
				if (Maths.pointInRect(xx, yy, sx, sy, sw, sh)) {
					hidden = true;
					foods = null;
					
					if (listener != null)
						listener.foodSelected(food);
					
					return true;
				}
			}
		}
		
		dragging = false;
		
		return true;
	}
}
