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

package de.vandermeer.svg2vector;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messagesets.errors.Templates_ExceptionRuntimeUnexpected;
import de.vandermeer.svg2vector.applications.base.AppBase;
import de.vandermeer.svg2vector.applications.core.SvgTargets;
import de.vandermeer.svg2vector.applications.is.AO_ExportDpi;
import de.vandermeer.svg2vector.applications.is.AO_ExportPdfVersion;
import de.vandermeer.svg2vector.applications.is.AO_ExportPsLevel;
import de.vandermeer.svg2vector.applications.is.AO_InkscapeExecutable;
import de.vandermeer.svg2vector.applications.is.AO_KeepTmpArtifacts;
import de.vandermeer.svg2vector.applications.is.AO_ManualLayers;
import de.vandermeer.svg2vector.applications.is.AO_SvgFirst;
import de.vandermeer.svg2vector.applications.is.IsExecutor;
import de.vandermeer.svg2vector.applications.is.IsLoader;
import de.vandermeer.svg2vector.applications.is.TmpArtefacts;

/**
 * The Svg2Vector application using an Inkscape executable.
 * It an SVG graphic to a vector format and to PNG as per Inkscape.
 * The tool does support SVG and SVGZ input formats from file or URI.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Svg2Vector_IS extends AppBase<IsLoader> {

	/** Application name. */
	public final static String APP_NAME = "s2v-is";

	/** Application display name. */
	public final static String APP_DISPLAY_NAME = "Svg2Vector Inkscape";

	/** Application version, should be same as the version in the class JavaDoc. */
	public final static String APP_VERSION = "v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8";

	/** Application option for the Inkscape executable. */
	protected final AO_InkscapeExecutable optionInkscapeExec = new AO_InkscapeExecutable();

	/** Application option to set DPI for bitmap export. */
	protected final AO_ExportDpi optionExpDpi = new AO_ExportDpi();

	/** Application option to set PS level for PS export. */
	protected final AO_ExportPsLevel optionExpPslevel = new AO_ExportPsLevel();

	/** Application option to set PDF version for PDF export. */
	protected final AO_ExportPdfVersion optionExpPdfver = new AO_ExportPdfVersion();

	/** Application option to require SVG transformation first, then do the actual target transformation. */
	protected final AO_SvgFirst optionSvgFirst = new AO_SvgFirst();

	/** Application option to manage layers manually when creating a temporary directory. */
	protected final AO_ManualLayers optionManualLayers = new AO_ManualLayers();

	/** Application option to keep (not remove) temporary created artifacts (files and directories). */
	final private AO_KeepTmpArtifacts aoKeepTmpArtifacts = new AO_KeepTmpArtifacts();

	/**
	 * Returns a new application.
	 */
	public Svg2Vector_IS(){
		super(APP_NAME, SvgTargets.values(), new IsLoader());

		this.addOption(this.optionExpDpi);
		this.addOption(this.optionExpPdfver);
		this.addOption(this.optionExpPslevel);
		this.addOption(this.optionSvgFirst);
		this.addOption(this.optionManualLayers);
		this.addOption(this.aoKeepTmpArtifacts);

		this.addOption(this.optionInkscapeExec);
	}

	@Override
	public String getAppDescription() {
		return "Converts SVG graphics into other vector formats using Inkscape, with options for handling layers";
	}

	@Override
	public String getAppDisplayName(){
		return APP_DISPLAY_NAME;
	}

	@Override
	public String getAppName() {
		return APP_NAME;
	}

	@Override
	public String getAppVersion() {
		return APP_VERSION;
	}

	@Override
	public void runApplication() {
		if(this.errNo!=0){
			return;
		}

		TmpArtefacts tmpArtifacts = null;
		try{
			this.init();
			tmpArtifacts = new TmpArtefacts(this.getMiscOptions().doesSimulate());

			final SvgTargets target = this.getTarget();
			this.setWarnings(target);

			final String isFn = this.optionInkscapeExec.getValue();
			final IsExecutor isCmd = new IsExecutor(isFn, this.getMiscOptions().doesSimulate());
			this.printDetailMessage("Inkscape exec:    " + isFn);

			isCmd.appendCmd(this.getConversionOptions());
			isCmd.appendCmd(target);
			isCmd.appendTargetSettings(target,
					this.optionExpDpi, this.optionExpPdfver, this.optionExpPslevel
			);

			final String fnIn = this.getRequiredOptions().getInputFilename();
			String fnOut;
			final IsLoader loader = this.getLoader();

			if(this.optionSvgFirst.inCli()){
				this.printProgressMessage("converting to temporary SVG first");

				final IsExecutor isTmpCmd = new IsExecutor(isFn, this.getMiscOptions().doesSimulate());
				isCmd.appendCmd(this.getConversionOptions());
				isTmpCmd.appendCmd(SvgTargets.svg);
				if(this.doesLayers()){
					if(this.optionManualLayers.inCli()){
						this.printDetailMessage("creating <" + loader.getLayers().size() + "> temporary SVGfiles (manual layer handling)");
						int index = 1;
						for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
							loader.switchOffAllLayers();
							loader.switchOnLayer(entry.getKey());
							tmpArtifacts.createTempFile(this.fopFileOnly(index, entry), loader.getLines());
							index++;
						}
					}
					else{
						this.printDetailMessage("creating <" + loader.getLayers().size() + "> temporary SVGfiles (Inkscape layer handling)");
						int index = 1;
						for(Entry<String, Integer> entry : loader.getLayers().entrySet()){
							fnOut = this.fopFileOnly(index, entry);
							String nodeId = "layer" + entry.getValue().toString();
							IsExecutor nodeCmd = new IsExecutor(isTmpCmd);
							nodeCmd.appendSelectedNode(nodeId);
							this.printDetailMessage(" -- using cmd: " + nodeCmd);
							tmpArtifacts.createTempFile(nodeCmd, fnIn, fnOut);
							index++;
						}
					}
				}
				else{
					this.printProgressMessage("single temporary file");
					this.printDetailMessage(" -- using cmd: " + isTmpCmd);
					tmpArtifacts.createTempFile(isTmpCmd, fnIn, "999");//TODO fout name
				}
			}
			if(tmpArtifacts.sizeSimulated()>0){
				this.printDetailMessage(" created <" + tmpArtifacts.sizeSimulated() + "> temporary file(s): \n      -- " + new StrBuilder().appendWithSeparators(tmpArtifacts.getSimulatedTmpFileList(), "\n      -- "));
				this.printDetailMessage("");
			}
			else if(tmpArtifacts.size()>0){
				this.printDetailMessage(" created <" + tmpArtifacts.size() + "> temporary file(s): \n      -- " + new StrBuilder().appendWithSeparators(tmpArtifacts.getTmpFileList(), "\n      -- "));
				this.printDetailMessage("");
			}

			if(tmpArtifacts.size()>=2){
				// multiple temporary files, means layers
				this.printProgressMessage("converting multiple temporary SVG files to target");
				this.printDetailMessage(" -- using cmd: " + isCmd);
				for(Path path : tmpArtifacts.getTmpFileList()){
					fnOut = this.fopOO(path.getFileName());
					this.printDetailMessage(" ---- input:  " + path);
					this.printDetailMessage(" ---- output: " + fnOut);
					isCmd.executeInkscape(path.toString(), fnOut);
				}
			}
			else if(tmpArtifacts.size()==1){
				// single temporary file, means single SVG file
				fnOut = this.fopOO();
				this.printProgressMessage("converting single temporary SVG file to target");
				this.printDetailMessage(" -- using cmd: " + isCmd);
				this.printDetailMessage(" ---- input:  " + tmpArtifacts.getTmpFileList().get(0));
				this.printDetailMessage(" ---- output: " + fnOut);
				isCmd.executeInkscape(tmpArtifacts.getTmpFileList().get(0).toString(), fnOut);
			}
			else{
				// no temporary files, means direct conversion
				if(this.doesLayers()){
					this.printProgressMessage("converting <" + loader.getLayers().size() + "> layers directly to target");
					int index = 1;
					for(final Entry<String, Integer> entry : loader.getLayers().entrySet()){
						fnOut = this.fopOO(index, entry);
						String nodeId = "layer" + entry.getValue().toString();
						IsExecutor nodeCmd = new IsExecutor(isCmd);
						nodeCmd.appendSelectedNode(nodeId);
						this.printDetailMessage(" -- using cmd: " + nodeCmd);
						this.printDetailMessage(" ---- input:  " + fnIn);
						this.printDetailMessage(" ---- output: " + fnOut);
						nodeCmd.executeInkscape(fnIn, fnOut);
						index++;
					}
				}
				else{
					fnOut = this.fopOO();
					this.printProgressMessage("converting single file directly to target");
					this.printDetailMessage(" -- using cmd: " + isCmd);
					this.printDetailMessage(" ---- input:  " + fnIn);
					this.printDetailMessage(" ---- output: " + fnOut);
					isCmd.executeInkscape(fnIn, fnOut);
				}
			}

			if(!this.aoKeepTmpArtifacts.inCli()){
				tmpArtifacts.deleteArtifacts();
			}
		}
		catch(ApplicationException s2vEx){
			this.printErrorMessage(s2vEx);
			this.errNo = s2vEx.getErrorCode();
		}
		catch(NullPointerException npEx){
			this.printErrorMessage(npEx);
			this.errNo = Templates_ExceptionRuntimeUnexpected.U_NULL_POINTER.getCode();//TODO
		}
		catch(IOException ioEx){
			this.printErrorMessage(ioEx);
			this.errNo = Templates_ExceptionRuntimeUnexpected.U_IO.getCode();//TODO
		}
		finally{
			// remove temp artifacts if object exists and we can delete them
			if(tmpArtifacts!=null){
				if(!this.aoKeepTmpArtifacts.inCli()){
					tmpArtifacts.deleteArtifactsFinally(this.getMiscOptions().printStackTrace());
				}
			}
		}

		this.printProgressMessage("finished successfully");
	}

	/**
	 * Checks for all CLI options and target and creates warnings if necessary.
	 * @param target the target, should not be null
	 * @throws NullPointerException if target was null
	 */
	private void setWarnings(final SvgTargets target){
		Validate.notNull(target);

		List<String> warnings = new ArrayList<>();
		final String ignored = "> used, will be ignored";

		if(target!=SvgTargets.pdf && this.optionExpPdfver.inCli()){
			warnings.add("target is not <pdf> but CLI option <" + this.optionExpPdfver.getCliLong() + ignored);
		}
		if(target!=SvgTargets.png && this.optionExpDpi.inCli()){
			warnings.add("target is not <png> but CLI option <" + this.optionExpDpi.getCliLong() + ignored);
		}
		if(target!=SvgTargets.ps && this.optionExpPslevel.inCli()){
			warnings.add("target is not <ps> but CLI option <" + this.optionExpPslevel.getCliLong() + ignored);
		}
		if(!this.optionSvgFirst.inCli() && this.optionManualLayers.inCli()){
			warnings.add("found CLI option <" + this.optionManualLayers.getCliLong() + "> but not <" + this.optionSvgFirst.getCliLong() + ">, option will be ignored");
		}

		if(this.doesLayers()){
			
		}
		else{
			if(this.optionManualLayers.inCli()){
				warnings.add("no layers processed but CLI option <" + this.optionManualLayers.getCliLong() + ignored);
			}
		}
		this.printWarnings(warnings);
	}

}
