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

package de.vandermeer.svg2vector.applications;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.ExecS_Application;
import de.vandermeer.execs.options.AO_DirectoryOut;
import de.vandermeer.execs.options.AO_FileIn;
import de.vandermeer.execs.options.AO_FileOut;
import de.vandermeer.execs.options.AO_Verbose;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.options.AO_OnePerLayer;
import de.vandermeer.svg2vector.applications.options.AO_TargetExt;
import de.vandermeer.svg2vector.applications.options.AO_UriIn;
import de.vandermeer.svg2vector.converters.SvgTargets;

/**
 * Abstract base class for Svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public abstract class AppBase implements ExecS_Application {

	/** CLI parser. */
	final ExecS_CliParser cli;

	/** List of application options. */
	final ArrayList<ApplicationOption<?>> options = new ArrayList<>();

	/** Supported targets. */
	final SvgTargets[] targets;

	/** Application option for verbose mode. */
	final AO_Verbose optionVerbose = new AO_Verbose('v');

	/** Application option for target. */
	final AO_TargetExt optionTarget;

	/** Application option for input file. */
	final AO_FileIn optionFileIn = new AO_FileIn(false, 'f', "input file <file>, must be a valid SVG file, can be compressed SVG (svgz)");

	/** Application option for output file. */
	final AO_FileOut optionFileOut = new AO_FileOut(false, 'o', "output file name, default is the basename of the input file plus '.pdf'");

	/** Application option for output directory. */
	final AO_DirectoryOut optionDirOut = new AO_DirectoryOut(false, 'd', "output directory, default value is the current directory");

	/** Application option for input URI. */
	final AO_UriIn optionUriIn = new AO_UriIn(false, 'u', "input URI <uri>, must point to a valid SVG file, can be compressed SVG (svgz)");

	/** Application option for one-per-layer mode. */
	final AO_OnePerLayer optionOnePerLayer = new AO_OnePerLayer(false, 'l', "create one output file (for given target) file per SVG layer");

	/**
	 * Returns a new application.
	 * @throws NullPointerException if
	 */

	/**
	 * Creates a new base application.
	 * @param targets the supported targets
	 * @throws NullPointerException if targets was null
	 * @throws IllegalArgumentException if targets contained null elements
	 */
	public AppBase(SvgTargets[] targets){
		Validate.noNullElements(targets);
		this.targets = targets;

		this.optionDirOut.setDefaultValue(System.getProperty("user.dir"));
		this.optionTarget = new AO_TargetExt(true, 't', "target for the conversion.", this.targets);

		this.cli = new ExecS_CliParser();
		this.addOption(this.optionVerbose);
		this.addOption(this.optionTarget);
		this.addOption(this.optionFileIn);
		this.addOption(this.optionFileOut);
		this.addOption(this.optionDirOut);
		this.addOption(this.optionUriIn);

		this.addOption(this.optionOnePerLayer);
	}

	@Override
	public int executeApplication(String[] args) {
		// parse command line, exit with help screen if error
		int ret = ExecS_Application.super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		SvgTargets target = this.optionTarget.getTarget();
		if(target==null){
			this.printError("given target <" + this.optionTarget.getValue() + "> not supported. Use one of the supported targets: " + new StrBuilder().appendWithSeparators(this.targets, ", "));
			return -10;
		}

		if(!this.optionFileIn.inCli() && !this.optionUriIn.inCli()){
			this.printError("no input given, no file nor URI specified");
			return -20;
		}

		if(this.optionFileIn.inCli() && this.optionUriIn.inCli()){
			this.printError("ambigious input - file and URI input given");
			return -21;
		}

		if(this.optionFileIn.getCliValue()!=null){
			String fn = this.optionFileIn.getCliValue();
			File testFD = new File(fn);
			if(!testFD.exists()){
				this.printError("input file <" + fn + "> does not exist, please check path and filename");
				return -22;
			}
			if(!testFD.isFile()){
				this.printError("give input file <" + fn + "> is not a file, please check path and filename");
				return -23;
			}
			if(!testFD.canRead()){
				this.printError("cannot read input file <" + fn + ">, please file permissions");
				return -24;
			}
			this.optionUriIn.setURI(testFD.toURI());
		}
		else if(this.optionUriIn.getCliValue()!=null){
			String err = null;
			try {
				this.optionUriIn.setURI(new URI(this.optionUriIn.getCliValue()));
			}
			catch (URISyntaxException e) {
				err = e.getMessage();
			}
			if(err!=null){
				this.printError(err);
				return -25;
			}
		}

		if(this.optionFileOut.inCli()){
			String fn = this.optionFileOut.getCliValue();
			if(StringUtils.containsAny(fn, "?!")){
				this.printError("output filename <" + fn + "> contains strange characters");
				return -30;
			}
			if(fn.endsWith("." + target.name())){
				this.printError("output filename <" + fn + "> should not contain target file extension");
				return -31;
			}
		}
		else{
			String fn = this.optionUriIn.getURI().getPath();
			fn = fn.substring(fn.lastIndexOf('/')+1, fn.length());
			if(fn.contains("." + target.name())){
				fn = fn.substring(0, fn.lastIndexOf('.'));
			}
			if(fn.contains(".svgz")){
				fn = fn.substring(0, fn.lastIndexOf('.'));
			}
			if(fn.contains("#")){
				fn = fn.substring(0, fn.lastIndexOf('#'));
			}
			if(fn.contains("?")){
			fn = fn.substring(0, fn.lastIndexOf('?'));
			}
			this.optionFileOut.setDefaultValue(fn);
		}

		String dout = this.optionDirOut.getValue();
		File testDir = new File(dout);
		if(!testDir.exists()){
			this.printError("output directory <" + dout + "> does not exist, please check path");
			return -40;
		}
		if(!testDir.isDirectory()){
			this.printError("give output directory <" + dout + "> is not a directory, please check path");
			return -41;
		}
		if(!testDir.canWrite()){
			this.printError("cannot write into output directory <" + dout + ">, please check permissions");
			return -42;
		}

		this.printProgress("input URI:        " + this.optionUriIn.getURI());
		this.printProgress("output directory: " + this.optionDirOut.getValue());
		this.printProgress("output file:      " + this.optionFileOut.getValue());

		return 0;
	}

	/**
	 * Prints an error message with the application name.
	 * @param err error message, ignored if null or blank
	 */
	public void printError(String err){
		if(!StringUtils.isBlank(err)){
			System.err.println(this.getAppName() + ": " + err + "\n");
		}
	}

	/**
	 * Prints progress of the conversion if tool is set to verbose.
	 * @param msg progress message, ignore if null or blank
	 */
	public void printProgress(String msg){
		if(this.optionVerbose.inCli()==true && !StringUtils.isBlank(msg)){
			System.out.println(this.getAppName() + ": " + msg);
		}
	}

	/**
	 * Adds a new option to CLI parser and option list.
	 * @param option new option, ignored if null
	 */
	protected void addOption(ApplicationOption<?> option){
		if(option!=null){
			this.getCli().addOption(option);
			this.options.add(option);
		}
	}

	@Override
	public ExecS_CliParser getCli() {
		return this.cli;
	}

	@Override
	public ApplicationOption<?>[] getAppOptions() {
		return this.options.toArray(new ApplicationOption<?>[]{});
	}
}
