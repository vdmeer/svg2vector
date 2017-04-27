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

import org.apache.commons.lang3.SystemUtils;
import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractTypedC_String;

/**
 * Application option `inkscape-executable`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class AO_InkscapeExecutable extends AbstractTypedC_String {

	/** Key for an environment variable pointing to the Inkscape executable. */
	public final static String ENV_KEY = "INKSCAPE";

	/** The default executable on a Windows system. */
	public final static String DEFAULT_WINDOWS = "C:/Program Files/Inkscape/inkscape.exe";

	/** The default executable on a UNIX system. */
	public final static String DEFAULT_UNIX = "/usr/bin/inkscape";

	/**
	 * Returns the new option.
	 */
	public AO_InkscapeExecutable(){
		super('x', "is-exec", true, "EXEC", false,
				"the Inkscape executable", "sets the Inkscape executable (default values from environment or for Windows and Unix)"
		);

		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/is/AO_InkscapeExecutable.stg");
		this.setLongDescription(stg.getInstanceOf("longDescription"));

		String env = System.getenv(ENV_KEY);
		if(env!=null){
			this.setDefaultValue(env);
		}
		else if(SystemUtils.IS_OS_WINDOWS){
			this.setDefaultValue(DEFAULT_WINDOWS);
		}
		else if(SystemUtils.IS_OS_UNIX){
			this.setDefaultValue(DEFAULT_UNIX);
		}
	}

}
