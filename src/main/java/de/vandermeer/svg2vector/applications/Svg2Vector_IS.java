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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.w3c.dom.Node;

import de.vandermeer.svg2vector.applications.options.AO_ExportDpi;
import de.vandermeer.svg2vector.applications.options.AO_ExportPdfVersion;
import de.vandermeer.svg2vector.applications.options.AO_ExportPsLevel;
import de.vandermeer.svg2vector.applications.options.AO_InkscapeExecutable;
import de.vandermeer.svg2vector.applications.options.AO_NotTextAsShape;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndex;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndexId;
import de.vandermeer.svg2vector.converters.SvgTargets;
import de.vandermeer.svg2vector.loaders.BatikLoader;
import de.vandermeer.svg2vector.loaders.SvgDocumentLoader;

/**
 * The Svg2Vector application using an Inkscape executable.
 * It an SVG graphic to a vector format and to PNG as per Inkscape.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * The application calls its FreeHep counterpart to create plain SVG, and then Inkscape to create the target format.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public class Svg2Vector_IS extends AppBase {

	/** Standard Inkscape arguments. */
	String stdIsArgs = "--without-gui --export-area-page";

	/** Application name. */
	public final static String APP_NAME = "s2v-is";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector Inkscape";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v1.1.0 build 170405 (05-Apr-17) for Java 1.8";

	/** Application option for use-layer-index mode. */
	AO_UseLayerIndex optionUseLayerIndex = new AO_UseLayerIndex(false, 'i', "use layer index for inkscape layer processing, default is layer ID");

	/** Application option for use-layer-index-id mode. */
	AO_UseLayerIndexId optionUseLayerIndexId = new AO_UseLayerIndexId(false, 'I', "use layer index and label (if exists) ID otherwise for inkscape layer processing, default is layer ID");

	/** Application option for not-text-as-shape mode. */
	AO_NotTextAsShape optionNotTextAsShape = new AO_NotTextAsShape(false, 's', "switch of text-as-shape property");

	/** Application option for the Inkscape executable. */
	AO_InkscapeExecutable optionInkscapeExec = new AO_InkscapeExecutable(true, 'x', "full path to the Inkscape executable");

	/** Application option to set DPI for bitmap export. */
	AO_ExportDpi optionExpDpi = new AO_ExportDpi(false);

	/** Application option to set PS level for PS export. */
	AO_ExportPsLevel optionExpPslevel = new AO_ExportPsLevel(false);

	/** Application option to set PDF version for PDF export. */
	AO_ExportPdfVersion optionExpPdfver = new AO_ExportPdfVersion(false);

	/** Path object for the temporary directory. */
	Path tmpDir;

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_IS(){
		super(SvgTargets.values());
		this.addOption(this.optionUseLayerIndex);
		this.addOption(this.optionUseLayerIndexId);
		this.addOption(this.optionNotTextAsShape);
		this.addOption(this.optionExpDpi);
		this.addOption(this.optionExpPdfver);
		this.addOption(this.optionExpPslevel);

		this.addOption(this.optionInkscapeExec);
	}

	@Override
	public int executeApplication(String[] args) {
		int ret = super.executeApplication(args);
		if(ret!=0){
			return ret;
		}

		String fn = this.optionInkscapeExec.getValue();
		File testFD = new File(fn);
		if(!testFD.exists()){
			this.printError("Inkscape executable <" + fn + "> does not exist, please check path and filename");
			return -3;
		}
		if(!testFD.isFile()){
			this.printError("Inkscape executable <" + fn + "> is not a file, please check path and filename");
			return -4;
		}
		if(!testFD.canExecute()){
			this.printError("cannot execute input Inkscape executable <" + fn + ">, please file permissions");
			return -5;
		}

		//TODO print other options here

		if(this.optionOnePerLayer.inCli()){
			if((ret = this.createTmpSvgs())!=0){
				return ret;
			}
		}

		SvgTargets target = this.optionTarget.getTarget();
		if(this.tmpDir==null){
			String fin = this.optionUriIn.getURI().getPath();
			if(fin.startsWith("/")){
				fin = StringUtils.substringAfter(fin, "/");
			}
			String fout = this.optionDirOut.getValue() + this.optionFileOut.getValue() + "." + target.name();
			this.printProgress(" -- converting single SVG");
			this.ExecuteInkscape(fin, fout);
			this.printProgress("      - done");
		}
		else{
			for (final File fileEntry : this.tmpDir.toFile().listFiles()) {
				if(fileEntry.isFile()){
					String fin = this.tmpDir + "/" + fileEntry.getName();
					String fout = this.optionDirOut.getValue() + StringUtils.substringBefore(fileEntry.getName(), ".svg") + "." + target.name();
					this.printProgress(" -- converting temporary layer SVG");
					this.ExecuteInkscape(fin, fout);
//					fileEntry.delete();
					this.printProgress("      - done, tmp file deleted");
				}
			}
//			this.tmpDir.toFile().delete();
		}

		return 0;
	}

	public int ExecuteInkscape(String fin, String fout){
		StrBuilder cmd = new StrBuilder();
		cmd.append("\"" + this.optionInkscapeExec.getCliValue() + "\"")
			.append(' ')
			.append(this.stdIsArgs)
		;
		if(!this.optionNotTextAsShape.inCli()){
			cmd.append(" --export-text-to-path");
		}
		if(this.optionExpDpi.inCli()){
			cmd.append(" --export-dpi").append(this.optionExpDpi.getValue());
		}
		if(this.optionExpPdfver.inCli()){
			cmd.append(" --export-pdf-version").append(this.optionExpPdfver.getValue());
		}
		if(this.optionExpPslevel.inCli()){
			cmd.append(" --export-ps-level").append(this.optionExpPslevel.getValue());
		}

		cmd
			.append(" --file=")
			.append(fin)
			.append(" --")
			.append(this.optionTarget.getTarget().getIsCliLong())
			.append('=')
			.append(fout)
		;
		try {
			this.printProgress("      - from: " + fin);
			this.printProgress("      - to:   " + fout);
			this.printProgress("      - cli:  " + cmd);
			Process p = Runtime.getRuntime().exec(cmd.toString());
			p.waitFor();
		}
		catch (IOException e) {
			this.printError("IO exception while executing Inkscape with error: " + e.getMessage());
			return -110;
		}
		catch (InterruptedException e) {
			this.printError("InterruptedException exception while executing Inkscape with error: " + e.getMessage());
			return -111;
		}
		return 0;
	}

	/**
	 * If the SVG input has layers: creates a temporary directory and safes each layer into a single plain SVG file.
	 * @return 0 on success, negative integer otherwise
	 */
	public int createTmpSvgs(){
		SvgDocumentLoader loader = new BatikLoader();

		boolean loaded = loader.load(this.optionUriIn.getURI());
		if(loaded==false || loader.documentLoaded()==false){
			this.printError("problems loading the SVG input file with Batik");
			return -50;
		}

		List<Node> layers = loader.getInkscapeLayers();
		if(layers.size()==0){
			this.printError("option one-per-layer selected but input SVG does not have layers");
			return -51;
		}

		try {
			this.tmpDir = Files.createTempDirectory("s2v");
		}
		catch (IOException e) {
			this.printError("problem creating temporary directory with error: " + e.getMessage());
			return -60;
		}
		this.printProgress("temp directory:   " + this.tmpDir);

		String fin = this.optionUriIn.getURI().getPath();
		if(fin.startsWith("/")){
			fin = StringUtils.substringAfter(fin, "/");
		}

		for(Node node : layers){
			String fout = this.tmpDir.toString() + "/" + this.genOutFilename(node, layers.indexOf(node)) + ".svg";
			String nodeId = SvgDocumentLoader.getID(node);

			StrBuilder cmd = new StrBuilder();
			cmd.append("\"" + this.optionInkscapeExec.getCliValue() + "\"")
				.append(' ')
				.append(this.stdIsArgs)
				.append(" --file=")
				.append(fin)
				.append(" --")
				.append(SvgTargets.svg.getIsCliLong())
				.append('=')
				.append(fout)
				.append(" --export-area-page")
				.append(" -j -i=").append(nodeId)
				.append(" --select=").append(nodeId);
			;
			this.printProgress(" -- generating temporary SVG");
			this.printProgress("      - from: " + fin);
			this.printProgress("      - to:   " + fout);
			this.printProgress("      - cli:  " + cmd);

			try{
				Process p = Runtime.getRuntime().exec(cmd.toString());
				p.waitFor();
			}
			catch (IOException e) {
				this.printError("(tmp svg) IO exception while executing Inkscape with error: " + e.getMessage());
				return -101;
			}
			catch (InterruptedException e) {
				this.printError("(tmp svg) InterruptedException exception while executing Inkscape with error: " + e.getMessage());
				return -102;
			}
		}

		return 0;
	}

	/**
	 * Generates a name for an output file.
	 * @param node the node to get ID or label from
	 * @param index the index of the node in the node list
	 * @return output name depending on CLI settings
	 */
	public String genOutFilename(Node node, int index){
		if(this.optionOnePerLayer.inCli()){
			String layerName = SvgDocumentLoader.getInkscapeLabel(node);
			if(layerName==null){
				layerName = SvgDocumentLoader.getID(node);
			}

			if(this.optionUseLayerIndex.inCli() && this.optionUseLayerIndexId.inCli()){
				return String.format("%s-%02d", this.optionFileOut.getValue(), index) + '-' + layerName;
			}
			else if(!this.optionUseLayerIndex.inCli() && !this.optionUseLayerIndexId.inCli()){
				return this.optionFileOut.getValue() + '-' + layerName;
			}
			else if(this.optionUseLayerIndex.inCli() && !this.optionUseLayerIndexId.inCli()){
				return String.format("%s-%02d", this.optionFileOut.getValue(), index);
			}
			else if(!this.optionUseLayerIndex.inCli() && this.optionUseLayerIndexId.inCli()){
				return this.optionFileOut.getValue() + '-' + layerName;
			}
		}
		return this.optionFileOut.getValue();
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
