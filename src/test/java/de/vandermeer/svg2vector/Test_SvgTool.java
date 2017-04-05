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

import java.io.File;

import org.junit.Test;

import de.vandermeer.svg2vector.Tool;

public class Test_SvgTool {
	String[] args;

	Tool tool=new Tool();

	String outDirPrefix = "target/test-outputs/files/svg/";

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
		String outDir = outDirPrefix;
		File file = new File(outDir);
		file.mkdirs();

		args=new String[]{
				"-t",
				"svg",
				"--file",
				"src/test/resources/files/input/chomsky-hierarchy.svgz",
				"-d",
				outDir,
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testSingleLayer2(){
		String outDir = outDirPrefix;
		File file = new File(outDir);
		file.mkdirs();

		args=new String[]{
				"-t",
				"svg",
				"--file",
				"src/test/resources/files/input/session-cards.svgz",
				"-d",
				outDir,
				"-o",
				"sessionCardsSvg"
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testMultiLayer1(){
		String outDir = outDirPrefix + "layers/index";
		File file = new File(outDir);
		file.mkdirs();

		args=new String[]{
				"-t",
				"svg",
				"--file",
				"src/test/resources/files/input/time-interval-based.svgz",
				"-d",
				outDir,
				"-v",
				"-l",
				"-i"
		};
		assertEquals(0, tool.execute(args));
	}

	@Test
	public void testMultiLayer2(){
		String outDir = outDirPrefix + "layers/id";
		File file = new File(outDir);
		file.mkdirs();

		args=new String[]{
				"-t",
				"svg",
				"--file",
				"src/test/resources/files/input/time-interval-based.svgz",
				"-d",
				outDir,
				"-v",
				"-l",
				"-I"
		};
		assertEquals(0, tool.execute(args));
	}
}
