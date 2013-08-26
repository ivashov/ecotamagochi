/* 
 * This file is part of Ecotamagochi.
 * Copyright (C) 2013, Ivashov Kirill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package ikm.states;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.TiledLayer;

import ikm.Button;
import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Translation;
import ikm.Button.ButtonListener;
import ikm.game.Game;
import ikm.util.Maths;

public class GameMenuState extends GameState implements ButtonListener {
	private Button mazeButton;
	private Button sortingButton;
	private Button lightoffButton;
	
	private int centerX;
	
	private MazeState mazeState;
	private SortingState sortingState;
	private LightOffState lightoffState;
	private Game game;
	
	public GameMenuState(String name, MainCanvas canvas, Game game) {
		super(name, canvas);
		this.game = game;
		centerX = Maths.posCenter(canvas.getWidth(), Res.button.getWidth());
		
		mazeButton = new Button(centerX, 30, Translation.tr("maze"));
		sortingButton = new Button(centerX, 70, Translation.tr("sorting"));
		lightoffButton = new Button(centerX, 110, Translation.tr("lightoff"));
		
		addClickable(mazeButton);
		addClickable(sortingButton);
		addClickable(lightoffButton);
		mazeButton.setListener(this);
		sortingButton.setListener(this);
		lightoffButton.setListener(this);
	}
	
	public void update() {

	}

	public void paint(Graphics g) {
		renderParent(g);
		
		mazeButton.paint(g);
		sortingButton.paint(g);
		lightoffButton.paint(g);
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
			sortingState = new SortingState(canvas, game.getWorld(), game.getCharacter());
			canvas.popState();
			canvas.pushState(sortingState);
		} else if (button == lightoffButton) {
			lightoffState = new LightOffState(canvas, game);
			canvas.popState();
			canvas.pushState(lightoffState);
		}
	}
	
	public boolean clicked(int x, int y) {
		if (!super.clicked(x, y))
			canvas.back();
		return true;
	}
}
