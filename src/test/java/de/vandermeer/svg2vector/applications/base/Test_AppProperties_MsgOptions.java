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

import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.converters.SvgTargets;
import de.vandermeer.svg2vector.loaders.StandardLoader;

/**
 * Tests for {@link AppProperties} - message options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties_MsgOptions {

	@Test
	public void test_DefaultMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(MessageTypes.error.getMask(), props.getMsgMode());
	}

	@Test
	public void test_WarningMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
				"-w"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(MessageTypes.error.getMask() | MessageTypes.warning.getMask(), props.getMsgMode());
	}

	@Test
	public void test_ProgressMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
				"-p"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(MessageTypes.error.getMask() | MessageTypes.progress.getMask(), props.getMsgMode());
	}

	@Test
	public void test_DetailsMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
				"-e"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(MessageTypes.error.getMask() | MessageTypes.detail.getMask(), props.getMsgMode());
	}

	@Test
	public void test_VerboseMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
				"-v"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(MessageTypes.error.getMask() | MessageTypes.warning.getMask() | MessageTypes.progress.getMask() | MessageTypes.detail.getMask(), props.getMsgMode());
	}

	@Test
	public void test_QuietMsgOptions(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<StandardLoader> props = new AppProperties<StandardLoader>(new SvgTargets[]{SvgTargets.pdf}, new StandardLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo",
				"-q"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		props.setMessageMode();
		assertEquals(0, props.getMsgMode());
	}
}
