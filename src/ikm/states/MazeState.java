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

import java.util.Random;

import ikm.GameState;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

class MapGenerator {
	int[] map;
	private int width, height;
	private Random r = Main.rand;
	
	private boolean isInner(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	private boolean isOpen(int x, int y) {
		return isInner(x, y) && map[x + y * width] == 1;
	}
	
	private int countOpenNeighbors(int x, int y) {
		return (isOpen(x, y + 1) ? 1 : 0) 
				+ (isOpen(x, y - 1) ? 1 : 0) 
				+ (isOpen(x + 1, y) ? 1 : 0) 
				+ (isOpen(x - 1, y) ? 1 : 0)
		
				+ ((isOpen(x + 1, y + 1) && !isOpen(x, y + 1) && !isOpen(x + 1, y)) ? 1 : 0)
				+ ((isOpen(x + 1, y - 1) && !isOpen(x, y - 1) && !isOpen(x + 1, y)) ? 1 : 0)
				+ ((isOpen(x - 1, y + 1) && !isOpen(x, y + 1) && !isOpen(x - 1, y)) ? 1 : 0)
				+ ((isOpen(x - 1, y - 1) && !isOpen(x, y - 1) && !isOpen(x - 1, y)) ? 1 : 0);
	}
	
	MapGenerator(int width, int height) {
		this.width = width;
		this.height = height;
		map = new int[width * height];
		for (int i = 0; i < map.length; i++) {
			map[i] = 9;
		}
	}
	
	void generate() {
		int xx = r.nextInt(width);
		int yy = r.nextInt(height);
		map[xx + width * yy] = 1;
		process(xx, yy);
	}
	
	private void process2(int x, int y) {
		if (isInner(x, y) && !isOpen(x, y)) {
			if (countOpenNeighbors(x, y) <= 1) {
				map[x + y * width] = 1;
				process(x, y);
			}
		}
	}
	
	private void process(int x, int y) {
		int[] choice = new int[4];
		for (int i = 0; i < 4; i++)
			choice[i] = i;
		Maths.shuffle(choice, r);
		
		for (int i = 0; i < 4; i++) {
			switch(choice[i]) {
			case 0: process2(x, y + 1); break;
			case 1:	process2(x, y - 1); break;
			case 2:	process2(x + 1, y); break;
			case 3:	process2(x - 1, y); break;
			}
		}
	}
}

public class MazeState extends GameState {
	private TiledLayer layer;
	private Sprite mask;
	
	private int width = 14, height = 16;
	private int[] map;
	
	private int posX, posY;
	
	public MazeState(MainCanvas canvas) {
		super("Maze", canvas);
	
		layer = new TiledLayer(width, height, Res.tiles, 16, 16);
		MapGenerator gen = new MapGenerator(width, height);
		gen.generate();
		this.map = gen.map;
		mask = new Sprite(Res.mazeMask);
		
		updateMaskPosition(posX, posY);
		calculateShadow(posX, posY);
		setMap(map);
	}
	
	private void setMap(int[] map) {	
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				layer.setCell(x, y, map[x + y * width]);
			}
		}
	}
	
	public int getUpdateRate() {
		return 100;
	}
	
	public void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		layer.paint(g);
		
		mask.paint(g);
	}
	
	private boolean isInner(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	private boolean isOpen(int x, int y) {
		return isInner(x, y) && map[x + y * width] >= 1 && map[x + y * width] <= 6;
	}
	
	private int[] sqrts = {0, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5};
	
	private void calculateShadow(int x, int y) {
		for (int yy = 0; yy < height; yy++) {
			for (int xx = 0; xx < width; xx++) {
				if (!isInner(xx, yy))
					continue;
				int add = isOpen(xx, yy) ? 0 : 8;

				int dist2 = Maths.dist2(xx, yy, x, y);
				if (dist2 > 5 * 5) {
					map[xx + yy * width] = 5 + add;
					continue;
				}
				
				//map[xx + yy * width] = sqrts[dist2] + 1 + add;
				map[xx + yy * width] = 1 + add;
			}
		}
	}
	
	public void update() {
		
	}
	
	public boolean clicked(int x, int y) {
		int nx = posX;
		int ny = posY;
		int dx = x / 16 - nx;
		int dy = y / 16 - ny;
		
		if (Math.abs(dx) > Math.abs(dy))
			if (dx > 0)
				nx++;
			else
				nx--;
		else if (dy > 0)
				ny++;
			else
				ny--;
		
		if (isOpen(nx, ny)) {
			posX = nx;
			posY = ny;
		}

		calculateShadow(posX, posY);

		updateMaskPosition(posX, posY);
		setMap(map);
		return true;
	}
	
	private void updateMaskPosition(int x, int y) {
		mask.setPosition(x * 16 - mask.getWidth() / 2 + 8, y * 16 - mask.getHeight() / 2 + 8);

	}
}
