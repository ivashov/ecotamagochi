package ikm.states;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.Log;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.game.Game;
import ikm.util.Maths;

final class Bulb {
	Bulb() {
		sprite = new Sprite(Res.bulb);
	}
	
	Sprite sprite;
	long lifeStart = System.currentTimeMillis();
}

final class Star {
	Star(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	int x, y;
	int state = 0; // 0 = up, 1 = live, 2 = down
	int color = 0;
}

public class LightOffState extends GameState {
	public static int APPEAR_RATE = 20;
	public static int APPEAR_RATE_MIN = 1;
	public static int FAIL_COUNT = 8;
	private int currentRate = APPEAR_RATE;
	
	private Vector bulbs = new Vector();
	private Vector stars = new Vector();
	private int score = 0;
	private int failsInRow = 0;
	private boolean finished = false;
	private Game game;
	
	public LightOffState(MainCanvas canvas, Game game) {
		super("Light off", canvas);
		this.game = game;
	}

	public synchronized void update() {
		if (finished) {
			return;
		}
		
		if (Main.rand.nextInt(currentRate) == 0) {
			createBulb();
		}
		
		updateStars();
		updateBulbs();
		
		if (failsInRow > 5) {
			finished = true;
		}
	}
	
	private void updateBulbs() {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < bulbs.size(); i++) {
			Bulb bulb = (Bulb) bulbs.elementAt(i);
			if (currentTime - bulb.lifeStart > 1200) {
				bulbs.removeElementAt(i--);
				failsInRow++;
			}
		}
	}

	public void createBulb() {
		if (bulbs.size() >= 10)
			return;
		
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
	
	public synchronized void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (Enumeration en = stars.elements(); en.hasMoreElements();) {
			Star star = ((Star) en.nextElement());
			g.setColor(25 * star.color + 5, 25 * star.color + 5, 25 * star.color + 5);
			g.fillRect(star.x, star.y, 1, 1);
		}
		
		for (Enumeration en = bulbs.elements(); en.hasMoreElements();) {
			Sprite sprite = ((Bulb) en.nextElement()).sprite;
			sprite.paint(g);
		}
		
		g.setColor(~0);
		g.drawString("" + (score/100), 2, 2, Graphics.TOP | Graphics.LEFT);
		
		if (finished) {
			g.drawImage(canvas.getTransparentImage(), 0, 0, Graphics.TOP | Graphics.LEFT);
			g.setColor(0x75eeff);
			int x = canvas.getWidth() / 2;
			g.drawString("Result", x, 64, Graphics.TOP | Graphics.HCENTER);
			g.drawString("" + (score/100), x, 96, Graphics.TOP | Graphics.HCENTER);
		}
	}

	public int getUpdateRate() {
		return 100;
	}
	
	public synchronized boolean clicked(int x, int y) {
		if (finished)
			canvas.back();
		
		for (Enumeration en = bulbs.elements(); en.hasMoreElements();) {
			Bulb bulb = (Bulb) en.nextElement();
			Sprite sprite = bulb.sprite;
			if (Maths.pointInRect(x, y, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight())) {
				int reaction = (int)(System.currentTimeMillis() - bulb.lifeStart);
				score += Maths.clamp(1000 - reaction, 0, 1000);
				failsInRow = 0;
				bulbs.removeElement(bulb);
				if (Main.rand.nextInt(3) == 0)
					currentRate--;
				if (currentRate < 2)
					currentRate = 2;
				System.out.println(score);

				return true;
			}
		}

		return false;
	}
	
	private void createStar() {
		int x = Main.rand.nextInt(canvas.getWidth());
		int y = Main.rand.nextInt(canvas.getHeight());
		
		Star star = new Star(x, y);
		stars.addElement(star);
	}
	
	private void updateStars() {
		while (stars.size() < 20) {
			createStar();
		}
		
		for (int i = 0; i < stars.size(); i++) {
			Star star = (Star) stars.elementAt(i);
			if (star.state == 0) {
				star.color++;
				if (star.color == 10) 
					star.state = 1;
			} else if (star.state == 1) {
				if (Main.rand.nextInt(30) == 0) {
					star.state = 2;
				}
			} else if (star.state == 2) {
				star.color--;
				if (star.color == 0) {
					stars.removeElementAt(i--);
					continue;
				}
			}
		}
	}
	
	public void shutdown() {
		game.getCharacter().changeMood(score * 5);
	}
}
