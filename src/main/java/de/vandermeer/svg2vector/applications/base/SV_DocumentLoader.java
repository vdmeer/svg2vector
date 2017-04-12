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

package de.vandermeer.svg2vector.applications.base;

import java.util.HashMap;

/**
 * Base class for an SVG document loader.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class SV_DocumentLoader {

	/** Flag indicating if a document is loaded or not. */
	protected boolean isLoaded;

	/** List of layers with identifier and index. */
	protected final HashMap<String, Integer> layers = new HashMap<>();

	/**
	 * Loads the SVG file.
	 * This method will not re-load an SVG file once the loader already has a document loaded.
	 * @param fn the file name for the SVG document
	 * @return null on success, error message on error
	 * @throws NullPointerException if argument was null
	 * @throws IllegalArgumentException if argument was blank
	 */
	public abstract String load(String fn); 

	/**
	 * Returns the status of the loader.
	 * @return true if document is loaded, false otherwise
	 */
	public boolean isLoaded(){
		return isLoaded;
	}

	/**
	 * Tests if the loaded document has Inkscape layers.
	 * @return true if the document has Inkscape layers (2 or more layers), false otherwise (0 or 1 layer)
	 */
	public boolean hasInkscapeLayers(){
		return isLoaded && this.layers.size()>1;
	}

	/**
	 * Switch all layers off.
	 */
	public abstract void switchOnAllLayers();

	/**
	 * Switch all layers off.
	 */
	public abstract void switchOffAllLayers();

	/**
	 * Switches the given layer on, nothing happens if the layer was blank or not in the layer list.
	 * @param layer the layer to be switched on
	 */
	public abstract void switchOnLayer(String layer);

	/**
	 * Returns the map of layers (layer name to index), empty if no layers found.
	 * @return map of layers
	 */
	public HashMap<String, Integer> getLayers(){
		return this.layers;
	}
}
