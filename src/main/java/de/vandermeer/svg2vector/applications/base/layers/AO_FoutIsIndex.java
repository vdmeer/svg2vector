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

import org.apache.commons.cli.Option;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option `fout-isindex`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class AO_FoutIsIndex extends AbstractApplicationOption<String> {

	/**
	 * Returns the new option.
	 */
	public AO_FoutIsIndex(){
		super(
				"use the Inkscape layer index in output file name",
				"If layers are processed, the output file name will use an index generated from the Inkscape layer name."
		);

		Option.Builder builder = Option.builder("I");
		builder.longOpt("fout-isindex");
		builder.required(false);
		this.setCliOption(builder.build());
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}

}
