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

package de.vandermeer.svg2vector.applications.base.conversion;

import de.vandermeer.execs.options.ApplicationOption;

/**
 * Conversion options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class ConversionOptions {

	/** Application option for text-as-shape mode. */
	final private AO_TextAsShape aoTextAsShape = new AO_TextAsShape();

	/** List of application options. */
	private final ApplicationOption<?>[] options;

	public ConversionOptions(){
		this.options = new ApplicationOption<?>[]{
			this.aoTextAsShape
		};
	}

	/**
	 * Returns the message options as array.
	 * @return message options array
	 */
	public final ApplicationOption<?>[] getOptions(){
		return this.options;
	}

	/**
	 * Returns the text-as-shape flag as set by CLI.
	 * @return true if text-as-shape is set, false otherwise
	 */
	public boolean doesTextAsShape(){
		return this.aoTextAsShape.inCli();
	}

}
