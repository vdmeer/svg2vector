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

import de.vandermeer.execs.ExecS;
import de.vandermeer.svg2vector.applications.fh.Svg2Vector_FH;
import de.vandermeer.svg2vector.applications.is.Svg2Vector_IS;

/**
 * An ExecS class for the tool.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v1.1.0
 */
public class Svg2VectorExecs extends ExecS {

	/**
	 * Creates a new ExecS object.
	 */
	public Svg2VectorExecs(){
		super("s2v");

		this.addApplication(Svg2Vector_FH.APP_NAME,			Svg2Vector_FH.class);
		this.addApplication(Svg2Vector_IS.APP_NAME,			Svg2Vector_IS.class);
	}

	/**
	 * Public main to start the tool.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Svg2VectorExecs execs = new Svg2VectorExecs();
		int ret = execs.execute(args);
		System.exit(ret);
	}
}
