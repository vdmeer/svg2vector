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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.vandermeer.execs.options.AO_DirectoryOut;
import de.vandermeer.execs.options.AO_FileOut;
import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.svg2vector.ErrorCodes;
import de.vandermeer.svg2vector.S2VExeception;
import de.vandermeer.svg2vector.applications.base.SvgTargets;

/**
 * Application output options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public final class OutputOptions {

	/** Extensions to remove from an input file name, `svg` and `svgz`. */
	public final static String[] EXTENSION_REMOVALS = new String[]{
			".svg",
			".svgz"
	};

	/** Application option for output file. */
	final private AO_FileOut aoFileOut = new AO_FileOut(false, 'o', "output file name, default is the basename of the input file plus target extension");

	/** Application option for output directory. */
	final private AO_DirectoryOut aoDirOut = new AO_DirectoryOut(false, 'd', "output directory, default value is the current directory");

	/** Application option to automatically create output directories. */
	final private AO_CreateDirectories aoCreateDirs = new AO_CreateDirectories();

	/** Application option to automatically overwrite existing files on output. */
	final private AO_OverwriteExisting aoOverwriteExisting = new AO_OverwriteExisting();

	/** List of application options. */
	private final ApplicationOption<?>[] options;

	/** Output file name without path elements (no layer mode only). */
	protected Path file;

	/** Output directory path. */
	protected Path directory;

	/** Target file extension. */
	protected String fileExtension;

	/** The pattern for output file names. */
	protected final OutputPattern pattern = new OutputPattern();

	/**
	 * Creates a new option object.
	 */
	public OutputOptions(){
		this.aoDirOut.setDefaultValue(System.getProperty("user.dir"));

		this.options = new ApplicationOption<?>[]{
			this.aoFileOut,
			this.aoDirOut,
			this.aoCreateDirs,
			this.aoOverwriteExisting
		};
	}

	/**
	 * Returns the settings for creating directories
	 * @return true if set, false otherwise
	 */
	public boolean createDirs(){
		return this.aoCreateDirs.inCli();
	}

	/**
	 * Returns the do-layers flag calculated from the options.
	 * Throws a state exception if output option where not yet set.
	 * @return true if layers can be processed, false otherwise
	 */
	public boolean doLayers(){
		Validate.validState(this.directory!=null);
		return (this.file==null);
	}

	/**
	 * Returns the output directory as path.
	 * Throws a state exception if output option where not yet set.
	 * @return output directory path, null if not set
	 */
	public Path getDirectory(){
		Validate.validState(this.directory!=null);
		return this.directory;
	}

	/**
	 * Returns the output file as path.
	 * Throws a state exception if output option where not yet set.
	 * @return output file path, null if not set
	 */
	public Path getFile(){
		Validate.validState(this.directory!=null);
		return this.file;
	}

	/**
	 * Returns the output file extension.
	 * Throws a state exception if output option where not yet set.
	 * @return output file extension, null if not set
	 */
	public String getFileExtension(){
		Validate.validState(this.directory!=null);
		return this.fileExtension;
	}

	/**
	 * Returns the message options as array.
	 * @return message options array
	 */
	public final ApplicationOption<?>[] getOptions(){
		return this.options;
	}

	/**
	 * Returns the pattern as string.
	 * @return pattern as string
	 */
	public String getPatternString(){
		return this.pattern.getPattern().build();
	}

	/**
	 * Checks for warnings and returns a list of warnings if options are set.
	 * Throws a state exception if output option where not yet set.
	 * @return list of warnings, empty if none found, exception if options have not yet been set
	 */
	public List<String> getWarnings(){
		Validate.validState(this.directory!=null);

		List<String> ret = new ArrayList<>();
		if(this.file==null){
			ApplicationOption<?>[] options = new ApplicationOption<?>[]{
				this.aoFileOut
			};
			for(ApplicationOption<?> ao : options){
				if(ao.inCli()){
					ret.add("layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
				}
			}
		}
		else{
			ApplicationOption<?>[] options = new ApplicationOption<?>[]{
			};
			for(ApplicationOption<?> ao : options){
				if(ao.inCli()){
					ret.add("no layers processed but CLI option <" + ao.getCliOption().getLongOpt() + "> used, will be ignored");
				}
			}
		}
		return ret;
	}

	/**
	 * Removes options from the output pattern.
	 * @param fout true to remove the output file name (not including the extension)
	 * @param index true if index option should be removed
	 * @param isIndex true if Inkscape index option should be removed
	 * @param isLabel true if Inkscape label option should be removed
	 * @throws S2VExeception if resulting pattern is not valid
	 */
	public void removePatternOptions(boolean fout, boolean index, boolean isIndex, boolean isLabel) throws S2VExeception{
		if(fout){
			pattern.pattern.replaceAll(OutputPattern.FILE_OUT, "");
		}
		if(index){
			pattern.pattern.replaceAll(OutputPattern.LAYER_INDEX, "");
		}
		if(isIndex){
			pattern.pattern.replaceAll(OutputPattern.IS_INDEX, "");
		}
		if(isLabel){
			pattern.pattern.replaceAll(OutputPattern.IS_LABEL, "");
		}
		this.pattern.testPattern();
	}

	/**
	 * Sets the options based on CLI settings and arguments.
	 * If successful
	 * 
	 * * the local directory path points to the output directory (null if none set)
	 * * the local file name will point to the file without file extension, only not in layer mode
	 * * the local file extension will have the target file extension
	 * 
	 * @param doLayers true if options should be set for layer mode, false otherwise
	 * @param target the target, must not be null
	 * @param inFilename the file name of the input file (with path), must not be blank
	 * @throws S2VExeception for any error
	 */
	public void setOptions(boolean doLayers, SvgTargets target, String inFilename) throws S2VExeception{
		Validate.notNull(target);
		Validate.notBlank(inFilename);

		if(doLayers){
			// we do layers, only outDir counts
			Path dirPath = FileSystems.getDefault().getPath(this.aoDirOut.getValue());
			this.testPath(dirPath, false);
			this.testDirectoryContent(dirPath, target);
			this.directory = dirPath;
			this.fileExtension = target.name();
		}
		else{
			// we do not do layers
			Path file = null;
			if(this.aoFileOut.inCli()){
				// first check the output file name
				if(StringUtils.isBlank(this.aoFileOut.getCliValue())){
					throw new S2VExeception(ErrorCodes.OUTPUT_FN_IS_BLANK__0);
				}
				file = FileSystems.getDefault().getPath(
						StringUtils.substringBeforeLast(this.aoFileOut.getCliValue(), "." + target.name())
				);
			}
			else{
				// use the file name of the input file, including directory information
				String fn = StringUtils.replaceEach(inFilename, EXTENSION_REMOVALS, new String[]{"", ""});
				file = FileSystems.getDefault().getPath(fn);
			}
			// file is set, no a given output directory overwrites the directory set
			if(this.aoDirOut.inCli()){
				Path ao = FileSystems.getDefault().getPath(this.aoDirOut.getCliValue());
				this.testPath(ao, false);
				file = FileSystems.getDefault().getPath(ao.toString(), file.getFileName().toString());
			}

			Validate.validState(file.getNameCount()>0);
			if(file.getNameCount()==1){
				//no path element, test file only with extension
				Path toTest = FileSystems.getDefault().getPath(file.getFileName() + "." + target.name());
				this.testIdentity(inFilename, toTest);
				this.testPath(toTest, true);
			}
			else{
				//path and file, use path and add file with extension then test
				Path toTest = FileSystems.getDefault().getPath(file.getParent().toString(), file.getFileName() + "." + target.name());
				this.testIdentity(inFilename, toTest);
				this.testPath(toTest, true);
			}

			if(file.getNameCount()==1){
				this.file = file;
			}
			else{
				this.directory = file.getParent();
				this.file = file.getFileName();
			}
			this.fileExtension = target.name();
		}
	}

	/**
	 * Sets the base name in the output pattern if not already set.
	 * @param basename the base name, ignored if blank
	 * @throws S2VExeception if resulting pattern is not valid
	 */
	public void setPatternBasename(String basename) throws S2VExeception{
		if(StringUtils.isBlank(basename)){
			return;
		}
		pattern.pattern.replaceAll(OutputPattern.FILE_OUT, basename);
		this.pattern.testPattern();
	}

	/**
	 * Tests a directory for files ending with the target extension.
	 * @param directory the directory, nothing tested if null or does not exist
	 * @param target the target, must not be null
	 * @throws S2VExeception for any error
	 */
	protected final void testDirectoryContent(Path directory, SvgTargets target) throws S2VExeception{
		if(directory==null){
			return;
		}
		Validate.notNull(target);

		File dirFile = directory.toFile();
		if(dirFile.exists()){
			for(File file : dirFile.listFiles()){
				if(file.getName().contains("." + target.name()) && !this.createDirs()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_DIR_MAY_HAVE_FN_NOOVERWRITE__2,
							directory.toString(),
							target.name()
					);
				}
			}
		}
	}

	/**
	 * Tests if an input file name is the same as an output file name.
	 * If they are the same, an exception is thrown, nothing happens otherwise
	 * @param in the input file name
	 * @param out the output file as path
	 * @throws S2VExeception if they are the same
	 */
	protected final void testIdentity(String in, Path out) throws S2VExeception{
		Validate.notBlank(in);
		Validate.notNull(out);

		Path pIn = FileSystems.getDefault().getPath(in);
		if(StringUtils.compare(pIn.toString(), out.toString())==0){
			throw new S2VExeception(
					ErrorCodes.OUTPUT_FN_SAMEAS_FIN__2,
					pIn.toFile(),
					out.toString()
			);
		}
	}

	/**
	 * Tests a path being either a directory or a file name (optional with path information).
	 * @param path the path to test for, must not be null
	 * @param hasFilename flag for testing path as a file name (true) or as a directory (false)
	 * @throws S2VExeception in any error case
	 */
	protected final void testPath(Path path, boolean hasFilename) throws S2VExeception{
		Validate.notNull(path);

		Path testDir = FileSystems.getDefault().getPath(path.toString());
		Path testFile = FileSystems.getDefault().getPath(path.toString());

		if(hasFilename){
			testDir = (path.getNameCount()>1)?FileSystems.getDefault().getPath(path.getParent().toString()):null;
		}
		else{
			testFile = null;
		}

		if(testDir!=null){
			// test directory first
			File dirFile = testDir.toFile();
			if(dirFile.exists()){
				if(!dirFile.isDirectory()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_DIR_IS_NOT_DIRECTORY__1,
							testDir.toString()
					);
				}
				else if(!dirFile.canWrite()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_DIR_EXISTS_CANNOT_WRITE__1,
							testDir.toString()
					);
				}
			}
			else{
				if(!this.aoCreateDirs.inCli()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_DIR_NOTEXISTS_NO_CREATE_DIR_OPTION__2,
							testDir.toString(),
							this.aoCreateDirs.getCliOption().getLongOpt()
					);
				}
			}
		}
		if(testFile!=null){
			// test file next
			File fileFile = testFile.toFile();
			if(fileFile.exists()){
				if(fileFile.isDirectory()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_FN_IS_DIRECTORY__1,
							fileFile.toString()
					);
				}
				else if(!this.aoOverwriteExisting.inCli()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_FN_EXISTS_NO_OVERWRITE_OPTION__2,
							fileFile.toString(),
							this.aoOverwriteExisting.getCliOption().getLongOpt()
					);
				}
				else if(!fileFile.canWrite()){
					throw new S2VExeception(
							ErrorCodes.OUTPUT_FN_EXISTS_CANNOT_WRITE__1,
							fileFile.toString()
					);
				}
			}
		}
	}

	/**
	 * Returns the output pattern.
	 * @return output pattern
	 */
	public OutputPattern getPattern(){
		return this.pattern;
	}
}
