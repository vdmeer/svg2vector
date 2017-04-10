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

package de.vandermeer.svg2vector.applications;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Simple tests for Svg2Vector_IS.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public class Test_Svg2Vector_IS {

	/** Prefix for tests that create output. */
	static String outDirPrefix = "target/testoutput/s2v-is/";

	@Test
	public void testVH(){
		Svg2Vector_IS app = new Svg2Vector_IS();

		String[] args = new String[]{
				""
		};
		assertEquals(-1, app.executeApplication(args));

		args=new String[]{
				"--version"
		};
		assertEquals(1, app.executeApplication(args));

		args=new String[]{
				"--help"
		};
		assertEquals(1, app.executeApplication(args));

		args=new String[]{
				"--help",
				"target"
		};
		assertEquals(1, app.executeApplication(args));
	}
}
