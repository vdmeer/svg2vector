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

package de.vandermeer.svg2vector.applications.is;

import de.vandermeer.svg2vector.applications.base.SvgTargets;

/**
 * Application option `export-dpi`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0 build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_ExportDpi extends IsTargetOption {

	/**
	 * Returns the new option as not required and without a short option.
	 * @param target the target to which this option applies, must not be null
	 * @param isCli the Inkscape command line option to be used including any leading dashes, must not be blank
	 * @throws NullPointerException - if any required parameter is null
	 * @throws IllegalArgumentException - if any required parameter is null
	 */
	public AO_ExportDpi(SvgTargets target, String isCli){
		super(
				target, isCli,
				"export-dpi", "DPI",
				"set DPI for export",
				"Resolution for exporting to bitmap and for rasterization of filters in PS/EPS/PDF (default 90)."
		);
	}
}
