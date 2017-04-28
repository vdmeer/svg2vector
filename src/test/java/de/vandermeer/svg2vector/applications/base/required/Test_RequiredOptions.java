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

import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Tests for {@link RequiredOptions}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class Test_RequiredOptions {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_Constructor_Targets(){
		RequiredOptions ro;

		ro = new RequiredOptions(new SvgTargets[]{});
		assertEquals(0, ro.getSupportedTargets().length);

		ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf});
		assertEquals(1, ro.getSupportedTargets().length);

		ro = new RequiredOptions(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf});
		assertEquals(2, ro.getSupportedTargets().length);
	}

	@Test
	public void test_Constructor_NullTarget(){
		thrown.expect(NullPointerException.class);
		@SuppressWarnings("unused")
		RequiredOptions ro = new RequiredOptions(null);
	}

	@Test
	public void test_Constructor_NullElementTarget(){
		thrown.expect(IllegalArgumentException.class);
		@SuppressWarnings("unused")
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{null});
	}

	@Test
	public void test_Constructor_Values(){
		RequiredOptions ro = new RequiredOptions(new SvgTargets[]{});
		assertEquals(2, ro.getTypedOptions().size());
		assertEquals(0, ro.getSupportedTargets().length);
		assertTrue(ro.getInputFilename()==null);
		assertTrue(ro.getTarget()==null);
	}
}
