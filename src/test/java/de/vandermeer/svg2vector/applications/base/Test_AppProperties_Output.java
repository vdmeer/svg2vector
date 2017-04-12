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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * Tests for {@link AppProperties} - file output options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties_Output {

	@Test
	public void test_NoValidTarget(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "eps",
				"-f", "foo",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("implementation error: cannot set output file w/o a valid target", err);
	}

	@Test
	public void test_NothingSet(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("implementation error: no input file name set", err);
	}

	@Test
	public void test_LayersButNoneInFile(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/chomsky-hierarchy.svgz",
				"-l",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals(1, props.getWarnings().size());
		assertEquals("layers activated but input file has no layers, continue for single output file", props.getWarnings().get(0));
	}

}
