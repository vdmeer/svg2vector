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

package de.vandermeer.svg2vector.sitedocs;

import org.junit.Test;

import de.vandermeer.svg2vector.S2VErrorCodes;

/**
 * Simple class to generate documentation for error codes and categories.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.1
 */
public class Test_ADOC_Errors {

	@Test
	public void test_Aec_ADOC(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printEcList("de/vandermeer/svg2vector/applications/aec/adoc.stg");
	}

	@Test
	public void test_Aec_Console(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printEcList("de/vandermeer/svg2vector/applications/aec/console.stg");
	}

	@Test
	public void test_CatList_ADOC(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printCatList("de/vandermeer/svg2vector/applications/aec/adoc.stg");
	}

	@Test
	public void test_CatList_Console(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printCatList("de/vandermeer/svg2vector/applications/aec/console.stg");
	}

	@Test
	public void test_FullTable_ADOC(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.fullTable("de/vandermeer/svg2vector/applications/aec/adoc.stg");
	}

	@Test
	public void test_FullTable_Console(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.fullTable("de/vandermeer/svg2vector/applications/aec/console.stg");
	}

	@Test
	public void test_CatFullTable_ADOC(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printCatFullTable("de/vandermeer/svg2vector/applications/aec/adoc.stg");
	}

	@Test
	public void test_CatFullTable_Console(){
		S2VErrorCodes sec = new S2VErrorCodes();
		sec.printCatFullTable("de/vandermeer/svg2vector/applications/aec/console.stg");
	}
}
