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

package de.vandermeer.svg2vector.applications.base;

import org.apache.commons.cli.Option;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option `no-errors`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_NoErrors extends AbstractApplicationOption<String> {

	/**
	 * Returns the new option.
	 */
	public AO_NoErrors(){
		super("switch off error messages", "This option will switch off all error messages.");

		Option.Builder builder = Option.builder();
		builder.longOpt("no-errors");
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
