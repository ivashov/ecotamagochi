package ikm.states;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.TiledLayer;

import ikm.GameState;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.game.Game;
import ikm.states.sgen.SGen;
import ikm.util.Maths;

public class SokobanState extends GameState {
	private TiledLayer tiledLayer;
	private Sokoban sokoban;
	private Sokoban first;
	private int totalHeight;
	private String map1 
	= "   * ."
	+ " #  #$"
	+ "      "
	+ "      "
	+ "$#  #$"
	+ "+    .";
	private boolean win = false;
	private Game game;
	
	public SokobanState(MainCanvas canvas, Game game) {
		super("Sokoban", canvas);
		this.game = game;
		
		int width = 7, height = 9;
		
		totalHeight = ((canvas.getHeight() - 1) / 32) + 1;
		tiledLayer = new TiledLayer(width, totalHeight, Res.sTiles, 32, 32);
		for (int x = 0; x < width; x++) {
			for (int y = height; y < totalHeight; y++) {
				tiledLayer.setCell(x, y, 9);
			}
		}
	}

	public void update() {
		if (sokoban == null) {
			if (SGen.isReady()) {
				sokoban = SGen.getSokobanLevel();
				first = sokoban.clone();
			} else {
				return;
			}
		}
		
		for (int x = 0; x < sokoban.getWidth(); x++) {
			for (int y = 0; y < sokoban.getHeight(); y++) {
				int idx = 0;
				int c = sokoban.getCell(x, y);
				
				if (c == Sokoban.WALL) {
					idx = ((x + y) & 1) == 0 ? 9 : 10;
				} else if (c == Sokoban.FREE){
					idx = 2;
				} else if (c == Sokoban.END)
					idx = 3;
				else if ((c & Sokoban.CRATE) != 0)
					idx = 4;
				
				tiledLayer.setCell(x, y, idx);
			}
		}
		
		tiledLayer.setCell(sokoban.getX(), sokoban.getY(), 6);
		
		if (sokoban.countEnds() == 0) {
			canvas.back();
			win = true;
		}
	}

	public void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		tiledLayer.paint(g);
		
		g.drawImage(Res.backArrow, 0, g.getClipHeight() - Res.backArrow.getWidth(),
				Graphics.TOP | Graphics.LEFT);
		g.drawImage(Res.exitArrow, g.getClipWidth() - Res.exitArrow.getWidth(),
				g.getClipHeight() - Res.exitArrow.getWidth(),
				Graphics.TOP | Graphics.LEFT);
		
		if (sokoban == null) {
			g.setFont(Main.largeFont);
			g.setColor(0xd53e07);
			g.drawString("Loading", g.getClipWidth() / 2, g.getClipHeight() / 2, Graphics.TOP | Graphics.HCENTER);
		}

	}

	public int getUpdateRate() {
		return 100;
	}
	
	public boolean clicked(int x, int y) {
		if (sokoban != null) {
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

			// Check buttons
			if (x < Res.exitArrow.getWidth()
					&& y > canvas.getHeight() - Res.exitArrow.getWidth()) {
				// Reset
				sokoban = first.clone();
			}
		}
		
		if (x > canvas.getWidth() - Res.exitArrow.getWidth() && y > canvas.getHeight() - Res.exitArrow.getHeight()) {
			canvas.back();
		}
		
		return true;
	}
	
	public void shutdown() {
		super.shutdown();
		if (win) {
			game.getWorld().addTrash(-3 * 2);
			game.getCharacter().changeMood(10000);
		}
	}
}
