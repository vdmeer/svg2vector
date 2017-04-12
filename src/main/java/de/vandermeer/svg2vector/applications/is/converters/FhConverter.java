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

package de.vandermeer.svg2vector.applications.is.converters;

import java.awt.Color;
import java.io.File;

import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;

import de.vandermeer.svg2vector.applications.fh.BatikLoader;

/**
 * A converter for SVG documents using the FreeHep library.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class FhConverter {

	/** Properties that can be set for conversion. */
	protected UserProperties properties;

	/**
	 * Creates a new converter with default configuration.
	 * Sets transparency to true, background to false, background color to white, clipping to false, text as shapes to false.
	 */
	public FhConverter(){
		this.properties=new UserProperties();

		this.setPropertyTransparent(true);
		this.setPropertyBackground(false);
		this.setPropertyBackgroundColor(Color.WHITE);
		this.setPropertyClip(false);
		this.setPropertyTextAsShapes(false);
	}

	/**
	 * Sets background property on or off.
	 * @param on new setting
	 */
	public abstract void setPropertyBackground(boolean on);

	/**
	 * Sets background color property to a color.
	 * @param color background color
	 */
	public abstract void setPropertyBackgroundColor(Color color);

	/**
	 * Sets transparency property on or off.
	 * @param on new setting
	 */
	public abstract void setPropertyTransparent(boolean on);

	/**
	 * Sets clipping property on or off.
	 * @param on new setting
	 */
	public void setPropertyClip(boolean on){
		this.properties.setProperty(AbstractVectorGraphicsIO.CLIP, on);
	}

	/**
	 * Sets text-as-shape property on or off.
	 * @param on new setting
	 */
	public void setPropertyTextAsShapes(boolean on){
		this.properties.setProperty(AbstractVectorGraphicsIO.TEXT_AS_SHAPES, on);
	}

	/**
	 * Converts the document maintained by the loader to a target format.
	 * @param loader the document loader, must have a document successfully loaded
	 * @param fout the file for the output
	 * @return null on success, error message otherwise
	 */
	public abstract String convertDocument(BatikLoader loader, File fout);

}
