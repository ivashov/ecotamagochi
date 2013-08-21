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
import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class Button implements Clickable {
	public interface ButtonListener {
		void buttonClicked(Button button);
	}
	
	private int x, y;
	private int width = Res.button.getWidth();
	private int height = Res.button.getHeight();
	
	private Sprite sprite;
	private String text;
	private ButtonListener listener;
	
	public Button(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
		
		sprite = new Sprite(Res.button);
		sprite.setPosition(x, y);
	}
	
	public void paint(Graphics g) {
		g.setColor(0xB6D8F9);
		sprite.paint(g);
		g.drawString(text, x + width / 2, y + height / 2 + 5,
				Graphics.BASELINE | Graphics.HCENTER);
	}

	public void setListener(ButtonListener listener) {
		this.listener = listener;
	}
	
	
	public boolean clicked(int px, int py) {
		if (listener != null && Maths.pointInRect(px, py, x, y, width, height)) {
			listener.buttonClicked(this);
			return true;
		}
		return false;

	}
}
