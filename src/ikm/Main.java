package ikm;

import ikm.game.Game;
import ikm.states.MainState;
import ikm.util.ByteArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

public class Main extends MIDlet implements CommandListener {
	public static final Random rand = new Random();
	static {
		try {
			Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/config.txt"));
			Settings.loadSettings(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Can't load config");
		}
	}
	
	private Game game = new Game();
	private Display display;
	
	private Command backCommand = new Command("Back", Command.BACK, 1);
	private MainCanvas canvas;
	private MainState state;
	
	public Main() {
		//Log.disable();
		display = Display.getDisplay(this);
	}
	
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		if (canvas != null) {
			try {
				while (canvas.back())
					;
				canvas.currentState().shutdown();
				
				synchronized (canvas) {
					saveGame();
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			Res.initialize();
			Res.loadFood(game);
			Res.loadTrash();
		} catch (IOException e) {
			throw new MIDletStateChangeException("Cannot load resources");
		}
		
		canvas = new MainCanvas();
		state = new MainState(game, canvas);
		canvas.pushState(state);
		canvas.setCommandListener(this);
		canvas.addCommand(backCommand);
		
		display.setCurrent(canvas);
		
		try {
			loadGame();
		} catch (RecordStoreException e) {
			e.printStackTrace();
			try {
				RecordStore.deleteRecordStore("ectmgch");
			} catch (RecordStoreException e1) {
				e1.printStackTrace();
			}
		}
		
		Thread thread = new Thread(canvas, "Game thread");
		thread.start();
	}
	
	public void simulateFor(long seconds) {
		Log.disable();
		long time = System.currentTimeMillis();
		for (int i = 0; i < seconds; i += Game.STEP_RATE) {
			game.step();
			if (game.getCharacter().isDead())
				break;
		}
		Log.enable();
		Log.log("Simulation " + seconds + " seconds performed in " + (System.currentTimeMillis() - time) + " ms");
	}
	
	public void commandAction(Command c, Displayable d) {
		if (c == backCommand) {
			if (!canvas.back()) {
				try {
					destroyApp(false);
				} catch (MIDletStateChangeException e) {
					e.printStackTrace();
				}
				notifyDestroyed();
			}
		}
	}
	
	public final static int STORAGE_VERSION = 6;
	public void saveGame() throws RecordStoreException {
		RecordStore rms = null;
		try {
			byte[] arr = new byte[1024];
			ByteArray barr = new ByteArray(arr);
			try {
				rms = RecordStore.openRecordStore("ectmgch", false);
			} catch (RecordStoreNotFoundException e) {
				// Initialize record store a first run
				rms = RecordStore.openRecordStore("ectmgch", true);
				rms.addRecord(arr, 0, 4);
				rms.addRecord(arr, 0, 4);
				rms.addRecord(arr, 0, 4);
				rms.addRecord(arr, 0, 4);
				rms.addRecord(arr, 0, 4);
				rms.addRecord(arr, 0, 4);
			}

			// Write version
			barr.writeInt(game.getCharacter().isDead() ? -1 : STORAGE_VERSION);
			rms.setRecord(1, arr, 0, barr.length());
			barr.resetIdx();

			// Write character
			game.getCharacter().save(barr);
			rms.setRecord(2, arr, 0, barr.length());
			barr.resetIdx();

			// Write food storage
			game.saveFoodStorage(barr);
			rms.setRecord(3, arr, 0, barr.length());
			barr.resetIdx();

			// Write game
			game.saveGame(barr);
			rms.setRecord(4, arr, 0, barr.length());
			barr.resetIdx();

			// Write time
			barr.writeLong(System.currentTimeMillis() / 1000);
			rms.setRecord(5, arr, 0, barr.length());
			barr.resetIdx();
			
			// Write world
			game.getWorld().save(barr);
			rms.setRecord(6, arr, 0, barr.length());
			barr.resetIdx();
			
			Log.log("Game successfully saved");
		} finally {
			if (rms != null)
				rms.closeRecordStore();
		}
	}
	
	public void loadGame() throws RecordStoreException {
		RecordStore rms = null;
		try {
			byte[] arr = new byte[1024];
			ByteArray barr = new ByteArray(arr);

			try {
				rms = RecordStore.openRecordStore("ectmgch", false);
			} catch (RecordStoreNotFoundException e) {
				return;
			}

			// Load version
			rms.getRecord(1, arr, 0);
			if (barr.readInt() != STORAGE_VERSION) {
				Log.log("Unsupported version in record store");
				rms.closeRecordStore();
				rms = null;
				RecordStore.deleteRecordStore("ectmgch");
				return;
			}
			barr.resetIdx();
			
			// Load character
			rms.getRecord(2, arr, 0);
			game.getCharacter().load(barr);
			barr.resetIdx();

			// Load food storage
			rms.getRecord(3, arr, 0);
			game.loadFoodStorage(barr);
			barr.resetIdx();

			// Load game
			rms.getRecord(4, arr, 0);
			game.loadGame(barr);
			barr.resetIdx();

			// Load time
			long currentTime = System.currentTimeMillis() / 1000;
			rms.getRecord(5, arr, 0);
			long lastTime = barr.readLong();
			if (lastTime < currentTime) {
				simulateFor(currentTime - lastTime);
			}
			barr.resetIdx();
			
			// Load world
			rms.getRecord(6, arr, 0);
			game.getWorld().load(barr);
			barr.resetIdx();
			
			Log.log("Game successfully loaded");
		} finally {
			if (rms != null)
				rms.closeRecordStore();
		}
	}
}
