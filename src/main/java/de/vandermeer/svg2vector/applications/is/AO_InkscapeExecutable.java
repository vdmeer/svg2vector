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
import org.apache.commons.lang3.SystemUtils;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option `inkscape-executable`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class AO_InkscapeExecutable extends AbstractApplicationOption<String> {

	/** Key for an environment variable pointing to the Inkscape executable. */
	public static String ENV_KEY = "INKSCAPE";

	/** The default executable on a Windows system. */
	public static String DEFAULT_WINDOWS = "C:/Program Files/Inkscape/inkscape.exe";

	/** The default executable on a UNIX system. */
	public static String DEFAULT_UNIX = "/usr/bin/inkscape";

	/**
	 * Returns the new option.
	 * @param shortOption character for sort version of the option
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_InkscapeExecutable(Character shortOption){
		super("path to Inkscape executable",
				"This option, if used, needs to point to the Inkscape executable. " +
				"Some default values are set in the following way: " +
				"first, the environment variable <" + ENV_KEY + "> is tested. If it is not null, it's value is set as default for the option. " +
				"Next, if the underlying operating system is a Windows system (using Apache SystemUtils), the default value is set to <" + DEFAULT_WINDOWS + ">. " +
				"Next, if the underlying operating system is a UNIX system (using Apache SystemUtils), the default value is set to <" + DEFAULT_UNIX + ">. " +
				"In all other cases, no default value will be set." +
				"\n" +
				"Using the option in the command line will use the given executable and ignore any default settings."
		);

		Option.Builder builder = (shortOption==null)?Option.builder():Option.builder(shortOption.toString());
		builder.longOpt("is-exec");
		builder.hasArg().argName("PATH");
		builder.required(false);
		this.setCliOption(builder.build());

		String env = System.getenv(ENV_KEY);
		if(env!=null){
			this.setDefaultValue(env);
		}
		else if(SystemUtils.IS_OS_WINDOWS){
			this.setDefaultValue("C:/Program Files/Inkscape/inkscape.exe");
		}
		else if(SystemUtils.IS_OS_UNIX){
			this.setDefaultValue("/usr/bin/inkscape");
		}
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}
}
