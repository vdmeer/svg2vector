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

package de.vandermeer.svg2vector.applications.fh.converters;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.freehep.graphicsio.pdf.PDFGraphics2D;

/**
 * A converter for SVG documents to PDF using the FreeHep library.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Fh_Svg2Pdf extends FhConverter {

	/**
	 * Creates a new PDF converter and sets margins.
	 */
	public Fh_Svg2Pdf() {
		this.properties.setProperty(PDFGraphics2D.PAGE_MARGINS, "0, 0, 0, 0");
	}

	@Override
	public void setPropertyBackground(boolean on) {
		this.properties.setProperty(PDFGraphics2D.BACKGROUND, on);
	}

	@Override
	public void setPropertyBackgroundColor(Color color) {
		this.properties.setProperty(PDFGraphics2D.BACKGROUND_COLOR, color);
	}

	@Override
	public void setPropertyTransparent(boolean on) {
		this.properties.setProperty(PDFGraphics2D.TRANSPARENT, on);
	}

	@Override
	public String convertDocument(BatikLoader loader, File fout) {
		//TODO error messages and parameter checks

		GVTBuilder gvtBuilder=new GVTBuilder();
		GraphicsNode rootNode = gvtBuilder.build(loader.getBridgeContext(), loader.getDocument());

		FileOutputStream pdfStream;
		PDFGraphics2D pdfGraphics2D;
		try{
			pdfStream = new FileOutputStream(fout);
			pdfGraphics2D = new PDFGraphics2D(pdfStream, loader.getSize());
		}
		catch(IOException fnfe){
			//TODO
			return "###";
		}

		this.properties.setProperty(PDFGraphics2D.PAGE_SIZE, PDFGraphics2D.CUSTOM_PAGE_SIZE);
		this.properties.setProperty(PDFGraphics2D.CUSTOM_PAGE_SIZE, loader.getSize());//TODO change if other page size required

		pdfGraphics2D.setProperties(this.properties);
		pdfGraphics2D.setDeviceIndependent(true);
		pdfGraphics2D.startExport();
		rootNode.paint(pdfGraphics2D);
		pdfGraphics2D.endExport();
		pdfGraphics2D.dispose();

		try{
			pdfStream.close();
		}
		catch(Exception ignore){}

		return null;
	}

}
