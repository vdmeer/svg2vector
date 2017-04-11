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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.ExecS_Application;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.converters.SvgTargets;

/**
 * Abstract base class for Svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class AppBase <L extends SV_DocumentLoader, P extends AppProperties<L>> implements ExecS_Application {

	/** CLI parser. */
	final private ExecS_CliParser cli;

	/** The properties of the application. */
	final private P props;

	/**
	 * Returns a new application.
	 * @throws NullPointerException if
	 */

	/**
	 * Creates a new base application.
	 * @param props the application properties
	 * @throws NullPointerException if options was null
	 */
	public AppBase(P props){
		Validate.notNull(props);
		this.props = props;

		this.cli = new ExecS_CliParser();
		this.cli.addAllOptions(this.props.getAppOptions());
	}

	@Override
	public int executeApplication(String[] args) {
		// parse command line, exit with help screen if error
		int ret = ExecS_Application.super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		this.props.setMessageMode();

		SvgTargets target = this.props.getTarget();
		if(target==null){
			this.printErrorMessage("given target <" + this.props.getTargetValue() + "> not supported. Use one of the supported targets: " + new StrBuilder().appendWithSeparators(this.props.getSupportedTargetts(), ", "));
			return -10;
		}

		String err = null;

		if((err = this.props.setInput()) != null){
			this.printErrorMessage(err);
			return -20;
		}

		if((err = this.props.setOutput()) != null){
			this.printErrorMessage(err);
			return -30;
		}

		if(this.props.doesNoLayers()){
			this.printProgressMessage("processing SVG file for single file output");
			this.printDetailMessage("input fn:  " + this.props.getFinFn());
			this.printDetailMessage("output fn: " + this.props.getFoutFn());
		}
		else if(props.doesLayers()){
			this.printProgressMessage("processing SVG file for file per layer output");
			this.printDetailMessage("input fn:   " + this.props.getFinFn());
			this.printDetailMessage("output dir: " + this.props.getDout());
			this.printDetailMessage("fn pattern: " + this.props.getFoutPattern());
		}
		else{
			this.printErrorMessage("implementation error: something wrong with property settings");
			return -40;
		}

		return 0;
	}

	/**
	 * Prints a progress message if activated in mode
	 * @param msg the message, not printed if blank
	 */
	public void printProgressMessage(String msg){
		this.printMessage(msg, MessageTypes.progress);
	}

	/**
	 * Prints a detail message if activated in mode
	 * @param msg the message, not printed if blank
	 */
	public void printDetailMessage(String msg){
		this.printMessage(msg, MessageTypes.detail);
	}

	/**
	 * Prints a warning message if activated in mode
	 * @param msg the message, not printed if blank
	 */
	public void printWarningMessage(String msg){
		this.printMessage(msg, MessageTypes.warning);
	}

	/**
	 * Prints a error message if activated in mode
	 * @param msg the message, not printed if blank
	 */
	public void printErrorMessage(String msg){
		this.printMessage(msg, MessageTypes.error);
	}

	/**
	 * Prints a message of give type.
	 * @param msg the message, not printed if blank
	 * @param type the message type, nothing printed if null or not set in message mode
	 */
	public void printMessage(String msg, MessageTypes type){
		if(StringUtils.isBlank(msg) || type==null){
			return;
		}
		if(type.isSet(this.props.getMsgMode())){
			if(type==MessageTypes.error){
				System.err.println(this.getAppName() + " error: " + msg);
			}
			else if(type==MessageTypes.warning){
				System.out.println(this.getAppName() + " warning: " + msg);
			}
			else{
				System.out.println(this.getAppName() + ": " + msg);
			}
		}
	}

	/**
	 * Adds a new option to CLI parser and option list.
	 * @param option new option, ignored if null
	 */
	protected void addOption(ApplicationOption<?> option){
		if(option!=null){
			this.getCli().addOption(option);
			this.props.addOption(option);
		}
	}

	@Override
	public ExecS_CliParser getCli() {
		return this.cli;
	}

	@Override
	public ApplicationOption<?>[] getAppOptions() {
		return this.props.getAppOptions();
	}
}
