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
import ikm.Main;
import ikm.MainCanvas;
import ikm.ProgressBar;
import ikm.Res;
import ikm.Settings;
import ikm.Splash;
import ikm.Translation;
import ikm.Button.ButtonListener;
import ikm.FoodSelector.FoodListener;
import ikm.game.Character;
import ikm.game.Food;
import ikm.game.Game;
import ikm.game.World;
import ikm.util.Maths;

public class MainState extends GameState implements ButtonListener, FoodListener {
	public static final int MOOD_FOOD_REJECT = Settings.getEntry("mood_food_reject");
	public static final int HUNGER_GAME_REJECT = Settings.getEntry("hunger_game_reject");
	
	private LayerManager lm = new LayerManager();
	private Sprite background;
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
	private Splash splash;
	
	public MainState(Game game, MainCanvas canvas) {
		super("Main state", canvas);
		
		this.game = game;
		character = game.getCharacter();
		world = game.getWorld();

		background = new Sprite(Res.back);
		background.setPosition(Maths.posCenter(canvas.getWidth(), background.getWidth()),
				Maths.posCenter(canvas.getHeight(), background.getHeight()) - 50);
		facesprite = new Sprite(Res.face);
		deadsprite = new Sprite(Res.faceDead);
		
		facesprite.setPosition(Maths.posCenter(canvas.getWidth(), facesprite.getWidth()),
				Maths.posCenter(canvas.getHeight(), facesprite.getHeight()) - 32);
		deadsprite.setPosition(facesprite.getX(), facesprite.getY());
		
		hpBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 0), 10, 0xef0000, ikm.game.Character.MAX_VALUE, Res.icoHealth);
		hungerBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 1), 10, 0x00ff32, ikm.game.Character.MAX_VALUE, Res.icoHunger);
		moodBar = new ProgressBar(Maths.posObject(canvas.getWidth(), Res.progressbar.getWidth(), 3, 2), 10, 0x0043ef, ikm.game.Character.MAX_VALUE, Res.icoMood);
		
		feedButton = new Button(Maths.posObject(canvas.getWidth(), Res.button.getWidth(), 2, 0), canvas.getHeight() - Res.button.getHeight(), Translation.tr("feed"));
		feedButton.setListener(this);
		
		gamesButton = new Button(Maths.posObject(canvas.getWidth(), Res.button.getWidth(), 2, 1), canvas.getHeight() - Res.button.getHeight(), Translation.tr("games"));
		gamesButton.setListener(this); 
		
		foodSelector = new FoodSelector(canvas.getHeight() - 100, canvas.getWidth());
		foodSelector.setListener(this);
		
		gamesMenuState = new MenuState("Games", canvas, game);
		
		splash = new Splash(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas);
		addClickable(foodSelector);
		addClickable(feedButton);
		addClickable(gamesButton);
		addDragable(foodSelector);
		
		lm.append(facesprite);
		lm.append(background);
	}
	
	public void paint(Graphics g) {		
		lm.paint(g, 0, 0);
		hpBar.paint(g);
		hungerBar.paint(g);
		moodBar.paint(g);
		
		feedButton.paint(g);
		gamesButton.paint(g);
		foodSelector.paint(g);
		splash.paint(g);
	}
	
	private int findProblems(int[] arr) {
		int c = 0;
		if (world.getTrash() > 15)
			arr[c++] = 0;
		
		if (character.getHunger() < Character.EAT_STARVATION)
			arr[c++] = 1;
		
		if (character.getHp() < Character.MAX_VALUE / 5)
			arr[c++] = 2;
		
		if (character.getMood() < Character.MAX_VALUE / 5)
			arr[c++] = 3;
		
		return c;
	}

	private int[] problems = new int[10];
	private void showAlert() {
		int size = 0;

		if (Main.rand.nextInt(8) == 1 && (size = findProblems(problems)) > 0) {
			switch (problems[Main.rand.nextInt(size)]) {
			case 0: // Trash too high
				splash.showSplash(Translation.tr("too_many_trash"));
				break;

			case 1: // Too hunger
				splash.showSplash(Translation.tr("i_am_hunger"));
				break;

			case 2: // Low hp
				splash.showSplash(Translation.tr("i_am_ill"));
				break;
				
			case 3: // Low mood
				splash.showSplash(Translation.tr("i_am_depression"));
				break;
			}
		}
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
		
		showAlert();
		
		if (character.isDead()) {
			lm.remove(facesprite);
			lm.insert(deadsprite, 0);
			
			removeClickable(feedButton);
			removeClickable(gamesButton);
			removeClickable(foodSelector);
			removeDragable(foodSelector);
		}
	}
	
	public void buttonClicked(Button button) {
		if (button == feedButton) {
			if (character.getMood() < MOOD_FOOD_REJECT && character.getHunger() > Character.MAX_VALUE / 5) {
				splash.showSplash(Translation.tr("dont_want_eat"));
			} else {
				foodSelector.setFood(game.getAvailableFood());
			}
		} else if (button == gamesButton) {
			if (character.getHunger() < HUNGER_GAME_REJECT && character.getMood() > Character.MAX_VALUE / 5) {
				splash.showSplash(Translation.tr("dont_want_play"));
			} else {
				canvas.pushState(gamesMenuState);
			}
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
