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

package de.vandermeer.svg2vector.sitedocs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.vandermeer.asciiparagraph.AsciiParagraph;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

/**
 * Simple class to generate documentation for an option.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.1
 */
public class Test_ADOC_Options {

	/** Flag for printing, can be switched of in a distribution. */
	static boolean print = true;

	@Test
	public void test_AllLayers(){
		this.printDoc("src/main/site-docs/options/all-layers.txt");
	}

	@Test
	public void test_BgrndColor(){
		this.printDoc("src/main/site-docs/options/bgrnd-color.txt");
	}

	@Test
	public void test_CreateDirectories(){
		this.printDoc("src/main/site-docs/options/create-directories.txt");
	}

	@Test
	public void test_ExportDpi(){
		this.printDoc("src/main/site-docs/options/export-dpi.txt");
	}

	@Test
	public void test_ExportPdfVersion(){
		this.printDoc("src/main/site-docs/options/export-pdf-version.txt");
	}

	@Test
	public void test_ExportPsLevel(){
		this.printDoc("src/main/site-docs/options/export-ps-level.txt");
	}

	@Test
	public void test_FOutIndex(){
		this.printDoc("src/main/site-docs/options/fout-index.txt");
	}

	@Test
	public void test_FOutIsIndex(){
		this.printDoc("src/main/site-docs/options/fout-isindex.txt");
	}

	@Test
	public void test_FOutIsLabel(){
		this.printDoc("src/main/site-docs/options/fout-islabel.txt");
	}

	@Test
	public void test_FOutNoBaseName(){
		this.printDoc("src/main/site-docs/options/fout-no-basename.txt");
	}

	@Test
	public void test_FileIn(){
		this.printDoc("src/main/site-docs/options/input-file.txt");
	}

	@Test
	public void test_LayersIsExec(){
		this.printDoc("src/main/site-docs/options/is-exec.txt");
	}

	@Test
	public void test_KeepTmpArtifacts(){
		this.printDoc("src/main/site-docs/options/keep-tmp-artifacts.txt");
	}

	@Test
	public void test_LayersIfExist(){
		this.printDoc("src/main/site-docs/options/layers-if-exist.txt");
	}

	@Test
	public void test_Layers(){
		this.printDoc("src/main/site-docs/options/layers.txt");
	}

	@Test
	public void test_ManualLayers(){
		this.printDoc("src/main/site-docs/options/manual-layers.txt");
	}

	@Test
	public void test_NoBackground(){
		this.printDoc("src/main/site-docs/options/no-background.txt");
	}

	@Test
	public void test_NoErrors(){
		this.printDoc("src/main/site-docs/options/no-errors.txt");
	}

	@Test
	public void test_NotTransparent(){
		this.printDoc("src/main/site-docs/options/not-transparent.txt");
	}


	@Test
	public void test_DirectoryOut(){
		this.printDoc("src/main/site-docs/options/output-directory.txt");
	}

	@Test
	public void test_FileOut(){
		this.printDoc("src/main/site-docs/options/output-file.txt");
	}

	@Test
	public void test_OverwriteExisting(){
		this.printDoc("src/main/site-docs/options/overwrite-existing.txt");
	}

	@Test
	public void test_PrintDetails(){
		this.printDoc("src/main/site-docs/options/print-details.txt");
	}

	@Test
	public void test_PrintProgress(){
		this.printDoc("src/main/site-docs/options/print-progress.txt");
	}

	@Test
	public void test_PrintWarnings(){
		this.printDoc("src/main/site-docs/options/print-warnings.txt");
	}

	@Test
	public void test_PrintStackTrace(){
		this.printDoc("src/main/site-docs/options/print-stack-trace.txt");
	}

	@Test
	public void test_Quiet(){
		this.printDoc("src/main/site-docs/options/quiet.txt");
	}

	@Test
	public void test_Simulate(){
		this.printDoc("src/main/site-docs/options/simulate.txt");
	}

	@Test
	public void test_SvgFirst(){
		this.printDoc("src/main/site-docs/options/svg-first.txt");
	}

	@Test
	public void test_Target(){
		this.printDoc("src/main/site-docs/options/target.txt");
	}

	@Test
	public void test_TextAsShape(){
		this.printDoc("src/main/site-docs/options/text-as-shape.txt");
	}

	@Test
	public void test_UseBaseName(){
		this.printDoc("src/main/site-docs/options/use-basename.txt");
	}

	@Test
	public void test_Verbose(){
		this.printDoc("src/main/site-docs/options/verbose.txt");
	}

	void printDoc(String filename){
		ArrayList<Object[]> lines = new ArrayList<>();

		try{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			ArrayList<String> para = new ArrayList<>();
			String str;
			while((str=in.readLine()) != null){
				para.add(str);
				if(StringUtils.isBlank(str)){
					lines.add(para.toArray());
					para.clear();
				}
			}
			in.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		for(Object[] list : lines){
			AsciiParagraph ap = new AsciiParagraph();
			ap.addText(list);
			ap.getContext().setAlignment(TextAlignment.JUSTIFIED_LEFT);
			if(print){
				System.out.println(ap.render(75));
			}
		}
	}
}
