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

package de.vandermeer.svg2vector.applications.base.required;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.skb.interfaces.application.ApoCliParser;
import de.vandermeer.skb.interfaces.messages.errors.Templates_CliGeneral;
import de.vandermeer.skb.interfaces.messages.errors.Templates_Target;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Tests for {@link RequiredOptions} - target.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_RequiredOptions_Target {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_NoneSet() {
		ApoCliParser cli = ApoCliParser.defaultParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{};
		cli.parse(args);
		assertEquals(Templates_CliGeneral.MISSING_OPTION.getCode(), cli.getErrNo());
	}

	@Test
	public void test_Unknown() {
		ApoCliParser cli = ApoCliParser.defaultParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "bla",
				"-f", "foo"
		};

		cli.parse(args);
		assertEquals(Templates_Target.NOT_UNKNOWN.getCode(), cli.getErrNo());
	}

	@Test
	public void test_NotSupported() {
		ApoCliParser cli = ApoCliParser.defaultParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "eps",
				"-f", "foo"
		};

		cli.parse(args);
		assertEquals(Templates_Target.NOT_UNKNOWN.getCode(), cli.getErrNo());
	}

	@Test
	public void test_ValidTarget() {
		ApoCliParser cli = ApoCliParser.defaultParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.getOptions().addAllOptions(ro.getAllOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo"
		};
		cli.parse(args);

//		ro.setTarget();
		assertTrue(ro.getTarget()==SvgTargets.pdf);
	}
}
