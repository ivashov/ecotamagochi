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

import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class ProgressBar {
	public static final int BAR_X = 16, BAR_Y = 3;
	public static final int WIDTH_X = 48, WIDTH_Y = 14;
	
	private Sprite sprite;

	private int color;
	private int maxValue = 1;
	
	private int x, y;
	private int currentValue = 0;
	
	public ProgressBar(int x, int y, int color, int maxValue) {
		this.color = color;
		this.maxValue = maxValue;
		
		this.x = x;
		this.y = y;
		
		sprite = new Sprite(Res.progressbar);
		sprite.setPosition(x, y);
	}
	
	public void setCurrentValue(int v) {
		currentValue = Maths.clamp(v, 0, maxValue);
	}

	public void paint(Graphics g) {
		g.setColor(color);
		sprite.paint(g);
		
		int width = WIDTH_X * currentValue / maxValue;
		g.fillRect(x + BAR_X, y + BAR_Y, width, WIDTH_Y);
	}

}
