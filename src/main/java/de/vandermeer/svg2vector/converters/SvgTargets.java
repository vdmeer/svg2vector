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

package de.vandermeer.svg2vector.converters;

import org.apache.commons.lang3.Validate;

/**
 * Conversion targets with associated Inkscape CLI option.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public enum SvgTargets {

	/** SVG (plain) as target. */
	svg ('l', "export-plain-svg", new Svg2Svg(), new TargetProperties_Svg()),

	/** PDF as target, optionally with PDF version. */
	pdf ('A', "export-pdf", new Svg2Pdf(), new TargetProperties_Pdf()),

	/** EMF as target. */
	emf ('M', "export-emf", new Svg2Emf(), new TargetProperties_Emf()),

	/** WMF as target. */
	wmf ('m', "export-wmf", null, null),

	/** PS as target, optionally with PS version. */
	ps ('P', "export-ps", null, null),

	/** EPS as target. */
	eps ('E', "export-eps", null, null),

	/** PNG as target, optionally with DPI. */
	png ('e', "export-png", null, null),

	;

	/** The short CLI option in Inkscape for this conversion target. */
	char cliShort;

	/** The long CLI option in Inkscape for this conversion target. */
	String cliLong;

	/** A converter for the target. */
	Svg converter;

	/** Properties for the target. */
	TargetProperties props;

	/**
	 * Creates a new target.
	 * @param shortCli short Inkscape CLI option
	 * @param longCli long Inkscape CLI option
	 */
	SvgTargets(char shortCli, String longCli, Svg converter, TargetProperties props){
		Validate.notBlank(longCli);
		this.cliShort = shortCli;
		this.cliLong = longCli;

		this.converter = converter;
		this.props = props;
	}

	/**
	 * Returns the Inkscape short CLI option for the target.
	 * @return short Inkscape CLI option
	 */
	public char getIsCliShort(){
		return this.cliShort;
	}

	/**
	 * Returns the Inkscape long CLI option for the target.
	 * @return long Inkscape CLI option
	 */
	public String getIsCliLong(){
		return this.cliLong;
	}

	/**
	 * Returns the target converter.
	 * @return converter, null if none set
	 */
	public Svg getConverter(){
		return this.converter;
	}

	/**
	 * Returns the target properties.
	 * @return properties, null if none set
	 */
	public TargetProperties getTargetProperties(){
		return this.props;
	}
}
