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

package de.vandermeer.svg2vector.applications.core;

/**
 * A standard exception for problems in the Svg2Vector applications.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class S2VExeception extends Exception {

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The exception error code. */
	private final ErrorCodes errorCode;

	/**
	 * Creates a new exception.
	 * @param errorCode the exception error code
	 */
	public S2VExeception(ErrorCodes errorCode){
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	/**
	 * Creates a new exception.
	 * @param errorCode the exception error code
	 * @param args the arguments for the error code
	 */
	public S2VExeception(ErrorCodes errorCode, Object ... args){
		super(errorCode.getMessageSubstituted(args));
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error code.
	 * @return error code
	 */
	public ErrorCodes getErrorCode(){
		return this.errorCode;
	}

}
