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

package de.vandermeer.svg2vector.applications.base.layers;

import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.Option_SimpleC;

/**
 * Application option `layers`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class AO_Layers extends Option_SimpleC {

	/**
	 * Returns the long description generated from an ST template.
	 * @return the long description
	 */
	private static final Object LONG_DESCRIPTION(){
		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/base/layers/AO_Layers.stg");
		return stg.getInstanceOf("longDescription");
	}

	/**
	 * Returns the new option.
	 */
	public AO_Layers(){
		super(
				"Layers", 'l', "layers", false,
				"switch on layer mode, process layers, create one file per layer",
				LONG_DESCRIPTION()
		);
	}
}
