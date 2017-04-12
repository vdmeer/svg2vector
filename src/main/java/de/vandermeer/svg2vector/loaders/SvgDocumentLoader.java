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
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public interface SvgDocumentLoader {

//	/**
//	 * Returns all SVG layer nodes for the given node list.
//	 * @param nodelist list of XML/SVG nodes, must not be null
//	 * @return list of SVG layer nodes
//	 * @throws NullPointerException if argument was null
//	 */
//	static List<Node> getInkscapeLayers(NodeList nodelist){
//		Validate.notNull(nodelist);
//		ArrayList<Node> ret = new ArrayList<Node>();
//
//		for(int i=0; i<nodelist.getLength(); i++){
//			if("g".equals(nodelist.item(i).getNodeName())){
//				NamedNodeMap nnm = nodelist.item(i).getAttributes();
//				for(int node=0; node<nnm.getLength(); node++){
//					if("inkscape:groupmode".equals(nnm.item(node).getNodeName())){
//						ret.add(nodelist.item(i));
//					}
//				}
//			}
//		}
//		return ret;
//	}

//	/**
//	 * Returns all SVG layer nodes for the loaded document.
//	 * @return list of SVG layer nodes calculated from loaded document if loaded, empty otherwise
//	 */
//	List<Node> getInkscapeLayers();

//	/**
//	 * Returns the list of nodes from the loaded document.
//	 * @return list of nodes, empty if no document is not loaded
//	 */
//	NodeList getNodeList();

}
