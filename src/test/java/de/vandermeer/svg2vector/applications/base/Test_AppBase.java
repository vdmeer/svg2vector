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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * General tests for {@link AppBase}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppBase {

	@Test
	public void test_AddedOptions(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		assertEquals(22, testApp.getAppOptions().length);
	}

	@Test
	public void test_Usage(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
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
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
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
	public void test_Error_Target(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "eps",
				"-f", "testfile",
				"-q"
		};
		assertEquals(-10, testApp.executeApplication(args));
	}

	@Test
	public void test_Error_Input(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile",
				"-q"
		};
		assertEquals(-11, testApp.executeApplication(args));
	}

	@Test
	public void test_Error_Output(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
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
		assertEquals(-12, testApp.executeApplication(args));
	}

	@Test
	public void test_Good(){
		AppBase<IsLoader, AppProperties<IsLoader>> testApp = new AppBase<IsLoader, AppProperties<IsLoader>>(new AppProperties<IsLoader>(SvgTargets.values(), new IsLoader())) {
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
//				"--verbose"
		};
		assertEquals(0, testApp.executeApplication(args));
	}
}
