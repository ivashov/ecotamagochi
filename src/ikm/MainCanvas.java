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

import ikm.game.Game;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class MainCanvas extends GameCanvas implements Runnable {
	public static final int FRAME_TIME = 50;
	public Vector states = new Vector();
	
	private boolean cont = true;
	private boolean needUpdate = true;
	private long last;

	private Image darkImage;
	
	protected MainCanvas() {
		super(false);
	}
	
	public void pushState(GameState state) {
		synchronized (this) {
			states.addElement(state);
			needUpdate = false;
			this.notifyAll();
			last = System.currentTimeMillis();
		}
	}
	
	public void popState() {
		synchronized (this) {
			states.removeElementAt(states.size() - 1);
		}
	}
	
	public GameState currentState() {
		return (GameState) states.elementAt(states.size() - 1);
	}
	
	public GameState previousState() {
		return (GameState) (states.size() == 1 ? null : states.elementAt(states.size() - 2));
	}

	protected void pointerPressed(int x, int y) {
		synchronized (this) {
			currentState().clicked(x, y);
			needUpdate = false;
			this.notifyAll();
		}
	}
	
	protected void pointerReleased(int x, int y) {
		synchronized (this) {
			currentState().released(x, y);
			needUpdate = false;
			this.notifyAll();
		}
	}
	
	protected void pointerDragged(int x, int y) {
		currentState().dragged(x, y);
		needUpdate = false;
	}
	
	public void run() {
		Graphics g = getGraphics();
		darkImage = generateTransparentImage();
		last = System.currentTimeMillis();
		
		while (cont) {
			synchronized (this) {
				GameState state = currentState();
				int rate = state.getUpdateRate();

				state.render(g, previousState());
				flushGraphics();
				if (needUpdate) {
					state.update();
					last += rate;
				}
				needUpdate = true;

				long sleep = last - System.currentTimeMillis();

				if (sleep <= 0)
					sleep = 1;

				try {
					this.wait(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Image generateTransparentImage() {
		int[] rgb = new int[getWidth() * getHeight()];
		for (int i = 0; i < rgb.length; i++)
			rgb[i] = 0xbf000010;
		
		Image img = Image.createRGBImage(rgb, getWidth(), getHeight(), true);
		return img;
	}
	
	public Image getTransparentImage() {
		return darkImage;
	}
	
	public boolean back() {
		if (states.size() > 1) {
			currentState().shutdown();
			popState();
			return true;
		} else {
			return false;
		}
	}
}
