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

package de.vandermeer.svg2vector.applications.is;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import de.vandermeer.svg2vector.applications.core.S2VExeception;

/**
 * Utility to handle (create, list, delete) temporary artifacts (files and directories).
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class TmpArtefacts {

	/** Temporary directory prefix as `s2vis-[date]-`. */
	public final static String TMP_DIR_PREFIX = "s2vis-" + LocalDate.now() + "-";

	/** Path object for the temporary directory. */
	private Path tmpDir;

	/** List of temporary created files. */
	private final List<Path> tmpFiles;

	/** List of temporary files simulated. */
	private final List<Path> tmpFilesSimulated;

	/** Simulate flag, no file system operation will be done if true. */
	private final boolean simulate;

	/**
	 * Creates a new temporary artifacts object.
	 * @throws IOException if temporary directory could not be created
	 */
	public TmpArtefacts() throws IOException{
		this(false);
	}

	/**
	 * Creates a new temporary artifacts object.
	 * @param simulate set true if all file system operations should be only simulated
	 * @throws IOException if temporary directory could not be created
	 */
	public TmpArtefacts(final boolean simulate) throws IOException{
		this.simulate = simulate;
		this.tmpFiles = new ArrayList<>();
		this.tmpFilesSimulated = new ArrayList<>();

		if(!this.simulate){
			this.tmpDir = Files.createTempDirectory(TMP_DIR_PREFIX);
		}
		else{
			this.tmpDir = FileSystems.getDefault().getPath("TMP_DIR", TMP_DIR_PREFIX);
		}
	}

	/**
	 * Creates a new temporary file and writes lines into it.
	 * @param extension file extension, can be null
	 * @param lines strings to write into the file, cannot be null or have null elements
	 * @throws IOException if an IO error occurred
	 * @throws NullPointerException if lines was null, or if no temporary directory was created or already deleted
	 * @throws IllegalArgumentException if lines had null members
	 * @throws IOException if creation of temporary file went wrong or writing to the file went wrong
	 */
	public void createTempFile(final String filename, final ArrayList<String> lines) throws IOException{
		Validate.notNull(this.tmpDir);
		Validate.noNullElements(lines);

		if(!this.simulate){
			Path path = Files.createFile(FileSystems.getDefault().getPath(this.tmpDir.toString(), filename));
			this.tmpFiles.add(path);
			Files.write(path, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		}
		else{
			Path path = FileSystems.getDefault().getPath(this.tmpDir.toString(), filename);
			this.tmpFilesSimulated.add(path);
		}
	}

	/**
	 * Executes Inkscape for an input SVG file creating a temporary file.
	 * @param cmd the Inkscape command, must not be null
	 * @param fin the input SVG file for Inkscape
	 * @param fout name of the output file without path information
	 * @throws NullPointerException if command, input file, or output file was null, or if no temporary directory was created or already deleted
	 * @throws IllegalArgumentException if input file or output file was blank
	 * @throws IOException if the temporary file could not be created
	 * @throws S2VExeception if Inkscape execution failed
	 */
	public void createTempFile(IsExecutor cmd, final String fin, String fout) throws IOException, S2VExeception{
		Validate.notNull(this.tmpDir);
		Validate.notNull(cmd);
		Validate.notBlank(fin);
		Validate.notBlank(fout);

		Path path = FileSystems.getDefault().getPath(this.tmpDir.toString(), fout);
		if(!this.simulate){
			path = Files.createFile(path);
			this.tmpFiles.add(path);
			cmd.executeInkscape(fin, path.toString());
		}
		else{
			this.tmpFilesSimulated.add(path);
		}
	}

	/**
	 * Deletes all created temporary artifacts.
	 * @throws IOException if an IO error occurred during deletion
	 */
	public void deleteArtifacts() throws IOException{
		if(this.simulate){
			this.tmpFilesSimulated.clear();
		}
		else{
			for(Path path : this.tmpFiles){
				Files.delete(path);
			}
			this.tmpFiles.clear();
			Files.delete(this.tmpDir);
		}
		this.tmpDir = null;
	}

	/**
	 * Deletes all temporary artifacts and handles exceptions.
	 * @param printTrace true to print a stack trace if exceptions are caught
	 */
	public void deleteArtifactsFinally(boolean printTrace) {
		try{
			this.deleteArtifacts();
		}
		catch(Exception ex){
			if(printTrace){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Returns the name of the temporary directory.
	 * @return name of the temporary directory
	 * @throws NullPointerException if no temporary directory was created or already deleted
	 */
	public String getTmpDirName(){
		Validate.notNull(this.tmpDir);
		return this.tmpDir.toString();
	}

	/**
	 * Returns the size of the temporary file list, that is the number of created temporary file.
	 * @return size of the temporary file list, 0 if none created
	 */
	public int size(){
		return this.tmpFiles.size();
	}

	/**
	 * Returns the list of created temporary files.
	 * @return list of created temporary files
	 */
	public List<Path> getTmpFileList(){
		return this.tmpFiles;
	}

	/**
	 * Returns the size of the simulated temporary file list, that is the number of created temporary file.
	 * @return size of the simulated temporary file list, 0 if none created
	 */
	public int sizeSimulated(){
		return this.tmpFilesSimulated.size();
	}

	/**
	 * Returns the simulated list of created temporary files.
	 * @return simulated list of created temporary files
	 */
	public List<Path> getSimulatedTmpFileList(){
		return this.tmpFilesSimulated;
	}
}
