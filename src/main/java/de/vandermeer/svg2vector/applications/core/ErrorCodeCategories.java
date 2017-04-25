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

/**
 * Categories for error codes.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public enum ErrorCodeCategories {

	/** General error codes. */
	general(
			-1, -49,
			"General Errors",
			"Category for all errors that do not fit into a specialized category. These are unexpected exceptions (e.g. null pointer, I/O) and internal implementation errors (aka bugs)."
	),

	/** Target error codes. */
	target(
			-50, -99,
			"Target Errors",
			"Category for all errors regarding the target. Those errors cover any reason to reject a given target: blank, not suported, etc."
	),

	/** Input error codes. */
	input_file(
			-100, -199,
			"Input File Errors",
			"Category for all errors that can occur processing the input file, which contains the SVG document to convert. The given file migh not exist, not be readable, etc."
	),

	/** Batik loader error codes. */
	loader_batik(
			-210, -219,
			"Apache Batik Loader Errors",
			"Category for the loader that uses Apache Batik to load an SVG document. Errors cover files that cannot be read and problems setting the document size."
	),

	/** Inkscape loader error codes. */
	loader_inkscape(
			-220, -229,
			"Inkscape Loader Errors",
			"Category for the loader used in the Inkscape applications. This loader tries to read a file 'manually', so here errors can be related to (beside file not found) encoding and compression."
	),

	/** Layer Errors. */
	layers(
			-300, -319,
			"Layer Errors",
			"Category for errors when an application is in layer mode. Core problem here are SVG document that do not have layers."
	),

	/** General Output Errors. */
	output_general(
			-400, -419,
			"General Output Errors",
			"Category for general output errors, currently only some implementation errors (bugs) identified at runtime."
	),

	/** Output File Errors. */
	output_fn(
			-420, -439,
			"Output File Errors",
			"Category for problems with an output filename, such as file does not exist, is of wrong type, cannot be written to, etc."
	),

	/** Output Directory Errors. */
	output_dir(
			-440, -459,
			"Output Directory Errors",
			"Category for problems with an output directory, such as wrong type, does not exist, etc."
	),

	/** Output Pattern Errors. */
	output_pattern(
			-460, -469,
			"Output Pattern Errors",
			"Category to for problems with a generated pattern for output filenames. This pattern is generated from various different combinations of CLI arguments. At runtime, problems with the pattern can still exist."
	),

	/** Inkscape Executor Errors (file name). */
	inkscape_exec_fn(
			-500, -509,
			"Inkscape Executor Errors (file name)",
			"Category to for problems with the filename of the Inkscape executable in an Inkscape application, such as file does not exist, is not executable, etc."
	),

	/** Inkscape Executor Errors (execution). */
	inkscape_exec_exec(
			-510, -519,
			"Inkscape Executor Errors (execution)",
			"Category to for problems when executing Inkscape. Those problems are mostly related to exceptions (e.g. I/O) and interruptions."
	)

	;

	/** Category title. */
	private final String title;

	/** Start for error codes (minimum error code). */
	private final int start;

	/** End for error codes (maximum error code). */
	private final int end;

	/** Category description. */
	private String description;

	/**
	 * Creates a new category.
	 * @param start category start
	 * @param end category end
	 * @param title category title, must not be blank
	 */
	ErrorCodeCategories(final int start, final int end, final String title, String description){
		Validate.notBlank(title);
		Validate.notBlank(description);

		this.start = start;
		this.end = end;
		this.title = title;
		this.description = description;
	}

	/**
	 * Returns the category title.
	 * @return category title
	 */
	public String getTitle(){
		return this.title;
	}

	/**
	 * Returns the category description.
	 * @return category description
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Returns the start of the error codes.
	 * @return error code start
	 */
	public int getStart(){
		return this.start;
	}

	/**
	 * Returns the end of the error codes.
	 * @return error code end
	 */
	public int getEnd(){
		return this.end;
	}

	/**
	 * Prints a string representation of the category including all members.
	 * @return string representation of the category
	 */
	@Override
	public String toString(){
		return this.name() + ": " + this.getTitle() + " (" + this.getStart() + ", " + this.getEnd() + ")";
	}
}
