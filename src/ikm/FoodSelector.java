package ikm;

import ikm.GameState.Clickable;
import ikm.game.Food;
import ikm.util.Maths;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class FoodSelector implements Clickable {
	public interface FoodListener {
		void foodSelected(Food food);
	}
	
	public static final int HEIGHT = 32;
	
	private int y;
	private boolean hidden = true;
	private Vector foods;
	private int width;
	private FoodListener listener;
	
	public FoodSelector(int y, int width) {
		this.y = y;
		this.width = width;
	}
	
	public void setFood(Vector foods) {
		this.foods = foods;
		hidden = false;
	}
	
	public void paint(Graphics g) {
		if (hidden || foods == null)
			return;
				
		int foodCount = foods.size();
		for (int i = 0; i < foodCount; i++) {
			Food food = (Food) foods.elementAt(i);
			Sprite foodSprite = (Sprite) food.getData();
			
			foodSprite.setPosition(Maths.posObject(width, HEIGHT, foodCount, i), y);
			foodSprite.paint(g);
		}
	}
	
	public void setListener(FoodListener listener) {
		this.listener = listener;
	}
	
	public boolean clicked(int x, int y) {
		if (hidden || foods == null)
			return false;
		
		int foodCount = foods.size();
		for (int i = 0; i < foodCount; i++) {
			Food food = (Food) foods.elementAt(i);
			Sprite foodSprite = (Sprite) food.getData();
			
			int sx = foodSprite.getX();
			int sy = foodSprite.getY();
			int sw = foodSprite.getWidth();
			int sh = foodSprite.getHeight();
			
			if (Maths.pointInRect(x, y, sx, sy, sw, sh)) {
				hidden = true;
				foods = null;
				
				if (listener != null)
					listener.foodSelected(food);
				
				return true;
			}
		}	
		return false;
	}
}
