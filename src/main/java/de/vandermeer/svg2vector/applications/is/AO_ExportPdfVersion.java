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

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.execs.options.AbstractTypedC_String;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Application option `export-pdf-version`.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_ExportPdfVersion extends AbstractTypedC_String implements IsTargetOption {

	/**
	 * Returns the new option as not required and without a short option.
	 */
	public AO_ExportPdfVersion(){
		super(null, "export-pdf-version", false, "VERSION", false,
				"the PDF version, must be an exact version string", "set PDF version for export"
		);

		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/applications/is/AO_ExportPdfVersion.stg");
		this.setLongDescription(stg.getInstanceOf("longDescription"));
	}

	@Override
	public SvgTargets getTarget() {
		return SvgTargets.pdf;
	}

	@Override
	public String getIsCmd() {
		return "--export-pdf-version";
	}

	@Override
	public ST getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOptValue() {
		return this.getValue();
	}
}
