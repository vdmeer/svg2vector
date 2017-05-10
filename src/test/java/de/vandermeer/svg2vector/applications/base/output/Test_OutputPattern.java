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

package de.vandermeer.svg2vector.applications.base.output;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.vandermeer.skb.interfaces.application.ApplicationException;
import de.vandermeer.skb.interfaces.messages.errors.Templates_OutputFile;

/**
 * Tests for {@link OutputPattern} - constructors and remove options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v2.1.0
 */
public class Test_OutputPattern {

	/* Set true to print all tested patterns to stdout. */
	protected final boolean print = true;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_Constructor_Std(){
		String pattern = "${dn}" + File.separator + "${fn}${index}${is-index}${is-label}.${fext}";
		if(this.print){
			System.out.println(pattern);
		}
		OutputPattern op = new OutputPattern();
		assertEquals(pattern, op.toString());
		assertEquals(pattern, op.getPattern().toString());
	}

	@Test
	public void test_Error_FextOnly() throws ApplicationException{
		OutputPattern op = new OutputPattern();

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_PATTERN_ONLY_EXT.getCode())));
		op.generateName(null, null, "pdf", -1, null);
	}

	@Test
	public void test_Error_DirFextOnly_Simple() throws ApplicationException{
		OutputPattern op = new OutputPattern();

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_PATTERN_ONLY_DIREXT.getCode())));
		op.generateName(FileSystems.getDefault().getPath("target"), null, "pdf", -1, null);
	}

	@Test
	public void test_Error_DirFextOnly_more() throws ApplicationException{
		OutputPattern op = new OutputPattern();

		thrown.expect(ApplicationException.class);
		thrown.expect(hasProperty("errorCode", is(Templates_OutputFile.FN_PATTERN_ONLY_DIREXT.getCode())));
		op.generateName(FileSystems.getDefault().getPath("target/foo"), null, "pdf", -1, null);
	}

	@Test
	public void test_Only_Dout() throws ApplicationException{
		Path path = FileSystems.getDefault().getPath("/bla/foo");
		OutputPattern op = new OutputPattern();
		Path ret = op.generateName(path, null, null, -1, null);
		assertEquals(ret.toString(), path.toString());
		if(print){
			System.out.println(ret);
		}
	}

	@Test
	public void test_Only_Fout() throws ApplicationException{
		Path path = FileSystems.getDefault().getPath("foo");
		OutputPattern op = new OutputPattern();
		Path ret = op.generateName(null, path, null, -1, null);
		assertEquals(ret.toString(), path.toString());
		if(print){
			System.out.println(ret);
		}
	}

	@Test
	public void test_Only_Fout2() throws ApplicationException{
		Path path = FileSystems.getDefault().getPath("bla/foo");
		OutputPattern op = new OutputPattern();
		Path ret = op.generateName(null, path, null, -1, null);
		assertEquals(ret.toString(), path.getFileName().toString());
		if(print){
			System.out.println(ret);
		}
	}

}
