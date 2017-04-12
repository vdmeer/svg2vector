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

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import de.vandermeer.execs.options.ApplicationOption;
import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * General tests for {@link AppProperties}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0-SNAPSHOT build 170411 (11-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties {

	@Test
	public void test_AddedOptions(){
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		assertEquals(22, props.getAppOptions().length);
	}

	@Test
	public void test_Usage(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		cli.usage("Test_AppOptions");
	}

	static int setCli4Options(CommandLine cmdLine, ApplicationOption<?>[] options){
		if(cmdLine==null){
			return -99;
		}

		int ret = 0;
		if(options!=null){
			for(ApplicationOption<?> opt : options){
				if(opt.getCliOption()!=null){
					ret += opt.setCliValue(cmdLine);
				}
			}
		}
		return ret;
	}
}
