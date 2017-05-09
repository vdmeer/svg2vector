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

import de.vandermeer.skb.interfaces.application.ApoCliParser;
import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_InputFile;
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
	public void test_NoLoader() throws ApplicationException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", ""
		};
		cli.parse(args);

		thrown.expect(NullPointerException.class);
		ro.setInput(null);
	}

	@Test
	public void test_Fin_Blank() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", ""
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_InputFile.FN_BLANK.getCode())));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_DoesNotexist() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_InputFile.FILE_NOTEXIST.getCode())));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_NotAFile() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/main/java"
		};
		cli.parse(args);

		IsLoader loader = new IsLoader();
		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_InputFile.FILE_NOTFILE.getCode())));
		ro.setInput(loader);
	}

	@Test
	public void test_Fin_ValidLayers() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

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
	public void test_Fin_ValidNoLayers() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

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
