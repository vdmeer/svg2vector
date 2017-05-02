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

package de.vandermeer.svg2vector.applications.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.interfaces.application.ApplicationErrorCode;
import de.vandermeer.skb.interfaces.application.ErrorCodeCategory;

/**
 * Error codes of the svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public enum ErrorCodes implements ApplicationErrorCode {

	/*******************************************
	 * General Errors
	 *******************************************/

	/**
	 * General code for a null pointer exception (no arguments).
	 */
	GENERAL_NULL_POINTER__0(
			-2,
			"a null pointer exception occurred",
			"General code for a null pointer exception.",
			EC_Categories.general
	),

	/**
	 * General code for an I/O exception (no arguments).
	 */
	GENERAL_IO__0(
			-3,
			"an I/O pointer exception occurred",
			"General code for an I/O exception.",
			EC_Categories.general
	),

	/**
	 * An internal implementation error, something wrong with property settings (no arguments).
	 */
	IMPL_PROPERTIES_WRONG__0(
			-4,
			"implementation error: something wrong with property settings",
			"An internal implementation error, something wrong with property settings.",
			EC_Categories.general
	),

	/**
	 * A given target was blank (1 argument).
	 * The argument is a list of supported targets.
	 */
	TARGET_BLANK__1(
			-50, 1,
			"given target is blank. Use one of the supported targets: {}",
			"A given target was blank, null or empty (arguments: supported targets).",
			EC_Categories.target
	),

	/**
	 * A given target is unknown (2 arguments).
	 * The first argument is the target, the second a list of supported targets.
	 */
	TARGET_UNKNOWN__2(
			-51, 2,
			"given target <{}> is unknown. Use one of the supported targets: {}",
			"A given target is unknown (arguments: target, supported targets).",
			EC_Categories.target
	),

	/**
	 * A given target is not supported by an application (2 arguments).
	 * The first argument is the target, the second a list of supported targets.
	 */
	TARGET_NOT_SUPPORTED__2(
			-52, 2,
			"given target <{}> not supported. Use one of the supported targets: {}",
			"A given target is not supported by an application (arguments: target, supported targets).",
			EC_Categories.target
	),



	/*******************************************
	 * Input File Errors
	 *******************************************/

	/**
	 * No input file given, was either null or blank (no arguments).
	 */
	NO_FIN__0(
			-100,
			"no input file given",
			"No input file given, was either null or blank.",
			EC_Categories.input_file
	),

	/**
	 * Input file does not exist (1 arguments).
	 * The argument is the file name used to test the input file.
	 */
	FIN_DOES_NOT_EXIST__1(
			-101, 1,
			"input file <{}> does not exist, please check path and filename",
			"Input file does not exist (file name).",
			EC_Categories.input_file
	),

	/**
	 * Input file does exist but is not a file (1 arguments).
	 * The argument is the file name used to test the input file.
	 */
	FIN_NOT_A_FILE__1(
			-102, 1,
			"input file <{}> is not a file, please check path and filename",
			"Input file does exist but is not a file (file name).",
			EC_Categories.input_file
	),

	/**
	 * Input file does exist but is not readable (1 arguments).
	 * The argument is the file name used to test the input file.
	 */
	CANNOT_READ_FIN__1(
			-103, 1,
			"cannot read input file <{}>, please file permissions",
			"Input file does exist but is not readable (file name).",
			EC_Categories.input_file
	),



	/*******************************************
	 * Inkscape Loader Errors
	 *******************************************/

	/**
	 * A loader could not open an SVG file as compressed SVG (3 arguments).
	 * The first argument is the loader class name, the second the input file name, the second is the original exception message.
	 */
	LOADER_ZIP__3(
			-220, 3,
			"{}: ZIP exception reading file <{}>: {}",
			"A loader could not open an SVG file as compressed SVG (loader class, input file, original exception message).",
			EC_Categories.loader_inkscape
	),

	/**
	 * A loader did catch an I/O exception while reading a GZIP stream (3 arguments).
	 * The first argument is the loader class name, the second the input file name, the third the original exception messages.
	 */
	LOADER_IO_GZIP__3(
			-221, 3,
			"{}: IO error reading GZIP file <{}>: {}",
			"A loader did catch an I/O exception while reading a GZIP stream (loader class, input file, original exception message).",
			EC_Categories.loader_inkscape
	),

	/**
	 * A loader did catch a file-not-found exception while reading a plain SVG file (3 arguments).
	 * The first argument is the loader class name, the second the input file name, the third the original exception messages.
	 */
	LOADER_FILE_NOT_FOUND_PLAIN_SVG__3(
			-222, 3,
			"{}: FileNotFoundException exception reading plain file {}: {}",
			"A loader did catch a file-not-found exception while reading a plain SVG file (loader class, input file, original exception message).",
			EC_Categories.loader_inkscape
	),

	/**
	 * A loader did catch an I/O exception while reading a plain SVG file (3 arguments).
	 * The first argument is the loader class name, the second the input file name, the third the original exception messages.
	 */
	LOADER_IO_PLAIN__3(
			-223, 3,
			"{}: IO exception reading plain file {}: {}",
			"A loader did catch an I/O exception while reading a plain SVG file (loader class, input file, original exception message).",
			EC_Categories.loader_inkscape
	),



	/*******************************************
	 * Batik Loader Errors
	 *******************************************/

	/**
	 * A loader using Apache Batik could not load an SVG document (2 arguments).
	 * The first argument is the loaders class name, the second the original exception message.
	 */
	LOADER_BATIK_CANNOT_LOAD_SVG__2(
			-210, 2,
			"{}: exception loading svgDocument: {}",
			"A loader using Apache Batik could not load an SVG document (loaders class, original exception message).",
			EC_Categories.loader_batik
	),

	/**
	 * A loader using Apache Batik could set the size for an SVG document, set the dimension (2 arguments).
	 * The first argument is the loaders class name, the second the original exception message.
	 */
	LOADER_BATIK_CANNOT_SET_SIZE__2(
			-211, 2,
			"{}: exception setting docucment size: {}",
			"A loader using Apache Batik could set the size for an SVG document, set the dimension (loaders class, original exception message).",
			EC_Categories.loader_batik
	),



	/*******************************************
	 * Layer Errors
	 *******************************************/

	/**
	 * An application was requested to process layers, but the input SVG file did not had any (no arguments).
	 */
	LAYERS_REQUESTED_DOC_WITHOUT_LAYERS__0(
			-300, 0,
			"layers requested in command line, but SVG input file has no layers",
			"An application was requested to process layers, but the input SVG file did not had any.",
			EC_Categories.layers
	),

	/*******************************************
	 * General Output Errors
	 *******************************************/

	/** Implementation error output options for a no-layer process with illegal arguments (no arguments). */
	OUTPUT_NOLAYERS_ILLEGAL_ARGS__0(
			-400,
			"implementation error: output for no-layers with illegal arguments",
			"An implementation error processing output options for a no-layer process: with illegal arguments.",
			EC_Categories.output_general
	),



	/*******************************************
	 * Output File Errors
	 *******************************************/

	/**
	 * A required output file name is blank (no arguments).
	 */
	OUTPUT_FN_IS_BLANK__0(
			-420,
			"output filename is blank",
			"A required output file name is blank.",
			EC_Categories.output_fn
	),

	/**
	 * An output file name is the same as the input file name (2 arguments).
	 * First argument is the output file name, second is the input file name
	 */
	OUTPUT_FN_SAMEAS_FIN__2(
			-422, 2,
			"output <{}> same as input <{}>",
			"An output file name is the same as the input file name (output file name, input file name).",
			EC_Categories.output_fn
	),

	/**
	 * The output file name points to a directory (1 argument).
	 * The argument is the file name.
	 */
	OUTPUT_FN_IS_DIRECTORY__1(
			-423, 1,
			"output file <{}> exists but is a directory",
			"The output file name points to a directory (file name).",
			EC_Categories.output_fn
	),

	/**
	 * The output file exists but no overwrite option given in CLI (2 arguments).
	 * The first argument is the file name, the second the overwrite CLI option.
	 */
	OUTPUT_FN_EXISTS_NO_OVERWRITE_OPTION__2(
			-424, 2,
			"output file <{}> exists and no option {} used",
			"The output file exists but no overwrite option given in CLI (file name, required CLI option).",
			EC_Categories.output_fn
	),

	/**
	 * Output file exists but no permission (on file system) to write (1 argument).
	 * The argument is the file name
	 */
	OUTPUT_FN_EXISTS_CANNOT_WRITE__1(
			-425, 1,
			"output file <{}> exists but cannot write to it, please check permissions",
			"Output file exists but no permission (on file system) to write (file name).",
			EC_Categories.output_fn
	),



	/*******************************************
	 * Output Directory Errors
	 *******************************************/

	/**
	 * An output directory does not exist and no CLI option given to create directories (2 arguments).
	 * The first argument is the directory, the second the required CLI option.
	 */
	OUTPUT_DIR_NOTEXISTS_NO_CREATE_DIR_OPTION__2(
			-440, 2,
			"output directory <{}> does not exist and CLI option {} not used",
			"An output directory does not exist and no CLI option given to create directories (directory, requried CLI option).",
			EC_Categories.output_dir
	),

	/**
	 * An output directory exists but is not a directory (1 argument).
	 * The argument is the output directory.
	 */
	OUTPUT_DIR_IS_NOT_DIRECTORY__1(
			-441, 1,
			"output directory <{}> exists but is not a directory",
			"An output directory exists but is not a directory (output directory).",
			EC_Categories.output_dir
	),

	/**
	 * An output directory exists no permissions (on file system) to write to it (1 argument).
	 * The argument is the output directory.
	 */
	OUTPUT_DIR_EXISTS_CANNOT_WRITE__1(
			-442, 1,
			"output directory <{}> exists but cannot write to it, please check permissions",
			"An output directory exists no permissions (on file system) to write to it (output directory).",
			EC_Categories.output_dir
	),

	/**
	 * An output directory may contain target files and no overwrite option given in CLI (2 arguments).
	 * This first argument is the directory, the second is the target file extension.
	 */
	OUTPUT_DIR_MAY_HAVE_FN_NOOVERWRITE__2(
			-443, 2,
			"output directory <{}> may contain target files for extension <{}> and no overwrite in CLI",
			"An output directory may contain target files and no overwrite option given in CLI",
			EC_Categories.output_dir
	),


	/*******************************************
	 * Output Pattern Errors
	 *******************************************/

	/**
	 * The output pattern configuration resulted in a path only having a file extension (1 argument).
	 * The argument is the pattern.
	 */
	PATTERN_GEN_ONLY_FEXT__1(
			-460, 1,
			"output pattern <{}> only contains file extension, check options for generating fnout",
			"The output pattern configuration resulted in a path only containing a file extension. This can happen for instance when processing layers, not using a base name, and not using any other fout options (such as indexes and labels). Check those options.",
			EC_Categories.output_pattern
	),

	/**
	 * The output pattern configuration resulted in a path only having a directory and a file extension (1 argument).
	 * The argument is the pattern.
	 */
	PATTERN_GEN_ONLY_DIR_AND_FEXT__1(
			-461, 1,
			"output pattern <{}> only contains directory and file extension, check options for generating fnout",
			"The output pattern configuration resulted in a path only containing a directory and a file extension. This can happen for instance when processing layers, not using a base name, and not using any other fout options (such as indexes and labels). Check those options.",
			EC_Categories.output_pattern
	),


	/*******************************************
	 * Inkscape Executor Errors (file name)
	 *******************************************/

	/**
	 * The given executable for Inkscape was blank (1 argument).
	 * The argument is the Inkscape executable.
	 */
	INKSCAPE_EXEC_FN_BLANK__1(
			-500, 1,
			"expected Inkscape executable, found <{}>",
			"The given executable for Inkscape was blank (Inkscape executable).",
			EC_Categories.inkscape_exec_fn
	),

	/**
	 * The given executable for Inkscape does not exist (1 argument).
	 * The argument is the Inkscape executable.
	 */
	INKSCAPE_EXEC_FN_NOTEXIST__1(
			-501, 1,
			"Inkscape executable <{}> does not exist, please check path and filename",
			"The given executable for Inkscape does not exist (Inkscape executable).",
			EC_Categories.inkscape_exec_fn
	),

	/**
	 * The given executable for Inkscape is not a file (1 argument).
	 * The argument is the Inkscape executable.
	 */
	INKSCAPE_EXEC_FN_NOT_FILE__1(
			-503, 1,
			"Inkscape executable <{}> is not a file, please check path and filename",
			"The given executable for Inkscape is not a file (Inkscape executable).",
			EC_Categories.inkscape_exec_fn
	),

	/**
	 * The given executable for Inkscape cannot be executed (1 argument).
	 * The argument is the Inkscape executable.
	 */
	INKSCAPE_EXEC_FN_CANNOT_EXECUTE__1(
			-504, 1,
			"cannot execute input Inkscape executable <{}>, please file permissions",
			"The given executable for Inkscape cannot be executed (Inkscape executable).",
			EC_Categories.inkscape_exec_fn
	),



	/*******************************************
	 * Inkscape Executor Errors (execution)
	 *******************************************/

	/**
	 * An Inkscape executor did catch an I/O exception during execution (1 argument).
	 * The argument is the original exception message.
	 */
	INKSCAPE_EXEC_IO__1(
			-510, 1,
			"IO exception while executing Inkscape with error: {}",
			"An Inkscape executor did catch an I/O exception during execution (original exception message).",
			EC_Categories.inkscape_exec_exec
	),

	/**
	 * An Inkscape executor did get interrupted during execution (1 argument).
	 * The argument is the original exception message.
	 */
	INKSCAPE_EXEC_INTERRUPTED__1(
			-511, 1,
			"InterruptedException exception while executing Inkscape with error: {}",
			"An Inkscape executor did get interrupted during execution (original exception message).",
			EC_Categories.inkscape_exec_exec
	),


	;

	/** The numeric error code. */
	private final int code;

	/** The error message using `{}` for argument substitution. */
	private final String message;

	/** The number of expected arguments for substitution. */
	private final int args;

	/** A description for the error code. */
	private final String description;

	/** The error code category. */
	private final ErrorCodeCategory category;

	/**
	 * Creates a new error code with message and no arguments.
	 * @param code the error code, must be smaller than 0, in the range of the used category, and unique in the enumerate
	 * @param message the message, must not be blank
	 * @param description a description for the error code, must not be blank
	 * @param category the error code category, will be used for testing code, must not be null
	 */
	ErrorCodes(final int code, final String message, final String description, final ErrorCodeCategory category){
		this(code, 0, message, description, category);
	}

	/**
	 * Creates a new error code with message and optional arguments.
	 * @param code the error code, must be smaller than 0, in the range of the used category, and unique in the enumerate
	 * @param args the number of expected arguments
	 * @param message the message, must not be blank
	 * @param description a description for the error code, must not be blank
	 * @param category the error code category, will be used for testing code, must not be null
	 */
	ErrorCodes(final int code, final int args, final String message, final String description, final ErrorCodeCategory category){
		this.args = args;
		this.message = message;
		this.description = description;
		this.code = code;
		this.category = category;
	}

	/**
	 * Returns the number of expected arguments.
	 * @return number of expected arguments
	 */
	public int getArgs(){
		return this.args;
	}

	/**
	 * Returns the error message.
	 * @return error message
	 */
	public String getMessage(){
		return this.message;
	}

	/**
	 * Returns the description of the error code.
	 * @return error code description
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Creates a new error message with all arguments substituted.
	 * The length of `arguments` must be equal to the number of expected arguments of this error code.
	 * If the number of expected arguments is 0, then `args` can be null or of length 0.
	 * @param args the arguments, must have same length as expected arguments
	 * @return new error message
	 */
	public String getMessageSubstituted(final Object ... args){
		if(this.args>0){
			Validate.noNullElements(args);
			Validate.validState(args.length==this.args);
		}
		else{
			Validate.validState(args==null || args.length==0);
			return this.message;
		}

		final StrBuilder ret = new StrBuilder().append(this.message);
		for(final Object arg : args){
			ret.replaceFirst("{}", arg.toString());
		}
		return ret.toString();
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public ErrorCodeCategory getCategory() {
		return this.category;
	}
}
