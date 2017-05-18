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

package de.vandermeer.svg2vector.applications.base.messages;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.vandermeer.skb.interfaces.MessageType;
import de.vandermeer.skb.interfaces.application.ApoCliParser;
import de.vandermeer.skb.interfaces.console.MessageConsole;

/**
 * Tests for {@link MessageOptions} - message options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_MessageOptions {

	@Test
	public void test_Constructor_Values(){
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		MessageOptions mo = new MessageOptions();
		assertEquals(6, mo.getSimpleOptions().size());
		assertEquals(MessageType.ERROR.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_DefaultMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.ERROR.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_WarningMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"-w"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.ERROR.getFlag() | MessageType.WARNING.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_ProgressMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"-p"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.ERROR.getFlag() | MessageType.TRACE.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_DetailsMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"-e"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.ERROR.getFlag() | MessageType.DEBUG.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_NoErrors() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"--no-errors"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.NONE.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_VerboseMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"-v"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.ALL.getFlag(), MessageConsole.getMessageFlag());
	}

	@Test
	public void test_QuietMsgOptions() throws IllegalStateException {
		MessageConsole.deActivateAll();
		MessageConsole.activate(MessageType.ERROR);

		ApoCliParser cli = ApoCliParser.defaultParser();
		MessageOptions mo = new MessageOptions();
		cli.getOptions().addAllOptions(mo.getAllOptions());

		String[] args = new String[]{
				"-q"
		};
		cli.parse(args);

		mo.setMessageMode();
		assertEquals(MessageType.NONE.getFlag(), MessageConsole.getMessageFlag());
	}
}
