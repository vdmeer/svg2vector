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

package de.vandermeer.svg2vector.applications.is.converters;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.freehep.graphicsio.svg.SVGGraphics2D;

import de.vandermeer.svg2vector.applications.fh.BatikLoader;

/**
 * A converter for SVG documents to SVG using the FreeHep library.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Fh_Svg2Svg extends FhConverter {

	@Override
	public void setPropertyBackground(boolean on) {
		this.properties.setProperty(SVGGraphics2D.BACKGROUND, on);
	}

	@Override
	public void setPropertyBackgroundColor(Color color) {
		this.properties.setProperty(SVGGraphics2D.BACKGROUND_COLOR, color);
	}

	@Override
	public void setPropertyTransparent(boolean on) {
		this.properties.setProperty(SVGGraphics2D.TRANSPARENT, on);
	}

	@Override
	public String convertDocument(BatikLoader loader, File fout) {
		//TODO error messages and parameter checks

		GVTBuilder gvtBuilder = new GVTBuilder();
		GraphicsNode rootNode = gvtBuilder.build(loader.getBridgeContext(), loader.getDocument());

		FileOutputStream svgStream;
		SVGGraphics2D svgGraphics2D;
		try{
			svgStream = new FileOutputStream(fout);
			svgGraphics2D = new SVGGraphics2D(fout, loader.getSize());
		}
		catch(IOException fnfe){
			//TODO
			return "###";
		}

		svgGraphics2D.setProperties(this.properties);
		svgGraphics2D.setDeviceIndependent(true);
		svgGraphics2D.startExport();
		rootNode.paint(svgGraphics2D);
		svgGraphics2D.endExport();
		svgGraphics2D.dispose();

		try{
			svgStream.close();
		}
		catch(Exception ignore){}

		return null;
	}

}
