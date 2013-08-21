package ikm.game;

import ikm.util.ByteArray;

public class World {
	private int trashCount;
	public void addTrash(int count) {
		trashCount += count;
		if (trashCount < 0)
			trashCount = 0;
	}
	
	public void save(ByteArray arr) {
		arr.writeInt(trashCount);
	}
	
	public void load(ByteArray arr) {
		trashCount = arr.readInt();
	}

	public int getTrash() {
		return trashCount;
	}
}
