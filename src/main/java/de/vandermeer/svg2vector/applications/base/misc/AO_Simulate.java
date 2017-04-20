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

package de.vandermeer.svg2vector.applications.base.misc;

import org.apache.commons.cli.Option;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option `simulate`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_Simulate extends AbstractApplicationOption<String> {

	/**
	 * Returns the new option.
	 */
	public AO_Simulate(){
		super("simulate application, no file output", "This option puts the application into simulation mode. All actions will be done as normal, except no directories will be created and no file will be created or written. This includes temporary directories. Use this option with detailed messaging to see what would happen for a given application run.");

		Option.Builder builder = Option.builder("S");
		builder.longOpt("simulate");
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
