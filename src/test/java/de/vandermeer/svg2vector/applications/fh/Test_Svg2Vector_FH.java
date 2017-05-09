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

import de.vandermeer.skb.interfaces.messagesets.errors.Templates_CliGeneral;
import de.vandermeer.svg2vector.Svg2Vector_FH;

/**
 * Simple tests for {@link Svg2Vector_FH}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Test_Svg2Vector_FH {

	/** Prefix for tests that create output. */
	public static String OUT_DIR_PREFIX = "target/output-tests/s2v-fh/";

	/** Standard CLI options for tests. */
	public static String[] STD_OPTIONS = new String[]{
			"--create-directories", "--overwrite-existing", "--all-layers", "-q"
	};

	@Test
	public void test_AddedOptions(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		assertEquals(20, app.getCliParser().getSimpleOptions().size());
		assertEquals(6, app.getCliParser().getTypedOptions().size());
	}

	@Test
	public void test_Error_AllMissingOptions(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				""
		};
		app.executeApplication(args);
		assertEquals(Templates_CliGeneral.MISSING_OPTION.getCode(), app.getErrNo());
	}

	@Test
	public void test_Usage(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--help"
		};
		app.executeApplication(args);
		assertEquals(1, app.getErrNo());
	}

	@Test
	public void test_Version(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--version"
		};
		app.executeApplication(args);
		assertEquals(1, app.getErrNo());
	}

	@Test
	public void test_TargetHelp(){
		Svg2Vector_FH app = new Svg2Vector_FH();
		String[] args = new String[]{
				"--help",
				"target"
		};
		app.executeApplication(args);
		assertEquals(1, app.getErrNo());
	}
}
