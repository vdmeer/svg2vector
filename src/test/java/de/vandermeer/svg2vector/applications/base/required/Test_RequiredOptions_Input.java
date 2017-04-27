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

package de.vandermeer.svg2vector.applications.base.required;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.execs.DefaultCliParser;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.S2VExeception;
import de.vandermeer.svg2vector.applications.core.SvgTargets;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * Tests for {@link RequiredOptions} - input options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_RequiredOptions_Input {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_NoLoader() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", ""
		};
		cli.parse(args);

		thrown.expect(NullPointerException.class);
		ro.setInput(null);
	}

	@Test
	public void test_Fin_Blank() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", ""
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(S2VExeception.class);
		thrown.expectMessage("no input file given");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.NO_FIN__0)));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_DoesNotexist() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(S2VExeception.class);
		thrown.expectMessage("input file <testfile> does not exist, please check path and filename");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.FIN_DOES_NOT_EXIST__1)));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_NotAFile() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/main/java"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(S2VExeception.class);
		thrown.expectMessage("input file <src/main/java> is not a file, please check path and filename");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.FIN_NOT_A_FILE__1)));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_ValidLayers() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		ro.setInput(loader);
		assertEquals("src/test/resources/svg-files/rina-ipc.svgz", ro.getInputFilename());
		assertTrue(loader.isLoaded());
		assertTrue(loader.hasInkscapeLayers());
	}

	@Test
	public void test_Fin_ValidNoLayers() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		ro.setInput(loader);
		assertEquals("src/test/resources/svg-files/chomsky-hierarchy.svgz", ro.getInputFilename());
		assertTrue(loader.isLoaded());
		assertFalse(loader.hasInkscapeLayers());
	}
}

