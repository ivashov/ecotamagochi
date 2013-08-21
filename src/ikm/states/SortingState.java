package ikm.states;

import ikm.GameState;
import ikm.Log;
import ikm.Main;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Res.TrashItem;
import ikm.game.World;
import ikm.util.Maths;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SortingState extends GameState {
	//private Vector fallingItems = new Vector();
	private Sprite activeItem;
	private int itemX;
	private int itemType;
	
	private int itemsSuccess = 0;
	
	private World world;
	
	private Sprite missItem;
	private Sprite binItem;
	
	private Sprite binBack1;
	private Sprite binBack2;
	private Sprite binBack3;
	
	private Sprite binPlastic;
	private Sprite binGlass;
	private Sprite binPaper;
	
	private boolean pointerDown = false;
	private int px, py;
	private int colorIndex = 0;
	private static final int[] BACK_COLORS = {0x1280a0, 0x2F6F8B, 0x4D5F77, 0x6B4F63, 0x883F4F, 0xA62F3B, 0xC41F27, 0xE20F13, 0xff0000};
	
	public SortingState(MainCanvas canvas, World world) {
		super("sorting", canvas);
		this. world = world;
		
		binBack1 = new Sprite(Res.binBack);
		binBack2 = new Sprite(Res.binBack);
		binBack3 = new Sprite(Res.binBack);
		
		binPlastic = new Sprite(Res.binPlastic);
		binGlass = new Sprite(Res.binGlass);
		binPaper = new Sprite(Res.binPaper);

		binPlastic.setPosition(Maths.posObject(canvas.getWidth(), binPlastic.getWidth(), 3, 0), canvas.getHeight() - binPlastic.getHeight());
		binGlass.setPosition(Maths.posObject(canvas.getWidth(), binGlass.getWidth(), 3, 1), canvas.getHeight() - binGlass.getHeight());
		binPaper.setPosition(Maths.posObject(canvas.getWidth(), binPaper.getWidth(), 3, 2), canvas.getHeight() - binPaper.getHeight());

		binBack1.setPosition(binPlastic.getX(), binPlastic.getY());
		binBack2.setPosition(binGlass.getX(), binGlass.getY());
		binBack3.setPosition(binPaper.getX(), binPaper.getY());
		
		createItem();
	}
	
	private void createItem() {
		TrashItem tritem = Res.trash[Main.rand.nextInt(Res.trash.length)];
		activeItem = new Sprite(tritem.image);
		itemX = Main.rand.nextInt(canvas.getWidth() - activeItem.getWidth() / 2);

		activeItem.setPosition(itemX - activeItem.getWidth() / 2, 0);
		itemType = tritem.type;
	}
	
	private boolean touchesBucket(Sprite item, Sprite bucket) {
		return item.getY() + item.getHeight() >= bucket.getY() && item.getX() >= bucket.getX()
				&& item.getX() + item.getWidth() <= bucket.getX() + bucket.getWidth();
	
	}
	
	private void update(Sprite item) {
		if (item != null) {
			item.setPosition(item.getX(), item.getY() + 6);
		}
	}

	public void update() {				
		update(activeItem);
		update(missItem);
		update(binItem);

		if (touchesBucket(activeItem, binPlastic) && itemType == 1
				|| touchesBucket(activeItem, binGlass) && itemType == 2
				|| touchesBucket(activeItem, binPaper) && itemType == 0) {
			binItem = activeItem;
			itemsSuccess++;
			createItem();
		}
		
		if (activeItem.getY() + activeItem.getHeight() > binPlastic.getY() + 5) {
			missItem = activeItem;
			colorIndex = BACK_COLORS.length - 1;
			createItem();
		}
		
		if (pointerDown) {
			int diff = px - itemX;
			int sing = Maths.sign(diff);
			itemX += sing * 8;
			activeItem.setPosition(itemX - activeItem.getWidth() / 2,
					activeItem.getY());
		}
	}

	public void paint(Graphics g) {
		final int backColor = BACK_COLORS[colorIndex];
		if (colorIndex > 0)
			colorIndex--;
		
		g.setColor(backColor);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		binBack1.paint(g);
		binBack2.paint(g);
		binBack3.paint(g);
		
		activeItem.paint(g);
		if (binItem != null)
			binItem.paint(g);
		
		g.setColor(backColor);
		g.fillRect(0, canvas.getHeight() - 63, canvas.getWidth(), 63);

		binPlastic.paint(g);
		binGlass.paint(g);
		binPaper.paint(g);
		
		if (missItem != null)
			missItem.paint(g);
	}

	public int getUpdateRate() {
		return 100;
	}

	public boolean clicked(int x, int y) {
		if (super.clicked(x, y))
			return true;
		
		pointerDown = true;
		px = x;
		py = y;
		return true;
	}
	
	public void released(int x, int y) {
		pointerDown = false;
	}
	
	public void dragged(int x, int y) {
		px = x;
		py = y;
		pointerDown = true;
	}
	
	public void shutdown() {
		world.addTrash(-itemsSuccess);
	}
}
