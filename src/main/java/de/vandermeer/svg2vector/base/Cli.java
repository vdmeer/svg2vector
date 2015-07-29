/* Copyright 2014 Sven van der Meer <vdmeer.sven@mykolab.com>
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
package de.vandermeer.svg2vector.base;

import java.awt.Color;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Command line parser for the converter.
 * 
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
public class Cli {

	/** CLI options */
	protected Options options;

	/** CLI line parsed */
	protected CommandLine line;

	/** Application name for logs and print outs */
	protected String appName;

	/** The target option value */
	protected String target;

	/** The file option value */
	protected String file;

	/** The URI option value */
	protected String uri;

	/** The directory option value */
	protected String directory;

	/** The output file option value */
	protected String output;

	/** Flag for switching on/off verbose mode */
	protected boolean verbose = false;

	/**
	 * Constructor.
	 * @param appName name of the application using the CLI class
	 */
	public Cli(String appName){
		this.appName = appName;
		this.options = new Options();
		this.buildCliOptions();
	}

	/**
	 * Registers a pre-defined set of options with all parameters.
	 */
	protected void buildCliOptions(){
		this.options.addOption("h", "help", false, "usage information");
		this.options.addOption("v", "verbose", false, "print detailed information about the process");

		this.options.addOption("t", "target", true, "target format <target>, supported targets are: pdf, emf, svg");

		this.options.addOption("f", "file", true, "input file <file>, must be a valid SVG file, can be compressed SVG (svgz)");
		this.options.addOption("u", "uri", true, "input URI <uri>, must point to a valid SVG file, can be compressed SVG (svgz)");
		this.options.addOption("d", "directory", true, "output directory, default value is the current directory");
		this.options.addOption("o", "output", true, "output file name, default is the basename of the input file plus '.pdf'");

		this.options.addOption("l", "one-per-layer", false, "create one pdf file per Inkscape layer");

		this.options.addOption("i", "use-layer-index", false, "use layer index for inkscape layer processing, default is layer ID");
		this.options.addOption("I", "use-layer-index-id", false, "use layer index and ID for inkscape layer processing, default is layer ID");

		this.options.addOption("n", "not-transparent", false, "switch off transparency");
		this.options.addOption("s", "not-text-as-shape", false, "switch of text-as-shape property");
		this.options.addOption("c", "clip", false, "activate clip property");
		this.options.addOption("r", "bgrnd-color", true, "sets the background color");
		this.options.addOption("b", "no-background", false, "switch off background property");
	}

	/**
	 * Parses command line and sets options.
	 * @param args command line arguments
	 * @throws ParseException an exception in case parsing went wrong
	 */
	public void parse(String[] args) throws ParseException {
		CommandLineParser parser = new PosixParser();
		try {
			this.line = parser.parse(this.options, args, false);
		}
		catch(ParseException ex){
			throw ex;
		}
	}

	/**
	 * Prints a usage screen with all options.
	 */
	public void usage(){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(this.appName, this.options);
	}

	/**
	 * Parses a pre-defined set of standard options.
	 * @return -1 in case of error (message on STDERR), 0 if successful and program should exit, 1 otherwise
	 */
	public int parseStandardOptions(){
		if(this.line==null){
			System.err.println(this.appName+": " + "to few arguments, try -h or --help for usage information\n");
			return -1;
		}

		if(this.line.hasOption('h')){
			this.usage();
			return 0;
		}

		if(!this.line.hasOption('t')){
			System.err.println(this.appName+": " + "target required, see -h for details\n");
			return -1;
		}

		if(this.line.hasOption('v')){
			this.verbose = true;
		}

		if(this.line.hasOption('f')){
			this.file=this.line.getOptionValue("f").trim();
		}

		if(this.line.hasOption('t')){
			this.target=this.line.getOptionValue("t").trim();
		}

		if(this.line.hasOption('u')){
			this.uri=this.line.getOptionValue("f").trim();
		}

		if(this.line.hasOption('o')){
			this.output=this.line.getOptionValue("o").trim();
		}

		if(!this.line.hasOption('d')){
			this.directory=System.getProperty("user.dir");
		}
		else{
			this.directory=this.line.getOptionValue("d").trim();
		}
	
		return 1;
	}

	/**
	 * Checks of verbose mode is set via CLI
	 * @return true if set
	 */
	public boolean isVerbose(){
		return this.verbose;
	}

	/**
	 * Returns the input filename from the respective CLI option
	 * @return input filename or null if not set
	 */
	public String getFile(){
		return this.file;
	}

	/**
	 * Returns the URI from the respective CLI option
	 * @return URI or null if not set
	 */
	public String getURI(){
		return this.uri;
	}

	/**
	 * Returns the directory from the respective CLI option
	 * @return directory or null if not set
	 */
	public String getDirectory(){
		return this.directory;
	}

	/**
	 * Returns the output filename from the respective CLI option
	 * @return output filename or null if not set
	 */
	public String getOutput(){
		return this.output;
	}

	/**
	 * Returns the target from the respective CLI option
	 * @return target or null if not set
	 */
	public String getTarget(){
		return this.target;
	}

	/**
	 * Sets all command line arguments in the given property object.
	 * @param props object with properties to set
	 */
	public void setProperties(TargetProperties props){

		//flags that require SVG to process things
		if(this.line.hasOption('i')){
			props.setUseInkscapeLayerName();
		}
		if(this.line.hasOption('I')){
			props.setUseInkscapeLayerIndex();
		}
		if(this.line.hasOption('l')){
			props.setUseOnePerInkscapeLayer();
		}

		//flags that are simply set in UserProperties
		if(this.line.hasOption('n')){
			props.setPropertyTransparent(false);
		}
		if(this.line.hasOption('s')){
			props.setPropertyTextAsShapes(false);
		}
		if(this.line.hasOption('c')){
			props.setPropertyClip(true);
		}
		if(this.line.hasOption('b')){
			props.setPropertyBackground(false);
		}
		if(this.line.hasOption('r')){
			Color color = Color.getColor(this.line.getOptionValue('r'));
			props.setPropertyBackgroundColor(color);
		}
	}
}
