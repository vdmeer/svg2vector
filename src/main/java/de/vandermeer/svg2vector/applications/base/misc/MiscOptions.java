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

import de.vandermeer.execs.options.AO_PrintStackTrace;
import de.vandermeer.execs.options.ApplicationOption;

/**
 * Miscellaneous application options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class MiscOptions {

	/** Application option activating simulation mode. */
	final private AO_Simulate aoSimulate = new AO_Simulate();

	/** Application option to print a stack trace if exceptions are caught. */
	final private AO_PrintStackTrace aoPrintStackTrace = new AO_PrintStackTrace("Print a full stack trace (not just a message and an optional probable cause) on errors triggered by exceptions, for instance any file operation.");

	/** Application option to keep (not remove) temporary created artifacts (files and directories). */
	final private AO_KeepTmpArtifacts aoKeepTmpArtifacts = new AO_KeepTmpArtifacts();

	/** List of application options. */
	private final ApplicationOption<?>[] options;

	/**
	 * Creates a new option object.
	 */
	public MiscOptions(){
		this.options = new ApplicationOption<?>[]{
			this.aoSimulate,
			this.aoPrintStackTrace,
			this.aoKeepTmpArtifacts
		};
	}

	/**
	 * Returns the message options as array.
	 * @return message options array
	 */
	public final ApplicationOption<?>[] getOptions(){
		return this.options;
	}

	/**
	 * Returns the flag for printing stack traces.
	 * @return true if requested, false otherwise
	 */
	public boolean printStackTrace(){
		return this.aoPrintStackTrace.inCli();
	}

	/**
	 * Returns the simulation flag.
	 * @return true if application is in simulation mode, false otherwise
	 */
	public boolean doesSimulate(){
		return this.aoSimulate.inCli();
	}

	/**
	 * Tests if the application should keep (not remove) temporary artifacts (files and directories).
	 * @return true if artifacts should be kept, false otherwise
	 */
	public boolean doesKeepTempArtifacts(){
		return this.aoKeepTmpArtifacts.inCli();
	}

}
