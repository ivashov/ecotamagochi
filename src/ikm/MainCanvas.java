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

import ikm.states.MazeState;
import ikm.states.SokobanState;
import ikm.util.Maths;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class MainCanvas extends GameCanvas implements Runnable {
	public static final int FRAME_TIME = 100;
	public Vector states = new Vector();
	
	private boolean cont = true;
	private long gameTime;

	private Image darkImage;
	private Graphics g;
	private Main main;
	private boolean backVisible = false;
	
	protected MainCanvas(Main main) {
		super(false);
		setFullScreenMode(true);
		
		this.main = main;
	}
	
	public void pushState(GameState state) {
		synchronized (this) {
			states.addElement(state);
			this.notifyAll();
			gameTime = System.currentTimeMillis();
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
			if (backVisible && Maths.pointInRect(x, y, getWidth() - 32, getHeight() - 16, 32, 16)) {
				back();
				return;
			}
			
			currentState().clicked(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	protected void pointerReleased(int x, int y) {
		synchronized (this) {
			currentState().released(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	protected void pointerDragged(int x, int y) {
		synchronized (this) {
			currentState().dragged(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	public void redraw() {
		synchronized (this) {
			currentState().render(g, previousState());
			flushGraphics();
		}
	}
	
	public void quit() {
		main.quit();
	}
	
	public void stop() {
		synchronized (this) {
			while (back())
				;
			currentState().shutdown();
			
			cont = false;
			notifyAll();
		}
	}
	
	private void safeRun() {
		g = getGraphics();
		darkImage = generateTransparentImage();
		gameTime = System.currentTimeMillis();
		
		while (cont) {
			synchronized (this) {
				GameState state = currentState();
				int rate = state.getUpdateRate();

				currentState().render(g, previousState());
				paintBackButton(g);
				flushGraphics();
				
				if (System.currentTimeMillis() >= gameTime) {
					state.update();
					gameTime += rate;
				}

				long sleep = gameTime - System.currentTimeMillis();

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
	
	public void run() {
		safeRun();
	}
	
	private void paintBackButton(Graphics g) {
		if (Main.noHardwareBack && states.size() > 1 && !(states.lastElement() instanceof SokobanState)) {
			int posX = getWidth();
			int posY = getHeight();
			backVisible = true;
			g.setColor(~0);
			g.drawString(Translation.tr("back"), posX, posY, Graphics.BOTTOM | Graphics.RIGHT);
		} else
			backVisible = false;
	}
	
	private Image generateTransparentImage() {
		int[] rgb = new int[getWidth() * getHeight()];
		for (int i = 0; i < rgb.length; i++)
			rgb[i] = 0xcb000010;
		
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

	public void restart() {
		synchronized(this) {
			notifyAll();
			main.restart();
		}
	}
}
