package ikm.states;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.util.Maths;

final class Bulb {
	Bulb() {
		sprite = new Sprite(Res.bulb);
		disappear = 30;
	}
	
	Sprite sprite;
	int disappear;
}

public class LightOffState extends GameState {
	public static int APPEAR_RATE = 20;
	public static int APPEAR_RATE_MIN = 1;
	public static int FAIL_COUNT = 8;
	private int currentRate = APPEAR_RATE;
	
	private Vector bulbs = new Vector();
	
	public LightOffState(MainCanvas canvas) {
		super("Light off", canvas);
	}

	public void update() {
		if (Main.rand.nextInt(currentRate) == 0) {
			createBulb();
		}
	}
	
	public void createBulb() {
		Bulb bulb = new Bulb();
		int tries = 5;
		do {
			bulb.sprite.setPosition(Main.rand.nextInt(canvas.getWidth() - bulb.sprite.getWidth()),
									Main.rand.nextInt(canvas.getHeight() - bulb.sprite.getHeight()));
			if (tries-- <= 0) 
				return;
		} while(!isFreePlace(bulb.sprite));
		bulbs.addElement(bulb);
	}
	
	private boolean isFreePlace(Sprite sprite) {
		for (Enumeration en = bulbs.elements(); en.hasMoreElements();) {
			Sprite sprite2 = ((Bulb) en.nextElement()).sprite;
			if (sprite2.collidesWith(sprite, false)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (Enumeration en = bulbs.elements(); en.hasMoreElements();) {
			Sprite sprite = ((Bulb) en.nextElement()).sprite;
			sprite.paint(g);
		}
	}

	public int getUpdateRate() {
		return 100;
	}
	
	public boolean clicked(int x, int y) {
		for (Enumeration en = bulbs.elements(); en.hasMoreElements();) {
			Bulb bulb = (Bulb) en.nextElement();
			Sprite sprite = bulb.sprite;
			if (Maths.pointInRect(x, y, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight())) {
				bulbs.removeElement(bulb);
				if (Main.rand.nextInt(2) == 0)
					currentRate--;
				if (currentRate < 1)
					currentRate = 1;
				return true;
			}
		}
		
		return false;
	}

}
