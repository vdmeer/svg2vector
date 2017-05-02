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

import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractTypedC_String;

/**
 * Application option `input-file`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class AO_FileInExt extends AbstractTypedC_String {

	/**
	 * Returns the new option.
	 */
	public AO_FileInExt(){
		super(
				"Input File",
				'f', "input-file", true, "FILE", false,
				"a valid SVG document either GZIP compressed or plain text file", "specifies the input file (path and filename)",
				LONG_DESCRIPTION()
		);
	}

	/**
	 * Returns the long description generated from an ST template.
	 * @return the long description
	 */
	private static final Object LONG_DESCRIPTION(){
		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/base/required/AO_FileInExt.stg");
		return stg.getInstanceOf("longDescription");
	}
}
