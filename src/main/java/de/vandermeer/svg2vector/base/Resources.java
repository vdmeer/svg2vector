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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Manages input and output resources for the converter.
 * 
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
public class Resources {

	/** Input URI */
	private URI uri;

	/** Basename of output file */
	private String basename;

	/** Output directory */
	private String outputDirectory;

	/** Output file */
	private String output;

	/** Constructor, initialises a resource */
	public Resources(){}

	/**
	 * Generates a URI from either a given filename or a given URI.
	 * @param cliFile filename to use as default
	 * @param cliUri UTI to use if filename did not work
	 * @return empty string on success, error message otherwise
	 */
	public String generateURI(String cliFile, String cliUri){
		if(cliFile!=null){
			File testFD = new File(cliFile);
			if(!testFD.exists()){
				return "input file <" + cliFile + "> does not exist, please check path and filename";
			}
			if(!testFD.isFile()){
				return "give input file <" + cliFile + "> is not a file, please check path and filename";
			}
			if(!testFD.canRead()){
				return "cannot read input file <" + cliFile + ">, please check path and file permissions";
			}
			this.basename = testFD.getName();
			this.basename = this.basename.substring(0, this.basename.indexOf('.'));
			this.uri = testFD.toURI();
		}
		else if(cliUri!=null){
			try {
				this.uri = new URI(cliUri);
			}
			catch (URISyntaxException e) {
				return e.getMessage();
			}
		}
		else{
			return "no file or URI name given, try -h or --help for usage information";
		}
		return "";
	}

	/**
	 * Tests the output directory.
	 * @param directory directory to test
	 * @return empty string on success, error message otherwise
	 */
	public String testOutputDir(String directory){
		this.outputDirectory = directory;

		File testDir = new File(this.outputDirectory);
		if(!testDir.exists()){
			return "output directory <" + this.outputDirectory + "> does not exist, please check path";
		}
		if(!testDir.isDirectory()){
			return "give output directory <" + this.outputDirectory + "> is not a directory, please check path";
		}
		if(!testDir.canWrite()){
			return "cannot write into output directory <" + this.outputDirectory + ">, please check permissions";
		}

		return "";
	}

	/**
	 * Tests the output filename
	 * @param output filename to test
	 * @return empty string on success, error message otherwise
	 */
	public String testOutput(String output){
		this.output = output;
		if(this.output==null){
			this.output = this.basename;
		}
		return "";
	}

	/**
	 * Returns the basename of the input file
	 * @return basename
	 */
	public String getBasename(){
		return this.basename;
	}

	/**
	 * Returns a URI (which must be generated prior)
	 * @return URI, null if not generated or an error occured during generation
	 */
	public URI getUri(){
		return this.uri;
	}

	/**
	 * Returns the output directory.
	 * @return output directory
	 */
	public String getDirectory(){
		return this.outputDirectory;
	}

	/**
	 * Returns the output filename
	 * @return output filename
	 */
	public String getOutput(){
		return this.output;
	}
}
