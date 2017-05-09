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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_InputFile;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_OutputDirectory;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_Target;
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
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		assertEquals(18, testApp.getCliParser().getOptions().getSimpleSet().size());
		assertEquals(5, testApp.getCliParser().getOptions().getTypedSet().size());
		assertEquals(1, testApp.getEnvironmentParser().getOptions().size());
		assertEquals(0, testApp.getPropertyParser().getOptions().size());
	}

	@Test
	public void test_Usage(){
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		String[] args = new String[]{
				"--help"
		};
		testApp.executeApplication(args);
		assertEquals(1, testApp.getErrNo());
	}

	@Test
	public void test_Version(){
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		String[] args = new String[]{
				"--version"
		};
		testApp.executeApplication(args);
		assertEquals(1, testApp.getErrNo());
	}

	@Test
	public void test_Init_Error_Target() throws ApplicationException{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", new SvgTargets[]{SvgTargets.pdf}, new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		String[] args = new String[]{
				"-t", "eps",
				"-f", "testfile",
				"-q"
		};
		testApp.executeApplication(args);
		assertEquals(Templates_Target.NOT_UNKNOWN.getCode(), testApp.getErrNo());
	}

	@Test
	public void test_Init_Error_Input() throws ApplicationException{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile",
				"-q"
		};

		testApp.executeApplication(args);
		assertEquals(0, testApp.getErrNo());

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_InputFile.FILE_NOTEXIST.getCode())));
		testApp.init();
	}

	@Test
	public void test_Init_Error_Output() throws ApplicationException{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-o", "target/output-tests/app-props/test/file",
				"-q"
		};
		testApp.executeApplication(args);
		assertEquals(0, testApp.getErrNo());

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputDirectory.DIR_EXIST_NOOVERWRITE.getCode())));
		testApp.init();
	}

	@Test
	public void test_Init_Good() throws ApplicationException{
		AppBase<IsLoader> testApp = new AppBase<IsLoader>("test-app", SvgTargets.values(), new IsLoader()) {
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
			@Override public void runApplication(){}
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

		testApp.executeApplication(args);
		assertEquals(0, testApp.getErrNo());
		testApp.init();
	}
}
