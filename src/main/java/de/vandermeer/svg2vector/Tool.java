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

package de.vandermeer.svg2vector;

import de.vandermeer.svg2vector.applications.Svg2Vector_FH;

/**
 * Executes {@link Svg2Vector_FH}.
 * 
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v1.1.0 build 170405 (05-Apr-17) for Java 1.8
 * @since      v1.0.0
 */
public class Tool {

	/**
	 * Main routine to start tool.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Svg2Vector_FH app = new Svg2Vector_FH();
		System.exit(app.executeApplication(args));
	}

}
