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

import de.vandermeer.svg2vector.applications.options.AO_BackgroundColor;
import de.vandermeer.svg2vector.applications.options.AO_Clip;
import de.vandermeer.svg2vector.applications.options.AO_NoBackground;
import de.vandermeer.svg2vector.applications.options.AO_NotTextAsShape;
import de.vandermeer.svg2vector.applications.options.AO_NotTransparent;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndex;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndexId;
import de.vandermeer.svg2vector.converters.Svg;
import de.vandermeer.svg2vector.converters.SvgTargets;
import de.vandermeer.svg2vector.converters.TargetProperties;

/**
 * The Svg2Vector application using the FreeHep library.
 * It an SVG graphic to a vector format.
 * Currently supported are EMF, PDF and SVG.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * It also can deal with SVG layers.
 * All options can be set via command line.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Svg2Vector_FH extends AppBase {

	/** Application name. */
	public final static String APP_NAME = "s2v-hp";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector FreeHep";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8";

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

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_FH(){
		super(new SvgTargets[]{SvgTargets.pdf, SvgTargets.emf, SvgTargets.svg});

		this.addOption(this.optionUseLayerIndex);
		this.addOption(this.optionUseLayerIndexId);
		this.addOption(this.optionNotTransparent);
		this.addOption(this.optionNotTextAsShape);
		this.addOption(this.optionClip);
		this.addOption(this.optionBackgroundColor);
		this.addOption(this.optionNoBackground);
	}

	@Override
	public int executeApplication(String[] args) {
		// parse command line, exit with help screen if error
		int ret = super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		SvgTargets target = this.optionTarget.getTarget();
		Svg converter = target.getConverter();
		if(converter==null){
			this.printError("no converter found for target <" + this.optionTarget.getValue() + ">");
			return -11;
		}

		TargetProperties properties = target.getTargetProperties();
		if(properties==null){
			this.printError("no target properties found for target <" + this.optionTarget.getValue() + ">");
			return -12;
		}

		this.setCliProperties(properties);
		converter.setProperties(properties);
		return this.convert(properties, converter);
	}

	/**
	 * Converts an SVG graphic to another vector format.
	 * @param properties conversion properties
	 * @param converter target converter
	 * @return -1 in case of error (messages printed on STDERR), 0 if successful
	 */
	public int convert(TargetProperties properties, Svg converter){
		this.printProgress("input URI:        " + this.optionUriIn.getURI());
		this.printProgress("output directory: " + this.optionDirOut.getValue());
		this.printProgress("output file:      " + this.optionFileOut.getValue());

		converter.load(this.optionUriIn.getURI());

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

		converter.convert(this.optionDirOut.getValue(), this.optionFileOut.getValue());
		return 0;
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
	public String getAppVersion() {
		return APP_VERSION;
	}
}
