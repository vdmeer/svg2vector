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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.svg2vector.applications.base.conversion.ConversionOptions;
import de.vandermeer.svg2vector.applications.core.ErrorCodes;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * An Inkscape command object creating a command line with entries for string substitution.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class IsExecutor {

	/** Substitution string for input file. */
	public final static String SUBS_FIN = "${fin}";

	/** Substitution string for output file. */
	public final static String SUBS_FOUT = "${fout}";

	/** The command as string builder. */
	protected final StrBuilder cmd;

	/** Simulate flag, no file system operation will be done if true. */
	private final boolean simulate;

	/**
	 * Creates a new command copying the given command.
	 * @param command command to copy values from, must not be null
	 * @throws NullPointerException if command was null
	 */
	public IsExecutor(IsExecutor command){
		Validate.notNull(command);
		this.cmd = new StrBuilder();
		this.cmd.append(command.cmd.toCharArray());
		this.simulate = command.simulate;
	}

	/**
	 * Creates a new executor.
	 * @param isExec the executable (including path) file name for Inkscape
	 * @param simulate flag for simulation, if true no execution happens
	 * @throws ApplicationException if isExec was blank or not a file or was not executable
	 */
	public IsExecutor(String isExec, final boolean simulate) throws ApplicationException {
		if(StringUtils.isBlank(isExec)){
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_FN_BLANK__1, isExec);
		}
		File testFD = new File(isExec);
		if(!testFD.exists()){
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_FN_NOTEXIST__1, isExec);
		}
		if(!testFD.isFile()){
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_FN_NOT_FILE__1, isExec);
		}
		if(!testFD.canExecute()){
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_FN_CANNOT_EXECUTE__1, isExec);
		}

		this.cmd = new StrBuilder();
		if(isExec.contains("\"")){
			this.cmd.clear().append('"');
		}
		this.cmd.append(isExec);
		if(isExec.contains("\"")){
			this.cmd.clear().append('"');
		}
		this.cmd
			.append(' ')
			.append("--without-gui --export-area-page")
			.append(" --file=")
			.append(SUBS_FIN);
		;

		this.simulate = simulate;
	}

	/**
	 * Appends conversion settings to the command.
	 * @param conversionProperties conversion options, must not be null
	 * @return self to allow chaining
	 * @throws NullPointerException if argument was null
	 */
	public IsExecutor appendCmd(ConversionOptions conversionProperties){
		Validate.notNull(conversionProperties);

		if(conversionProperties.doesTextAsShape()){
			this.cmd.append(" --export-text-to-path");
		}
		return this;
	}

	public IsExecutor appendCmd(SvgTargets target){
		Validate.notNull(target);
		this.cmd.append(' ').append(target2CLI(target, true)).append('=').append(SUBS_FOUT);
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
	public IsExecutor appendTargetSettings(SvgTargets target, IsTargetOption ... options){
		Validate.notNull(target);
		Validate.notNull(options);
		Validate.noNullElements(options);

		for(IsTargetOption to : options){
			if(target==to.getTarget() && to.inCli() && to.getOptValue()!=null){
				this.cmd.append(' ').append(to.getIsCmd()).append('=').append(to.getOptValue());
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
	public IsExecutor appendSelectedNode(String nodeId){
		Validate.notBlank(nodeId);
		this.cmd.append(" -j -i=").append(nodeId);
		this.cmd.append(" --select=").append(nodeId);
		return this;
	}

	/**
	 * Executes Inkscape with the created command for given input and output files.
	 * @param fin the input file, must not be blank
	 * @param fout the output file, must not be blank
	 * @throws ApplicationException if execution had errors
	 */
	public void executeInkscape(final String fin, final String fout) throws ApplicationException{
		Validate.notBlank(fin);
		Validate.notBlank(fout);

		if(this.simulate){
			return;
		}

		try{
			Process p = Runtime.getRuntime().exec(this.getExecCommand(fin, fout));
			p.waitFor();
		}
		catch (IOException e) {
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_IO__1, e.getMessage());
		}
		catch (InterruptedException e) {
			throw new ApplicationException(ErrorCodes.INKSCAPE_EXEC_INTERRUPTED__1, e.getMessage());
		}
	}

	/**
	 * Returns the Inkscape command substituting placeholder with input and output files.
	 * @param fin input file, must not be blank
	 * @param fout output file, must not be blank
	 * @return the Inkscape command
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any argument was blank
	 */
	public String getExecCommand(final String fin, final String fout){
		Validate.notBlank(fin);
		Validate.notBlank(fout);

		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("fin", fin);
		valuesMap.put("fout", fout);
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		return sub.replace(this.cmd.toString());
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

	/**
	 * Returns the command as string.
	 * @return the command as string
	 */
	public String toString(){
		return this.cmd.toString();
	}
}
