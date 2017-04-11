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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import de.vandermeer.svg2vector.applications.base.SV_DocumentLoader;

/**
 * Standard SVG document loader, tries GZIP first and plain text next.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class StandardLoader extends SV_DocumentLoader {

	/** Lines read from file. */
	protected ArrayList<String> lines;

	@Override
	public String load(String fn) {
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
			catch(ZipException ignore){}
			catch(IOException e){
				this.lines = null;
				return "IO error reading GZIP file <" + fn + ">: " + e.getMessage();
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
				return "FileNotFoundException error reading plain file <" + fn + ">: " + e.getMessage();
			}
			catch(IOException e){
				this.lines = null;
				return "IO error reading plain file <" + fn + ">: " + e.getMessage();
			}
		}

		int l = 0;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				l++;
			}
			if(l>1){
				this.hasLayers = true;
				break;
			}
		}

		return null;
	}

	@Override
	public void switchOnAllLayers() {
		boolean inLine = false;
		for(int i=0; i<this.lines.size(); i++){
			if(this.lines.get(i).contains("inkscape:groupmode=\"layer\"")){
				inLine = true;
			}
			if(inLine==true && this.lines.get(i).contains("style=\"display:")){
				this.lines.set(i, this.lines.get(i).replace("display:none", "display:inline"));
				inLine = false;
			}
		}
	}

}
