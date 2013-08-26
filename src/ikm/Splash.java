package ikm;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Splash {
	public static final int MOVE_PRE_FRAME = 4;
	
	private int x;
	private int y;
	
	private boolean active = false;
	private int steps;
	private String text;
	
	private int threadNumber = 0;
	private Thread splashThread;
	private MainCanvas canvas;

	public Splash(int x, int y, MainCanvas canvas) {
		this.x = x;
		this.y = y;
		this.canvas = canvas;
	}
	
	public void paint(Graphics g) {
		if (!active)
			return;
		g.setFont(Main.largeFont);
		g.setColor(0x00aaff);
		g.drawString(text, x, y - steps * MOVE_PRE_FRAME, Graphics.TOP | Graphics.HCENTER);
	}
	
	public void showSplash(String text) {
		active = true;
		this.text = text;
		steps = 0;
		
		final int number = ++threadNumber;
		
		splashThread = new Thread(new Runnable() {
			public void run() {
				while (number == threadNumber) {
					steps++;
					
					if (steps > 16) {
						active = false;
						canvas.redraw();
						break;
					}
					
					canvas.redraw();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		splashThread.start();
	}
}
