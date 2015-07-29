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

import java.awt.Color;

import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;

/**
 * Target properties for a conversion.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
public abstract class TargetProperties {

	/** Properties that can be set for conversion */
	protected UserProperties properties;

	/** Flag for using Inkscape layer names */
	boolean useInkscapeLayerName = true;

	/** Flag for using Inkscape layer indices */
	boolean useInkscapeLayerIndex = false;

	/** Flag for using Inkscape layers */
	boolean useOnePerInkscapeLayer = false;

	/**
	 * Constructor.
	 * Sets transparency to true, background to false, background color to white, clipping to false and text-as-shapes to true
	 */
	public TargetProperties(){
		this.properties=new UserProperties();

		this.setPropertyTransparent(true);
		this.setPropertyBackground(false);
		this.setPropertyBackgroundColor(Color.WHITE);
		this.setPropertyClip(false);
		this.setPropertyTextAsShapes(true);
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
	 * Returns user properties.
	 * @return user properties
	 */
	public UserProperties getProperties(){
		return this.properties;
	}

	/**
	 * Switches use of Inkscape layer names on.
	 */
	public void setUseInkscapeLayerName(){
		this.useInkscapeLayerName = false;
		this.useInkscapeLayerIndex = true;
	}

	/**
	 * Switches use of Inkscape layer indices on.
	 */
	public void setUseInkscapeLayerIndex(){
		this.useInkscapeLayerName = true;
		this.useInkscapeLayerIndex = true;
	}

	/**
	 * Switches use of Inkscape layers on.
	 */
	public void setUseOnePerInkscapeLayer(){
		this.useOnePerInkscapeLayer = true;
	}

	/**
	 * Returns the current setting for using Inkscape layer names.
	 * @return current settings
	 */
	public boolean useInkscapeLayerName(){
		return this.useInkscapeLayerName;
	}

	/**
	 * Returns the current setting for using Inkscape indices.
	 * @return current settings
	 */
	public boolean useInkscapeLayerIndex(){
		return this.useInkscapeLayerIndex;
	}

	/**
	 * Returns the current setting for using Inkscape layers.
	 * @return current settings
	 */
	public boolean useOnePerInkscapeLayer(){
		return this.useOnePerInkscapeLayer;
	}
}
