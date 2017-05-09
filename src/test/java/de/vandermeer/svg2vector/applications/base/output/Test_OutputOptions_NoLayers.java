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

import de.vandermeer.skb.interfaces.application.ApoCliParser;
import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_OutputDirectory;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_OutputFile;
import de.vandermeer.svg2vector.applications.base.Test_Artifacts;
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
	public void test_Error_FoutBlank() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", ""
		};
		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_BLANK.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "foo");
	}

	@Test
	public void test_Error_SameAsTarget_FN() throws ApplicationException, IllegalStateException {
		OutputOptions oo = new OutputOptions();

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_SAMEAS_INFN.getCode())));
		oo.setOptions(false, SvgTargets.svg, "simple.svg");
	}

	@Test
	public void test_Error_SameAsTarget_FNDir() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-d", "target/output-tests/app-props/bla/foo"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props/bla/foo");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newDir = new File("target/output-tests/app-props/bla");

		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_SAMEAS_INFN.getCode())));
		oo.setOptions(false, SvgTargets.svg, "target/output-tests/app-props/bla/foo/simple.svg");
	}

	@Test
	public void test_Error_Fout_Isdir() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/fout-exists"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props/fout-exists.pdf");
		Test_Artifacts.newFile.mkdirs();

		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FILE_IS_DIRECTORY.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_FoutExistsNoOverwrite() throws ApplicationException, IOException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/fout-exists"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newFile = new File("target/output-tests/app-props/fout-exists.pdf");
		Test_Artifacts.newFile.createNewFile();

		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FILE_EXIST_NOOVERWRITE.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_FoutExistsCantWrite() throws ApplicationException, IOException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

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

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FILE_CANT_WRITE.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_ParrentNoDir() throws ApplicationException, IOException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file"
		};

		Test_Artifacts.newFile = new File("target/output-tests/app-props");
		Test_Artifacts.newFile.mkdirs();
		Test_Artifacts.newFile = new File("target/output-tests/app-props/test");
		Test_Artifacts.newFile.createNewFile();
		Test_Artifacts.newFile.setWritable(false);

		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputDirectory.DIR_NOTDIR.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_Error_NoParrentNoCreate() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file"
		};

		cli.parse(args);

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputDirectory.DIR_EXIST_NOOVERWRITE.getCode())));
		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
	}

	@Test
	public void test_DoesNoLayers() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

		String[] args = new String[]{
				"-o", "target/output-tests/app-props/test/file",
				"--create-directories",
		};
		cli.parse(args);

		oo.setOptions(false, SvgTargets.pdf, "src/test/resources/svg-files/chomsky-hierarchy.svgz");
		assertTrue(oo.file!=null);
	}

	@Test
	public void test_Opt1() throws ApplicationException, IllegalStateException {
		ApoCliParser cli = ApoCliParser.defaultParser("my-app");
		OutputOptions oo = new OutputOptions();
		cli.getOptions().addAllOptions(oo.getAllOptions());

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
