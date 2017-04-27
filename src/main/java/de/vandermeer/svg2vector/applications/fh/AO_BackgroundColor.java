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

package de.vandermeer.svg2vector.applications.fh;

import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractTypedC_String;

/**
 * Application option `bgrnd-color`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class AO_BackgroundColor extends AbstractTypedC_String {

	/**
	 * Returns the new option.
	 */
	public AO_BackgroundColor(){
		super(
				'r', "bgrnd-color", false, "COLOR", false,
				"name of a system property that has the background color", "sets a background color"
		);

		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/fh/AO_BackgroundColor.stg");
		this.setLongDescription(stg.getInstanceOf("longDescription"));
	}

}
