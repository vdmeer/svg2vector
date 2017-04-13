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

import static org.junit.Assert.*;

import org.junit.Test;

import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * Tests for {@link AppProperties} - input options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0 build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties_Input {

	@Test
	public void test_Fin_Blank(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", ""
		};

		assertEquals(null, cli.parse(args));
		assertEquals(-1, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		String err = props.setInput();
		assertNotNull(err);
		assertEquals("no input file given", err);
	}

	@Test
	public void test_Fin_DoesNotexist(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "testfile"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		String err = props.setInput();
		assertNotNull(err);
		assertEquals("input file <testfile> does not exist, please check path and filename", err);
	}

	@Test
	public void test_Fin_NotAFile(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/main/java"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		String err = props.setInput();
		assertNotNull(err);
		assertEquals("input file <src/main/java> is not a file, please check path and filename", err);
	}

	@Test
	public void test_Fin_ValidLayers(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertEquals("src/test/resources/svg-files/rina-ipc.svgz", props.getFinFn());
		assertTrue(props.getLoader().isLoaded());
		assertTrue(props.getLoader().hasInkscapeLayers());
	}

	@Test
	public void test_Fin_ValidNoLayers(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertEquals("src/test/resources/svg-files/chomsky-hierarchy.svgz", props.getFinFn());
		assertTrue(props.getLoader().isLoaded());
		assertFalse(props.getLoader().hasInkscapeLayers());
	}
}

