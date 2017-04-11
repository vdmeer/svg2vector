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
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.options.AO_DirectoryOut;
import de.vandermeer.execs.options.AO_FileIn;
import de.vandermeer.execs.options.AO_FileOut;
import de.vandermeer.execs.options.AO_Quiet;
import de.vandermeer.execs.options.AO_Verbose;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.svg2vector.converters.SvgTargets;

/**
 * Properties and options for SVG2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AppProperties <L extends SV_DocumentLoader> {

	/** Substitution pattern for layers using layer index in output file names. */
	public static String SUBST_PATTERN_INDEX = "${index}";

	/** Substitution pattern for layers using layer identifier (id) in output file names. */
	public static String SUBST_PATTERN_ID = "${id}";

	/** List of application options. */
	final private ArrayList<ApplicationOption<?>> options = new ArrayList<>();

	/** List of application options that should cause a warning when used in no-layer process. */
	final private ArrayList<ApplicationOption<?>> noLayersWarnings;

	/** List of application options that should cause a warning when used in with-layer process. */
	final private ArrayList<ApplicationOption<?>> withLayersWarnings;

	/** Application option for verbose mode. */
	final private AO_Verbose aoVerbose = new AO_Verbose('v');

	/** Application option for quiet mode. */
	final private AO_Quiet aoQuiet = new AO_Quiet("appliction will be absolutely quiet, no output to sterr or stout.");

	/** Application option for printing progress information. */
	final private AO_MsgProgress aoMsgProgress = new AO_MsgProgress();

	/** Application option for printing warning messages. */
	final private AO_MsgWarning aoMsgWarning = new AO_MsgWarning();

	/** Application option for printing detailed messages. */
	final private AO_MsgDetail aoMsgDetail = new AO_MsgDetail();

	/** Application option for target. */
	final private AO_TargetExt aoTarget;

	/** Application option for input file. */
	final private AO_FileIn aoFileIn = new AO_FileIn(true, 'f', "input file <file>, must be a valid SVG file, can be compressed SVG (svgz)");

	/** Application option for output file. */
	final private AO_FileOut aoFileOut = new AO_FileOut(false, 'o', "output file name, default is the basename of the input file plus target extension");

	/** Application option for output directory. */
	final private AO_DirectoryOut aoDirOut = new AO_DirectoryOut(false, 'd', "output directory, default value is the current directory");

	/** Application option to automatically create output directories. */
	final private AO_CreateDirectories aoCreateDirs = new AO_CreateDirectories();

	/** Application option to automatically overwrite existing files on output. */
	final private AO_OverwriteExisting aoOverwriteExisting = new AO_OverwriteExisting();

	/** Application option to automatically switch on all layers when no layers are processed. */
	final private AO_SwitchOnLayers aoSwitchOnLayers = new AO_SwitchOnLayers();

	/** Application option for processing layers. */
	final private AO_Layers aoLayers = new AO_Layers();

	/** Application option for processing layers if layers exist in input file. */
	final private AO_LayersIfExist aoLayersIfExists = new AO_LayersIfExist();

	/** Application option for using layer index in output file name. */
	final private AO_FoutLayerIndex aoFoutLayerIndex = new AO_FoutLayerIndex();

	/** Application option for using layer identifier in output file name. */
	final private AO_FoutLayerId aoFoutLayerId = new AO_FoutLayerId();

	/** Application option for not using a base name when processing layers. */
	final private AO_FoutNoBasename aoFoutNoBasename = new AO_FoutNoBasename();

	/** Application option for using a specified base name when processing layers. */
	final private AO_UseBaseName aoUseBaseName = new AO_UseBaseName();

	/** Application option for text-as-shape mode. */
	final private AO_TextAsShape aoTextAsShape = new AO_TextAsShape();

	/** The file name of the input file. */
	private String fin;

	/** The output directory. */
	private String dout;

	/** A pattern for generating fout when processing layers, in StrSubstitutor format. */
	private String foutPattern;

	/** The SVG document loader. */
	private L loader;

	/** List of warning messages collected during process. */
	private ArrayList<String> warnings = new ArrayList<>();

	/** Message mode for the application, 0 is quiet, all other values are generated using message type bit masks. */
	private int msgMode = MessageTypes.error.getMask();

	public AppProperties(SvgTargets[] targets, L loader){
		Validate.noNullElements(targets);
		Validate.notNull(loader);
		this.loader = loader;

		this.aoDirOut.setDefaultValue(System.getProperty("user.dir"));
		this.aoTarget = new AO_TargetExt(true, 't', "target for the conversion", targets);

		this.addOption(this.aoVerbose);
		this.addOption(this.aoQuiet);
		this.addOption(this.aoMsgProgress);
		this.addOption(this.aoMsgDetail);
		this.addOption(this.aoMsgWarning);

		this.addOption(this.aoTarget);

		this.addOption(this.aoFileIn);
		this.addOption(this.aoFileOut);
		this.addOption(this.aoDirOut);
		this.addOption(this.aoCreateDirs);
		this.addOption(this.aoOverwriteExisting);

		this.addOption(this.aoSwitchOnLayers);

		this.addOption(this.aoLayers);
		this.addOption(this.aoLayersIfExists);
		this.addOption(this.aoFoutLayerIndex);
		this.addOption(this.aoFoutLayerId);
		this.addOption(this.aoFoutNoBasename);
		this.addOption(this.aoUseBaseName);

		this.addOption(this.aoTextAsShape);

		this.noLayersWarnings = new ArrayList<>();
		this.noLayersWarnings.add(this.aoDirOut);
		this.noLayersWarnings.add(this.aoFoutLayerIndex);
		this.noLayersWarnings.add(this.aoFoutLayerId);
		this.noLayersWarnings.add(this.aoFoutNoBasename);
		this.noLayersWarnings.add(this.aoUseBaseName);

		this.withLayersWarnings = new ArrayList<>();
		this.withLayersWarnings.add(this.aoSwitchOnLayers);
		this.withLayersWarnings.add(this.aoFileOut);
	}

	/**
	 * Adds an application option.
	 * @param option new option, ignored if null
	 */
	public void addOption(ApplicationOption<?> option){
		if(option!=null){
			this.options.add(option);
		}
	}

	/**
	 * Tests if the properties are set to process layers.
	 * @return true if set to process layers, false otherwise
	 */
	public boolean doesLayers(){
		return this.getFoutFn()==null && this.getDout()!=null && this.getFoutPattern()!=null;
	}

	/**
	 * Tests if the properties are set to process for a single output file, not processing patterns.
	 * @return true if set of single output file, false otherwise
	 */
	public boolean doesNoLayers(){
		return this.getFoutFn()!=null && this.getDout()==null && this.getFoutPattern()==null;
	}

	/**
	 * Returns the list of added application options.
	 * @return application option list, empty if none added
	 */
	public ApplicationOption<?>[] getAppOptions() {
		return this.options.toArray(new ApplicationOption<?>[]{});
	}

	/**
	 * Returns the output directory for processing layers.
	 * @return directory name, null if not set or errors on setting
	 */
	public String getDout(){
		return this.dout;
	}

	/**
	 * Returns the input file name.
	 * @return input file name, null if none set
	 */
	public String getFinFn(){
		return this.fin;
	}

	/**
	 * Returns the file name for a single output file when not processing layers.
	 * @return file name, null if not set or if errors on setting
	 */
	public String getFoutFn(){
		return this.aoFileOut.getDefaultValue();
	}

	/**
	 * Returns the generated pattern for output files.
	 * @return generated pattern, null if not set
	 */
	public String getFoutPattern(){
		return this.foutPattern;
	}

	/**
	 * Returns the document loader.
	 * @return the document loader
	 */
	public L getLoader(){
		return this.loader;
	}

	/**
	 * Returns the message mode.
	 * @return message mode: 0 for quiet, bit mask otherwise
	 */
	public int getMsgMode(){
		return this.msgMode;
	}

	/**
	 * Returns the supported targets.
	 * @return supported targets
	 */
	public SvgTargets[] getSupportedTargetts(){
		return this.aoTarget.getSupportedTargets();
	}

	/**
	 * Returns the application target.
	 * @return application target, null if none set or if a set target was not in the list of supported targets
	 */
	public SvgTargets getTarget(){
		return this.aoTarget.getTarget();
	}

	/**
	 * Returns the set value of the target option.
	 * @return target option value
	 */
	public String getTargetValue(){
		return this.aoTarget.getValue();
	}

	/**
	 * Returns current warnings.
	 * @return current warnings, empty (size 0) if none collected
	 */
	public ArrayList<String> getWarnings(){
		return this.warnings;
	}

	/**
	 * Returns the flag for processing layers individually.
	 * @return true if layers should be processed, false otherwise
	 */
	public boolean processLayers(){
		return this.aoLayers.inCli();
	}

	/**
	 * Tests input file settings and loads it.
	 * @return null in success, error string on error
	 */
	public String setInput(){
		if(StringUtils.isBlank(this.aoFileIn.getCliValue())){
			return "no input file given";
		}
		else{
			String fn = this.aoFileIn.getValue();
			File testFD = new File(fn);
			if(!testFD.exists()){
				return "input file <" + fn + "> does not exist, please check path and filename";
			}
			if(!testFD.isFile()){
				return "input file <" + fn + "> is not a file, please check path and filename";
			}
			if(!testFD.canRead()){
				return "cannot read input file <" + fn + ">, please file permissions";
			}
			this.fin = fn;
		}
		return this.loader.load(this.fin);
	}

	/**
	 * Sets the message mode according to CLI settings.
	 */
	public void setMessageMode(){
		if(this.aoQuiet.inCli()){
			this.msgMode = 0;
			return;
		}
		if(this.aoVerbose.inCli()){
			this.msgMode = MessageTypes.error.getMask()
					| MessageTypes.warning.getMask()
					| MessageTypes.progress.getMask()
					| MessageTypes.detail.getMask()
			;
		}

		if(this.aoMsgProgress.inCli()){
			this.msgMode = this.msgMode | MessageTypes.progress.getMask();
		}
		if(this.aoMsgWarning.inCli()){
			this.msgMode = this.msgMode | MessageTypes.warning.getMask();
		}
		if(this.aoMsgDetail.inCli()){
			this.msgMode = this.msgMode | MessageTypes.detail.getMask();
		}
	}

	/**
	 * Tests all CLI options that influence output names and sets the output name
	 * @return null in success, error string on error
	 */
	public String setOutput(){
		//fin, fout, dout, l, layer-i, layers-I, b, L

		SvgTargets target = this.getTarget();
		if(target==null){
			return "implementation error: cannot set output file w/o a valid target";
		}
		if(this.getFinFn()==null){
			return "implementation error: no input file name set";
		}

		if(this.aoLayers.inCli() && !this.loader.hasInkscapeLayers()){
			this.warnings.add("layers activated but input file has no layers, continue for single output file");
		}

		if((this.aoLayers.inCli() || this.aoLayersIfExists.inCli()) && this.loader.hasInkscapeLayers()){
			return this.setOutputWithLayers(target);
		}
		else{
			return this.setOutputNoLayers(target);
		}
	}

	/**
	 * Set output and do all tests for no layer processing.
	 * @param target the set target
	 * @return null on success, error message on error
	 */
	private String setOutputNoLayers(SvgTargets target){
		//warnings first
		for(ApplicationOption<?> ao : this.noLayersWarnings){
			if(ao.inCli()){
				this.warnings.add("no layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
			}
		}

		String fn = null;
		if(this.aoFileOut.inCli()){
			fn = this.aoFileOut.getCliValue();
			if(StringUtils.isBlank(fn)){
				return "output filename is blank";
			}
			if(fn.endsWith("." + target.name())){
				return "output filename <" + fn + "> should not contain target file extension";
			}
		}
		else if(this.fin!=null){
			fn = this.fin;
			if(fn.endsWith("." + target.name())){
				return "no output name given and target extension same as input extension, do not want to overwrite input file";
			}
			else if(fn.endsWith(".svg")){
				fn = fn.substring(0, fn.lastIndexOf('.'));
			}
			else if(fn.endsWith(".svgz")){
				fn = fn.substring(0, fn.lastIndexOf('.'));
			}
		}
		fn += "." + target.name();
		File fnF = new File(fn);
		if(fnF.exists() && fnF.isDirectory()){
			return "output file <" + fn + "> exists but is a directory";
		}
		if(fnF.exists() && !this.aoOverwriteExisting.inCli()){
			return "output file <" + fn + "> exists and no option <" + this.aoOverwriteExisting.getCliOption().getLongOpt() + "> used";
		}
		if(fnF.exists() && !fnF.canWrite() && this.aoOverwriteExisting.inCli()){
			return "output file <" + fn + "> exists but cannot write to it";
		}
		File fnFParent = fnF.getParentFile();
		if(fnFParent.exists() && !fnFParent.isDirectory()){
			return "output directory <" + fnFParent.toString().replace('\\', '/') + "> exists but is not a directory";
		}
		if(!fnFParent.exists() && !this.aoCreateDirs.inCli()){
			return "output directory <" + fnFParent.toString().replace('\\', '/') + "> does not exist and CLI option <" + this.aoCreateDirs.getCliOption().getLongOpt() + "> not used";
		}

		//switch on all layers if requested
		if(this.aoSwitchOnLayers.inCli()){
			this.loader.switchOnAllLayers();
		}

		//all tests ok, out fn into Fout
		this.aoFileOut.setDefaultValue(fn);
		return null;
	}

	/**
	 * Set output and do all tests for layer processing.
	 * @param target the set target
	 * @return null on success, error message on error
	 */
	private String setOutputWithLayers(SvgTargets target){
		//warnings first
		for(ApplicationOption<?> ao : this.withLayersWarnings){
			if(ao.inCli()){
				this.warnings.add("layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
			}
		}

		String dout = this.aoDirOut.getValue();
		File testDir = new File(dout);
		if(testDir.exists() && !testDir.isDirectory()){
			return "output directory <" + dout + "> exists but is not a directory";
		}
		if(testDir.exists() && !testDir.canWrite()){
			return "output directory <" + dout + "> exists but cannot write into it, check permissions";
		}
		if(!testDir.exists() && !this.aoCreateDirs.inCli()){
			return "output directory <" + dout + "> does not exist and CLI option <" + this.aoCreateDirs.getCliOption().getLongOpt() + "> not used";
		}
		this.dout = dout;

		StrBuilder pattern = new StrBuilder();
		pattern.append(dout);
		if(!pattern.endsWith("/")){
			pattern.append('/');
		}

		if(!this.aoFoutNoBasename.inCli()){
			if(this.aoUseBaseName.inCli()){
				pattern.append(this.aoUseBaseName.getValue());
			}
			else{
				String bn = StringUtils.substringAfterLast(this.fin, "/");
				bn = StringUtils.substringBeforeLast(bn, ".");
				pattern.append(bn);
			}
		}
		else{
			if(!this.aoFoutLayerId.inCli() && !this.aoFoutLayerIndex.inCli()){
				return "CLI option <" + this.aoFoutNoBasename.getCliOption().getLongOpt() + "> used but neither <" + this.aoFoutLayerId.getCliOption().getLongOpt() + "> nor <" + this.aoFoutLayerIndex.getCliOption().getLongOpt() + "> options requestes, amigious output file names";
			}
		}

		if(this.aoFoutLayerIndex.inCli()){
			if(!pattern.endsWith("/")){
				pattern.append('-');
			}
			pattern.append(SUBST_PATTERN_INDEX);
		}

		if(this.aoFoutLayerId.inCli()){
			if(!pattern.endsWith("/")){
				pattern.append('-');
			}
			pattern.append(SUBST_PATTERN_ID);
		}

		this.foutPattern = pattern.toString();
		return null;
	}
}
