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

package de.vandermeer.svg2vector.applications.base.output;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messages.errors.Templates_OutputFile;

/**
 * Pattern for generating output file names.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public final class OutputPattern {

	/** Substitution pattern for layers setting output directory name `${dn}`. */
	public final static String DIR_OUT = "${dn}";

	/** Substitution pattern for layers setting output file name `${fn}`. */
	public final static String FILE_OUT = "${fn}";

	/** Substitution pattern for layers setting output file name extension `${fext}`. */
	public final static String FILE_EXT = "${fext}";

	/** Substitution pattern for layers using a continuous index `${index}`. */
	public final static String LAYER_INDEX = "${index}";

	/** Substitution pattern for layers using an Inkscape label `${is-label}`. */
	public final static String IS_LABEL = "${is-label}";

	/** Substitution pattern for layers using an Inkscape layer index `${is-index}`. */
	public final static String IS_INDEX = "${is-index}";

	/** The pattern for output file names when in layer mode. */
	protected final StrBuilder pattern;

	/**
	 * Creates a new pattern object.
	 * Layer and index patterns are used in the following order.
	 * 
	 * . Index, for a continuous index using {@link #LAYER_INDEX}
	 * . Inkscape index, for an index generated from Inkscape layers, using {@link #IS_INDEX}
	 * . Inkscape label, for a label generated from Inkscape layers, using {@link #IS_LABEL}
	 */
	public OutputPattern(){
		this.pattern = new StrBuilder();
		this.pattern.append(DIR_OUT);
		this.pattern.append(File.separator);
		this.pattern.append(FILE_OUT);
		this.pattern.append(LAYER_INDEX).append(IS_INDEX).append(IS_LABEL);
		this.pattern.append('.').append(FILE_EXT);
	}

	/**
	 * Generates an output path based on the arguments.
	 * Any existing option (indexes and labels) will be separated by a `-`.
	 * Any pattern not used will be removed, including extra characters associated to the pattern (e.g. file separator for output directory).
	 * The resulting path will be tested to be valid.
	 * Invalid paths are:
	 * 
	 * * only file extension
	 * * directory and only file extension
	 * 
	 * @param directory the directory, ignored if null or blank path
	 * @param filename the file name, ignored if null or blank path, only filename of the path being used
	 * @param extension the extension, ignored of blank
	 * @param index the continuous index, ignored if smaller than 1
	 * @param entry the entry for Inkscape label and index, ignored if null or key was null or value was smaller than 0
	 * @return a file name, all non-used patterns will be removed
	 * @throws ApplicationException if resulting path is not valid as tested by {@link #testPattern(String)}
	 */
	public Path generateName(Path directory, Path filename, String extension, int index, Entry<String, Integer> entry) throws ApplicationException{
		StrBuilder sb = new StrBuilder().append(this.pattern.toCharArray());
		Map<String, String> map = new HashMap<>();

		if(directory!=null && !StringUtils.isBlank(directory.toString())){
			map.put(StringUtils.substringBetween(DIR_OUT, "{", "}"), directory.toString());
		}
		else{
			sb.replaceAll(DIR_OUT + File.separator, "");
		}

		if(filename!=null && !StringUtils.isBlank(filename.getFileName().toString())){
			map.put(StringUtils.substringBetween(FILE_OUT, "{", "}"), filename.getFileName().toString());
		}
		else{
			sb.replaceAll(FILE_OUT, "");
		}

		if(!StringUtils.isBlank(extension)){
			map.put(StringUtils.substringBetween(FILE_EXT, "{", "}"), extension);
		}
		else{
			sb.replaceAll("." + FILE_EXT, "");
		}

		if(index>0){
			map.put(StringUtils.substringBetween(LAYER_INDEX, "{", "}"), String.format("%02d", index));
		}
		else{
			sb.replaceAll(LAYER_INDEX, "");
		}

		if(entry!=null && entry.getKey()!=null && entry.getValue()>-1){
			map.put(StringUtils.substringBetween(IS_LABEL, "{", "}"), entry.getKey());
			map.put(StringUtils.substringBetween(IS_INDEX, "{", "}"), String.format("%02d", entry.getValue()));
		}
		else{
			sb.replaceAll(IS_INDEX, "");
			sb.replaceAll(IS_LABEL, "");
		}

		sb.replaceAll("}${", "}-${");
		String ret = new StrSubstitutor(map).replace(sb);
		this.testPattern(ret);

		return FileSystems.getDefault().getPath(ret);
	}

	/**
	 * Returns a copy of the pattern.
	 * @return copy of the pattern
	 */
	public StrBuilder getPattern(){
		return new StrBuilder().append(this.pattern.toCharArray());
	}

	/**
	 * Tests the current pattern using {@link #testPattern(String)}.
	 * @throws ApplicationException if pattern is not valid
	 */
	public void testPattern() throws ApplicationException{
		this.testPattern(this.pattern.build());
	}

	/**
	 * Tests a pattern string for validity.
	 * Invalid means that no actual file name is used but a file extension is in the pattern
	 * @param test input string, must not be blank
	 * @throws ApplicationException if resulting path is not valid
	 */
	public void testPattern(String test) throws ApplicationException{
		Validate.notBlank(test);
		if(test.startsWith(".")){
			throw new ApplicationException(
					Templates_OutputFile.FN_PATTERN_ONLY_EXT,
					this.getClass().getSimpleName(),
					test
			);
		}
		if(test.contains(File.separator + ".")){
			throw new ApplicationException(
					Templates_OutputFile.FN_PATTERN_ONLY_DIREXT,
					this.getClass().getSimpleName(),
					test
			);
		}
	}

	@Override
	public String toString(){
		return this.pattern.toString();
	}
}
