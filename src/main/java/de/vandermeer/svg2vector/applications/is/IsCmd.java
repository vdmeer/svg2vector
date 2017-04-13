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
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;

import de.vandermeer.svg2vector.applications.base.AppProperties;
import de.vandermeer.svg2vector.applications.base.SvgTargets;

/**
 * An Inkscape command object creating a command line with entries for string substitution.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class IsCmd {

	/** Substitution string for input file. */
	public static String SUBS_FIN = "${fin}";

	/** Substitution string for output file. */
	public static String SUBS_FOUT = "${fout}";

	/** The command as string builder. */
	protected final StrBuilder cmd = new StrBuilder();

	/**
	 * Creates a new command copying the given command.
	 * @param command command to copy values from, must not be null
	 * @throws NullPointerException if command was null
	 */
	public IsCmd(IsCmd command){
		Validate.notNull(command);
		this.cmd.append(command.cmd.toCharArray());
	}

	/**
	 * Creates a new command with default entries.
	 * This will add the executable, no GUI, export area as page, text to path if set in properties, substituter for input file, and target.
	 * @param isExec Inkscape executable file name, must not be blank
	 * @param target the target, must not be null
	 * @param properties application properties, must not be null
	 * @throws NullPointerException if any argument was null
	 */
	public IsCmd(String isExec, SvgTargets target, AppProperties<?> properties){
		Validate.notNull(isExec);
		Validate.notNull(target);
		Validate.notNull(properties);

		if(isExec.contains("\"")){
			this.cmd.clear().append('"');
		}
		this.cmd.append(isExec);
		if(isExec.contains("\"")){
			this.cmd.clear().append('"');
		}
		this.cmd.append(' ').append("--without-gui --export-area-page");
		if(properties.doesTextAsShape()){
			this.cmd.append(" --export-text-to-path");
		}
		this.cmd.append(" --file=").append(SUBS_FIN);

		this.cmd.append(' ').append(target2CLI(target, true)).append('=').append(SUBS_FOUT);
	}

	/**
	 * Appends target specific settings read from application properties.
	 * @param target the target, must not be null
	 * @param options application options, must not be null and have no null elements
	 * @return self to allow chaining
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any option was null
	 */
	public IsCmd appendTargetSettings(SvgTargets target, IsTargetOption ... options){
		Validate.notNull(target);
		Validate.notNull(options);
		Validate.noNullElements(options);

		for(IsTargetOption to : options){
			if(target==to.getTarget() && to.inCli() && to.getValue()!=null){
				this.cmd.append(' ').append(to.getIsCmd()).append('=').append(to.getValue());
			}
		}
		return this;
	}

	/**
	 * Substitutes input and output file name in the command with given values
	 * @param fin input file, must not be blank
	 * @param fout output file, must not be blank
	 * @return a string with the substitutions
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any argument was blank
	 */
	public String substitute(String fin, String fout){
		Validate.notBlank(fin);
		Validate.notBlank(fout);

		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("fin", fin);
		valuesMap.put("fout", fout);
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		return sub.replace(this.cmd.toString());
	}

	/**
	 * Appends all required CLI options for a node that is selected.
	 * @param nodeId the node identifier, must not be blank
	 * @return self to allow chaining
	 * @throws NullPointerException if nodeId was null
	 * @throws IllegalArgumentException if nodeId was blank
	 */
	public IsCmd appendSelectedNode(String nodeId){
		Validate.notBlank(nodeId);
		this.cmd.append(" -j -i=").append(nodeId);
		this.cmd.append(" --select=").append(nodeId);
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

	@Override
	public String toString(){
		return this.cmd.toString();
	}
}
