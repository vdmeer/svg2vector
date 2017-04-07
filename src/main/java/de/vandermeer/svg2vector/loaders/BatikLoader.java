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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Loads an SVG document using Batik and provides some methods to deal with layers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public class BatikLoader implements SvgDocumentLoader {

	/** Local bridge context. */
	protected BridgeContext bridgeContext;

	/** SVG document object. */
	protected Document svgDocument;

	/** List of SVG nodes in document. */
	protected NodeList svgNodeList;

	/** Height value. */
	protected Double height = 0.0;

	/** Width value. */
	protected Double width = 0.0;

	/** Size value. */
	protected Dimension size;

	/**
	 * Checks if an SVG document is loaded.
	 * @return true on success, false otherwise
	 */
	@Override
	public boolean documentLoaded(){
		return SvgDocumentLoader.super.documentLoaded() && (this.bridgeContext!=null);
	}

	@Override
	public Document getDocument() {
		return this.svgDocument;
	}

	/**
	 * Returns all SVG layer nodes for the loaded document.
	 * @return list of SVG layer nodes calculated from loaded document if loaded, empty otherwise
	 */
	public List<Node> getInkscapeLayers(){
		if(this.documentLoaded()!=false){
			return SvgDocumentLoader.getInkscapeLayers(this.svgNodeList);
		}
		return new ArrayList<Node>();
	}

	@Override
	public NodeList getNodeList() {
		return this.svgNodeList;
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
			this.bridgeContext = null;
			this.svgDocument = null;
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
			this.bridgeContext = null;
			this.svgDocument = null;
			this.height = 0.0;
			this.width = 0.0;
			return false;
		}

		return true;
	}
}
