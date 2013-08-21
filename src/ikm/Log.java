package ikm;

public class Log {
	private static long step = 0;
	private static boolean enabled = true;
	
	public static void log(String string) {
		if (enabled) 
			System.out.println("" + step + ": " + string);
	}
	
	public static void err(String err) {
		if (enabled)
			System.err.println(err);
	}
	
	public static void enable() {
		enabled = true;
	}
	
	public static void disable() {
		enabled = false;
	}
	
	public static void setStep(long step) {
		Log.step = step;
	}
}
