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

package de.vandermeer.svg2vector.applications.base;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.S2VExeception;
import de.vandermeer.svg2vector.applications.core.SvgTargets;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * General tests for {@link AppBase}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppBase {

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
	public void test_AddedOptions(){
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		assertEquals(23, testApp.getAppOptions().length);
	}

	@Test
	public void test_Usage(){
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"--help"
		};
		assertEquals(1, testApp.executeApplication(args));
	}

	@Test
	public void test_Version(){
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"--version"
		};
		assertEquals(1, testApp.executeApplication(args));
	}

	@Test
	public void test_Init_Error_Target() throws S2VExeception{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "eps",
				"-f", "testfile",
				"-q"
		};

		assertEquals(0, testApp.executeApplication(args));
		thrown.expect(S2VExeception.class);
		thrown.expectMessage("given target <eps> not supported. Use one of the supported targets: pdf");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.TARGET_NOT_SUPPORTED__2)));
		testApp.init();
	}

	@Test
	public void test_Init_Error_Input() throws S2VExeception{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile",
				"-q"
		};

		assertEquals(0, testApp.executeApplication(args));

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("input file <testfile> does not exist, please check path and filename");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.FIN_DOES_NOT_EXIST__1)));
		testApp.init();
	}

	@Test
	public void test_Init_Error_Output() throws S2VExeception{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-o", "target/output-tests/app-props/test/file",
				"-q"
		};
		assertEquals(0, testApp.executeApplication(args));

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("output directory <" + StringUtils.replace("target/output-tests/app-props/test", "/", File.separator) + "> does not exist and CLI option create-directories not used");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.OUTPUT_DIR_NOTEXISTS_NO_CREATE_DIR_OPTION__2)));
		testApp.init();
	}

	@Test
	public void test_Init_Good() throws S2VExeception{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-o", "target/output-tests/app-props/test/file",
				"--create-directories",
				"--simulate",
				"--verbose"
		};

		Test_Artifacts.newDir = new File("target/output-tests/app-props/test");

		assertEquals(0, testApp.executeApplication(args));
		testApp.init();
	}
}
