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

package de.vandermeer.skb.svg2vector;

import java.util.Iterator;
import java.util.Set;

import org.freehep.graphicsbase.util.UserProperties;

import de.vandermeer.skb.svg2vector.base.Cli;
import de.vandermeer.skb.svg2vector.base.Resources;
import de.vandermeer.skb.svg2vector.base.SVG;
import de.vandermeer.skb.svg2vector.base.TargetProperties;
import de.vandermeer.skb.svg2vector.converters.EmfProperties;
import de.vandermeer.skb.svg2vector.converters.PdfProperties;
import de.vandermeer.skb.svg2vector.converters.Svg2Emf;
import de.vandermeer.skb.svg2vector.converters.Svg2Pdf;
import de.vandermeer.skb.svg2vector.converters.Svg2Svg;
import de.vandermeer.skb.svg2vector.converters.SvgProperties;

/**
 * SVG to vector converter.
 * This tool converts an SVG graphic to a vector format. Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats. It also can deal with SVG layers. All options
 * can be set via command line.
 * 
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.1 build 140626 (26-Jun-14) with Java 1.8
 */
public class Tool {

	/** Command line parser */
	private Cli cli;

	/** Application name for logs and print outs */
	private String appName;

	/** Resource for conversion */
	private Resources resources;

	/**
	 * Main routine to start tool.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Tool tool = new Tool();
		System.exit(tool.execute(args));
	}

	/**
	 * Standard constructor.
	 */
	public Tool(){
		this.appName = "svg2vector";
		this.cli = new Cli(this.appName);
		this.resources = new Resources();
	}

	/**
	 * Executes the conversion process.
	 * @param args command line arguments
	 * @return -1 in case of error (messages printed on STDERR), 0 if successful
	 */
	public int execute(String[] args){
		try{
			this.cli.parse(args);
		}
		catch(Exception ignore){
			this.printError(ignore.getMessage());
			this.cli.usage();
			System.out.println("\n");
			return -1;
		}

		int ret=this.cli.parseStandardOptions();
		if(ret==-1 || ret==0){
			return ret;
		}

		SVG converter = null;
		TargetProperties properties = null;
		switch(cli.getTarget()){
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
				System.err.println(this.appName+": " + "target required, see -h for details\n");
				return -1;
		}
		this.cli.setProperties(properties);
		converter.setProperties(properties);
		return this.convert(properties, converter);
	}

	/**
	 * Converts an SVG graphic to another vector format.
	 * @param properties conversion properties
	 * @param converter target converter
	 * @return -1 in case of error (messages printed on STDERR), 0 if successful
	 */
	public int convert(TargetProperties properties, SVG converter){
		String res = this.resources.generateURI(this.cli.getFile(), this.cli.getURI());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}
		if(this.resources.getBasename()==null || this.resources.getUri()==null){
			return -1;
		}

		res = this.resources.testOutputDir(this.cli.getDirectory());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}

		res = this.resources.testOutput(this.cli.getOutput());
		if(!"".equals(res)){
			this.printError(res);
			return -1;
		}

		this.printProgress("input URI=" + this.resources.getUri());
		this.printProgress("input file basename=" + this.resources.getBasename());
		this.printProgress("output directory=" + this.resources.getDirectory());
		this.printProgress("output file=" + this.resources.getOutput());

		converter.load(this.resources.getUri());

		if(this.cli.isVerbose()==true){
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
		System.err.println(this.appName + ": " + err + "\n");
	}

	/**
	 * Prints progress of the conversion if tool is set to verbose.
	 * @param msg progress message
	 */
	public void printProgress(String msg){
		if(this.cli.isVerbose()==true && msg!=null){
			System.out.println(this.appName + ": " + msg);
		}
	}
}
