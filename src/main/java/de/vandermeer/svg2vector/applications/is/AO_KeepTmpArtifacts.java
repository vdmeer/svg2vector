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

import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractSimpleC;

/**
 * Application option `keep-tmp-artifacts`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_KeepTmpArtifacts extends AbstractSimpleC {

	/**
	 * Returns the new option.
	 */
	public AO_KeepTmpArtifacts(){
		super(
				"Keep Temporary Artifacts", null, "keep-tmp-artifacts", false,
				"keep temporary created artifacts (files and directories)",
				LONG_DESCRIPTION()
		);
	}

	/**
	 * Returns the long description generated from an ST template.
	 * @return the long description
	 */
	private static final Object LONG_DESCRIPTION(){
		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/is/AO_KeepTmpArtifacts.stg");
		return stg.getInstanceOf("longDescription");
	}
}
