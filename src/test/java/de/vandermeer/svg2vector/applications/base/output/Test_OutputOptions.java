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

package de.vandermeer.svg2vector.applications.base.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.svg2vector.applications.core.S2VExeception;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Tests for {@link OutputOptions}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_OutputOptions {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_Constructor_Values(){
		OutputOptions oo = new OutputOptions();
		assertEquals(2, oo.getSimpleOptions().size());
		assertEquals(2, oo.getTypedOptions().size());
		assertTrue(oo.directory==null);
		assertTrue(oo.file==null);
		assertTrue(oo.fileExtension==null);
	}

	@Test
	public void test_StateError_Warnings(){
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalStateException.class);
		oo.getWarnings();
	}

	@Test
	public void test_StateError_DoLayers(){
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalStateException.class);
		oo.doLayers();
	}

	@Test
	public void test_StateError_File(){
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalStateException.class);
		oo.getFile();
	}

	@Test
	public void test_StateError_FileExt(){
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalStateException.class);
		oo.getFileExtension();
	}

	@Test
	public void test_StateError_Dir(){
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalStateException.class);
		oo.getDirectory();
	}

	@Test
	public void test_Set_NullTarget() throws S2VExeception{
		OutputOptions oo = new OutputOptions();
		thrown.expect(NullPointerException.class);
		oo.setOptions(false, null, "foo");
	}

	@Test
	public void test_Set_NullFin() throws S2VExeception{
		OutputOptions oo = new OutputOptions();
		thrown.expect(NullPointerException.class);
		oo.setOptions(false, SvgTargets.pdf, null);
	}

	@Test
	public void test_Set_BlankFin() throws S2VExeception{
		OutputOptions oo = new OutputOptions();
		thrown.expect(IllegalArgumentException.class);
		oo.setOptions(false, SvgTargets.pdf, " 	");
	}

	/**
	 * Substitutes all `/` with the system's file separator.
	 * @param input string for substitution
	 * @return string with only system's file separator in path
	 */
	public static String substPathSeparator(String input){
		return StringUtils.replace(input, "/", File.separator);
	}
}
