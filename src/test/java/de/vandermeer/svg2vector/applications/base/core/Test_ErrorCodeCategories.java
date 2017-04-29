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

package de.vandermeer.svg2vector.applications.base.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.vandermeer.svg2vector.applications.core.EC_Categories;

/**
 * Tests for {@link EC_Categories}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class Test_ErrorCodeCategories {

	@Test
	public void test_Init(){
		for(EC_Categories cat : EC_Categories.values()){
//			System.err.println(cat.name());
			assertTrue(StringUtils.isNotBlank(cat.getDescription()));

			assertTrue(cat.getStart()<0);
			assertTrue(cat.getEnd()<cat.getStart());
		}
	}

	@Test
	public void test_CodeUsed(){
		Map<Integer, String> inuse = new HashMap<>();
		for(EC_Categories cat : EC_Categories.values()){
			for (int i=cat.getStart(); i>=cat.getEnd(); i--){
				if(inuse.containsKey(i)){
					System.err.println(i + " == " + inuse.get(i) + " ==> " + cat.name());
				}
				assertFalse(inuse.containsKey(i));
				inuse.put(i, cat.name());
			}
		}
	}
}
