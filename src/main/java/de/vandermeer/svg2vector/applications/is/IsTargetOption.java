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

import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * An Inkscape target option.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public interface IsTargetOption {

	/**
	 * Returns the target the option applies to.
	 * @return the target
	 */
	SvgTargets getTarget();

	/**
	 * Returns the Inkscape CLI option.
	 * @return Inkscape CLI option
	 */
	String getIsCmd();

	/**
	 * Tests if the option was in the command line.
	 * @return true if it was in the CLI, false otherwise
	 */
	boolean inCli();

	/**
	 * Returns the option value.
	 * @return value
	 */
	String getOptValue();
}
