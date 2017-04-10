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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Loads an SVG document and provides some methods to deal with layers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public interface SvgDocumentLoader {

	/**
	 * Returns the Inkscape label for a given node.
	 * @param node XML/SVG node
	 * @return null if node was null or no Inkscape label found, label otherwise
	 */
	static String getInkscapeLabel(Node node){
		String ret = null;

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
	 * Returns the Inkscape ID for a given node.
	 * @param node XML/SVG node
	 * @return null if node was null or no IDs found, ID otherwise
	 */
	static String getID(Node node){
		String ret = null;

		if(node==null){
			return ret;
		}

		NamedNodeMap nnm = node.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			if("id".equals(nnm.item(i).getNodeName())){
				return nnm.item(i).getNodeValue();
			}
		}
		return ret;
	}

	/**
	 * Returns all SVG layer nodes for the given node list.
	 * @param nodelist list of XML/SVG nodes, must not be null
	 * @return list of SVG layer nodes
	 * @throws NullPointerException if argument was null
	 */
	static List<Node> getInkscapeLayers(NodeList nodelist){
		Validate.notNull(nodelist);
		ArrayList<Node> ret = new ArrayList<Node>();

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
		return ret;
	}

//	/**
//	 * Returns a list of all nodes with their attributes.
//	 * @param nodes list of XML/SVG nodes
//	 * @return a list of all nodes with their attributes, empty if input list was null or of length 0
//	 */
//	static ArrayList<StrBuilder> getNodeAttributes(List<Node> nodes){
//		ArrayList<StrBuilder> ret = new ArrayList<>();
//		if(nodes!=null){
//			for(Node node : nodes){
//				ret.addAll(SvgDocumentLoader.getNodeAttributes(node));
//				ret.add(new StrBuilder().appendNewLine());
//			}
//		}
//		return ret;
//	}

//	/**
//	 * Returns a list with all attributes of a given node.
//	 * @param node the XML/SVG node to get attributes from
//	 * @return a list with all attributes, empty of node was null or had no attributes
//	 */
//	static ArrayList<StrBuilder> getNodeAttributes(Node node){
//		ArrayList<StrBuilder> ret = new ArrayList<>();
//		if(node!=null){
//			NamedNodeMap nnm = node.getAttributes();
//			for(int i=0; i<nnm.getLength(); i++){
//				ret.add(new StrBuilder()
//					.append("  -> ")
//					.append(nnm.item(i).getNodeName())
//					.append("==")
//					.append(nnm.item(i).getNodeValue())
//				);
//			}
//		}
//		return ret;
//	}

	/**
	 * Checks if an SVG document is loaded.
	 * @return true on success, false otherwise
	 */
	default boolean documentLoaded(){
		if(this.getDocument()==null){
			return false;
		}
		if(this.getNodeList()==null){
			return false;
		}
		return true;
	}

	/**
	 * Returns the document.
	 * @return loaded document, null if none loaded
	 */
	Document getDocument();

	/**
	 * Returns all SVG layer nodes for the loaded document.
	 * @return list of SVG layer nodes calculated from loaded document if loaded, empty otherwise
	 */
	List<Node> getInkscapeLayers();

	/**
	 * Returns the list of nodes from the loaded document.
	 * @return list of nodes, empty if no document is not loaded
	 */
	NodeList getNodeList();

	/**
	 * Loads an SVG document from URI.
	 * @param uri URI pointing to document
	 * @return false on error, true on success
	 */
	boolean load(URI uri);

}
