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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrSubstitutor;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.fidibus.files.ExecutableSource;
import de.vandermeer.skb.interfaces.fidibus.files.FileExecutor;
import de.vandermeer.svg2vector.applications.base.conversion.ConversionOptions;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * An Inkscape command object creating a command line with entries for string substitution.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.1
 */
public class InkscapeExecutor {

	/** Substitution string for input file. */
	public final static String SUBS_FIN = "${fin}";

	/** Substitution string for output file. */
	public final static String SUBS_FOUT = "${fout}";

	/** The source object for Inkscape. */
	protected final ExecutableSource source ;

	/** The exectuor object that runs Inkscape. */
	protected final FileExecutor executor;

	/**
	 * Creates a new executor.
	 * @param filename the executable (including path) file name for Inkscape, must not be blank
	 * @param simulate flag for simulation, if true no execution happens
	 * @throws ApplicationException if isExec was blank or not a file or was not executable
	 */
	public InkscapeExecutor(String filename, boolean simulate){
		Validate.notBlank(filename);
		this.source = ExecutableSource.create(
				filename,
				"inkscape",
				"Inkscape",
				"Inkscape executable for s2v-is"
		);
		this.executor = FileExecutor.create(this.source, simulate, null);
		this.executor.addCommand("--without-gui --export-area-page");
		this.executor.addCommand("--file=", SUBS_FIN);
	}

	/**
	 * Creates a new Inkscape executor copying all parameters from the given executor
	 * @param executor original executor, must not be null
	 */
	public InkscapeExecutor(InkscapeExecutor executor){
		Validate.notNull(executor);
		this.source = ExecutableSource.create(
				executor.getSource().getFilename(),
				executor.getSource().getName(),
				executor.getSource().getDisplayName(),
				executor.getSource().getDescription()
		);
		this.executor = FileExecutor.create(
				this.source,
				executor.getExecutor().simulate(),
				executor.getExecutor().buildCommand()
		);
	}

	/**
	 * Returns the source.
	 * @return source
	 */
	protected ExecutableSource getSource(){
		return this.source;
	}

	/**
	 * Returns the executor.
	 * @return executor
	 */
	protected FileExecutor getExecutor(){
		return this.executor;
	}

	/**
	 * Returns the command that will be executed without input/output file substitutions.
	 * @return
	 */
	public String getCommand(){
		return this.executor.buildCommand();
	}

	/**
	 * Runs the command.
	 * @param inputFile the input file, must not be blank, file will not be validated
	 * @param outputFile the output file, must not be blank, file will not be validated
	 * @return true on success or if simulated, false on error
	 * @throws ApplicationException for IO and Interrupted exceptions during execution
	 */
	public boolean runCommand(String inputFile, String outputFile) throws ApplicationException{
		Validate.notBlank(inputFile);
		Validate.notBlank(outputFile);

		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("fin", inputFile);
		valuesMap.put("fout", outputFile);
		StrSubstitutor subst = new StrSubstitutor(valuesMap);
		return this.executor.runCommand(subst);
	}

	/**
	 * Returns the command as string.
	 * @return the command as string
	 */
	public String toString(){
		return this.getCommand();
	}

	/**
	 * Appends conversion settings to the command.
	 * @param conversionProperties conversion options, must not be null
	 * @return self to allow chaining
	 */
	public InkscapeExecutor appendCmd(ConversionOptions conversionProperties){
		Validate.notNull(conversionProperties);
		if(conversionProperties.doesTextAsShape()){
			this.executor.addCommand("--export-text-to-path");
		}
		return this;
	}

	/**
	 * Append a target specific argument to the command
	 * @param target the target with the arguments, must not be null
	 * @return self to allow chaining
	 */
	public InkscapeExecutor appendCmd(SvgTargets target){
		Validate.notNull(target);
		this.executor.addCommand(this.target2CLI(target, true), "=", SUBS_FOUT);
		return this;
	}

	/**
	 * Appends target specific settings read from application properties.
	 * @param target the target, must not be null
	 * @param options application options, must not be null and have no null elements
	 * @return self to allow chaining
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any option was null
	 */
	public InkscapeExecutor appendTargetSettings(SvgTargets target, IsTargetOption ... options){
		Validate.notNull(target);
		Validate.notNull(options);
		Validate.noNullElements(options);

		for(IsTargetOption to : options){
			if(target==to.getTarget() && to.inCli() && to.getOptValue()!=null){
				this.executor.addCommand(to.getIsCmd(), "=", to.getOptValue());
			}
		}
		return this;
	}

	/**
	 * Appends all required CLI options for a node that is selected.
	 * @param nodeId the node identifier, must not be blank
	 * @return self to allow chaining
	 * @throws NullPointerException if nodeId was null
	 * @throws IllegalArgumentException if nodeId was blank
	 */
	public InkscapeExecutor appendSelectedNode(String nodeId){
		Validate.notBlank(nodeId);
		this.executor.addCommand("-j -i=", nodeId);
		this.executor.addCommand("--select=", nodeId);
		return this;
	}

	/**
	 * Returns long or short Inkscape command line option for given target.
	 * @param target the target, can be null
	 * @param assumeLong true to get a long option, false to get the short option
	 * @return the requested option, including leading dashes, null if no option found
	 */
	private String target2CLI(SvgTargets target, boolean assumeLong){
		if(target==null){
			return null;
		}
		switch(target){
			case emf:
				return (assumeLong)?"--export-emf":"-M";
			case eps:
				return (assumeLong)?"--export-eps":"-E";
			case pdf:
				return (assumeLong)?"--export-pdf":"-A";
			case png:
				return (assumeLong)?"--export-png":"-e";
			case ps:
				return (assumeLong)?"--export-ps":"-P";
			case svg:
				return (assumeLong)?"--export-plain-svg":"-l";
			case wmf:
				return (assumeLong)?"--export-wmf":"-m";
		}
		return null;
	}

}
