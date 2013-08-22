package ikm;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Splash {
	public static final int MOVE_PRE_FRAME = 2;
	
	private int x;
	private int y;

	
	private boolean active = false;
	private int steps;
	private String text;
	private Font font;

	public Splash(int x, int y) {
		this.x = x;
		this.y = y;
		font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
	}
	
	public void paint(Graphics g) {
		if (!active)
			return;
		steps ++;
		
		g.setFont(font);
		g.setColor(0x7000ffaa);
		g.drawString(text, x, y + steps * MOVE_PRE_FRAME, Graphics.TOP | Graphics.HCENTER);
	
		if (steps > 10)
			active = false;
	}
	
	public void showSplash(String text) {
		active = true;
		this.text = text;
		steps = 0;
	}
}
