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

/**
 * Categories for error codes.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public enum ErrorCodeCategories {

	/** General error codes. */
	general(-1, -49, "General Errors"),

	/** Target error codes. */
	target(-50, -99, "General Errors"),

	/** Input error codes. */
	input(-100, -199, "Input File Errors"),

	/** Inkscape loader error codes. */
	loader_batik(-210, -219, "Apache Batik Loader Errors"),

	/** Batik loader error codes. */
	loader_inkscape(-220, -229, "Inkscape Loader Errors"),

	/** Layer Errors. */
	layers(-300, -319, "Layer Errors"),

	/** General Output Errors. */
	output_general(-400, -419, "General Output Errors"),

	/** Output File Errors. */
	output_fn(-420, -439, "Output File Errors"),

	/** Output Directory Errors. */
	output_dir(-440, -459, "Output Directory Errors"),

	/** Output Pattern Errors. */
	output_pattern(-460, -469, "Output Pattern Errors"),

	/** Inkscape Executor Errors (file name). */
	inkscape_exec_fn(-500, -509, "Inkscape Executor Errors (file name)"),

	/** Inkscape Executor Errors (execution). */
	inkscape_exec_exec(-510, -519, "Inkscape Executor Errors (execution)")

	;

	/** Category description. */
	private final String description;

	/** Start for error codes (minimum error code). */
	private final int start;

	/** End for error codes (maximum error code). */
	private final int end;

	/**
	 * Creates a new category.
	 * @param start category start
	 * @param end category end
	 * @param description category description, must not be blank
	 */
	ErrorCodeCategories(final int start, final int end, final String description){
		this.start = start;
		this.end = end;
		this.description = description;
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
		return this.name() + ": " + this.getDescription() + " (" + this.getStart() + ", " + this.getEnd() + ")";
	}
}
