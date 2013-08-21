package ikm.game;

import ikm.Settings;
import ikm.util.ByteArray;
import ikm.util.Maths;

public class Food {
	public static final int ADAPTATION_RATE = Settings.getEntry("adaptation_rate");
	public static final int ADAPTATION_MAX = Settings.getEntry("adaptation_max");
	
	private String name;
	
	private int hungerEffect;
	private int healthEffect;
	private int moodEffect;
	private int trash;
	private Object data;
	
	private int adaptation = 0;
	private int foodCount = 1;
	
	public Food(String name, int hunger, int trash_, int health, int mood) {
		this.name = name;
		
		hungerEffect = hunger;
		trash = trash_;
		healthEffect = health;
		moodEffect = mood;
	}
	
	public void increaseCount(int count) {
		foodCount += count;
	}

	public void feed(Character character, World world) {
		if (foodCount == 0 || character.isDead())
			return;
		foodCount--;
		
		world.addTrash(trash);
		
		character.changeHpDelta(healthEffect);
		character.changeHunger(hungerEffect);
		character.changeMood(moodEffect * (ADAPTATION_MAX - adaptation) * ADAPTATION_RATE / ADAPTATION_MAX);
		adaptation = Maths.clamp(adaptation + 1, 0, ADAPTATION_MAX);
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public void decreaseAdaptation() {
		adaptation = Maths.clamp(adaptation - 1, 0, ADAPTATION_MAX);
	}
	
	public void save(ByteArray arr) {
		arr.writeString(name);
		arr.writeInt(adaptation);
		arr.writeInt(foodCount);
	}
	
	public void load(ByteArray arr) {
		adaptation = arr.readInt();
		foodCount = arr.readInt();
	}
}
