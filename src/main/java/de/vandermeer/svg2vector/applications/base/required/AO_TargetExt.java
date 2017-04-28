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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.options.AbstractTypedC;
import de.vandermeer.skb.interfaces.application.CliParseException;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Application option `target`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.1
 */
public class AO_TargetExt extends AbstractTypedC<SvgTargets> {

	/** The supported targets of the application. */
	protected SvgTargets[] supportedTargets;

	protected AO_TargetExt(SvgTargets[] supportedTargets) {
		super('t', "target", true, "TARGET", false,
				"the actual target, " + supportedTargets(supportedTargets), "specifies a conversion target"
		);

		Validate.noNullElements(supportedTargets);
		this.supportedTargets = supportedTargets;
	}

	/**
	 * Returns the set supported targets.
	 * @return supported targets
	 */
	public SvgTargets[] getSupportedTargets(){
		return this.supportedTargets;
	}

	/**
	 * Creates a string for argument description.
	 * @param supportedTargets the supported targets
	 * @return string
	 */
	protected static final String supportedTargets(SvgTargets[] supportedTargets){
		StrBuilder ret = new StrBuilder();
		ret.append("supported targets are: ").appendWithSeparators(supportedTargets, ", ");
		return ret.build();
	}

	@Override
	public void setCliValue(Object value) throws IllegalStateException, CliParseException {
		Validate.validState(value!=null, this.getCliLong() + " argument <" + this.getCliArgumentName() +  "> mandatory but trying to set null");
		String str = value.toString();

		if(StringUtils.isBlank(str)){
			throw new CliParseException(
					ErrorCodes.TARGET_BLANK__1.getCode(),
					ErrorCodes.TARGET_BLANK__1.getMessageSubstituted(
							new StrBuilder().appendWithSeparators(this.getSupportedTargets(), ", ")
					)
			);
		}
		try{
			this.cliValue = SvgTargets.valueOf(str);
		}
		catch(IllegalArgumentException iaEx){
			throw new CliParseException(
					ErrorCodes.TARGET_UNKNOWN__2.getCode(),
					ErrorCodes.TARGET_UNKNOWN__2.getMessageSubstituted(
							str,
							new StrBuilder().appendWithSeparators(this.getSupportedTargets(), ", ")
					)
			);
		}
		if(!ArrayUtils.contains(this.getSupportedTargets(), this.cliValue)){
			throw new CliParseException(
					ErrorCodes.TARGET_NOT_SUPPORTED__2.getCode(),
					ErrorCodes.TARGET_NOT_SUPPORTED__2.getMessageSubstituted(
							str,
							new StrBuilder().appendWithSeparators(this.getSupportedTargets(), ", ")
					)
			);
		}
	}

}
