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
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.execs.ExecS_Application;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;

/**
 * Abstract base class for Svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0 build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class AppBase <L extends SV_DocumentLoader, P extends AppProperties<L>> implements ExecS_Application {

	/** CLI parser. */
	final private ExecS_CliParser cli;

	/** The properties of the application. */
	final private P props;

	/**
	 * Creates a new base application.
	 * @param props the application properties
	 * @throws NullPointerException if props was null
	 */
	protected AppBase(P props){
		Validate.notNull(props);
		this.props = props;

		this.cli = new ExecS_CliParser();
		this.cli.addAllOptions(this.props.getAppOptions());
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
			return -11;
		}
		this.printWarnings();

		if((err = this.props.setOutput()) != null){
			this.printErrorMessage(err);
			return -12;
		}
		this.printWarnings();

		if(this.props.doesNoLayers()){
			this.printProgressMessage("processing single output, no layers");
			this.printDetailMessage("target:           " + target.name());
			this.printDetailMessage("input fn:         " + this.props.getFinFn());
			this.printDetailMessage("output fn:        " + this.props.getFoutFn());
		}
		else if(props.doesLayers()){
			this.printProgressMessage("processing multi layer, multi file output");
			this.printDetailMessage("target:           " + target.name());
			this.printDetailMessage("input fn:         " + this.props.getFinFn());
			this.printDetailMessage("output dir:       " + this.props.getDout());
			this.printDetailMessage("fn pattern:       " + this.props.getFoutPattern());
		}
		else{
			this.printErrorMessage("implementation error: something wrong with property settings");
			return -13;
		}

		if(this.props.doesCreateDirectories()){
			this.printProgressMessage("creating directories for output");
			if(this.props.getFoutFile()!=null){
				if(this.props.canWriteFiles()){
					this.props.getFoutFile().getParentFile().mkdirs();
				}
				this.printDetailMessage("create directories (fout): " + this.props.getFoutFile().getParent());
			}
			if(this.props.getDoutFile()!=null){
				if(this.props.canWriteFiles()){
					this.props.getDoutFile().mkdirs();
				}
				this.printDetailMessage("create directories (dout): " + this.props.getDout());
			}
		}

		return 0;
	}

	@Override
	public ApplicationOption<?>[] getAppOptions() {
		return this.props.getAppOptions();
	}

	@Override
	public ExecS_CliParser getCli() {
		return this.cli;
	}

	/**
	 * Returns the application properties.
	 * @return application properties
	 */
	public P getProps(){
		return this.props;
	}

	/**
	 * Tests if the type is activate in the given mode.
	 * @param mask the mask to test against
	 * @return true if the message type (mask) is activated in the message mode, false otherwise
	 */
	private boolean isSet(int mask){
		return ((this.props.getMsgMode() & mask) == mask);
	}

	/**
	 * Prints a detail message if activated in mode
	 * @param msg the detail message, not printed if null
	 */
	public void printDetailMessage(String msg){
		this.printMessage(msg, AppProperties.P_OPTION_DEAILS);
	}

	/**
	 * Prints a error message if activated in mode
	 * @param err the error message, not printed if null
	 */
	public void printErrorMessage(String err){
		this.printMessage(err, AppProperties.P_OPTION_ERROR);
	}

	/**
	 * Prints a message of give type.
	 * @param msg the message, not printed if null
	 * @param type the message type, nothing printed if not set in message mode
	 */
	private void printMessage(String msg, int type){
		if(msg==null){
			return;
		}

		if(this.isSet(type)){
			if(type==AppProperties.P_OPTION_ERROR){
				System.err.println(this.getAppName() + " error: " + msg);
			}
			else if(type==AppProperties.P_OPTION_WARNING){
				System.out.println(this.getAppName() + " warning: " + msg);
			}
			else if(type==AppProperties.P_OPTION_PROGRESS){
				System.out.println(this.getAppName() + ": --- " + msg);
			}
			else if(type==AppProperties.P_OPTION_DEAILS){
				System.out.println(this.getAppName() + ": === " + msg);
			}
			else{
				throw new IllegalArgumentException("messaging: unknown type: " + type);
			}
		}
	}

	/**
	 * Prints a progress message if activated in mode
	 * @param msg the progress message, not printed if null
	 */
	public void printProgressMessage(String msg){
		this.printMessage(msg, AppProperties.P_OPTION_PROGRESS);
	}

	/**
	 * Prints a warning message if activated in mode
	 * @param msg the warning message, not printed if null
	 */
	public void printWarningMessage(String msg){
		this.printMessage(msg, AppProperties.P_OPTION_WARNING);
	}

	/**
	 * Prints all warnings collected in properties and empties the warning list.
	 */
	public void printWarnings(){
		if(this.props.warnings.size()>0){
			for(String msg : this.props.warnings){
				this.printWarningMessage(msg);
			}
			this.props.warnings.clear();
		}
	}
}
