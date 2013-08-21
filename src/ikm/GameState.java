package ikm;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public abstract class GameState {
	protected String name;
	private Vector clickables = new Vector();
	protected MainCanvas canvas;
	private GameState parent;
	
	public interface Clickable {
		boolean clicked(int x, int y);
	}
	
	public GameState(String name, MainCanvas canvas) {
		this.name = name;
		this.canvas = canvas;
	}
	
	public void addClickable(Clickable c) {
		clickables.addElement(c);
	}
	
	public void removeClickable(Clickable c) {
		clickables.removeElement(c);
	}
	
	public boolean clicked(int x, int y) {
		for (Enumeration en = clickables.elements(); en.hasMoreElements();) {
			Clickable clk = (Clickable) en.nextElement();
			if (clk.clicked(x, y))
				return true;
		}
		
		return false;
	}
	
	public void released(int x, int y) {}
	
	public void dragged(int x, int y) {}
	
	final void render(Graphics g, GameState parent) {
		this.parent = parent;		
		paint(g);
		this.parent = null;
	}
	
	protected void renderParent(Graphics g) {
		if (parent != null) {
			parent.render(g, null);
			g.drawImage(canvas.getTransparentImage(), 0, 0, Graphics.TOP | Graphics.LEFT);
		}
	}
	
	public abstract void update();
	public abstract void paint(Graphics g);
	public abstract int getUpdateRate();
	public void shutdown() {}
}
