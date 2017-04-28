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

import de.vandermeer.svg2vector.applications.core.CliOptionPackage;

/**
 * Miscellaneous application options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class MiscOptions extends CliOptionPackage {

	/** Application option activating simulation mode. */
	final private AO_Simulate aoSimulate = new AO_Simulate();

	/** Application option to print a stack trace if exceptions are caught. */
	final private AO_PrintStackTraceExt aoPrintStackTrace = new AO_PrintStackTraceExt();

	/**
	 * Creates a new option object.
	 */
	public MiscOptions(){
		this.setSimpleOptions(
			this.aoSimulate,
			this.aoPrintStackTrace
		);
	}

	/**
	 * Returns the simulation flag.
	 * @return true if application is in simulation mode, false otherwise
	 */
	public boolean doesSimulate(){
		return this.aoSimulate.inCli();
	}

	/**
	 * Returns the flag for printing stack traces.
	 * @return true if requested, false otherwise
	 */
	public boolean printStackTrace(){
		return this.aoPrintStackTrace.inCli();
	}

}
