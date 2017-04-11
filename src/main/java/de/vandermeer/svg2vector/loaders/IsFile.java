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

package de.vandermeer.svg2vector.loaders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * Loads a file into a list trying GZIP first and  plain text next.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class IsFile {

	/** Lines read from file. */
	protected ArrayList<String> lines;

	/** Flag indicating if the file has Inkscape layers. */
	protected boolean hasLayers = false;

	/**
	 * Reads a file, trying GZIP compression first and plain file next.
	 * Also tests if the file has Inkscape layers.
	 * @param fn the file name (including path)
	 * @return null on success, error string on error
	 */
	public String read(String fn){
		try{
			InputStream decompressed = new GZIPInputStream(new FileInputStream(fn));
			BufferedReader in = new BufferedReader(new InputStreamReader(decompressed));
			this.lines = new ArrayList<>();
			String str;
			while((str=in.readLine()) != null){
				this.lines.add(str);
			}
			in.close();
			decompressed.close();
			return null;
		}
		catch(ZipException ignore){}
		catch(IOException e){
			this.lines = null;
			return "IO error reading GZIP file <" + fn + ">: " + e.getMessage();
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(fn));
			this.lines = new ArrayList<>();
			String str;
			while((str=in.readLine()) != null){
				this.lines.add(str);
			}
			in.close();
		}
		catch(FileNotFoundException e){
			this.lines = null;
			return "FileNotFoundException error reading plain file <" + fn + ">: " + e.getMessage();
		}
		catch(IOException e){
			this.lines = null;
			return "IO error reading plain file <" + fn + ">: " + e.getMessage();
		}

		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("id=\"layer")){
				this.hasLayers = true;
				break;
			}
		}
		return null;
	}

	/**
	 * Writes the line from the local array to a text file.
	 * @param fn the file name (including path)
	 * @return null on success, null on success, error string on error
	 */
	public String write(String fn){
		FileWriter writer;

		try {
			writer = new FileWriter(fn);
		}
		catch (IOException e) {
			return "IO error creating file writer: " + e.getMessage();
		} 

		try {
			for(String str: this.lines) {
				writer.write(str);
			}
			writer.close();
		}
		catch (IOException e) {
			return "IO error writing to file <" + fn + "> or closing writer: " + e.getMessage();
		}

		return null;
	}

	/**
	 * Returns the lines of the read file, null of none read.
	 * @return read lines
	 */
	public ArrayList<String> getLines(){
		return this.lines;
	}

	/**
	 * Switches off all Inkscape layers in the local list of lines.
	 */
	public void switchOffAllLayers(){
		boolean inLine = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("id=\"layer")){
				inLine = true;
			}
			if(inLine==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:inline", "display:none"));
				inLine = false;
			}
		}
	}

	/**
	 * Switches on a named (id) layer in the local list of lines.
	 * @param id the Inkscape identifier of the layer, nothing happens if layer was not found
	 */
	public void switchOnLayer(String id){
		boolean inLine = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("id=\"" + id + "\"")){
				inLine = true;
			}
			if(inLine==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:none", "display:inline"));
				inLine = false;
			}
		}
	}
}
