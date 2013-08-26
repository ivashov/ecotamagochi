package ikm.states;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import ikm.GameState;
import ikm.MainCanvas;

public class AboutState extends GameState {
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	
	public AboutState(String name, MainCanvas canvas) {
		super(name, canvas);
	}

	public void update() {
		
	}

	public void paint(Graphics g) {
		renderParent(g);
		
		int x = canvas.getWidth() / 2;
		g.setFont(font);
		g.setColor(0xaaeeff);

		g.drawString("About", x, 16, Graphics.TOP | Graphics.HCENTER);
		
		g.setColor(0x75eeff);

		g.drawString("EcoTamagochi", x, 64, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Version: 0.0.1", x, 96, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Kirill Ivashov", x, 128, Graphics.TOP | Graphics.HCENTER);
		g.drawString("ivashov@cs.karelia.ru", x, 160, Graphics.TOP | Graphics.HCENTER);
	}

	public int getUpdateRate() {
		return 1000;
	}
	
	public boolean clicked(int x, int y) {
		canvas.back();
		return true;
	}
}
