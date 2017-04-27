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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.execs.DefaultCliParser;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.S2VExeception;
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
	public void test_NoneSet() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{};
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Missing required options: f, t");
		cli.parse(args);

//		thrown.expect(S2VExeception.class);
//		thrown.expectMessage("given target is blank. Use one of the supported targets: ");
//		thrown.expect(hasProperty("errorCode", is(ErrorCodes.TARGET_BLANK__1)));
//		ro.setTarget();
	}

	@Test
	public void test_Unknown() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "bla",
				"-f", "foo"
		};
		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("given target <bla> is unknown. Use one of the supported targets: pdf, emf");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.TARGET_UNKNOWN__2)));
		ro.setTarget();
	}

	@Test
	public void test_NotSupported() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "eps",
				"-f", "foo"
		};
		cli.parse(args);

		thrown.expect(S2VExeception.class);
		thrown.expectMessage("given target <eps> not supported. Use one of the supported targets: pdf, emf");
		thrown.expect(hasProperty("errorCode", is(ErrorCodes.TARGET_NOT_SUPPORTED__2)));
		ro.setTarget();
	}

	@Test
	public void test_ValidTarget() throws S2VExeception{
		DefaultCliParser cli = new DefaultCliParser();
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		cli.addAllOptions(ro.getTypedOptions());

		String[] args = new String[]{
				"-t", "pdf",
				"-f", "foo"
		};
		cli.parse(args);

		ro.setTarget();
		assertTrue(ro.getTarget()==SvgTargets.pdf);
		assertEquals("pdf", ro.getTargetValue());
	}
}
