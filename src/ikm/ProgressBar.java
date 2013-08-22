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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class ProgressBar {
	public static final int BAR_X = 19, BAR_Y = 6;
	public static final int WIDTH_X = 44, WIDTH_Y = 7;
	
	private Sprite sprite;

	private int color;
	private int maxValue = 1;
	
	private int x, y;
	private int currentValue = 0;
	private Image icon;
	
	public ProgressBar(int x, int y, int color, int maxValue, Image icon) {
		this.color = color;
		this.maxValue = maxValue;
		this.icon = icon;
		
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
		g.drawImage(icon, x - 12, y - 8, Graphics.TOP | Graphics.LEFT);
		
		int width = WIDTH_X * currentValue / maxValue;
		g.fillRect(x + BAR_X, y + BAR_Y, width, WIDTH_Y);
	}

}
