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

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import org.freehep.graphicsbase.util.UserProperties;

import de.vandermeer.execs.ExecS_Application;
import de.vandermeer.execs.options.AO_DirectoryOut;
import de.vandermeer.execs.options.AO_FileIn;
import de.vandermeer.execs.options.AO_FileOut;
import de.vandermeer.execs.options.AO_Target;
import de.vandermeer.execs.options.AO_Verbose;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.options.AO_BackgroundColor;
import de.vandermeer.svg2vector.applications.options.AO_Clip;
import de.vandermeer.svg2vector.applications.options.AO_NoBackground;
import de.vandermeer.svg2vector.applications.options.AO_NotTextAsShape;
import de.vandermeer.svg2vector.applications.options.AO_NotTransparent;
import de.vandermeer.svg2vector.applications.options.AO_OnePerLayer;
import de.vandermeer.svg2vector.applications.options.AO_UriIn;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndex;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndexId;
import de.vandermeer.svg2vector.base.Resources;
import de.vandermeer.svg2vector.base.SVG;
import de.vandermeer.svg2vector.base.TargetProperties;
import de.vandermeer.svg2vector.converters.EmfProperties;
import de.vandermeer.svg2vector.converters.PdfProperties;
import de.vandermeer.svg2vector.converters.Svg2Emf;
import de.vandermeer.svg2vector.converters.Svg2Pdf;
import de.vandermeer.svg2vector.converters.Svg2Svg;
import de.vandermeer.svg2vector.converters.SvgProperties;

/**
 * The Svg2Vector application using the FreeHep library.
 * It an SVG graphic to a vector format.
 * Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * It also can deal with SVG layers.
 * All options can be set via command line.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.8 build 170404 (04-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Svg2Vector_FH implements ExecS_Application {

	/** Application name. */
	public final static String APP_NAME = "s2v-hp";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector FreeHep";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v1.1.0 build 170404 (04-Apr-17) for Java 1.8";

	/** CLI parser. */
	ExecS_CliParser cli;

	/** Application option for verbose mode. */
	AO_Verbose optionVerbose = new AO_Verbose('v');

	/** Application option for target. */
	AO_Target optionTarget = new AO_Target(true, 't', "target format <target>, supported targets are: pdf, emf, svg");

	/** Application option for input file. */
	AO_FileIn optionFileIn = new AO_FileIn(true, 'f', "input file <file>, must be a valid SVG file, can be compressed SVG (svgz)");

	/** Application option for output file. */
	AO_FileOut optionFileOut = new AO_FileOut(false, 'o', "output file name, default is the basename of the input file plus '.pdf'");

	/** Application option for output directory. */
	AO_DirectoryOut optionDirOut = new AO_DirectoryOut(false, 'd', "output directory, default value is the current directory");

	/** Application option for input URI. */
	AO_UriIn optionUriIn = new AO_UriIn(false, 'u', "input URI <uri>, must point to a valid SVG file, can be compressed SVG (svgz)");

	/** Application option for one-per-layer mode. */
	AO_OnePerLayer optionOnePerLayer = new AO_OnePerLayer(false, 'l', "create one output file (for given target) file per SVG layer");

	/** Application option for use-layer-index mode. */
	AO_UseLayerIndex optionUseLayerIndex = new AO_UseLayerIndex(false, 'i', "use layer index for inkscape layer processing, default is layer ID");

	/** Application option for use-layer-index-id mode. */
	AO_UseLayerIndexId optionUseLayerIndexId = new AO_UseLayerIndexId(false, 'I', "use layer index and ID for inkscape layer processing, default is layer ID");

	/** Application option for not-transparent mode. */
	AO_NotTransparent optionNotTransparent = new AO_NotTransparent(false, 'n', "switch off transparency");

	/** Application option for not-text-as-shape mode. */
	AO_NotTextAsShape optionNotTextAsShape = new AO_NotTextAsShape(false, 's', "switch of text-as-shape property");

	/** Application option for clip mode. */
	AO_Clip optionClip = new AO_Clip(false, 'c', "activate clip property");

	/** Application option for background-color mode. */
	AO_BackgroundColor optionBackgroundColor = new AO_BackgroundColor(false, 'r', "sets the background color");

	/** Application option for no-background mode. */
	AO_NoBackground optionNoBackground = new AO_NoBackground(false, 'b', "switch off background property");

	/** Resource for conversion */
	protected final Resources resources = new Resources();

	/**
	 * Returns a new language application.
	 */
	public Svg2Vector_FH(){
		this.cli = new ExecS_CliParser();
		this.cli.addOption(this.optionVerbose);
		this.cli.addOption(this.optionTarget);
		this.cli.addOption(this.optionFileIn);
		this.cli.addOption(this.optionFileOut);
		this.cli.addOption(this.optionDirOut);
		this.cli.addOption(this.optionUriIn);

		this.cli.addOption(this.optionOnePerLayer);
		this.cli.addOption(this.optionUseLayerIndex);
		this.cli.addOption(this.optionUseLayerIndexId);
		this.cli.addOption(this.optionNotTransparent);
		this.cli.addOption(this.optionNotTextAsShape);
		this.cli.addOption(this.optionClip);
		this.cli.addOption(this.optionBackgroundColor);
		this.cli.addOption(this.optionNoBackground);
	}

	@Override
	public int executeApplication(String[] args) {
		// parse command line, exit with help screen if error
		int ret = ExecS_Application.super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		SVG converter = null;
		TargetProperties properties = null;
		switch(this.optionTarget.getValue()){
			case "pdf":
				converter = new Svg2Pdf();
				properties = new PdfProperties();
				break;
			case "emf":
				converter = new Svg2Emf();
				properties = new EmfProperties();
				break;
			case "svg":
				converter = new Svg2Svg();
				properties = new SvgProperties();
				break;
			default:
				System.err.println(this.getAppName() + ": " + "target required, see --help for details\n");
				return -1;
		}
		this.setCliProperties(properties);
		converter.setProperties(properties);
		return this.convert(properties, converter);
//		return 0;
	}

	/**
	 * Converts an SVG graphic to another vector format.
	 * @param properties conversion properties
	 * @param converter target converter
	 * @return -1 in case of error (messages printed on STDERR), 0 if successful
	 */
	public int convert(TargetProperties properties, SVG converter){
		String res = this.resources.generateURI(this.optionFileIn.getValue(), this.optionUriIn.getValue());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}
		if(this.resources.getBasename()==null || this.resources.getUri()==null){
			return -1;
		}

		res = this.resources.testOutputDir(this.optionDirOut.getValue());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}

		res = this.resources.testOutput(this.optionFileOut.getValue());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}

		this.printProgress("input URI=" + this.resources.getUri());
		this.printProgress("input file basename=" + this.resources.getBasename());
		this.printProgress("output directory=" + this.resources.getDirectory());
		this.printProgress("output file=" + this.resources.getOutput());

		converter.load(this.resources.getUri());

		if(this.optionVerbose.inCli()==true){
			UserProperties up = properties.getProperties();
			Set<Object> keys = up.keySet();
			Iterator<Object>it = keys.iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				String val = up.getProperty(key);
				key=key.substring(key.lastIndexOf('.')+1, key.length());
				this.printProgress("SVG property <" + key + ">=" + val);
			}
		}

		converter.convert(this.resources.getDirectory(), this.resources.getOutput());
		return 0;
	}

	/**
	 * Prints an error message with the application name.
	 * @param err error message
	 */
	public void printError(String err){
		System.err.println(this.getAppName() + ": " + err + "\n");
	}

	/**
	 * Prints progress of the conversion if tool is set to verbose.
	 * @param msg progress message
	 */
	public void printProgress(String msg){
		if(this.optionVerbose.inCli()==true && msg!=null){
			System.out.println(this.getAppName() + ": " + msg);
		}
	}

	/**
	 * Sets all command line arguments in the given property object.
	 * @param props object with properties to set
	 */
	protected void setCliProperties(TargetProperties props){
		//flags that require SVG to process things
		if(this.optionUseLayerIndex.inCli()){
			props.setUseInkscapeLayerName();
		}
		if(this.optionUseLayerIndexId.inCli()){
			props.setUseInkscapeLayerIndex();
		}
		if(this.optionOnePerLayer.inCli()){
			props.setUseOnePerInkscapeLayer();
		}

		//flags that are simply set in UserProperties
		if(this.optionNotTransparent.inCli()){
			props.setPropertyTransparent(false);
		}
		if(this.optionNotTextAsShape.inCli()){
			props.setPropertyTextAsShapes(false);
		}
		if(this.optionClip.inCli()){
			props.setPropertyClip(true);
		}
		if(this.optionNoBackground.inCli()){
			props.setPropertyBackground(false);
		}
		if(this.optionBackgroundColor.inCli()){
			Color color = Color.getColor(this.optionBackgroundColor.getValue());
			props.setPropertyBackgroundColor(color);
		}
	}

	@Override
	public ExecS_CliParser getCli() {
		return this.cli;
	}

	@Override
	public String getAppName() {
		return APP_NAME;
	}

	@Override
	public String getAppDisplayName(){
		return APP_DISPLAY_NAME;
	}

	@Override
	public String getAppDescription() {
		return "Converts SVG graphics into other vector formats using FreeHep libraries, with options for handling layers";
	}

	@Override
	public ApplicationOption<?>[] getAppOptions() {
		return new ApplicationOption<?>[]{
				this.optionVerbose,
				this.optionTarget,
				this.optionFileIn,
				this.optionFileOut,
				this.optionDirOut,
				this.optionUriIn,

				this.optionOnePerLayer,
				this.optionUseLayerIndex,
				this.optionUseLayerIndexId,
				this.optionNotTransparent,
				this.optionNotTextAsShape,
				this.optionClip,
				this.optionBackgroundColor,
				this.optionNoBackground,
			};
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}
}
