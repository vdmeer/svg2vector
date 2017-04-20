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

import java.io.File;

import org.apache.commons.cli.CommandLine;

import de.vandermeer.execs.options.ApplicationOption;

/**
 * Temporary created artifacts for tests (files and directories).
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class Test_Artifacts {

	/** A file created during a test. */
	public static File newFile = null;

	/** A directory created during a test. */
	public static File newDir = null;

	/**
	 * Removes temporarily created artifacts.
	 */
	public static void removeArtifacts(){
		if(newFile!=null){
			newFile.delete();
			newFile = null;
		}
		if(newDir!=null){
			newDir.delete();
			newDir = null;
		}
	}

	/**
	 * Parse a command line and set application option.
	 * @param cmdLine the command line
	 * @param options the application options
	 * @return 1 on exit option, 0 on success, -1 on error
	 */
	public static int setCli4Options(CommandLine cmdLine, ApplicationOption<?>[] options){
		if(cmdLine==null){
			return -99;
		}

		int ret = 0;
		if(options!=null){
			for(ApplicationOption<?> opt : options){
				if(opt.getCliOption()!=null){
					ret += opt.setCliValue(cmdLine);
				}
			}
		}
		return ret;
	}
}
