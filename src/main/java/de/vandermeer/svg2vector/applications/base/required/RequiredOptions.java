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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.vandermeer.svg2vector.applications.core.CliOptionPackage;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.S2VExeception;
import de.vandermeer.svg2vector.applications.core.SV_DocumentLoader;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Required application options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public final class RequiredOptions extends CliOptionPackage {

	/** Application option for input file. */
	private final AO_FileInExt aoFileIn = new AO_FileInExt();

	/** Application option for target. */
	private final AO_TargetExt aoTarget;

	/** Input file name, set by set options. */
	private String fileName;

	/**
	 * Creates a new option object
	 * @param supportedTargets the supported targets, must not be null and must not contain null elements
	 */
	public RequiredOptions(SvgTargets[] supportedTargets){
		Validate.noNullElements(supportedTargets);
		this.aoTarget = new AO_TargetExt(supportedTargets);

		this.setTypedOptions(
			this.aoFileIn,
			this.aoTarget
		);
	}

	/**
	 * Sets the input and loads the document
	 * @param loader a document loader, must not be null
	 * @throws S2VExeception in any error case
	 */
	public void setInput(final SV_DocumentLoader loader) throws S2VExeception{
		Validate.notNull(loader);

		if(StringUtils.isBlank(this.aoFileIn.getCliValue())){
			throw new S2VExeception(ErrorCodes.NO_FIN__0);
		}

		String fileName = this.aoFileIn.getValue();
		File testFD = new File(fileName);
		if(!testFD.exists()){
			throw new S2VExeception(ErrorCodes.FIN_DOES_NOT_EXIST__1, fileName);
		}
		if(!testFD.isFile()){
			throw new S2VExeception(ErrorCodes.FIN_NOT_A_FILE__1, fileName);
		}
		if(!testFD.canRead()){
			throw new S2VExeception(ErrorCodes.CANNOT_READ_FIN__1, fileName);
		}
		this.fileName = fileName;
		loader.load(fileName);
	}

	/**
	 * Returns the input file name.
	 * @return input file name, null if not set
	 */
	public String getInputFilename(){
		return this.fileName;
	}

	/**
	 * Returns the conversion target.
	 * @return conversion target, null if not set
	 */
	public SvgTargets getTarget(){
		return this.aoTarget.getValue();
	}

	/**
	 * Returns the supported targets.
	 * @return supported targets, empty if none supported
	 */
	public SvgTargets[] getSupportedTargets(){
		return this.aoTarget.getSupportedTargets();
	}
}
