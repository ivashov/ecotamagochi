package ikm.states;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.TiledLayer;

import ikm.Button;
import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Button.ButtonListener;
import ikm.game.Game;
import ikm.util.Maths;

public class MenuState extends GameState implements ButtonListener {
	private Button mazeButton;
	private Button sortingButton;
	
	private int centerX;
	
	private MazeState mazeState;
	private SortingState sortingState;
	private Game game;
	
	public MenuState(String name, MainCanvas canvas, Game game) {
		super(name, canvas);
		this.game = game;
		centerX = Maths.posCenter(canvas.getWidth(), Res.button.getWidth());
		
		mazeButton = new Button(centerX, 30, "Maze");
		sortingButton = new Button(centerX, 70, "Sorting");
		
		addClickable(mazeButton);
		addClickable(sortingButton);
		mazeButton.setListener(this);
		sortingButton.setListener(this);
	}
	
	public void update() {

	}

	public void paint(Graphics g) {
		renderParent(g);
		
		mazeButton.paint(g);
		sortingButton.paint(g);
	}

	public int getUpdateRate() {
		return 1000;
	}
	
	public void buttonClicked(Button button) {
		if (button == mazeButton) {
			mazeState = new MazeState(canvas);
			canvas.popState();
			canvas.pushState(mazeState);
		} else if (button == sortingButton) {
			sortingState = new SortingState(canvas, game.getWorld());
			canvas.popState();
			canvas.pushState(sortingState);
		}
	}
	
	public boolean clicked(int x, int y) {
		if (!super.clicked(x, y))
			canvas.back();
		return true;
	}
}
