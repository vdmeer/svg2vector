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

package de.vandermeer.svg2vector.applications.base.layers;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.svg2vector.ErrorCodes;
import de.vandermeer.svg2vector.S2VExeception;

/**
 * Application options for processing Inkscpe layers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public final class LayerOptions {

	/** Application option to automatically switch on all layers when no layers are processed. */
	final private AO_SwitchOnLayers aoSwitchOnLayers = new AO_SwitchOnLayers();

	/** Application option for processing layers. */
	final private AO_Layers aoLayers = new AO_Layers();

	/** Application option for processing layers if layers exist in input file. */
	final private AO_LayersIfExist aoLayersIfExists = new AO_LayersIfExist();

	/** Application option for using continuous index in output file name. */
	final private AO_FoutIndex aoFoutIndex = new AO_FoutIndex();

	/** Application option for using Inkscape label in output file name. */
	final private AO_FoutIsLabel aoFoutIsLabel = new AO_FoutIsLabel();

	/** Application option for using Inkscape index in output file name. */
	final private AO_FoutIsIndex aoFoutIsIndex = new AO_FoutIsIndex();

	/** Application option for not using a base name when processing layers. */
	final private AO_FoutNoBasename aoFoutNoBasename = new AO_FoutNoBasename();

	/** Application option for using a specified base name when processing layers. */
	final private AO_UseBaseName aoUseBaseName = new AO_UseBaseName();

	/** List of application options. */
	private final ApplicationOption<?>[] options;

	/** Flag for layer mode, default is false. */
	private boolean doLayers = false;

	/**
	 * Creates a new option object.
	 */
	public LayerOptions(){
		this.options = new ApplicationOption<?>[]{
			this.aoSwitchOnLayers,
			this.aoLayers,
			this.aoLayersIfExists,
			this.aoFoutIsIndex,
			this.aoFoutIsLabel,
			this.aoFoutIndex,
			this.aoFoutNoBasename,
			this.aoUseBaseName
		};
	}

	/**
	 * Sets options from CLI settings.
	 * @param docHasLayers flag for layers detected in a loaded SVG file, true if has layers, false otherwise
	 * @throws S2VExeception for any error
	 */
	public void setOptions(boolean docHasLayers) throws S2VExeception{
		if(this.aoLayers.inCli()){
			if(!docHasLayers){
				throw new S2VExeception(ErrorCodes.LAYERS_REQUESTED_DOC_WITHOUT_LAYERS__0);
			}
			else{
				this.doLayers = true;
			}
		}
		if(this.aoLayersIfExists.inCli()){
			this.doLayers = docHasLayers;
		}
	}

	/**
	 * Checks for warnings and returns a list of warnings.
	 * @return list of warnings, empty if none found
	 */
	public List<String> getWarnings(){
		List<String> ret = new ArrayList<>();
		if(this.doLayers){
			ApplicationOption<?>[] options = new ApplicationOption<?>[]{
				this.aoSwitchOnLayers
			};
			for(ApplicationOption<?> ao : options){
				if(ao.inCli()){
					ret.add("layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
				}
			}
		}
		else{
			ApplicationOption<?>[] options = new ApplicationOption<?>[]{
				this.aoFoutIndex,
				this.aoFoutIsIndex,
				this.aoFoutIsLabel,
				this.aoFoutNoBasename,
				this.aoUseBaseName
			};
			for(ApplicationOption<?> ao : options){
				if(ao.inCli()){
					ret.add("no layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
				}
			}
		}
		return ret;
	}

	/**
	 * Returns the message options as array.
	 * @return message options array
	 */
	public final ApplicationOption<?>[] getOptions(){
		return this.options;
	}

	/**
	 * Returns the do-layers flag calculated from the options.
	 * @return true if layers should be processed, false otherwise
	 */
	public boolean doLayers(){
		return this.doLayers;
	}

	/**
	 * Returns the switch for all layers on (when not processing layers).
	 * @return true if all layers should be switched on, false otherwise
	 */
	public boolean allLayersOn(){
		return this.aoSwitchOnLayers.inCli();
	}

	/**
	 * Tests if continuous index for output file names is requested.
	 * @return true if requested, false otherwise
	 */
	public boolean foutIndex(){
		return this.aoFoutIndex.inCli();
	}

	/**
	 * Tests if Inkscape index for output file names is requested.
	 * @return true if requested, false otherwise
	 */
	public boolean foutIsIndex(){
		return this.aoFoutIsIndex.inCli();
	}

	/**
	 * Tests if Inkscape label for output file names is requested.
	 * @return true if requested, false otherwise
	 */
	public boolean foutIsLabel(){
		return this.aoFoutIsLabel.inCli();
	}

	/**
	 * Tests if output file names should not use a base name.
	 * @return true if no base name should be used, false otherwise
	 */
	public boolean foutNoBasename(){
		return this.aoFoutNoBasename.inCli();
	}

	/**
	 * Returns the requested base name.
	 * @return requested base name, null if not set
	 */
	public String getBasename(){
		return this.aoUseBaseName.getValue();
	}
}
