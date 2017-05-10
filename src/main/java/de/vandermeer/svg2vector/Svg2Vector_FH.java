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

package de.vandermeer.svg2vector;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.freehep.graphicsbase.util.UserProperties;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messages.errors.Templates_ExceptionRuntimeUnexpected;
import de.vandermeer.svg2vector.applications.base.AppBase;
import de.vandermeer.svg2vector.applications.core.SvgTargets;
import de.vandermeer.svg2vector.applications.fh.AO_BackgroundColor;
import de.vandermeer.svg2vector.applications.fh.AO_NoBackground;
import de.vandermeer.svg2vector.applications.fh.AO_NotTransparent;
import de.vandermeer.svg2vector.applications.fh.converters.BatikLoader;
import de.vandermeer.svg2vector.applications.fh.converters.FhConverter;
import de.vandermeer.svg2vector.applications.fh.converters.Fh_Svg2Emf;
import de.vandermeer.svg2vector.applications.fh.converters.Fh_Svg2Pdf;
import de.vandermeer.svg2vector.applications.fh.converters.Fh_Svg2Svg;

/**
 * The Svg2Vector application using the FreeHep library.
 * It an SVG graphic to a vector format.
 * Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * It also can deal with SVG layers.
 * All options can be set via command line.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Svg2Vector_FH extends AppBase<BatikLoader> {

	/** Application name. */
	public final static String APP_NAME = "s2v-fh";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector FreeHep";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8";

	/**
	 * Returns a converter for a given target
	 * @param target the target, can be null
	 * @return null if target was null or no converter found, a new converter object for the target otherwise
	 */
	public static FhConverter TARGET_2_CONVERTER(SvgTargets target){
		if(target==null){
			return null;
		}
		switch(target){
			case eps:
			case png:
			case ps:
			case wmf:
				break;

			case pdf:
				return new Fh_Svg2Pdf();
			case svg:
				return new Fh_Svg2Svg();
			case emf:
				return new Fh_Svg2Emf();
		}
		return null;
	}

	/** Application option for not-transparent mode. */
	AO_NotTransparent optionNotTransparent = new AO_NotTransparent();

	/** Application option for background-color mode. */
	AO_BackgroundColor optionBackgroundColor = new AO_BackgroundColor();

	/** Application option for no-background mode. */
	AO_NoBackground optionNoBackground = new AO_NoBackground();

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_FH(){
		super(APP_NAME, new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf, SvgTargets.svg}, new BatikLoader());

		this.addOption(this.optionNotTransparent);
		this.addOption(this.optionBackgroundColor);
		this.addOption(this.optionNoBackground);
	}

	@Override
	public String getAppDescription() {
		return "Converts SVG graphics into other vector formats using FreeHep libraries, with options for handling layers";
	}

	@Override
	public String getAppDisplayName(){
		return APP_DISPLAY_NAME;
	}

	@Override
	public String getAppName() {
		return APP_NAME;
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}

	@Override
	public void runApplication() {
		try{
			this.init();

			SvgTargets target = this.getTarget();

			FhConverter converter = TARGET_2_CONVERTER(target);
			if(converter==null){
				this.printErrorMessage("no converter found for target <" + target.name() + ">");
				this.errNo = -20;//TODO
			}

			converter.setPropertyTransparent(!this.optionNotTransparent.inCli());
			converter.setPropertyBackground(!this.optionNoBackground.inCli());
			converter.setPropertyTextAsShapes(this.getConversionOptions().doesTextAsShape());
			if(this.optionBackgroundColor.inCli()){
				Color color = Color.getColor(this.optionBackgroundColor.getValue());
				converter.setPropertyBackgroundColor(color);
			}

			UserProperties up = converter.getProperties();
			Set<Object> keys = up.keySet();
			Iterator<Object>it = keys.iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				String val = up.getProperty(key);
				key=key.substring(key.lastIndexOf('.')+1, key.length());
				this.printDetailMessage("using SVG property " + key + "=" + val);
			}

			BatikLoader loader = this.getLoader();
			if(this.doesLayers()){
				int index = 1;
				for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
					loader.switchOffAllLayers();
					loader.switchOnLayer(entry.getKey());
					this.printProgressMessage("processing layer " + entry.getKey());
					String fout = this.fopOO(index, entry);
					this.printDetailMessage("writing to file " + fout);
					if(!this.getMiscOptions().doesSimulate()){
						converter.convertDocument(loader, new File(fout));
					}
					index++;
				}
			}
			else{
				this.printProgressMessage("converting input");
				String fout = this.fopOO();
				this.printDetailMessage("writing to file " + fout);
				if(!this.getMiscOptions().doesSimulate()){
					converter.convertDocument(loader, new File(fout));
				}
			}

			this.printProgressMessage("finished successfully");
		}
		catch(ApplicationException s2vEx){
			this.printErrorMessage(s2vEx);
			this.errNo = s2vEx.getErrorCode();
		}
		catch(NullPointerException npEx){
			this.printErrorMessage(npEx);
			this.errNo = Templates_ExceptionRuntimeUnexpected.U_NULL_POINTER.getCode();
		}
		catch(IOException ioEx){
			this.printErrorMessage(ioEx);
			this.errNo = Templates_ExceptionRuntimeUnexpected.U_IO.getCode();
		}
	}
}
