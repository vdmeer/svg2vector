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

package de.vandermeer.svg2vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Tests for {@link ErrorCodes}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class Test_ErrorCodes {

	@Test
	public void test_Init(){
		for(ErrorCodes code : ErrorCodes.values()){
//			System.err.println(code.name());
			assertTrue(StringUtils.isNotBlank(code.getMessage()));
			assertTrue(StringUtils.isNotBlank(code.getDescription()));
			assertTrue(code.getCategory()!=null);

			assertTrue(code.getCode()<0);
			assertTrue(code.getCode()<=code.getCategory().getStart());
			assertTrue(code.getCode()>=code.getCategory().getEnd());
		}
	}

	@Test
	public void test_Names(){
		for(ErrorCodes code : ErrorCodes.values()){
			assertTrue(code.name().endsWith("__" + code.getArgs()));
		}
	}

	@Test
	public void testCodeUnique(){
		List<Integer> usedCodes = new ArrayList<>();
		for(ErrorCodes code : ErrorCodes.values()){
//			System.err.println(code.name());
			assertFalse(usedCodes.contains(code.getCode()));
			usedCodes.add(code.getCode());
		}
	}
}
