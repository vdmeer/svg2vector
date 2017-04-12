/* Copyright 2017 Sven van der Meer <vdmeer.sven@mykolab.com>
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

package de.vandermeer.svg2vector.applications;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import de.vandermeer.svg2vector.applications.fh.Svg2Vector_FH;

/**
 * Tests for Svg2Vector_FH with PDF conversion.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Test_Svg2Vector_FH_PDF {

	/** Prefix for tests that create output. */
	static String outDirPrefix = Test_Svg2Vector_FH.outDirPrefix + "pdf/";

	@Test
	public void testSingleLayer1(){
		Svg2Vector_FH app = new Svg2Vector_FH();

		String outDir = outDirPrefix;
		File file = new File(outDir);
		file.mkdirs();

		String[] args = new String[]{
				"-t",
				"pdf",
				"--input-file",
				"src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-d",
				outDir,
				"-o",
				"ChomskyHierarchyPdf"
		};
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testSingleLayer2(){
		Svg2Vector_FH app = new Svg2Vector_FH();

		String outDir = outDirPrefix;
		File file = new File(outDir);
		file.mkdirs();

		String[] args = new String[]{
				"-t",
				"pdf",
				"--input-file",
				"src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-d",
				outDir,
				"-o",
				"ChomskyHierarchyPdf"
		};
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testMultiLayer1(){
		Svg2Vector_FH app = new Svg2Vector_FH();

		String outDir = outDirPrefix + "layers/index";
		File file = new File(outDir);
		file.mkdirs();

		String[] args = new String[]{
				"-t",
				"pdf",
				"--input-file",
				"src/test/resources/svg-files/time-interval-based.svgz",
				"-d",
				outDir,
				"-v",
				"-l",
				"-i"
		};
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testMultiLayer2(){
		Svg2Vector_FH app = new Svg2Vector_FH();

		String outDir = outDirPrefix + "layers/id";
		File file = new File(outDir);
		file.mkdirs();

		String[] args = new String[]{
				"-t",
				"pdf",
				"--input-file",
				"src/test/resources/svg-files/time-interval-based.svgz",
				"-d",
				outDir,
				"-v",
				"-l",
				"-I"
		};
		assertEquals(0, app.executeApplication(args));
	}
}
