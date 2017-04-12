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

import org.apache.commons.lang3.Validate;

import de.vandermeer.svg2vector.applications.is.converters.FhConverter;
import de.vandermeer.svg2vector.applications.is.converters.Fh_Svg2Emf;
import de.vandermeer.svg2vector.applications.is.converters.Fh_Svg2Pdf;
import de.vandermeer.svg2vector.applications.is.converters.Fh_Svg2Svg;

/**
 * Conversion targets with associated Inkscape CLI option.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public enum SvgTargets {

	/** SVG (plain) as target. */
	svg ('l', "export-plain-svg", new Fh_Svg2Svg()),

	/** PDF as target, optionally with PDF version. */
	pdf ('A', "export-pdf", new Fh_Svg2Pdf()),

	/** EMF as target. */
	emf ('M', "export-emf", new Fh_Svg2Emf()),

	/** WMF as target. */
	wmf ('m', "export-wmf", null),

	/** PS as target, optionally with PS version. */
	ps ('P', "export-ps", null),

	/** EPS as target. */
	eps ('E', "export-eps", null),

	/** PNG as target, optionally with DPI. */
	png ('e', "export-png", null),

	;

	/** The short CLI option in Inkscape for this conversion target. */
	char cliShort;

	/** The long CLI option in Inkscape for this conversion target. */
	String cliLong;

	/** A converter for the target. */
	FhConverter converter;

	/**
	 * Creates a new target.
	 * @param shortCli short Inkscape CLI option
	 * @param longCli long Inkscape CLI option
	 * @param converter an SVG FH converter for the target
	 */
	SvgTargets(char shortCli, String longCli, FhConverter converter){
		Validate.notBlank(longCli);
		this.cliShort = shortCli;
		this.cliLong = longCli;

		this.converter = converter;
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
	public FhConverter getConverter(){
		return this.converter;
	}
}
