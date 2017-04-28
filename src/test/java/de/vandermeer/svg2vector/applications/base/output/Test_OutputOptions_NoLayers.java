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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.execs.DefaultCliParser;
import de.vandermeer.skb.interfaces.application.CliParseException;
import de.vandermeer.svg2vector.applications.base.Test_Artifacts;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.S2VExeception;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Tests for {@link OutputOptions} - no layers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_OutputOptions_NoLayers {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void testArtifacts(){
		assertTrue(Test_Artifacts.newFile==null);
		assertTrue(Test_Artifacts.newDir==null);
	}

	@After
	public void deleteArtifacts(){
		Test_Artifacts.removeArtifacts();
	}

	@Test
	public void test_Error_FoutBlank() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", ""
		};
		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output filename is blank");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_IS_BLANK__0)));
		oo.setOptions(false, SvgTargets.pdf, "foo");
	}

	@Test
	public void test_Error_SameAsTarget_FN() throws S2VExeception, IllegalStateException, CliParseException{
		OutputOptions oo = new OutputOptions();

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output <simple.svg> same as input <simple.svg>");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_SAMEAS_FIN__2)));
		oo.setOptions(false, SvgTargets.svg, "simple.svg");
	}

	@Test
	public void test_Error_SameAsTarget_FNDir() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-d", "target/output-tests/app-props/bla/foo"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props/bla/foo");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newDir = new File("target/output-tests/app-props/bla");

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/bla/foo/simple.svg") + "> same as input <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/bla/foo/simple.svg") + ">");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_SAMEAS_FIN__2)));
		oo.setOptions(false, SvgTargets.svg, "target/output-tests/app-props/bla/foo/simple.svg");
	}

	@Test
	public void test_Error_Fout_Isdir() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/fout-exists"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props/fout-exists.pdf");
		Test_Artifacts.newFile.mkdirs();

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output file <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/fout-exists.pdf") + "> exists but is a directory");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_IS_DIRECTORY__1)));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_FoutExistsNoOverwrite() throws S2VExeception, IOException, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/fout-exists"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newFile = new File("target/output-tests/app-props/fout-exists.pdf");
		Test_Artifacts.newFile.createNewFile();

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output file <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/fout-exists.pdf") + "> exists and no option overwrite-existing used");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_EXISTS_NO_OVERWRITE_OPTION__2)));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_FoutExistsCantWrite() throws S2VExeception, IOException, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"--overwrite-existing",
				"-o", "target/output-tests/app-props/fout-exists"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props");
		Test_Artifacts.newFile.mkdirs();

		Test_Artifacts.newFile = new File("target/output-tests/app-props/fout-exists.pdf");
		Test_Artifacts.newFile.createNewFile();
		Test_Artifacts.newFile.setWritable(false);

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output file <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/fout-exists.pdf") + "> exists but cannot write to it, please check permissions");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_FN_EXISTS_CANNOT_WRITE__1)));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_ParrentNoDir() throws S2VExeception, IOException, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newFile = new File("target/output-tests/app-props/test");
		Test_Artifacts.newFile.createNewFile();
		Test_Artifacts.newFile.setWritable(false);

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output directory <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/test") + "> exists but is not a directory");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_DIR_IS_NOT_DIRECTORY__1)));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_NoParrentNoCreate() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file"
		};

		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output directory <" + Test_OutputOptions.substPathSeparator("target/output-tests/app-props/test") + "> does not exist and CLI option create-directories not used");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_DIR_NOTEXISTS_NO_CREATE_DIR_OPTION__2)));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_DoesNoLayers() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file",
				"--create-directories",
		};
		cli.parse(args);

		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
		assertTrue(oo.file!=null);
	}

	@Test
	public void test_Opt1() throws S2VExeception, IllegalStateException, CliParseException{
		DefaultCliParser cli = new DefaultCliParser();
		OutputOptions oo = new OutputOptions();
		cli.addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-d", "target/output-tests/app-props/pdf",
				"-o", "chomsky1",
				"--create-directories",
		};
		cli.parse(args);

		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
		assertEquals(oo.file.toString(), "chomsky1");
		assertEquals(oo.directory.toString(), Test_OutputOptions.substPathSeparator("target/output-tests/app-props/pdf"));
	}
}
