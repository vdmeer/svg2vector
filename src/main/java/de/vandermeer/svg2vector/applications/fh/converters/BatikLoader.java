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

package de.vandermeer.svg2vector.applications.fh.converters;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_ExceptionRuntimeUnexpected;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_InputFile;
import de.vandermeer.svg2vector.applications.core.SV_DocumentLoader;

/**
 * Loads an SVG document using Batik and provides some methods to deal with layers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class BatikLoader extends SV_DocumentLoader {

	/** Local bridge context. */
	private BridgeContext bridgeContext;

	/** SVG document object. */
	private Document svgDocument;

	/** Size value. */
	private Dimension size;

	/** Mapping from node id to actual DOM node. */
	private final Map<String, Node> layerNodes = new HashMap<>();

	/**
	 * Returns the Inkscape label for a given node.
	 * @param node XML/SVG node
	 * @return null if node was null or no Inkscape label found, label otherwise
	 */
	public static String getLabel(final Node node){
		if(node==null){
			return null;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("inkscape:label".equals(nnm.item(i).getNodeName())){
				return nnm.item(i).getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Returns the Inkscape index (actual id with layer removed) for a given node.
	 * @param node XML/SVG node
	 * @return 0 if node was null or no IDs found, index otherwise
	 */
	public static int getIndex(final Node node){
		if(node==null){
			return 0;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("id".equals(nnm.item(i).getNodeName())){
				String index = nnm.item(i).getNodeValue();
				index = StringUtils.substringAfter(index, "layer");
				return new Integer(index);
			}
		}
		return 0;
	}

	@Override
	public void load(String fn) throws ApplicationException {
		Validate.notBlank(fn);

		if(!this.isLoaded){
			this.bridgeContext = null;
			this.svgDocument = null;

			UserAgent userAgent = new UserAgentAdapter();
			DocumentLoader documentLoader = new DocumentLoader(userAgent);

			this.bridgeContext = new BridgeContext(userAgent, documentLoader);
			this.bridgeContext.setDynamic(true);

			try{
				this.svgDocument = documentLoader.loadDocument(new File(fn).toURI().toString());
			}
			catch(Exception ex){
				this.bridgeContext = null;
				this.svgDocument = null;
				throw new ApplicationException(
						Templates_InputFile.EXCEPTION_LOADING,
						this.getClass().getSimpleName(),
						"SVG",
						fn,
						ex.getMessage());
			}
			documentLoader.dispose();

			Element elem = this.svgDocument.getDocumentElement();
			this.size = new Dimension();
			try{
				this.size.setSize(Double.valueOf(elem.getAttribute("width")), Double.valueOf(elem.getAttribute("height")));
			}
			catch(Exception ex){
				this.bridgeContext = null;
				this.svgDocument = null;
				this.size = null;
				throw new ApplicationException(
						Templates_ExceptionRuntimeUnexpected.U_EXCEPTION,
						this.getClass().getSimpleName(),
						"load",
						"loading SVG document",
						ex.getMessage()
				);
			}

			NodeList nodes = elem.getChildNodes();
			if(nodes!=null){
				for(int i=0; i<nodes.getLength(); i++){
					if("g".equals(nodes.item(i).getNodeName())){
						NamedNodeMap nnm = nodes.item(i).getAttributes();
						for(int node=0; node<nnm.getLength(); node++){
							if("inkscape:groupmode".equals(nnm.item(node).getNodeName())){
								String id = BatikLoader.getLabel(nodes.item(i));
								int index = BatikLoader.getIndex(nodes.item(i));
								this.layers.put(id, index);
								this.layerNodes.put(id, nodes.item(i));
							}
						}
					}
				}
			}
		}

		this.isLoaded = true;
	}

	@Override
	public void switchOnAllLayers() {
		for(Node node : this.layerNodes.values()){
			NamedNodeMap nnm = node.getAttributes();
			for(int i=0; i<nnm.getLength(); i++){
				if("style".equals(nnm.item(i).getNodeName())){
					nnm.item(i).setNodeValue("display:inline");
					break;
				}
			}
		}
	}

	@Override
	public void switchOffAllLayers() {
		for(Node node : this.layerNodes.values()){
			NamedNodeMap nnm = node.getAttributes();
			for(int i=0; i<nnm.getLength(); i++){
				if("style".equals(nnm.item(i).getNodeName())){
					nnm.item(i).setNodeValue("display:none");
					break;
				}
			}
		}
	}

	@Override
	public void switchOnLayer(final String layer) {
		if(StringUtils.isBlank(layer)){
			return;
		}

		Node node = this.layerNodes.get(layer);
		if(node==null){
			return;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("style".equals(nnm.item(i).getNodeName())){
				nnm.item(i).setNodeValue("display:inline");
				return;
			}
		}
	}

	/**
	 * Returns the loader's document.
	 * @return loaded document, null if none loaded
	 */
	public Document getDocument() {
		return this.svgDocument;
	}

	/**
	 * Returns the loader's bridge context.
	 * @return bridge context, null if no document loaded
	 */
	public BridgeContext getBridgeContext(){
		return this.bridgeContext;
	}

	/**
	 * Returns the document size.
	 * @return document size, null if no document loaded
	 */
	public Dimension getSize(){
		return this.size;
	}


}
