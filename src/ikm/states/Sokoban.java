package ikm.states;

import ikm.Log;

public class Sokoban {
	public static final int FREE = 0;
	public static final int WALL = 1;
	public static final int CRATE = 2;
	public static final int END = 4;
	
	private int width, height;
	private int x, y;
	
	/*
	 * 0 - free
	 * 1 - wall
	 * 2 - crate
	 * 3 - end
	 */
	private int[][] map;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Sokoban(int width, int height) {
		this.width = width;
		this.height = height;
		
		map = new int[width][height];
	}
	
	public boolean isInner(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public int getCell(int x, int y) {
		if (isInner(x, y))
			return map[x][y];
		else
			return WALL;
	}
	
	public boolean isCell(int x, int y, int type) {
		return (getCell(x, y) & type) != 0;
	}
	
	/*
	 #.0#
	 #.x#
	 #p.#
	 */
	public void loadFromString(String mapStr) {
		int height = mapStr.length() / width;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				char c = mapStr.charAt(x + y * width);
				switch (c) {
				case '#': map[x][y] = WALL; break;
				case '.': map[x][y] = FREE; break;
				case 'x': map[x][y] = CRATE; break;
				case '0': map[x][y] = END; break;
				case '*': map[x][y] = END | CRATE; break;
				case 'p': this.x = x; this.y = y; break;
				default: Log.err("Unknown symbol in map: " + c);
				}
			}
		}
	}
	
	public int countEnds() {
		int openEnds = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isCell(x, y, END) && !isCell(x, y, CRATE))
					openEnds++;
			}
		}
		return openEnds;
	}
	
	public String toString() {
		char[] arr = new char[width * height];
		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isCell(x, y, WALL))
					arr[i] = '#';
				else if (isCell(x, y, CRATE) && isCell(x, y, END))
					arr[i] = '*';
				else if (isCell(x, y, CRATE))
					arr[i] = 'x';
				else if (isCell(x, y, END))
					arr[i] = '0';
				else
					arr[i] = '.';
				
				i++;
			}
		}
		
		arr[x + y * width] = 'p';
		
		return new String(arr);
	}
	
	public void moveObject(int x0, int y0, int x1, int y1, int type) {
		if (!isInner(x0, y0) || !isInner(x1, y1) 
				|| !isCell(x0, y0, type) || isCell(x1, y1, type)) {
			Log.err("Wrong move");
			return;
		}
		
		map[x0][y0] &= ~type;
		map[x1][y1] |= type;
	}
	
	public void move(int dx, int dy) {
		int nx = x + dx;
		int ny = y + dy;
		
		// Player stuck to wall
		if (isCell(nx, ny, WALL)) {
			return;
		}
		
		// Player touches crate
		if (isCell(nx, ny, CRATE)) {
			int cx = nx + dx;
			int cy = ny + dy;
			
			// Crate can move
			if (!isCell(cx, cy, WALL) && !isCell(cx, cy, CRATE)) {
				moveObject(nx, ny, cx, cy, CRATE);
				x = nx;
				y = ny;
			}
			return;
		}
		
		if (!isCell(nx, ny, WALL)) {
			x = nx;
			y = ny;
			return;
		}
	}
}