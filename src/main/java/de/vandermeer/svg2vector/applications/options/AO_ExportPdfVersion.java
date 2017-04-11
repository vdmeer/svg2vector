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

package de.vandermeer.svg2vector.applications.options;

import org.apache.commons.cli.Option;

import de.vandermeer.execs.options.AbstractApplicationOption;

/**
 * Application option "export-pdf-version".
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class AO_ExportPdfVersion extends AbstractApplicationOption<String> {

	/**
	 * Returns the new option.
	 * @param required true if option is required, false of it is optional
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_ExportPdfVersion(boolean required){
		this(required, null);
	}

	/**
	 * Returns the new option.
	 * @param required true if option is required, false of it is optional
	 * @param shortOption character for sort version of the option
	 * @throws NullPointerException - if description parameter is null
	 * @throws IllegalArgumentException - if description parameter is empty
	 */
	public AO_ExportPdfVersion(boolean required, Character shortOption){
		super(
				"set PDf version for export",
				"Make sure to input the exact string found in the PDF export dialog, e.g. \"PDF 1.4\" which is PDF-a conformant."
		);

		Option.Builder builder = (shortOption==null)?Option.builder():Option.builder(shortOption.toString());
		builder.longOpt("export-pdf-version");
		builder.hasArg().argName("VERSION");
		builder.required(required);
		this.setCliOption(builder.build());
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}
}
