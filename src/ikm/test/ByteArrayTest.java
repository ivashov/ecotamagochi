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

package ikm.test;

import java.util.Random;

import ikm.util.ByteArray;
import jmunit.framework.cldc11.TestCase;

public class ByteArrayTest extends TestCase {
	private ByteArray arr;
	private byte[] b;
	
	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public ByteArrayTest() {
		super(4, "ByteArrayTest");
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
		arr = new ByteArray(b = new byte[1024]);
	}

	public final void test1() {
		arr.writeInt(1);
		arr.writeInt(-1);
		arr.writeInt(0xafffffaa);
		
		arr.resetIdx();
				
		assertEquals(1, arr.readInt());
		assertEquals(-1, arr.readInt());
		assertEquals(0xafffffaa, arr.readInt());
	}
	
	public final void testRandom() {
		Random r = new Random();
		int[] iarr = new int[100];
		for (int i = 0; i < 100; i++) {
			iarr[i] = r.nextInt();
			arr.writeInt(iarr[i]);
		}
		
		arr.resetIdx();
		
		for (int i = 0; i < 100; i++) {
			assertEquals(iarr[i], arr.readInt());
		}
	}
	
	public final void testRandomLong() {
		Random r = new Random();
		long[] iarr = new long[100];
		for (int i = 0; i < 100; i++) {
			iarr[i] = r.nextLong();
			arr.writeLong(iarr[i]);
		}
		
		arr.resetIdx();
		
		for (int i = 0; i < 100; i++) {
			assertEquals(iarr[i], arr.readLong());
		}
	}
	
	public final void testString() {
		arr.writeString("Hello world");
		arr.writeString("");
		arr.writeString("\u2665\u2666\u2663\u2660");
		
		arr.resetIdx();
		
		assertEquals("Hello world", arr.readString());
		assertEquals("", arr.readString());
		assertEquals("\u2665\u2666\u2663\u2660", arr.readString());
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
			test1();
			break;
		case 1:
			testRandom();
			break;
		case 2:
			testRandomLong();
			break;
		case 3:
			testString();
			break;
		}
	}

}
