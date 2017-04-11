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

package de.vandermeer.svg2vector.applications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;

import de.vandermeer.svg2vector.applications.base.AppBase;
import de.vandermeer.svg2vector.applications.base.AppProperties;
import de.vandermeer.svg2vector.applications.options.AO_ExportDpi;
import de.vandermeer.svg2vector.applications.options.AO_ExportPdfVersion;
import de.vandermeer.svg2vector.applications.options.AO_ExportPsLevel;
import de.vandermeer.svg2vector.applications.options.AO_InkscapeExecutable;
import de.vandermeer.svg2vector.applications.options.AO_ManualLayers;
import de.vandermeer.svg2vector.applications.options.AO_SvgFirst;
import de.vandermeer.svg2vector.converters.SvgTargets;
import de.vandermeer.svg2vector.loaders.StandardLoader;

/**
 * The Svg2Vector application using an Inkscape executable.
 * It an SVG graphic to a vector format and to PNG as per Inkscape.
 * The tool does support SVG and SVGZ input formats from file or URI.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Svg2Vector_IS extends AppBase<StandardLoader, AppProperties<StandardLoader>> {

	/** Application name. */
	public final static String APP_NAME = "s2v-is";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector Inkscape";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8";

	/** Application option for the Inkscape executable. */
	AO_InkscapeExecutable optionInkscapeExec = new AO_InkscapeExecutable(true, 'x', "full path to the Inkscape executable");

	/** Application option to set DPI for bitmap export. */
	AO_ExportDpi optionExpDpi = new AO_ExportDpi(false);

	/** Application option to set PS level for PS export. */
	AO_ExportPsLevel optionExpPslevel = new AO_ExportPsLevel(false);

	/** Application option to set PDF version for PDF export. */
	AO_ExportPdfVersion optionExpPdfver = new AO_ExportPdfVersion(false);

	/** Application option to require SVG transformation first, then do the actual target transformation. */
	AO_SvgFirst optionSvgFirst = new AO_SvgFirst(false, 'g', "requires the tool to generate temporary SVG files first and then us those files to generate the actual target");

	/** Application option to manage layers manually when creating a temporary directory. */
	AO_ManualLayers optionManualLayers = new AO_ManualLayers(false, 'm', "layers are switched off/on on a raw text file, i.e. not using any SVG or XML library");

	/** Path object for the temporary directory. */
	Path tmpDir;

	/** File for a temporary created SVG file. */
	Path tmpFile;

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_IS(){
		super(new AppProperties<StandardLoader>(SvgTargets.values(), new StandardLoader()));

		this.addOption(this.optionExpDpi);
		this.addOption(this.optionExpPdfver);
		this.addOption(this.optionExpPslevel);
		this.addOption(this.optionSvgFirst);
		this.addOption(this.optionManualLayers);

		this.addOption(this.optionInkscapeExec);
	}

	@Override
	public int executeApplication(String[] args) {
		int ret = super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		SvgTargets target = this.getProps().getTarget();

		String fn = this.optionInkscapeExec.getValue();
		if(StringUtils.isBlank(fn)){
			this.printErrorMessage("expected Inkscape executable, found <" + fn + ">");
			return -20;
		}
		File testFD = new File(fn);
		if(!testFD.exists()){
			this.printErrorMessage("Inkscape executable <" + fn + "> does not exist, please check path and filename");
			return -21;
		}
		if(!testFD.isFile()){
			this.printErrorMessage("Inkscape executable <" + fn + "> is not a file, please check path and filename");
			return -22;
		}
		if(!testFD.canExecute()){
			this.printErrorMessage("cannot execute input Inkscape executable <" + fn + ">, please file permissions");
			return -23;
		}

		StrBuilder isCmd = new StrBuilder();
		if(fn.contains("\"")){
			isCmd.clear().append('"');
		}
		isCmd.append(fn);
		if(fn.contains("\"")){
			isCmd.clear().append('"');
		}
		isCmd.append(' ').append("--without-gui --export-area-page");
		if(this.getProps().doesTextAsShape()){
			isCmd.append(" --export-text-to-path");
		}
		isCmd.append(" --file=${fin}");

		StrBuilder isTmpCmd = new StrBuilder();
		isTmpCmd.append(isCmd.toCharArray());
		isTmpCmd.append(" --").append(SvgTargets.svg.getIsCliLong()).append("=${fout}");

		isCmd.append(" --").append(target.getIsCliLong()).append("=${fout}");
		switch(target){
			case pdf:
				if(this.optionExpPdfver.inCli()){
					isCmd.append(" --export-pdf-version").append(this.optionExpPdfver.getValue());
				}
				break;
			case png:
				if(this.optionExpDpi.inCli()){
					isCmd.append(" --export-dpi").append(this.optionExpDpi.getValue());
				}
				break;
			case ps:
				if(this.optionExpPslevel.inCli()){
					isCmd.append(" --export-ps-level").append(this.optionExpPslevel.getValue());
				}
				break;
			case emf:
			case eps:
			case svg:
			case wmf:
			default:
				break;
		}

		if(this.optionSvgFirst.inCli()){
			if(this.getProps().doesLayers()){
				try{
					this.tmpDir = Files.createTempDirectory("s2v-");
				}
				catch (IOException e) {
					this.printErrorMessage("problem creating temporary directory with error: " + e.getMessage());
					return -60;
				}
				this.printProgressMessage("temp directory:   " + this.tmpDir);

				StandardLoader loader = this.getProps().getLoader();
				if(this.optionManualLayers.inCli()){
					for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
						loader.switchOffAllLayers();
						loader.switchOnLayer(entry.getKey());
						String err = this.write(this.tmpDir.toString() + "/" + this.getProps().getFnOutNoDir(entry) + ".svg", loader.getLines());
						if(err!=null){
							this.printErrorMessage(err);
							return -99;//TODO
						}
					}
				}
				else{
					for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
						String fout = this.tmpDir.toString() + "/" + this.getProps().getFnOutNoDir(entry) + ".svg";
						String nodeId = "layer" + entry.getValue().toString();
						StrBuilder nodeCmd = new StrBuilder();
						nodeCmd.append(isTmpCmd.toCharArray())
							.append(" -j -i=").append(nodeId)
							.append(" --select=").append(nodeId);
						;
						this.ExecInkscape(nodeCmd, this.getProps().getFinFn(), fout);
						if(ret<0){
							return ret;
						}
					}
				}
			}
			else{
				try{
					this.tmpFile = Files.createTempFile("s2v", null);
				}
				catch (IOException e) {
					this.printErrorMessage("problem creating temporary file with error: " + e.getMessage());
					return -60;
				}
//				this.printProgress("temp file:   " + this.tmpFile);
//				this.printProgress(" -- generating temporary SVG");
				this.ExecInkscape(isTmpCmd, this.getProps().getFinFn(), this.tmpFile.toString());
				if(ret<0){
					return ret;
				}
			}
		}

		if(this.tmpDir!=null){
			for (final File fileEntry : this.tmpDir.toFile().listFiles()) {
				if(fileEntry.isFile()){
					String finTmp = this.tmpDir + "/" + fileEntry.getName();
					String fout = this.getProps().getDout() + "/" + StringUtils.substringBefore(fileEntry.getName(), ".svg") + "." + target.name();
					//this.printProgress(" -- converting temporary layer SVG");
					this.ExecInkscape(isCmd, finTmp, fout);
				}
			}
		}
		else if(this.tmpFile!=null){
System.err.println(this.getProps().getFoutFn());
//			this.ExecInkscape(isCmd, this.tmpFile.toString(), this.optionDirOut.getValue() + this.optionFileOut.getValue() + "." + target.name());
		}
//		else{
//			if(this.optionOnePerLayer.inCli()){
//				for(Node node : this.layers){
//					String fout = this.optionDirOut.getValue() + this.genOutFilename(node, this.layers.indexOf(node)) + "." + target.name();
//					String nodeId = SvgDocumentLoader.getID(node);
//					StrBuilder nodeCmd = new StrBuilder();
//					nodeCmd.append(isCmd.toCharArray())
//						.append(" -j -i=").append(nodeId)
//						.append(" --select=").append(nodeId);
//					;
//					this.ExecInkscape(nodeCmd, fin, fout);
//					if(ret<0){
//						return ret;
//					}
//				}
//			}
//			else{
//				this.ExecInkscape(isCmd, fin, this.optionDirOut.getValue() + this.optionFileOut.getValue() + "." + target.name());
//			}
//		}

//		if(this.tmpDir!=null){
//			for (final File fileEntry : this.tmpDir.toFile().listFiles()) {
//				fileEntry.delete();
//			}
//			this.tmpDir.toFile().delete();
//		}
//		if(this.tmpFile!=null){
//			this.tmpFile.toFile().delete();
//		}

		return 0;
	}

	/**
	 * Writes lines of an list to a file.
	 * @param fn the name of the file
	 * @param lines the lines to write to the file
	 * @return null on success, error message on error
	 */
	public String write(String fn, ArrayList<String> lines){
		if(StringUtils.isBlank(fn)){
			return "write: file name was blank";
		}
		if(lines==null){
			return "write: lines was null";
		}
		if(lines.size()==0){
			return "write: size of lines was 0";
		}

		FileWriter writer;
		try {
			writer = new FileWriter(fn);
		}
		catch (IOException e) {
			return "IO error creating file writer: " + e.getMessage();
		} 

		try {
			for(String str: lines) {
				writer.write(str);
			}
			writer.close();
		}
		catch (IOException e) {
			return "IO error writing to file <" + fn + "> or closing writer: " + e.getMessage();
		}
		return null;
	}

	public int ExecInkscape(StrBuilder cmd, String fin, String fout){
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("fin", fin);
		valuesMap.put("fout", fout);
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String cli = sub.replace(cmd.toString());

		try {
			this.printProgressMessage("      - from: " + fin);
			this.printProgressMessage("      - to:   " + fout);
			this.printProgressMessage("      - cli:  " + cli);
			Process p = Runtime.getRuntime().exec(cli);
			p.waitFor();
		}
		catch (IOException e) {
			this.printErrorMessage("IO exception while executing Inkscape with error: " + e.getMessage());
			return -110;
		}
		catch (InterruptedException e) {
			this.printErrorMessage("InterruptedException exception while executing Inkscape with error: " + e.getMessage());
			return -111;
		}
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
		return "Converts SVG graphics into other vector formats using Inkscape, with options for handling layers";
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}
}
