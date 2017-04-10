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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.w3c.dom.Node;

import de.vandermeer.svg2vector.applications.options.AO_ExportDpi;
import de.vandermeer.svg2vector.applications.options.AO_ExportPdfVersion;
import de.vandermeer.svg2vector.applications.options.AO_ExportPsLevel;
import de.vandermeer.svg2vector.applications.options.AO_InkscapeExecutable;
import de.vandermeer.svg2vector.applications.options.AO_ManualLayers;
import de.vandermeer.svg2vector.applications.options.AO_NotTextAsShape;
import de.vandermeer.svg2vector.applications.options.AO_SvgFirst;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndex;
import de.vandermeer.svg2vector.applications.options.AO_UseLayerIndexId;
import de.vandermeer.svg2vector.converters.SvgTargets;
import de.vandermeer.svg2vector.loaders.BatikLoader;
import de.vandermeer.svg2vector.loaders.IsFile;
import de.vandermeer.svg2vector.loaders.SvgDocumentLoader;

/**
 * The Svg2Vector application using an Inkscape executable.
 * It an SVG graphic to a vector format and to PNG as per Inkscape.
 * The tool does support SVG and SVGZ input formats from file or URI.
 * The application calls its FreeHep counterpart to create plain SVG, and then Inkscape to create the target format.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8
 * @since      v1.2.0
 */
public class Svg2Vector_IS extends AppBase {

	/** Application name. */
	public final static String APP_NAME = "s2v-is";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector Inkscape";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v1.2.0-SNAPSHOT build 170410 (10-Apr-17) for Java 1.8";

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

	/** Application option to require SVG transformation first, then do the actual target transformation. */
	AO_SvgFirst optionSvgFirst = new AO_SvgFirst(false, 'g', "requires the tool to generate temporary SVG files first and then us those files to generate the actual target");

	/** Application option to manage layers manually when creating a temporary directory. */
	AO_ManualLayers optionManualLayers = new AO_ManualLayers(false, 'm', "layers are switched off/on on a raw text file, i.e. not using any SVG or XML library");

	/** Path object for the temporary directory. */
	Path tmpDir;

	/** File for a temporary created SVG file. */
	Path tmpFile;

	/** List of Inkscpe layers in an SVG document. */
	List<Node> layers;

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

		SvgTargets target = this.optionTarget.getTarget();

		String fn = this.optionInkscapeExec.getValue();
		if(StringUtils.isBlank(fn)){
			this.printError("expected Inkscape executable, found <" + fn + ">");
			return -2;
		}
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

		StrBuilder isCmd = new StrBuilder();
		if(fn.contains("\"")){
			isCmd.clear().append('"');
		}
		isCmd.append(fn);
		if(fn.contains("\"")){
			isCmd.clear().append('"');
		}
		isCmd.append(' ').append("--without-gui --export-area-page");
		if(!this.optionNotTextAsShape.inCli()){
			isCmd.append(" --export-text-to-path");
		}
		isCmd.append(" --file=${fin}");

		StrBuilder isTmpCmd = new StrBuilder();
		isTmpCmd.append(isCmd.toCharArray());
		isTmpCmd.append(" --").append(SvgTargets.svg.getIsCliLong()).append("=${fout}");

		isCmd.append(" --").append(target.getIsCliLong()).append("=${fout}");
		switch(target){
			case emf:
				break;
			case eps:
				break;
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
			case svg:
				break;
			case wmf:
				break;
			default:
				break;
		}

		// check for layers if one-per-layer is activated
		if(this.optionOnePerLayer.inCli()){
			ret = this.getLayers();
			if(ret<0){
				return ret;
			}
		}

		String fin = this.optionUriIn.getURI().getPath();
		if(fin.startsWith("/")){
			fin = StringUtils.substringAfter(fin, "/");
		}

		if(this.optionSvgFirst.inCli()){
			if(this.optionOnePerLayer.inCli()){
				try{
					this.tmpDir = Files.createTempDirectory("s2v");
				}
				catch (IOException e) {
					this.printError("problem creating temporary directory with error: " + e.getMessage());
					return -60;
				}
//				this.printProgress("temp directory:   " + this.tmpDir);

				if(this.optionManualLayers.inCli()){
					IsFile isFile = new IsFile();
					String err = isFile.read(fin);
					if(err!=null){
						this.printError(err);
						return -99;
					}
					for(Node node : this.layers){
						isFile.switchOffAllLayers();
						isFile.switchOnLayer(SvgDocumentLoader.getID(node));
						err = isFile.write(this.tmpDir.toString() + "/" + this.genOutFilename(node, this.layers.indexOf(node)) + ".svg");
						if(err!=null){
							this.printError(err);
							return -99;
						}
					}
				}
				else{
					for(Node node : this.layers){
						String fout = this.tmpDir.toString() + "/" + this.genOutFilename(node, this.layers.indexOf(node)) + ".svg";
						String nodeId = SvgDocumentLoader.getID(node);
						StrBuilder nodeCmd = new StrBuilder();
						nodeCmd.append(isTmpCmd.toCharArray())
							.append(" -j -i=").append(nodeId)
							.append(" --select=").append(nodeId);
						;
						this.ExecInkscape(nodeCmd, fin, fout);
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
					this.printError("problem creating temporary file with error: " + e.getMessage());
					return -60;
				}
//				this.printProgress("temp file:   " + this.tmpFile);
//				this.printProgress(" -- generating temporary SVG");
				this.ExecInkscape(isTmpCmd, fin, this.tmpFile.toString());
				if(ret<0){
					return ret;
				}
			}
		}

		if(this.tmpDir!=null){
			for (final File fileEntry : this.tmpDir.toFile().listFiles()) {
				if(fileEntry.isFile()){
					String finTmp = this.tmpDir + "/" + fileEntry.getName();
					String fout = this.optionDirOut.getValue() + StringUtils.substringBefore(fileEntry.getName(), ".svg") + "." + target.name();
//					this.printProgress(" -- converting temporary layer SVG");
					this.ExecInkscape(isCmd, finTmp, fout);
				}
			}
		}
		else if(this.tmpFile!=null){
			this.ExecInkscape(isCmd, this.tmpFile.toString(), this.optionDirOut.getValue() + this.optionFileOut.getValue() + "." + target.name());
		}
		else{
			if(this.optionOnePerLayer.inCli()){
				for(Node node : this.layers){
					String fout = this.optionDirOut.getValue() + this.genOutFilename(node, this.layers.indexOf(node)) + "." + target.name();
					String nodeId = SvgDocumentLoader.getID(node);
					StrBuilder nodeCmd = new StrBuilder();
					nodeCmd.append(isCmd.toCharArray())
						.append(" -j -i=").append(nodeId)
						.append(" --select=").append(nodeId);
					;
					this.ExecInkscape(nodeCmd, fin, fout);
					if(ret<0){
						return ret;
					}
				}
			}
			else{
				this.ExecInkscape(isCmd, fin, this.optionDirOut.getValue() + this.optionFileOut.getValue() + "." + target.name());
			}
		}

		if(this.tmpDir!=null){
			for (final File fileEntry : this.tmpDir.toFile().listFiles()) {
				fileEntry.delete();
			}
			this.tmpDir.toFile().delete();
		}
		if(this.tmpFile!=null){
			this.tmpFile.toFile().delete();
		}

		return 0;
	}

	/**
	 * Gets the layers from an SVG document and stores them in the layers member.
	 * @return 0 on success, negative integer on error (with error message printed)
	 */
	public int getLayers(){
		SvgDocumentLoader loader = new BatikLoader();

		boolean loaded = loader.load(this.optionUriIn.getURI());
		if(loaded==false || loader.documentLoaded()==false){
			this.printError("problems loading the SVG input file with Batik");
			return -50;
		}

		this.layers = loader.getInkscapeLayers();
		if(this.layers.size()==0){
			this.printError("option one-per-layer selected but input SVG does not have layers");
			return -51;
		}
		return 0;
	}

	public int ExecInkscape(StrBuilder cmd, String fin, String fout){
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("fin", fin);
		valuesMap.put("fout", fout);
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String cli = sub.replace(cmd.toString());

		try {
			this.printProgress("      - from: " + fin);
			this.printProgress("      - to:   " + fout);
			this.printProgress("      - cli:  " + cli);
			Process p = Runtime.getRuntime().exec(cli);
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
