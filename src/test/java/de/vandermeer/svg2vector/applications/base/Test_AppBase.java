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

import de.vandermeer.svg2vector.loaders.StandardLoader;

/**
 * General tests for {@link AppBase}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppBase {

	@Test
	public void test_AddedOptions(){
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		assertEquals(21, testApp.getAppOptions().length);
	}

	@Test
	public void test_Usage(){
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
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
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
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
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "eps",
				"-f", "testfile"
		};
		assertEquals(-10, testApp.executeApplication(args));
	}

	@Test
	public void test_Error_Input(){
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile"
		};
		assertEquals(-11, testApp.executeApplication(args));
	}

	@Test
	public void test_Error_Output(){
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
			@Override public String getAppName() {return "test-app";}
			@Override public String getAppDescription() {return "app for testing";}
			@Override public String getAppVersion() {return "0.0.0";}
		};
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-o", "target/output-tests/app-props/test/file"
		};
		assertEquals(-12, testApp.executeApplication(args));
	}

	@Test
	public void test_Good(){
		AppBase<StandardLoader, AppProperties<StandardLoader>> testApp = new AppBase<StandardLoader, AppProperties<StandardLoader>>(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader())) {
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
		assertEquals(0, testApp.executeApplication(args));
	}
}
