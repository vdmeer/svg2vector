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

package de.vandermeer.svg2vector.applications.is;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * Tests for Svg2Vector_IS with PDF conversion.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_Svg2Vector_IS_PDF {

	/** Prefix for tests that create output. */
	static String OUT_DIR_PREFIX = Test_Svg2Vector_IS.OUT_DIR_PREFIX + "pdf/";

	/** Standard CLI options for tests. */
	static String[] STD_OPTIONS = ArrayUtils.addAll(Test_Svg2Vector_IS.STD_OPTIONS,
			"-t", "pdf"
	);

	@Test
	public void test_SingleDir(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-d", OUT_DIR_PREFIX
		);
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void test_SingleDirFile(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-d", OUT_DIR_PREFIX,
				"-o", "chomsky1"
		);
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void test_SingleFile(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-o", OUT_DIR_PREFIX + "chomsky2"
		);
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testMultiLayerIndex(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/time-interval-based.svgz",
				"-d", OUT_DIR_PREFIX + "layers-index",
				"-l", "-i"
		);
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testMultiLayerId(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/time-interval-based.svgz",
				"-d", OUT_DIR_PREFIX + "layers-id",
				"-l", "-I"
		);
		assertEquals(0, app.executeApplication(args));
	}

	@Test
	public void testMultiLayerIdInex(){
		Svg2Vector_IS app = new Svg2Vector_IS();
		String[] args = ArrayUtils.addAll(STD_OPTIONS,
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-d", OUT_DIR_PREFIX + "layers-id-index",
				"-l", "-I", "-i"
		);
		assertEquals(0, app.executeApplication(args));
	}

}
