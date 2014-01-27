package ikm.states;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import ikm.GameState;
import ikm.Main;
import ikm.MainCanvas;

public class AboutState extends GameState {
	public AboutState(String name, MainCanvas canvas) {
		super(name, canvas);
	}

	public void update() {
		
	}

	public void paint(Graphics g) {
		renderParent(g);
		
		int x = canvas.getWidth() / 2;
		g.setFont(Main.largeFont);
		g.setColor(0xaaeeff);
		g.drawString("About", x, 16, Graphics.TOP | Graphics.HCENTER);
	
		g.setFont(Main.font);
		g.setColor(0x75eeff);

		int y = 64;
		g.drawString("EcoTamagochi", x, y, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Version: 0.0.1", x, y + 20, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Kirill Ivashov", x, y + 40, Graphics.TOP | Graphics.HCENTER);
		g.drawString("ivashov@cs.karelia.ru", x, y + 60, Graphics.TOP | Graphics.HCENTER);
		
		y = 158;
		g.setFont(Main.smallFont);
		g.drawString("This application is a part of grant",
				x, y, Graphics.TOP | Graphics.HCENTER);
		g.drawString("KA179 of Karelia ENPI CBC",
				x, y + 16, Graphics.TOP | Graphics.HCENTER);
		g.drawString("programme, which is co-funded by", 
				x, y + 16 * 2, Graphics.TOP | Graphics.HCENTER);
		g.drawString("the European Union,",
				x, y + 16 * 3, Graphics.TOP | Graphics.HCENTER);
		g.drawString("the Russian Federation",
				x, y + 16 * 4, Graphics.TOP | Graphics.HCENTER);
		g.drawString("and the Republic of Finland",
				x, y + 16 * 5, Graphics.TOP | Graphics.HCENTER);
		
		y = y + 16 * 5 + 10;
		g.drawString("Uses graphics by Hyptosis",
				x, y + 16, Graphics.TOP | Graphics.HCENTER);
		
		g.drawString("licensed CC-BY 3.0",
				x, y + 16 * 2, Graphics.TOP | Graphics.HCENTER);
	}

	public int getUpdateRate() {
		return 1000;
	}
	
	public boolean clicked(int x, int y) {
		canvas.back();
		return true;
	}
}
