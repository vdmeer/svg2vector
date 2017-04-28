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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import de.vandermeer.skb.interfaces.application.Apo_SimpleC;
import de.vandermeer.skb.interfaces.application.Apo_TypedC;

/**
 * A package of CLI options for the application base.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public abstract class CliOptionPackage {

	/** List of simple options. */
	protected final Set<Apo_SimpleC> simpleOptions;

	/** List of typed options. */
	protected final Set<Apo_TypedC<?>> typedOptions;

	/**
	 * Creates a new CLI option package.
	 */
	protected CliOptionPackage(){
		this.simpleOptions = new HashSet<>();
		this.typedOptions = new HashSet<>();
	}

	/**
	 *  Sets the simple options.
	 * @param simpleOptions options, must not be null nor contain null elements
	 */
	protected void setSimpleOptions(final Apo_SimpleC ... simpleOptions){
		Validate.noNullElements(simpleOptions);
		for(Apo_SimpleC opt : simpleOptions){
			this.simpleOptions.add(opt);
		}
	}

	/**
	 *  Sets the typed options.
	 * @param typedOptions options, must not be null nor contain null elements
	 */
	protected void setTypedOptions(Apo_TypedC<?> ... typedOptions){
		Validate.noNullElements(typedOptions);
		for(Apo_TypedC<?> opt : typedOptions){
			this.typedOptions.add(opt);
		}
	}

	/**
	 * Returns the typed options.
	 * @return typed options, empty if none set
	 */
	public Set<Apo_TypedC<?>> getTypedOptions(){
		return this.typedOptions;
	}

	/**
	 * Returns the simple options.
	 * @return simple options, empty if none set
	 */
	public Set<Apo_SimpleC> getSimpleOptions(){
		return this.simpleOptions;
	}

	/**
	 * Returns all options.
	 * @return all options
	 */
	public Set<Object> getAllOptions(){
		Set<Object> ret = new HashSet<>();
		ret.addAll(this.simpleOptions);
		ret.addAll(this.typedOptions);
		return ret;
	}
}
