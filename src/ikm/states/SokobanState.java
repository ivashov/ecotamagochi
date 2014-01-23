package ikm.states;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.TiledLayer;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;
import ikm.util.Maths;

public class SokobanState extends GameState {
	private TiledLayer tiledLayer;
	private Sokoban sokoban;
	
	private String map1 
	= "...*.0"
	+ ".#..#x"
	+ "......"
	+ "......"
	+ "x#..#x"
	+ "0....0";
	
	public SokobanState(MainCanvas canvas) {
		super("Sokoban", canvas);
		
		int width = 6, height = 6;
		sokoban = new Sokoban(width, height);
		sokoban.loadFromString(map1);
		
		tiledLayer = new TiledLayer(sokoban.getWidth(), sokoban.getHeight(), Res.sTiles, 32, 32);
	}

	public void update() {
		for (int x = 0; x < sokoban.getWidth(); x++) {
			for (int y = 0; y < sokoban.getHeight(); y++) {
				int idx = 0;
				int c = sokoban.getCell(x, y);
				
				if (c == Sokoban.WALL) {
					idx = 1;
				} else if (c == Sokoban.FREE){
					idx = 2;
				} else if (c == Sokoban.END)
					idx = 3;
				else if ((c & Sokoban.CRATE) != 0)
					idx = 4;
				
				tiledLayer.setCell(x, y, idx);
			}
		}
		
		tiledLayer.setCell(sokoban.getX(), sokoban.getY(), 5);
		
		
		if (sokoban.countEnds() == 0) {
			canvas.back();
		}
	}

	public void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		tiledLayer.paint(g);
	}

	public int getUpdateRate() {
		return 100;
	}
	
	public boolean clicked(int x, int y) {
		int nx = sokoban.getX();
		int ny = sokoban.getY();
		int dx = x - nx * 32 - 16;
		int dy = y - ny * 32 - 16;
		
		if (Math.abs(dx) > Math.abs(dy))
			if (dx > 0)
				sokoban.move(1, 0);
			else
				sokoban.move(-1, 0);
		else if (dy > 0)
				sokoban.move(0, 1);
			else
				sokoban.move(0, -1);
		
		return true;
	}
	
}
