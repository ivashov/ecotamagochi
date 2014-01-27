package ikm.states.sgen;

import ikm.states.Sokoban;

import java.io.*;
import java.util.*;

class Set {
	private Object obj = new Object();
	private Hashtable hashtable = new Hashtable();
	
	public void add(Map map) {
		hashtable.put(map, obj);
	}
	
	public Enumeration elements() {
		return hashtable.keys();
	}
	
	public boolean isEmpty() {
		return hashtable.isEmpty();
	}
	
	public void removeAll(Set set) {
		for (Enumeration en = set.elements(); en.hasMoreElements();) {
			hashtable.remove(en.nextElement());
		}
	}
}

class Point {
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point() {
        x = y = 0;
    }

    int x, y;
}

class Map {
    static int FLOOR = 0;
    static int WALL = 1;
    static int CRATE = 2;
    static int END = 4;
    
    static int REACH = 8;

    static int VOID = 16;


    int[] map;
    int w, h;
    int lines = 0;
    int pushes = 0;

    int ci = 0;
    Point[] cratePositions;

    Map(int w, int h) {
        map = new int[w * h];
        this.w = w;
        this.h = h;
    }

    /*Map(String file, int w, int h) throws IOException {
        this(w, h);
 
        FileReader in = new FileReader(file);

        int c = 0;
        int i = 0;
        while ((c = in.read()) != -1) {

            char cc = (char) c;

            if (cc == '\n')
                continue;
            else if (cc == '#')
                set(i % w, i / w, WALL);
            else
                set(i % w, i / w, FLOOR);
            i++;
        }

        in.close();
    }*/

    Map(Map m) {
    	map = new int[m.map.length];
    	for (int i = 0; i < map.length; i++)
    		map[i] = m.map[i];
    	
        cratePositions = new Point[m.cratePositions.length];
        for (int i = 0; i < cratePositions.length; i++)
        	cratePositions[i] = m.cratePositions[i];

        w = m.w;
        h = m.h;
        lines = m.lines;
        pushes = m.pushes;
    }

    void setCrateCount(int n) {
        cratePositions = new Point[n];
        ci = 0;
    }

    void flood(int x, int y) {
        Point start = new Point(x, y);

        for (int i = 0; i < map.length; i++) {
            map[i] &= ~REACH;
        }

        Vector queue = new Vector();
        queue.addElement(start);

        int idx = 0;
        while (idx < queue.size()) {
            Point p = (Point) queue.elementAt(idx++);

            if (isInner(p.x, p.y)) {
                int f = map[p.x + p.y * w];
                if ((f & WALL) == 0 && (f & REACH) == 0 && (f & CRATE) == 0) {
                    // Expand point
                    queue.addElement(new Point(p.x + 1, p.y));
                    queue.addElement(new Point(p.x - 1, p.y));
                    queue.addElement(new Point(p.x, p.y + 1));
                    queue.addElement(new Point(p.x, p.y - 1));

                    set(p.x, p.y, REACH);
                }
            }
        }
    }

    void placeCrate(int x, int y) {
        set(x, y, CRATE);
        set(x, y, END);
        cratePositions[ci++] = new Point(x, y);

    }

    boolean isFree(int x, int y) {
        return isInner(x, y) && !is(x, y, WALL) && !is(x, y, CRATE);
    }

    boolean isInner(int x, int y) {
        return x >= 0 && y >=0 && x < w && y < h;
    }

    boolean isWall(int x, int y) {
    	return !isInner(x, y) || is(x, y, WALL);
    }

    void set(int x, int y, int flag) {
        map[x + y * w] |= flag;
    }

    boolean is(int x, int y, int flag) {
        return (map[x + y * w] & flag) != 0;
    }

    void clear(int x, int y, int flag) {
        map[x + y * w] &= ~flag;
    }


    int hash = 0;
    void commit() {
    	int h = 1;
    	for (int i = 0; i < map.length; i++) {
    		h = 31 * h + map[i];
    	}
    	hash = h;
    }


    public int hashCode() {
        return hash;
    }

    public boolean equals(Object o) {
        Map m = (Map) o;
        return hash == m.hash;
    }

    public int value() {
        return 100 * (pushes + 4 * lines - 12 * cratePositions.length);
    }

    public boolean applyTemplate(int x0, int y0, String t) {
        int[] tmp = new int[map.length];
    	for (int i = 0; i < map.length; i++)
    		tmp[i] = map[i];

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                char c = t.charAt(x + y * 5);
                int xx = x + x0;
                int yy = y + y0;

                if (!isInner(xx, yy))
                    continue;

                if (c == '#') {
                    if (is(xx, yy, FLOOR)) {
                        map = tmp;
                        return false;
                    } 
                    set(xx, yy, WALL);
                } else if (c == '.') {
                    if (is(xx, yy, WALL)) {
                        map = tmp;
                        return false;
                    }
                    set(xx, yy, FLOOR);
                }
            }
        }
        return true;
    }

    public void dump() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (is(x, y, WALL))
                    System.out.print('#');
                else if (is(x, y, CRATE) && is(x, y, END))
                    System.out.print('*');
                else if (is(x, y, CRATE))
                    System.out.print('$');
                else if (is(x, y, END))
                    System.out.print('.');
                else if (is(x, y, REACH))
                    System.out.print('~');
                else
                    System.out.print(' ');
            }
            System.out.println();
        }

        System.out.println("Lines " + lines);
        System.out.println("Pushes " + pushes);
        System.out.println("Value " + value());
    }
}

public class SGen {
    public static void main(String[] args) throws IOException {
    }
    public static Random r;

    static Set generate(Map map, int w, int h) {
        int c = 3;

        map.setCrateCount(c);
        
        // Make start set
        for (int i = 0; i < c; i++) {
            int cx, cy;
            do {
                cx = r.nextInt(w);
                cy = r.nextInt(h);
            } while (!map.isFree(cx, cy));
            map.placeCrate(cx, cy);
        }

        int px, py;
        do {
            px = r.nextInt(w);
            py = r.nextInt(h);
        } while (!map.isFree(px, py));

        map.flood(px, py);

        map.commit();
        map.dump();

        Set startSet = new Set();
        startSet.add(map);

        Set prevSet;
        Set resultSet = startSet;
        int depth = 1;

        long beginTime = System.currentTimeMillis();
        for (;;) {
            prevSet = resultSet;

            if (System.currentTimeMillis() - beginTime > 1200)
            	break;
            
            resultSet = tri(startSet, prevSet, depth);
            if (resultSet.isEmpty())
                break;

            depth++;
        }

        return prevSet;
    }

    static Set tri(Set startSet, Set prevSet, int depth) {
        Set resultSet = expand(prevSet);
        Set tempSet = startSet;

        for (int i = 1; i < depth; i++) {
            resultSet.removeAll(tempSet);
            tempSet = expand(tempSet);
        }

        return resultSet;
    }

    static Set expand(Set set) {
        Set resultSet = new Set();

        for (Enumeration en = set.elements(); en.hasMoreElements();) {
        	Map map = (Map) en.nextElement();
            expand(map, resultSet);
        }

        return resultSet;
    }

    public static final Point[] dirs = {new Point(1, 0),
                                        new Point(-1, 0),
                                        new Point(0, 1),
                                        new Point(0, -1)};

    static void expand(Map map, Set resultSet) {
        for (int i = 0; i < map.cratePositions.length; i++) {
            Point crate = map.cratePositions[i];
            for (int j = 0; j < dirs.length; j++) {
            	Point dir = dirs[j];
                // Player position
                int px = crate.x + dir.x;
                int py = crate.y + dir.y;

                // Player next position
                int nx = px + dir.x;
                int ny = py + dir.y;
                
                // Crate position
                int cx = crate.x;
                int cy = crate.y;

                int push = 1;
                // REACH always means that tile is free
                while (map.isInner(px, py) && map.is(px, py, Map.REACH) && map.isInner(nx, ny) && map.is(nx, ny, Map.REACH)) {
                    Map map2 = new Map(map);

                    // Pull crate
                    map2.clear(crate.x, crate.y, Map.CRATE);
                    map2.set(px, py, Map.CRATE);
                    map2.cratePositions[i] = new Point(px, py);
                    map2.flood(nx, ny);
                    map2.commit();

                    map2.lines++;
                    map2.pushes += push;

                    resultSet.add(map2);
                    px = nx;
                    py = ny;

                    nx += dir.x;
                    ny += dir.y;
                    push++;
                }
            }
        }
    }

    static String[] templates1;
    static final String[] templates 
        = {
           "     "
        +  " ... "
        +  " ... "
        +  " ... "
        +  "     ",

           "     "
        +  " #.. "
        +  " ... "
        +  " ... "
        +  "     ",
        
           "   .."
        +  " ##.."
        +  " ... "
        +  " ... "
        +  "     ",

           "     "
        +  " ### "
        +  " ... "
        +  " ... "
        +  "     ",

           "     "
        +  " ### "
        +  " #.. "
        +  " #.. "
        +  "     ",
        
           "  .  "
        +  " #.. "
        +  ".... "
        +  " ..# "
        +  "     ",

           "     "
        +  " #.. "
        +  ".... "
        +  " #.. "
        +  "     ",

           "  .  "
        +  " #.. "
        +  ".... "
        +  " #.# "
        +  "  .  ",

           "  .  "
        +  " #.# "
        +  "....."
        +  " #.# "
        +  "  .  ",

           "  .  "
        +  " #.# "
        +  " #..."
        +  " ### "
        +  "     ",

           "     "
        +  " ### "
        +  "....."
        +  " ### "
        +  "     ",

           "     "
        +  " ...."
        +  " .#.."
        +  " ... "
        +  "     ",

           "     "
        +  " ### "
        +  " ### "
        +  " ### "
        +  "     ",

           "     "
        +  " ### "
        +  " #.. "
        +  ".... "
        +  "..   ",

           " . . "
        +  " ... "
        +  " #.# "
        +  " ... "
        +  " . . ",

           "     "
        +  " ### "
        +  " ### "
        +  " ... "
        +  " ... ",

           "     "
        +  " ### "
        +  "..#.."
        +  " ... "
        +  " ..  ",
        }; 
    
    static String rotate90(String arr25) {
    	char[] ret = new char[25];
    	
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                ret[i + (j * 5)] = arr25.charAt((5 - j - 1) + (i * 5));
            }
        }
        
        return new String(ret);
    }
    
    // Rotate templates
    static {
    	int l = templates.length;
    	templates1 = new String[l * 4];
    	
    	for (int i = 0; i < l; i++)
    		templates1[i] = templates[i];
    	
    	for (int i = l; i < l * 4; i++) {
    		templates1[i] = rotate90(templates1[i - l]);
    	}
    }

    static Map tryGenerateMap(Random r, int w, int h) {
        Map map = new Map(w, h);
        for (int i = 0; i < map.map.length; i++)
            map.map[i] = Map.VOID;

        int um = (w - 1) / 3 + 1;
        int vm = (h - 1) / 3 + 1;

        for (int u = 0; u < um; u++) {
v:
            for (int v = 0; v < vm; v++) {
                for (int i = 0; i < 10; i++) {
                    String t = templates1[r.nextInt(templates1.length)];
                    
                    if (map.applyTemplate(u * 3 - 1, v * 3 - 1, t)) {
                        continue v;
                    }
                }
                return null;
            }
        }
        return map;
    }

    static boolean validate(Map map, int w, int h) {
        if (map == null)
            return false;

        // Check connectivity
        int ex = 0;
        int ey = 0;
        int freeCount = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (map.isFree(x, y)) {
                    ex = x;
                    ey = y;
                    freeCount++;
                }
            }
        }

        int floodCount = 0;
        map.flood(ex, ey);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (map.is(x, y, Map.REACH)) {
                    floodCount++;
                    map.clear(x, y, Map.REACH);
                }
            }
        }

        if (floodCount < freeCount) {
            System.err.println("Check connectivity failed");
            return false;
        }
        
        /*// Check 3-wall cells
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {  
                int countWall = 0;
            	countWall += map.isWall(x + 1, y) ? 1 : 0;
            	countWall += map.isWall(x - 1, y) ? 1 : 0;
            	countWall += map.isWall(x, y + 1) ? 1 : 0;
            	countWall += map.isWall(x, y - 1) ? 1 : 0;
            	
                if (countWall >= 3) {
                    System.err.println("3-wall failed: " + countWall);
                	return false;
                }
            }
        }*/
        
        return true;
    }

    static Map generateMap(Random r, int w, int h) {
        Map map = null;
        do {
            map = tryGenerateMap(r, w, h);
        } while (!validate(map, w, h));
        return map;
    }
    
    public static Sokoban generateSokobalLevel(int w, int h) {
    	Sokoban s = new Sokoban(w, h);
    	
        //long seed = System.currentTimeMillis();
        r = new Random();
        Map best = null;
        do {
        	//Map map = new Map("in.txt", 7, 7);
        	Map map = generateMap(r, w, h);
        	Set set = generate(map, map.w, map.h);
        
        	//System.out.println("========");

        	best = null;
        	int max = Integer.MIN_VALUE;
        	for (Enumeration en = set.elements(); en.hasMoreElements();) {
        		Map m = (Map) en.nextElement();
        		//m.dump();
            
        		int v = m.value();
        		if (v > max) {
        			best = m;
        			max = v;
        		}
        	}
        } while (best == null || best.value() < 0);

        best.dump();
        // Generate player position
        int px, py;
        do {
            px = r.nextInt(w);
            py = r.nextInt(h);
        } while (!best.is(px, py, Map.REACH));
        
        for (int x = 0; x < w; x++) {
        	for (int y = 0; y < h; y++) {
        		s.setCell(x, y, best.map[x + w * y] & (4 + 2 + 1));
        	}
        }
        s.setPlayer(px, py);
        
        return s;
    }
}

