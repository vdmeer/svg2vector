/* Copyright 2014 Sven van der Meer <vdmeer.sven@mykolab.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vandermeer.svg2vector;

import static org.junit.Assert.*;

import org.junit.Test;

import de.vandermeer.svg2vector.Tool;

public class PdfToolTests {
	String[] args;

	Tool tool=new Tool();

	@Test
	public void testVH(){
		args=new String[]{
				""
		};
		assertEquals(-1, tool.execute(args));

		args=new String[]{
				"-v"
		};
		assertEquals(-1, tool.execute(args));

		args=new String[]{
				"-h"
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testSingleLayer1(){
		args=new String[]{
				"-t",
				"pdf",
				"--file",
				"src/test/resources/files/input/chomsky-hierarchy.svgz",
				"-d",
				"src/test/outputs/files/pdf",
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testSingleLayer2(){
		args=new String[]{
				"-t",
				"pdf",
				"--file",
				"src/test/resources/files/input/session-cards.svgz",
				"-d",
				"src/test/outputs/files/pdf",
				"-o",
				"sessionCardsPdf"
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testMultiLayer1(){
		args=new String[]{
				"-t",
				"pdf",
				"--file",
				"src/test/resources/files/input/time-interval-based.svgz",
				"-d",
				"src/test/outputs/files/pdf/layers/index",
				"-v",
				"-l",
				"-i"
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testMultiLayer2(){
		args=new String[]{
				"-t",
				"pdf",
				"--file",
				"src/test/resources/files/input/time-interval-based.svgz",
				"-d",
				"src/test/outputs/files/pdf/layers/id",
				"-v",
				"-l",
				"-I"
		};
		assertEquals(0, tool.execute(args));
	}
}
