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

package de.vandermeer.svg2vector;

import java.util.TreeMap;

import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.ExecS_Application;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;

/**
 * Simple application that prints all S2V application error codes.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class S2VErrorCodes implements ExecS_Application {

	/** Application name. */
	public final static String APP_NAME = "s2v-aec";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector Application Error Codes";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8";

	/** CLI parser. */
	final private ExecS_CliParser cli;

	public S2VErrorCodes(){
		this.cli = new ExecS_CliParser();
	}

	@Override
	public int executeApplication(String[] args){
		// parse command line, exit with help screen if error
		final int ret = ExecS_Application.super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		StrBuilder out = new StrBuilder();
		out
			.append("S2V application Error Codes")
			.appendNewLine()
			.append(" - sorted by categories, then by error codes")
			.appendNewLine()
			.append(" - shown as:")
			.appendNewLine()
			.append("   [error code] -> [description]")
			.appendNewLine()
			.append("                   ")
			.append('"')
			.append("[message]")
			.append('"')
			.appendNewLine()
			.appendNewLine()
		;

		TreeMap<String, ErrorCodeCategories> catMap = new TreeMap<>();
		for(ErrorCodeCategories cat : ErrorCodeCategories.values()){
			catMap.put(String.format(" %1$3d", cat.getStart()), cat);
		}

		for(ErrorCodeCategories cat : catMap.values()){
			out
				.append("- ")
				.append(cat.getDescription())
				.append(" (")
				.append(cat.getStart())
				.append(" to ")
				.append(cat.getEnd())
				.append(')')
				.appendNewLine()
			;

			TreeMap<String, ErrorCodes> ecMap = new TreeMap<>();
			for(ErrorCodes ec : ErrorCodes.values()){
				if(ec.getCategory()==cat){
					ecMap.put(String.format(" %1$3d", ec.getCode()), ec);
				}
			}
			for(ErrorCodes ec : ecMap.values()){
				String padding = "  " + ec.getCode() + " -> ";
				out
					.append("  ")
					.append(ec.getCode())
					.append(" -> ")
					.append(ec.getDescription())
					.appendNewLine()
					.appendPadding(padding.length(), ' ')
					.append('"')
					.append(ec.getMessage())
					.append('"')
					.appendNewLine()
				;
			}
			out.appendNewLine();
		}

		System.out.println(out);
		return 0;
	}

	@Override
	public String getAppName() {
		return APP_NAME;
	}

	@Override
	public String getAppDisplayName(){
		return APP_DISPLAY_NAME;
	}

	@Override
	public String getAppDescription() {
		return "Simply prints a list of all S2V application error codes for reference";
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}

	@Override
	public ApplicationOption<?>[] getAppOptions() {
		return null;
	}

	@Override
	public ExecS_CliParser getCli() {
		return this.cli;
	}
}
