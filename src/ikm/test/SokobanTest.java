package ikm.test;

import ikm.Log;
import ikm.states.Sokoban;
import jmunit.framework.cldc11.TestCase;

public class SokobanTest extends TestCase {
	private String map1 
		= "#...#"
		+ "#.#.#"
		+ "#px0#"
		+ ".....";
	
	private Sokoban generate1() {
		Sokoban s = new Sokoban(5, 4);
		s.loadFromString(map1);
		return s;
	}
	
	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public SokobanTest() {
		super(5, "SokobanTest");
	}

	public final void Sokoban1Test() {
		Sokoban s = generate1();
		assertEquals(Sokoban.WALL, s.getCell(0, 0));
		assertEquals(Sokoban.FREE, s.getCell(1, 0));
		assertEquals(Sokoban.END, s.getCell(3, 2));
		assertEquals(1, s.getX());
		assertEquals(2, s.getY());
		assertEquals(1, s.countEnds());
	}

	public final void isInner1Test() {
		Sokoban s = generate1();
		assertTrue(s.isInner(0, 0));
		assertTrue(s.isInner(4, 3));
		assertFalse(s.isInner(-1, -1));
		assertFalse(s.isInner(5, 0));
		assertFalse(s.isInner(4, 4));
	}

	public final void moveObjectTest() {
		Sokoban s = generate1();
		s.moveObject(2, 2, 3, 2, Sokoban.CRATE);
		assertTrue(s.isCell(3, 2, Sokoban.CRATE));
		assertTrue(s.isCell(3, 2, Sokoban.END));
		assertEquals(Sokoban.FREE, s.getCell(2, 2));
	}

	public final void moveTest1() {
		Sokoban s = generate1();
		s.move(0, -1);
		s.move(0, -1);
		s.move(0, -1);
		
		assertEquals(1, s.getX());
		assertEquals(0, s.getY());
	}
	
	public final void moveTest2() {
		Sokoban s = generate1();
		s.move(1, 0);
		s.move(1, 0);

		assertTrue(s.isCell(3, 2, Sokoban.CRATE));
		assertTrue(s.isCell(3, 2, Sokoban.END));
		
		s.move(0, 1);
		s.move(1, 0);
		s.move(0, -1);
		
		assertEquals(3, s.getX());
		assertEquals(2, s.getY());
		
		String m 
		= "#...#"
		+ "#.#x#"
		+ "#..p#"
		+ ".....";
		assertEquals(m, s.toString());
	}

	/**
	 * A empty method used by the framework to initialize the tests. If there's
	 * 5 test methods, the setUp is called 5 times, one for each method. The
	 * setUp occurs before the method's execution, so the developer can use it
	 * to any necessary initialization. It's necessary to override it, however.
	 * 
	 * @throws Throwable anything that the initialization can throw.
	 */
	public void setUp() throws Throwable {
	}

	/**
	 * A empty mehod used by the framework to release resources used by the
	 * tests. If there's 5 test methods, the tearDown is called 5 times, one for
	 * each method. The tearDown occurs after the method's execution, so the
	 * developer can use it to close something used in the test, like a
	 * nputStream or the RMS. It's necessary to override it, however.
	 */
	public void tearDown() {
	}

	/**
	 * This method stores all the test methods invocation. The developer must
	 * implement this method with a switch-case. The cases must start from 0 and
	 * increase in steps of one until the number declared as the total of tests
	 * in the constructor, exclusive. For example, if the total is 3, the cases
	 * must be 0, 1 and 2. In each case, there must be a test method invocation.
	 * 
	 * @param testNumber the test to be executed.
	 * @throws Throwable anything that the executed test can throw.
	 */
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			Sokoban1Test();
			break;
		case 1:
			isInner1Test();
			break;
		case 2:
			moveObjectTest();
			break;
		case 3:
			moveTest1();
			break;
		case 4:
			moveTest2();

			break;
		}
	}

}
