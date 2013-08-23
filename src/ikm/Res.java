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

package ikm;

import ikm.game.Food;
import ikm.game.Game;
import ikm.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Res {
	public static class TrashItem {
		TrashItem(Image img, int type) {
			image = img;
			this.type = type;
		}
		public Image image;
		public int type;
	}
	
	public static Image face;
	public static Image faceDead;
	public static Image progressbar;
	public static Image button;
	public static Image foodImg;
	public static Image tiles;
	
	public static TrashItem[] trash;
	
	public static Image binBack;
	public static Image binPlastic;
	public static Image binGlass;
	public static Image binPaper;

	public static Image mazeMask;
	
	public static Image icoHealth, icoHunger, icoMood;
	public static Image back;
	
	public static void initialize() throws IOException {
		if (true) {
			face = Image.createImage("/face/face1.png");
			faceDead = Image.createImage("/face/face1dead.png");
			back = Image.createImage("/scene_day.jpg");
		} else if (true) {
			face = Image.createImage("/face/facestub.png");
			faceDead = Image.createImage("/face/facestubdead.png");
			back = Image.createImage("/scene_day.jpg");
		} else {
			face = Image.createImage("/face/face2.gif");
			faceDead = Image.createImage("/face/facestubdead.png");
			back = Image.createImage("/distant-sun.jpg");
		}
		

		progressbar = Image.createImage("/progressbar.png");
		button = Image.createImage("/button2.png");
		foodImg = Image.createImage("/food.png");
		tiles = Image.createImage("/mazetile.png");
		
		binBack = Image.createImage("/trash/back.png");
		binPlastic = Image.createImage("/trash/bin-plastic.png");
		binGlass = Image.createImage("/trash/bin-glass.png");
		binPaper = Image.createImage("/trash/bin-paper.png");

		mazeMask = Image.createImage("/mazemask.png");
		
		icoHealth = Image.createImage("/icons/ico_health.png");
		icoHunger = Image.createImage("/icons/ico_hungry.png");
		icoMood = Image.createImage("/icons/ico_happy.png");
		
		
	}
	
	public static void loadFood(Game game) throws IOException {
		Reader foodDesc = new InputStreamReader(Res.class.getResourceAsStream("/food.txt"));

		for (;;) {
			String line = StringUtils.readLine(foodDesc);
			if (line == null)
				break;
			
			String[] args = StringUtils.split(line, ' ');
			if (args.length != 5) 
				break;

			try {
				String name = args[0];
				int litter = Integer.parseInt(args[1]);
				int hp = Integer.parseInt(args[2]);
				int hunger = Integer.parseInt(args[3]);
				int mood = Integer.parseInt(args[4]);

				game.createFood(name, hunger, litter, hp, mood);
				Food food = game.getFood(name);
				food.setData(new Sprite(Image.createImage("/food/" + name)));
				food.increaseCount(10);
			} catch (NumberFormatException ex) {
				break;
			}
		}
		
		foodDesc.close();
	}
	
	public static void loadTrash() throws IOException {
		Reader trashDesc = new InputStreamReader(Res.class.getResourceAsStream("/trash/trash.txt"));
		String line = null;
		Vector arr = new Vector();
		
		while (null != (line = StringUtils.readLine(trashDesc))) {
			String[] args = StringUtils.split(line, ' ');
			if (args.length < 2)
				continue;
			
			try {
			String src = args[0];
			int type = Integer.parseInt(args[1]);
			Image img = Image.createImage("/trash/" + src);
			
			arr.addElement(new TrashItem(img, type));
			
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}
		
		trash = new TrashItem[arr.size()];
		arr.copyInto(trash);
		
		trashDesc.close();
	}
}
