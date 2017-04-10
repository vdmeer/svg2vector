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

package de.vandermeer.svg2vector.converters;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.vandermeer.svg2vector.loaders.BatikLoader;
import de.vandermeer.svg2vector.loaders.SvgDocumentLoader;

/**
 * SVG converter framework.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.0.0
 */
public abstract class Svg extends BatikLoader {

	/** Properties for the conversion. */
	protected TargetProperties properties;

	/**
	 * Set the target properties to use.
	 * @param properties target properties
	 */
	public void setProperties(TargetProperties properties){
		this.properties = properties;
	}

	/**
	 * Checks if SVG document is loaded.
	 * @return true on success, false otherwise
	 */
	public boolean documentLoaded(){
		if(super.documentLoaded()==false){
			return false;
		}
		if(this.properties==null || this.properties.getProperties()==null){
			return false;
		}
		return true;
	}

	/**
	 * Switches off SVG layers.
	 * @param layers list of layers to switch off
	 * @return false on error (empty list), true on success
	 */
	public boolean switchOffAllInkscapeLayers(List<Node> layers){
		if(layers==null){
			return false;
		}

		for(int i=0; i<layers.size(); i++){
			this.switchOffInkscapeLayer(layers.get(i));
		}
		return true;
	}

	/**
	 * Switches off a single SVG layer.
	 * @param node XML/SVG node
	 * @return false on error (empty node), true on success
	 */
	public boolean switchOffInkscapeLayer(Node node){
		if(node==null){
			return false;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("style".equals(nnm.item(i).getNodeName())){
				nnm.item(i).setNodeValue("display:none");
				return true;
			}
		}
		return false;
	}

	/**
	 * Switches on SVG layers.
	 * @param layers list of layers to switch on
	 * @return false on error (empty list), true on success
	 */
	public boolean switchOnAllInkscapeLayers(List<Node> layers){
		if(layers==null){
			return false;
		}

		for(int i=0; i<layers.size(); i++){
			this.switchOnInkscapeLayer(layers.get(i));
		}
		return true;
	}

	/**
	 * Switches on a single SVG layer.
	 * @param node XML/SVG node
	 * @return false on error (empty node), true on success
	 */
	public boolean switchOnInkscapeLayer(Node node){
		if(node==null){
			return false;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("style".equals(nnm.item(i).getNodeName())){
				nnm.item(i).setNodeValue("display:inline");
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts the loaded SVG document to a vector target format.
	 * @param directory output directory
	 * @param output output filename
	 * @return false on error, true on success
	 */
	public boolean convert(String directory, String output){
		if(this.properties.useOnePerInkscapeLayer()){
			return this.convertMultiLayer(directory, output);
		}
		else{
			return this.convertSingleLayer(directory, output);
		}
	}

	/**
	 * Converts a single layer (or all active layers) of an SVG document to a vector target format.
	 * @param directory output directory
	 * @param filename output filename
	 * @return false on error, true on success
	 */
	public abstract boolean convertSingleLayer(String directory, String filename);

	/**
	 * Converts a multiple layers of an SVG document to a vector target format, switching only 1 layer on per conversion.
	 * @param directory output directory
	 * @param basename output base name
	 * @return false on error, true on success
	 */
	public boolean convertMultiLayer(String directory, String basename){
		if(this.documentLoaded()==false){
			return false;
		}

		List<Node> layers = this.getInkscapeLayers();
		this.switchOffAllInkscapeLayers(layers);
		for(int i=0; i<layers.size(); i++){
			this.switchOnInkscapeLayer(layers.get(i));
			this.convertSingleLayer(directory, this.getFilenameForLayer(basename, SvgDocumentLoader.getInkscapeLabel(layers.get(i)), i));
			this.switchOffInkscapeLayer(layers.get(i));
		}
		return true;
	}

	/**
	 * Returns an output filename.
	 * @param basename input file base name
	 * @param layerName name of an SVG layer
	 * @param layerIndex index of an SVG layer
	 * @return output filename
	 */
	public String getFilenameForLayer(String basename, String layerName, int layerIndex){
		String ret;
		if(this.properties.useInkscapeLayerIndex()==true && this.properties.useInkscapeLayerName()==true){
			ret = String.format("%s-%02d", basename, layerIndex) + '-' + layerName;
		}
		else if(this.properties.useInkscapeLayerIndex==false && this.properties.useInkscapeLayerName()==false){
			ret = basename + '-' + layerName;
		}
		else if(this.properties.useInkscapeLayerIndex()==true && this.properties.useInkscapeLayerName()==false){
			ret = String.format("%s-%02d", basename, layerIndex);
		}
		else if(this.properties.useInkscapeLayerIndex()==false && this.properties.useInkscapeLayerName()==true){
			ret = basename + '-' + layerName;
		}
		else{
			return "default";//TODO default return
		}
		return ret;
	}
}
