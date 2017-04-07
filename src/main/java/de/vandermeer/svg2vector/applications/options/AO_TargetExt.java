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

package de.vandermeer.svg2vector.applications.options;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.options.AO_Target;
import de.vandermeer.svg2vector.converters.SvgTargets;

/**
 * Application option "target" with extended long description.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.1.1
 */
public class AO_TargetExt extends AO_Target {

	protected SvgTargets[] supportedTargets;

	/**
	 * Returns the new option.
	 * @param required true if option is required, false of it is optional
	 * @param shortOption character for sort version of the option
	 * @param longDescription option long description
	 * @param supportedTargets list of supported targets
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_TargetExt(boolean required, Character shortOption, String longDescription, SvgTargets[] supportedTargets){
		super(required, shortOption, AO_TargetExt.buildLongDescr(longDescription, supportedTargets));
		this.descr = "specifies a conversion target";
		this.supportedTargets = supportedTargets;
	}

	/**
	 * Creates a long description with supported targets.
	 * @param descr original long description
	 * @param supportedTargets targets
	 * @return long description with added list of supported targets
	 */
	protected static String buildLongDescr(String descr, SvgTargets[] supportedTargets){
		StrBuilder ret = new StrBuilder();
		ret.append(descr);
		ret.append(" Supported targets are: ").appendWithSeparators(supportedTargets, ", ");
		return ret.toString();
	}

	/**
	 * Returns the target for a given set value.
	 * @return the target if valid value in the option, null if no value was set or the required target not in the list of supported targets
	 */
	public SvgTargets getTarget(){
		SvgTargets target = null;
		try{
			target = SvgTargets.valueOf(this.getValue());
		}
		catch(Exception ignore) {}

		return (ArrayUtils.contains(supportedTargets, target))?target:null;
	}
}
