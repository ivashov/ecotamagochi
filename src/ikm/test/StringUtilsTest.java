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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import ikm.Settings;
import ikm.util.StringUtils;
import jmunit.framework.cldc11.TestCase;

public class StringUtilsTest extends TestCase {
	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public StringUtilsTest() {
		super(4, "StringUtilsTest");
	}

	public final void split1Test() {
		String str ="aaa bbb ccc";
		String res[] = StringUtils.split(str, ' ');
	
		assertEquals(3, res.length);
		
		assertEquals("aaa", res[0]);
		assertEquals("bbb", res[1]);
		assertEquals("ccc", res[2]);
	}
	
	public final void split2Test() {
		String str =" 000  aaa       bbbccc";
		String res[] = StringUtils.split(str, ' ');
	
		assertEquals(3, res.length);
		
		assertEquals("000", res[0]);
		assertEquals("aaa", res[1]);
		assertEquals("bbbccc", res[2]);
	}
	
	public final void split3Test() {
		String str ="aaa bbb  ccc        ddd ";
		String res[] = StringUtils.split(str, ' ');
	
		assertEquals(4, res.length);
		
		assertEquals("aaa", res[0]);
		assertEquals("bbb", res[1]);
		assertEquals("ccc", res[2]);
		assertEquals("ddd", res[3]);
	}

	public final void readLineTest() {
		String str = "aaa\nbbb\r\nccc\r\nddd";
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
		Reader reader = new InputStreamReader(in);
		try {
			assertEquals(StringUtils.readLine(reader), "aaa");
			assertEquals(StringUtils.readLine(reader), "bbb");
			assertEquals(StringUtils.readLine(reader), "ccc");
			assertEquals(StringUtils.readLine(reader), "ddd");
			assertNull(StringUtils.readLine(reader));
		} catch (IOException e) {
			fail(e.toString());
		}
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
			split1Test();
			break;
		case 1:
			split2Test();
			break;
		case 2:
			split3Test();
			break;
		case 3:
			readLineTest();
			break;
		}
	}

}
