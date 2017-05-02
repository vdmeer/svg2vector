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

package de.vandermeer.svg2vector.applications.base.output;

import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractSimpleC;

/**
 * Application option `create-directories`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_CreateDirectories extends AbstractSimpleC {

	/**
	 * Returns the new option.
	 */
	public AO_CreateDirectories(){
		super(
				"Create Directories", null, "create-directories", false,
				"automatically create directories for output",
				LONG_DESCRIPTION()
		);
	}

	/**
	 * Returns the long description generated from an ST template.
	 * @return the long description
	 */
	private static final Object LONG_DESCRIPTION(){
		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/base/output/AO_CreateDirectories.stg");
		return stg.getInstanceOf("longDescription");
	}
}
