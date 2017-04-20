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

package de.vandermeer.svg2vector.applications.is;

import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.vandermeer.execs.options.AbstractApplicationOption;
import de.vandermeer.svg2vector.applications.base.SvgTargets;

/**
 * An Inkscape target option.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class IsTargetOption extends AbstractApplicationOption<String> {

	/** The target the option applies to. */
	protected SvgTargets target;

	/** The Inkscape command line option for this target option. */
	protected String isCli;

	/**
	 * Returns the new option as not required and without a short option.
	 * @param target the target to which this option applies, must not be null
	 * @param isCli the Inkscape command line option to be used including any leading dashs, must not be blank
	 * @param longOption the long option, must not be blank
	 * @param argument the argument, no argument added if blank
	 * @param description the short description, must not be blank
	 * @param longDescription the long description, must not be blank
	 * @throws NullPointerException - if any required parameter is null
	 * @throws IllegalArgumentException - if any required parameter is null
	 */
	public IsTargetOption(SvgTargets target, String isCli, String longOption, String argument, String description, String longDescription){
		super(description, longDescription);
		Validate.notNull(target);
		Validate.notBlank(isCli);
		Validate.notBlank(longOption);

		final Option.Builder builder = Option.builder();
		builder.longOpt(longOption);
		if(!StringUtils.isBlank(argument)){
			builder.hasArg().argName(argument);
		}
		builder.required(false);
		this.setCliOption(builder.build());

		this.target = target;
		this.isCli = isCli;
	}

	/**
	 * Returns the target the option applies to.
	 * @return the target
	 */
	public SvgTargets getTarget(){
		return this.target;
	}

	/**
	 * Returns the Inkscape CLI option.
	 * @return Inkscape CLI option
	 */
	public String getIsCmd(){
		return this.isCli;
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}
}
