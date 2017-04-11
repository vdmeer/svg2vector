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

	/** Flag indicating if a loaded document has layers (2 or more) or not (0 or 1). */
	protected boolean hasLayers = false;

	/**
	 * Loads the SVG file.
	 * @param fn the file name for the SVG document
	 * @return null on success, error message on error
	 * @throws NullPointerException if fn was null
	 * @throws IllegalArgumentException if fn was blank
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
		return isLoaded && hasLayers;
	}

	/**
	 * Switch on all layers.
	 */
	public abstract void switchOnAllLayers();
}
