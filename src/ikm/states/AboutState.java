package ikm.states;

import javax.microedition.lcdui.Graphics;

import ikm.GameState;
import ikm.MainCanvas;

public class AboutState extends GameState {
	public AboutState(String name, MainCanvas canvas) {
		super(name, canvas);
	}

	public void update() {
		
	}

	public void paint(Graphics g) {
		renderParent(g);
		
		g.drawString("about", 0, 0, Graphics.TOP | Graphics.LEFT);
	}

	public int getUpdateRate() {
		return 1000;
	}
	
	public boolean clicked(int x, int y) {
		canvas.back();
		return true;
	}
	
}
