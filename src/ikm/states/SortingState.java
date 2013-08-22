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

package ikm.states;

import ikm.GameState;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Res.TrashItem;
import ikm.Settings;
import ikm.game.World;
import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class SortingState extends GameState {
	public static final int SORTING_GAME_MOOD_EFFECT = Settings.getEntry("sorting_game_mood_effect");
	
	//private Vector fallingItems = new Vector();
	private Sprite activeItem;
	private int itemX;
	private int itemType;
	
	private int itemsSuccess = 0;
	private int itemsFail = 0;
	
	private World world;
	private ikm.game.Character character;
	
	private Sprite missItem;
	private Sprite binItem;
	
	private Sprite binBack1;
	private Sprite binBack2;
	private Sprite binBack3;
	
	private Sprite binPlastic;
	private Sprite binGlass;
	private Sprite binPaper;
	
	private boolean pointerDown = false;
	private int px, py;
	private int colorIndex = 0;
	private static final int[] BACK_COLORS = {0x1280a0, 0x2F6F8B, 0x4D5F77, 0x6B4F63, 0x883F4F, 0xA62F3B, 0xC41F27, 0xE20F13, 0xff0000};
	
	public SortingState(MainCanvas canvas, World world, ikm.game.Character character) {
		super("sorting", canvas);
		this. world = world;
		this.character = character;
		
		binBack1 = new Sprite(Res.binBack);
		binBack2 = new Sprite(Res.binBack);
		binBack3 = new Sprite(Res.binBack);
		
		binPlastic = new Sprite(Res.binPlastic);
		binGlass = new Sprite(Res.binGlass);
		binPaper = new Sprite(Res.binPaper);

		binPlastic.setPosition(Maths.posObject(canvas.getWidth(), binPlastic.getWidth(), 3, 0), canvas.getHeight() - binPlastic.getHeight());
		binGlass.setPosition(Maths.posObject(canvas.getWidth(), binGlass.getWidth(), 3, 1), canvas.getHeight() - binGlass.getHeight());
		binPaper.setPosition(Maths.posObject(canvas.getWidth(), binPaper.getWidth(), 3, 2), canvas.getHeight() - binPaper.getHeight());

		binBack1.setPosition(binPlastic.getX(), binPlastic.getY());
		binBack2.setPosition(binGlass.getX(), binGlass.getY());
		binBack3.setPosition(binPaper.getX(), binPaper.getY());
		
		createItem();
	}
	
	private void createItem() {
		TrashItem tritem = Res.trash[Main.rand.nextInt(Res.trash.length)];
		activeItem = new Sprite(tritem.image);
		itemX = Main.rand.nextInt(canvas.getWidth() - activeItem.getWidth() / 2);

		activeItem.setPosition(itemX - activeItem.getWidth() / 2, 0);
		itemType = tritem.type;
	}
	
	private boolean touchesBucket(Sprite item, Sprite bucket) {
		return item.getY() + item.getHeight() >= bucket.getY() && item.getX() >= bucket.getX()
				&& item.getX() + item.getWidth() <= bucket.getX() + bucket.getWidth();
	
	}
	
	private void update(Sprite item) {
		if (item != null) {
			item.setPosition(item.getX(), item.getY() + 6);
		}
	}

	public void update() {				
		update(activeItem);
		update(missItem);
		update(binItem);

		if (touchesBucket(activeItem, binPlastic) && itemType == 1
				|| touchesBucket(activeItem, binGlass) && itemType == 2
				|| touchesBucket(activeItem, binPaper) && itemType == 0) {
			binItem = activeItem;
			itemsSuccess++;
			createItem();
		}
		
		if (activeItem.getY() + activeItem.getHeight() > binPlastic.getY() + 5) {
			missItem = activeItem;
			itemsFail++;
			colorIndex = BACK_COLORS.length - 1;
			createItem();
		}
		
		if (pointerDown) {
			int diff = px - itemX;
			int sing = Maths.sign(diff);
			itemX += sing * 8;
			activeItem.setPosition(itemX - activeItem.getWidth() / 2,
					activeItem.getY());
		}
	}

	public void paint(Graphics g) {
		final int backColor = BACK_COLORS[colorIndex];
		if (colorIndex > 0)
			colorIndex--;
		
		g.setColor(backColor);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		binBack1.paint(g);
		binBack2.paint(g);
		binBack3.paint(g);
		
		activeItem.paint(g);
		if (binItem != null)
			binItem.paint(g);
		
		g.setColor(backColor);
		g.fillRect(0, canvas.getHeight() - 63, canvas.getWidth(), 63);

		binPlastic.paint(g);
		binGlass.paint(g);
		binPaper.paint(g);
		
		if (missItem != null)
			missItem.paint(g);
	}

	public int getUpdateRate() {
		return 100;
	}

	public boolean clicked(int x, int y) {
		if (super.clicked(x, y))
			return true;
		
		pointerDown = true;
		px = x;
		py = y;
		return true;
	}
	
	public boolean released(int x, int y) {
		if (super.released(x, y))
			return true;
		
		pointerDown = false;
		return true;
	}
	
	public boolean dragged(int x, int y) {
		if (super.dragged(x, y))
			return true;
		
		px = x;
		py = y;
		pointerDown = true;
		return true;
	}
	
	public void shutdown() {
		world.addTrash(-itemsSuccess);
		character.changeMood((itemsSuccess - itemsFail) * SORTING_GAME_MOOD_EFFECT);
	}
	
	public boolean needExtraRedraw() {
		return false;
	}
}
