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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.vandermeer.execs.options.ExecS_CliParser;
import de.vandermeer.svg2vector.applications.is.IsLoader;

/**
 * Tests for {@link AppProperties} - file output options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.0.0 build 170413 (13-Apr-17) for Java 1.8
 * @since      v2.0.0
 */
public class Test_AppProperties_Output_Layers {

	@Test
	public void test_Warning_SwitchOnLayers(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-d", "target",
				"-l",
				"--all-layers",
				"--fout-layer-index",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals(1, props.getWarnings().size());
		assertEquals("layers processed but CLI option <all-layers> used, will be ignored", props.getWarnings().get(0));
	}

	@Test
	public void test_Warning_Fout(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-d", "target",
				"-l",
				"-o", "foo",
				"--fout-layer-index",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals(1, props.getWarnings().size());
		assertEquals("layers processed but CLI option <output-file> used, will be ignored", props.getWarnings().get(0));
	}

	@Test
	public void test_Error_DoutNotDir(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l",
				"-d", "target/output-tests/app-props/test"
		};

		File dir = new File("target/output-tests/app-props");
		dir.mkdirs();
		File f = new File("target/output-tests/app-props/test");
		try {
			f.createNewFile();
		}
		catch (IOException ignore) {}

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("output directory <target/output-tests/app-props/test> exists but is not a directory", err);

		f.delete();
	}

	// cannot test dir exists and cannot write...

	@Test
	public void test_Error_NoDoutNoCreate(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l",
				"-d", "target/output-tests/app-props/test/file"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("output directory <target/output-tests/app-props/test/file> does not exist and CLI option <create-directories> not used", err);
	}

	@Test
	public void test_Error_NoBnNoId(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l",
				"-d", "target/output-tests/app-props/",
				"--create-directories",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("processing layers but neither <fout-layer-id> nor <fout-layer-index> options requestes, amigious output file names", err);
	}

	@Test
	public void test_Error_NoBnNoIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l",
				"-d", "target/output-tests/app-props/",
				"--create-directories",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		String err = props.setOutput();
		assertNotNull(err);
		assertEquals("processing layers but neither <fout-layer-id> nor <fout-layer-index> options requestes, amigious output file names", err);
	}

	@Test
	public void test_PatternId(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-layer-index",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${index}", props.getFoutPattern());
	}

	@Test
	public void test_PatternIdIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-layer-index", "--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${index}-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnId(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--use-basename", "base", "--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/base-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--use-basename", "base", "--fout-layer-index",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/base-${index}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnIdIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--use-basename", "base", "--fout-layer-index", "--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/base-${index}-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnFinId(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories", "--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnFinIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories", "--fout-layer-index",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${index}", props.getFoutPattern());
	}

	@Test
	public void test_PatternBnFinIdIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories", "--fout-layer-index", "--fout-layer-id",
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/rina-ipc-${index}-${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternNoBnId(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-no-basename", "--fout-layer-id"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/${id}", props.getFoutPattern());
	}

	@Test
	public void test_PatternNoBnIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-no-basename", "--fout-layer-index"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/${index}", props.getFoutPattern());
	}

	@Test
	public void test_PatternNoBnIdIndex(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-no-basename", "--fout-layer-index", "--fout-layer-id"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertEquals("target/output-tests/app-props/${index}-${id}", props.getFoutPattern());
	}

	@Test
	public void test_DoesLayers(){
		ExecS_CliParser cli = new ExecS_CliParser();
		AppProperties<IsLoader> props = new AppProperties<IsLoader>(new SvgTargets[]{SvgTargets.pdf}, new IsLoader());
		cli.addAllOptions(props.getAppOptions());
		String[] args = new String[]{
				"-t", "pdf",
				"-f", "src/test/resources/svg-files/rina-ipc.svgz",
				"-l", "-d", "target/output-tests/app-props/",
				"--create-directories",
				"--fout-layer-index"
		};

		assertEquals(null, cli.parse(args));
		assertEquals(0, Test_AppProperties.setCli4Options(cli.getCommandLine(), props.getAppOptions()));

		assertNull(props.setInput());
		assertNull(props.setOutput());
		assertFalse(props.doesNoLayers());
		assertTrue(props.doesLayers());
	}
}
