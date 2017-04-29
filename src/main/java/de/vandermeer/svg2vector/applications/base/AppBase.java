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

import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;

import de.vandermeer.execs.AbstractAppliction;
import de.vandermeer.execs.DefaultCliParser;
import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.application.IsApplication;
import de.vandermeer.svg2vector.applications.base.conversion.ConversionOptions;
import de.vandermeer.svg2vector.applications.base.layers.LayerOptions;
import de.vandermeer.svg2vector.applications.base.messages.MessageOptions;
import de.vandermeer.svg2vector.applications.base.misc.MiscOptions;
import de.vandermeer.svg2vector.applications.base.output.OutputOptions;
import de.vandermeer.svg2vector.applications.base.required.RequiredOptions;
import de.vandermeer.svg2vector.applications.core.SV_DocumentLoader;
import de.vandermeer.svg2vector.applications.core.SvgTargets;

/**
 * Abstract base class for Svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public abstract class AppBase <L extends SV_DocumentLoader> extends AbstractAppliction implements IsApplication {

	/** The SVG document loader. */
	final private L loader;

	/** Application messaging options. */
	final private MessageOptions messageOptions;

	/** Miscellaneous application options. */
	final private MiscOptions miscOptions;

	/** Conversion options. */
	final private ConversionOptions conversionOptions;

	/** Required options. */
	final private RequiredOptions requiredOptions;

	/** Layer options. */
	final private LayerOptions layerOptions;

	/** Output options. */
	final private OutputOptions outputOptions;

	/**
	 * Creates a new base application.
	 * @param supportedTargets the supported targets, must not be null and have no null elements
	 * @param loader the SVG document loader, must not be null
	 */
	protected AppBase(SvgTargets[] supportedTargets, L loader){
		super(new DefaultCliParser(), AbstractAppliction.HELP_TYPED_SHORTLONG, AbstractAppliction.VERSION_SHORTLONG);

		Validate.notNull(loader);
		this.loader = loader;

		this.messageOptions = new MessageOptions();
		this.addAllOptions(this.messageOptions.getAllOptions());

		this.miscOptions = new MiscOptions();
		this.addAllOptions(this.miscOptions.getAllOptions());

		this.conversionOptions = new ConversionOptions();
		this.addAllOptions(this.conversionOptions.getAllOptions());

		this.requiredOptions = new RequiredOptions(supportedTargets);
		this.addAllOptions(this.requiredOptions.getAllOptions());

		this.layerOptions = new LayerOptions();
		this.addAllOptions(this.layerOptions.getAllOptions());

		this.outputOptions = new OutputOptions();
		this.addAllOptions(this.outputOptions.getAllOptions());
	}

	/**
	 * Tests if the properties are set to process layers.
	 * @return true if set to process layers (layer and output options do layers), false otherwise
	 */
	public boolean doesLayers(){
		return this.layerOptions.doLayers() && this.outputOptions.doLayers();
	}

	@Override
	public int executeApplication(String[] args){
		// parse command line, exit with help screen if error
		return IsApplication.super.executeApplication(args);
	}

	/**
	 * Returns an output file name using the set output file name and given options without file extension.
	 * @param index an index, ignored if smaller than 1
	 * @param entry an entry, ignored if null
	 * @return the output file name
	 * @throws ApplicationException in case the resulting file name was not valid
	 */
	public String fopFileOnly(int index, Entry<String, Integer> entry) throws ApplicationException{
		return this.outputOptions.getPattern().generateName(
				null,
				this.outputOptions.getFile(),
				null,
				index,
				entry
		).toString();
	}

	/**
	 * Returns an output file name using the settings for the OutputOptions.
	 * @return the output file name
	 * @throws ApplicationException in case the resulting file name was not valid
	 */
	public String fopOO() throws ApplicationException{
		return this.outputOptions.getPattern().generateName(
				this.outputOptions.getDirectory(),
				this.outputOptions.getFile(),
				this.outputOptions.getFileExtension(),
				-1,
				null
		).toString();
	}

	/**
	 * Returns an output file name using the settings for the OutputOptions.
	 * @param index an index, ignored if smaller than 1
	 * @param entry an entry, ignored if null
	 * @return the output file name
	 * @throws ApplicationException in case the resulting file name was not valid
	 */
	public String fopOO(int index, Entry<String, Integer> entry) throws ApplicationException{
		return this.outputOptions.getPattern().generateName(
				this.outputOptions.getDirectory(),
				this.outputOptions.getFile(),
				this.outputOptions.getFileExtension(),
				index,
				entry
		).toString();
	}

	/**
	 * Returns an output file name using the settings for the OutputOptions.
	 * @param filename file name to be used
	 * @return the output file name
	 * @throws ApplicationException in case the resulting file name was not valid
	 */
	public String fopOO(Path filename) throws ApplicationException{
		return this.outputOptions.getPattern().generateName(
				this.outputOptions.getDirectory(),
				filename,
				this.outputOptions.getFileExtension(),
				-1,
				null
		).toString();
	}

	/**
	 * Returns the conversion options.
	 * @return conversion options
	 */
	public ConversionOptions getConversionOptions(){
		return this.conversionOptions;
	}

	/**
	 * Returns the document loader.
	 * @return the document loader
	 */
	public L getLoader(){
		return this.loader;
	}

	/**
	 * Returns the miscellaneous options.
	 * @return miscellaneous options
	 */
	public MiscOptions getMiscOptions(){
		return this.miscOptions;
	}

	/**
	 * Returns the required options.
	 * @return required options
	 */
	public RequiredOptions getRequiredOptions(){
		return this.requiredOptions;
	}

	/**
	 * Returns the set target.
	 * @return target, null if not set
	 */
	public SvgTargets getTarget(){
		return this.requiredOptions.getTarget();
	}

	/**
	 * Initialized the properties, loads all input and output.
	 * @throws ApplicationException if anything went wrong
	 */
	protected void init() throws ApplicationException{
		this.messageOptions.setMessageMode();
		this.requiredOptions.setInput(this.loader);
		this.layerOptions.setOptions(this.loader.hasInkscapeLayers());
		this.printWarnings(this.layerOptions.getWarnings());

		this.outputOptions.setOptions(
				this.layerOptions.doLayers(),
				this.requiredOptions.getTarget(),
				this.requiredOptions.getInputFilename()
		);

		if(outputOptions.doLayers()){
			this.printProgressMessage("processing multi layer, multi file output");
			this.outputOptions.removePatternOptions(
					this.layerOptions.foutNoBasename(),
					this.layerOptions.foutIndex(),
					this.layerOptions.foutIsIndex(),
					this.layerOptions.foutIsLabel()
			);
			this.outputOptions.setPatternBasename(this.layerOptions.getBasename());
		}
		else{
			this.printProgressMessage("processing single output, no layers");
			this.outputOptions.removePatternOptions(false, true, true, true);
		}
		this.printDetailMessage("target:           " + this.requiredOptions.getTarget().name());
		this.printDetailMessage("input file:       " + this.requiredOptions.getInputFilename());
		this.printDetailMessage("output pattern:   " + this.outputOptions.getPatternString());
		this.printDetailMessage("output directory: " + this.outputOptions.getDirectory());
		if(this.outputOptions.getFile()!=null){
			this.printDetailMessage("output file name: " + this.outputOptions.getFile());
			this.printDetailMessage("output extension: " + this.outputOptions.getFileExtension());
		}

		if(this.outputOptions.createDirs()){
			this.printProgressMessage("creating directories for output");
			if(this.outputOptions.doLayers()){
				if(!this.miscOptions.doesSimulate()){
					this.outputOptions.getDirectory().toFile().mkdirs();
				}
				this.printDetailMessage("create directories (dout): " + this.outputOptions.getDirectory());
			}
			else{
				Path dir = this.outputOptions.getDirectory();
				if(dir!=null){
					if(!this.miscOptions.doesSimulate()){
						dir.toFile().mkdirs();
					}
					this.printDetailMessage("create directories (fout): " + dir);
				}
			}
		}
	}

	/**
	 * Prints a detail message if activated in mode
	 * @param msg the detail message, not printed if null
	 */
	public void printDetailMessage(String msg){
		this.printMessage(msg, MessageOptions.OPTION_DEAILS);
	}

	/**
	 * Prints a error message generated from message, cause, and stack trace of an exception if activated in mode
	 * @param ex the exception, not printed if null
	 */
	public void printErrorMessage(Exception ex){
		if(ex==null){
			return;
		}
		this.printErrorMessage("catched " + ex.getClass().getSimpleName() + " exception");
		this.printErrorMessage(" - message: " + ex.getMessage());
		if(ex.getCause()!=null){
			this.printErrorMessage(" - cause: " + ex.getCause().getLocalizedMessage());
		}
		if(this.miscOptions.printStackTrace()){
			if(ex.getStackTrace()!=null){
				this.printErrorMessage(" - stack trace (interal classes): ");
				for(StackTraceElement trace : ex.getStackTrace()){
					if(trace.toString().contains("vandermeer") || trace.toString().contains("apache")){
						this.printErrorMessage(" ----> " + trace);
					}
				}
			}
		}
	}

	/**
	 * Prints a error message if activated in mode
	 * @param err the error message, not printed if null
	 */
	public void printErrorMessage(String err){
		this.printMessage(err, MessageOptions.OPTION_ERROR);
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

		if((this.messageOptions.getMessageMode() & type) == type){
			if(type==MessageOptions.OPTION_ERROR){
				System.err.println(this.getAppName() + " error: " + msg);
			}
			else if(type==MessageOptions.OPTION_WARNING){
				System.out.println(this.getAppName() + " warning: " + msg);
			}
			else if(type==MessageOptions.OPTION_PROGRESS){
				System.out.println(this.getAppName() + ": --- " + msg);
			}
			else if(type==MessageOptions.OPTION_DEAILS){
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
		this.printMessage(msg, MessageOptions.OPTION_PROGRESS);
	}

	/**
	 * Prints a warning message if activated in mode
	 * @param msg the warning message, not printed if null
	 */
	public void printWarningMessage(String msg){
		this.printMessage(msg, MessageOptions.OPTION_WARNING);
	}

	/**
	 * Prints all warnings from a given list
	 * @param warnings list of warnings, can be null
	 */
	public void printWarnings(List<String> warnings){
		if(warnings!=null){
			for(String msg : warnings){
				this.printWarningMessage(msg);
			}
		}
	}
}
