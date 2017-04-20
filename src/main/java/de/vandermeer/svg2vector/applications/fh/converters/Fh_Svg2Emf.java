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
import org.freehep.graphicsio.emf.EMFGraphics2D;

/**
 * A converter for SVG documents to EMF using the FreeHep library.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Fh_Svg2Emf extends FhConverter {

	@Override
	public void setPropertyBackground(boolean on) {
		this.properties.setProperty(EMFGraphics2D.BACKGROUND, on);
	}

	@Override
	public void setPropertyBackgroundColor(Color color) {
		this.properties.setProperty(EMFGraphics2D.BACKGROUND_COLOR, color);
	}

	@Override
	public void setPropertyTransparent(boolean on) {
		this.properties.setProperty(EMFGraphics2D.TRANSPARENT, on);
	}

	@Override
	public void convertDocument(BatikLoader loader, File fout) throws IOException {
		//TODO error messages and parameter checks

		GVTBuilder gvtBuilder = new GVTBuilder();
		GraphicsNode rootNode = gvtBuilder.build(loader.getBridgeContext(), loader.getDocument());

		FileOutputStream emfStream;
		EMFGraphics2D emfGraphics2D;
		emfStream = new FileOutputStream(fout);
		emfGraphics2D = new EMFGraphics2D(fout, loader.getSize());

		emfGraphics2D.setProperties(this.properties);
		emfGraphics2D.setDeviceIndependent(true);
		emfGraphics2D.startExport();
		rootNode.paint(emfGraphics2D);
		emfGraphics2D.endExport();
		emfGraphics2D.dispose();

		emfStream.close();
	}

}
