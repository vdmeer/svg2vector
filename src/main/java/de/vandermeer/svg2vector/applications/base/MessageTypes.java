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

package de.vandermeer.svg2vector.applications.base;

/**
 * Application message types with bit mask.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public enum MessageTypes {

	/** An error message. */
	error (0b0001),

	/** An warning message. */
	warning (0b0010),

	/** An progress message. */
	progress (0b0100),

	/** An detailed message. */
	detail (0b1000)

	;

	/** Bit mask for the message. */
	private int mask;

	/**
	 * Creates a new message type with a bit mask.
	 * @param mask
	 */
	MessageTypes(int mask){
		this.mask = mask;
	}

	/**
	 * Returns the bit mask of the message type.
	 * @return bit mask
	 */
	public int getMask(){
		return this.mask;
	}

	/**
	 * Tests if the type is activate in the given mode.
	 * @param mode the message mode
	 * @return true if the message type is activated in the message mode, false otherwise
	 */
	public boolean isSet(int mode){
		return ((mode & this.mask) == this.mask);
	}
}
