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
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

import ikm.Button;
import ikm.FoodSelector;
import ikm.GameState;
import ikm.MainCanvas;
import ikm.ProgressBar;
import ikm.Res;
import ikm.Button.ButtonListener;
import ikm.FoodSelector.FoodListener;
import ikm.game.Food;
import ikm.game.Game;
import ikm.game.World;
import ikm.util.Maths;

public class MainState extends GameState implements ButtonListener, FoodListener {
	private LayerManager lm = new LayerManager();
	private Sprite facesprite;
	private Sprite deadsprite;
	
	private ProgressBar hpBar;
	private ProgressBar hungerBar;
	private ProgressBar moodBar;
	
	private Game game;
	private ikm.game.Character character;
	private World world;
	
	private Button feedButton;
	private Button gamesButton;
	
	private FoodSelector foodSelector;
	
	private MenuState gamesMenuState;
	
	public MainState(Game game, MainCanvas canvas) {
		super("Main state", canvas);
		
		this.game = game;
		character = game.getCharacter();
		world = game.getWorld();

		facesprite = new Sprite(Res.face);
		deadsprite = new Sprite(Res.faceDead);
		
		facesprite.setPosition(Maths.posCenter(canvas.getWidth(), facesprite.getWidth()),
				Maths.posCenter(canvas.getHeight(), facesprite.getHeight()) - 32);
		deadsprite.setPosition(facesprite.getX(), facesprite.getY());
		
		hpBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 0), 10, 0xef0000, ikm.game.Character.MAX_VALUE);
		hungerBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 1), 10, 0x00ff32, ikm.game.Character.MAX_VALUE);
		moodBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 2), 10, 0x0043ef, ikm.game.Character.MAX_VALUE);
		
		feedButton = new Button(Maths.posObject(canvas.getWidth(), Res.button.getWidth(), 2, 0), canvas.getHeight() - Res.button.getHeight(), "Feed");
		feedButton.setListener(this);
		
		gamesButton = new Button(Maths.posObject(canvas.getWidth(), Res.button.getWidth(), 2, 1), canvas.getHeight() - Res.button.getHeight(), "Games");
		gamesButton.setListener(this); 
		
		foodSelector = new FoodSelector(canvas.getHeight() - 100, canvas.getWidth());
		foodSelector.setListener(this);
		
		gamesMenuState = new MenuState("Games", canvas, game);
		
		addClickable(foodSelector);
		addClickable(feedButton);
		addClickable(gamesButton);
		addDragable(foodSelector);
		
		lm.append(facesprite);
	}
	
	public void paint(Graphics g) {
		g.setColor(110, 255, 200);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		lm.paint(g, 0, 0);
		hpBar.paint(g);
		hungerBar.paint(g);
		moodBar.paint(g);
		
		feedButton.paint(g);
		gamesButton.paint(g);
		foodSelector.paint(g);
		
	}

	public int getUpdateRate() {
		return Game.STEP_RATE * 1000;
	}

	public void update() {
		synchronized (game) {
			game.step();

			hpBar.setCurrentValue(character.getHp());
			hungerBar.setCurrentValue(character.getHunger());
			moodBar.setCurrentValue(character.getMood());
		}
		
		if (character.isDead()) {
			lm.remove(facesprite);
			lm.append(deadsprite);
		}
	}
	
	public void buttonClicked(Button button) {
		if (button == feedButton) {
			foodSelector.setFood(game.getAvailableFood());
		} else if (button == gamesButton) {
			canvas.pushState(gamesMenuState);
		}
	}
	
	public void foodSelected(Food food) {
		synchronized (game) {
			food.feed(character, game.getWorld());
		}
	}
	
	public boolean needExtraRedraw() {
		return true;
	}
}
