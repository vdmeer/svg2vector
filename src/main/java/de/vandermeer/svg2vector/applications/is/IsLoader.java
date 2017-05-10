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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messages.errors.Templates_InputFile;
import de.vandermeer.svg2vector.applications.core.SV_DocumentLoader;

/**
 * Standard SVG document loader, tries GZIP first and plain text next.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class IsLoader extends SV_DocumentLoader {

	/** Lines read from file. */
	protected ArrayList<String> lines;

	@Override
	public void load(String fn) throws ApplicationException {
		Validate.notBlank(fn);

		if(!this.isLoaded){
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
				this.isLoaded = true;
			}
			catch(ZipException exZip){
				new ApplicationException(Templates_InputFile.ZIP_EXCEPTION_READING, this.getClass().getSimpleName(), "input", fn, exZip.getMessage());
			}
			catch(IOException exIO){
				this.lines = null;
				throw new ApplicationException(Templates_InputFile.IO_EXCEPTION_READING, this.getClass().getSimpleName(), "input", fn, exIO.getMessage());
			}
		}

		if(!this.isLoaded){
			try {
				BufferedReader in = new BufferedReader(new FileReader(fn));
				this.lines = new ArrayList<>();
				String str;
				while((str=in.readLine()) != null){
					this.lines.add(str);
				}
				in.close();
				this.isLoaded = true;
			}
			catch(FileNotFoundException e){
				this.lines = null;
				throw new ApplicationException(Templates_InputFile.FILE_NOT_FOUND, this.getClass().getSimpleName(), "plain SVG input", fn, e.getMessage());
			}
			catch(IOException e){
				this.lines = null;
				throw new ApplicationException(Templates_InputFile.IO_EXCEPTION_READING, this.getClass().getSimpleName(), "plain SVG input", fn, e.getMessage());
			}
		}

		boolean inLayer = false;
		String id = null;
		String index = null;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				inLayer = true;
			}
			if(inLayer==true && this.lines.get(i).contains("id=\"layer")){
				index = StringUtils.substringBetween(this.lines.get(i), "\"");
				index = StringUtils.substringAfter(index, "layer");
			}
			if(inLayer==true && this.lines.get(i).contains("inkscape:label=\"")){
				id = StringUtils.substringBetween(this.lines.get(i), "\"");
			}
			if(id!=null && index!=null){
				this.layers.put(id, new Integer(index));
				inLayer = false;
				id = null;
				index = null;
			}
		}
	}

	@Override
	public void switchOnAllLayers() {
		boolean inLayer = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				inLayer = true;
			}
			if(inLayer==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:none", "display:inline"));
				inLayer = false;
			}
		}
	}

	@Override
	public void switchOffAllLayers() {
		boolean inLayer = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				inLayer = true;
			}
			if(inLayer==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:inline", "display:none"));
				inLayer = false;
			}
		}
	}

	@Override
	public void switchOnLayer(String layer) {
		if(StringUtils.isBlank(layer)){
			return;
		}
		if(!this.getLayers().keySet().contains(layer)){
			return;
		}

		boolean inLayer = false;
		boolean foundLayer = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				inLayer = true;
			}
			if(inLayer==true && this.lines.get(i).contains("inkscape:label=\"")){
				if(layer.equals(StringUtils.substringBetween(this.lines.get(i), "\""))){
					foundLayer = true;
				}
			}
			if(inLayer==true && foundLayer==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:none", "display:inline"));
				inLayer = false;
				foundLayer = false;
			}
		}
	}

	/**
	 * Returns the current list of lines.
	 * @return current list of lines, empty if none added
	 */
	public ArrayList<String> getLines(){
		return this.lines;
	}
}
