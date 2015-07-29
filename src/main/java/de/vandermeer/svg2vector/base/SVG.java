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

import java.awt.Dimension;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * SVG converter framework.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
public abstract class SVG {
	/** Height value */
	protected Double height;

	/** Width value */
	protected Double width;

	/** Size value */
	protected Dimension size;

	/** Local bridge context */
	protected BridgeContext bridgeContext;

	/** SVG document object */
	protected Document svgDocument;

	/** List of SVG nodes in document */
	protected NodeList svgNodeList;

	/** Properties for the conversion */
	protected TargetProperties properties;

	/**
	 * Constructor.
	 */
	public SVG(){
		this.init();
	}

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
		if(this.bridgeContext==null){
			return false;
		}
		if(this.svgDocument==null){
			return false;
		}
		if(this.svgNodeList==null){
			return false;
		}
		if(this.properties==null || this.properties.getProperties()==null){
			return false;
		}
		return true;
	}

	/**
	 * Returns the Inkscape or SVG layer name for given node
	 * @param node XML/SVG node
	 * @return "layer" on default, actual name if found
	 */
	public String getInkscapeLayerName(Node node){
		String ret = "layer";

		if(node==null){
			return ret;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("inkscape:label".equals(nnm.item(i).getNodeName())){
				return nnm.item(i).getNodeValue();
			}
		}
		return ret;
		
	}

	/**
	 * Returns all SVG layer names for the given node list
	 * @param nodelist list of XML/SVG nodes
	 * @return list of SVG layer names
	 */
	public List<Node> getInkscapeLayers(NodeList nodelist){
		ArrayList<Node> ret = new ArrayList<Node>();

		if(this.documentLoaded()!=false){
			if(nodelist==null){
				nodelist = this.svgNodeList;
			}

			for(int i=0; i<nodelist.getLength(); i++){
				if("g".equals(nodelist.item(i).getNodeName())){
					NamedNodeMap nnm = nodelist.item(i).getAttributes();
					for(int node=0; node<nnm.getLength(); node++){
						if("inkscape:groupmode".equals(nnm.item(node).getNodeName())){
							ret.add(nodelist.item(i));
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Initialisation routine.
	 */
	private void init(){
		this.height = 0.0;
		this.width = 0.0;
		this.bridgeContext = null;
		this.svgDocument = null;
		this.svgNodeList = null;
	}

	/**
	 * Loads an SVG document from URI.
	 * @param uri URI pointing to document
	 * @return false on error, true on success
	 */
	public boolean load(URI uri){
		this.bridgeContext = null;
		this.svgDocument = null;

		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader documentLoader = new DocumentLoader(userAgent);

		this.bridgeContext = new BridgeContext(userAgent, documentLoader);
		this.bridgeContext.setDynamic(true);

		try{
			this.svgDocument = documentLoader.loadDocument(uri.toString());
		}
		catch(Exception ignore){
			System.err.println(ignore);
			this.init();
			return false;
		}
		documentLoader.dispose();

		Element elem = this.svgDocument.getDocumentElement();
		this.svgNodeList = elem.getChildNodes();

		try{
			this.width = Double.valueOf(elem.getAttribute("width"));
			this.height = Double.valueOf(elem.getAttribute("height"));
			this.size = new Dimension();
			this.size.setSize(this.width, this.height);
		}
		catch(Exception ignore){
			this.init();
			return false;
		}

		return true;
	}

	/**
	 * Prints all node attributes for a list of nodes.
	 * @param nodes list of XML/SVG nodes
	 */
	public void printNodeAttributes(List<Node> nodes){
		if(nodes!=null){
			for(int i=0; i<nodes.size(); i++){
				this.printNodeAttributes(nodes.get(i));
			}
		}
	}

	/**
	 * Prints all attributes for a single node.
	 * @param node XML/SVG node
	 */
	public void printNodeAttributes(Node node){
		if(node!=null){
			System.err.println(node.getNodeName());
			NamedNodeMap nnm = node.getAttributes();
			for(int i=0; i<nnm.getLength(); i++){
				System.err.println("  -> " + nnm.item(i).getNodeName() + "==" + nnm.item(i).getNodeValue());
			}
			System.err.println();
		}
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
	 * Converts the loaded SVG document to a vector target format
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
	 * Converts a single layer (or all active layers) of an SVG document to a vector target format
	 * @param directory output directory
	 * @param filename output filename
	 * @return false on error, true on success
	 */
	public abstract boolean convertSingleLayer(String directory, String filename);

	/**
	 * Converts a multiple layers of an SVG document to a vector target format, switching only 1 layer on per conversion.
	 * @param directory output directory
	 * @param basename output basename
	 * @return false on error, true on success
	 */
	public boolean convertMultiLayer(String directory, String basename){
		if(this.documentLoaded()==false){
			return false;
		}

		List<Node> layers = this.getInkscapeLayers(null);
		this.switchOffAllInkscapeLayers(layers);
		for(int i=0; i<layers.size(); i++){
			this.switchOnInkscapeLayer(layers.get(i));
			this.convertSingleLayer(directory, this.getFilenameForLayer(basename, this.getInkscapeLayerName(layers.get(i)), i));
			this.switchOffInkscapeLayer(layers.get(i));
		}
		return true;
	}

	/**
	 * Returns an output filename.
	 * @param basename input file basename
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
