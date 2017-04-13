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

package de.vandermeer.svg2vector.applications.fh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.vandermeer.svg2vector.applications.fh.Svg2Vector_FH;

/**
 * Simple tests for Svg2Vector_FH.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0 build 170413 (13-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Test_Svg2Vector_FH {

	/** Prefix for tests that create output. */
	static String OUT_DIR_PREFIX = "target/output-tests/s2v-fh/";

	/** Standard CLI options for tests. */
	static String[] STD_OPTIONS = new String[]{
			"--create-directories", "--overwrite-existing", "--all-layers", "-v"
	};

	@Test
	public void test_AddedOptions(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		assertEquals(26, app.getAppOptions().length);
	}

	@Test
	public void test_Error_AllMissingOptions(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				""
		};
		assertEquals(-1, app.executeApplication(args));
	}

	@Test
	public void test_Usage(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--help"
		};
		assertEquals(1, app.executeApplication(args));
	}

	@Test
	public void test_Version(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--version"
		};
		assertEquals(1, app.executeApplication(args));
	}

	@Test
	public void test_TargetHelp(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--help",
				"target"
		};
		assertEquals(1, app.executeApplication(args));
	}
}
