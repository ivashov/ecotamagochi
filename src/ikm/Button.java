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
