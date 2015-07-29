/* Copyright 2014 Sven van der Meer <vdmeer.sven@mykolab.com>
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

package de.vandermeer.svg2vector.converters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.freehep.graphicsio.pdf.PDFGraphics2D;

import de.vandermeer.svg2vector.base.SVG;

/**
 * PDF target converter.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.0.2 build 150701 (01-Jul-15) for Java 1.7
 */
public class Svg2Pdf extends SVG {

	/** Constructor */
	public Svg2Pdf() {
		super();
	}

	@Override
	public boolean convertSingleLayer(String directory, String filename) {
		if(this.documentLoaded()==false){
			return false;
		}

		GVTBuilder gvtBuilder=new GVTBuilder();
		GraphicsNode rootNode = gvtBuilder.build(this.bridgeContext, this.svgDocument);

		FileOutputStream pdfStream;
		PDFGraphics2D pdfGraphics2D;
		try{
			if(directory==null){
				directory = System.getProperty("user.dir");
			}
			File output = new File(directory+'/'+filename+".pdf");
			pdfStream = new FileOutputStream(output);
			pdfGraphics2D = new PDFGraphics2D(pdfStream, this.size);
			this.properties.getProperties().setProperty(PDFGraphics2D.PAGE_SIZE, PDFGraphics2D.CUSTOM_PAGE_SIZE);
			this.properties.getProperties().setProperty(PDFGraphics2D.CUSTOM_PAGE_SIZE, this.size);//TODO change if other page size required
		}
		catch(IOException fnfe){
			return false;
		}

		pdfGraphics2D.setProperties(this.properties.getProperties());
		pdfGraphics2D.setDeviceIndependent(true);
		pdfGraphics2D.startExport();
		rootNode.paint(pdfGraphics2D);
		pdfGraphics2D.endExport();
		pdfGraphics2D.dispose();

		try{
			pdfStream.close();
		}
		catch(Exception ignore){}

		return true;
	}
}
