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
 * Tests for {@link AppProperties} - target.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties_Target {

	@Test
	public void test_SupportedTargets(){
		AppProperties<IsLoader> props;

		props = new AppProperties<IsLoader>(new SvgTargets[]{}, new IsLoader());
		assertEquals(0, props.getSupportedTargetts().length);

		props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		assertEquals(1, props.getSupportedTargetts().length);

		props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf}, new IsLoader());
		assertEquals(2, props.getSupportedTargetts().length);
	}

	@Test
	public void test_NoneSet(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{};

		assertNotNull(cli.parse(args));
		assertEquals(-99, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.getTarget());
		assertNull(props.getTargetValue());
	}

	@Test
	public void test_NotSupported(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "eps",
				"-f", "foo"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.getTarget());
		assertEquals("eps", props.getTargetValue());
	}

	@Test
	public void test_ValidTarget(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(AppProperties.P_OPTION_ERROR, props.getMsgMode());
	}
}
