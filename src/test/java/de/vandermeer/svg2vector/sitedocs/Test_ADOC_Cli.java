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

package de.vandermeer.svg2vector.sitedocs;

/**
 * Simple class to generate documentation for application CLI options.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v2.1.0-SNAPSHOT build 170420 (20-Apr-17) for Java 1.8
 * @since      v1.1.1
 */
public class Test_ADOC_Cli {

//	@Test
//	public void test_BaseOptions(){
//		AppBase<IsLoader> base = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
//			@Override public String getAppName() {return "test-app";}
//			@Override public String getAppDescription() {return "app for testing";}
//			@Override public String getAppVersion() {return "0.0.0";}
//		};
//		assertEquals(1, base.executeApplication(new String[]{"--version"}));
//
//		Set<Apo_SimpleC> simpleOptions = base.getCliParser().getSimpleOptions();
//		simpleOptions.add(new AO_Version_New('v'));
//
//		Set<Apo_TypedC<?>> typedOptions = base.getCliParser().getTypedOptions();
//		typedOptions.add(new AO_HelpTyped('h'));
//
//		this.printOptions(this.sortOptions(options));
//	}
//
//	@Test
//	public void test_IsOptions(){
//		AppBase<IsLoader> base = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
//			@Override public String getAppName() {return "test-app";}
//			@Override public String getAppDescription() {return "app for testing";}
//			@Override public String getAppVersion() {return "0.0.0";}
//		};
//
//		Svg2Vector_IS app = new Svg2Vector_IS();
//
//		ApplicationOption<?>[] options = app.getAppOptions();
//		List<ApplicationOption<?>> remove = new ArrayList<>();
//		for(ApplicationOption<?> optBase : base.getAppOptions()){
//			for(ApplicationOption<?> optApp : options){
//				if(optBase.getCliOption().getLongOpt().equals(optApp.getCliOption().getLongOpt())){
//					remove.add(optApp);
//				}
//			}
//		}
//		for(ApplicationOption<?> rem : remove){
//			options = ArrayUtils.removeElement(options, rem);
//		}
//		this.printOptions(this.sortOptions(options));
//	}
//
//	@Test
//	public void test_FhOptions(){
//		AppBase<IsLoader> base = new AppBase<IsLoader>(SvgTargets.values(), new IsLoader()) {
//			@Override public String getAppName() {return "test-app";}
//			@Override public String getAppDescription() {return "app for testing";}
//			@Override public String getAppVersion() {return "0.0.0";}
//		};
//
//		Svg2Vector_FH app = new Svg2Vector_FH();
//
//		ApplicationOption<?>[] options = app.getAppOptions();
//		List<ApplicationOption<?>> remove = new ArrayList<>();
//		for(ApplicationOption<?> optBase : base.getAppOptions()){
//			for(ApplicationOption<?> optApp : options){
//				if(optBase.getCliOption().getLongOpt().equals(optApp.getCliOption().getLongOpt())){
//					remove.add(optApp);
//				}
//			}
//		}
//		for(ApplicationOption<?> rem : remove){
//			options = ArrayUtils.removeElement(options, rem);
//		}
//		this.printOptions(this.sortOptions(options));
//	}
//
//
//	/**
//	 * Takes an array of options and prints them using the STG.
//	 * @param options array of options, ignored if null
//	 */
//	protected void printOptions(ApplicationOption<?>[] options){
//		if(options==null){
//			return;
//		}
//
//		STGroupFile stg = new STGroupFile("de/vandermeer/svg2vector/sitedocs/cli.stg");
//		ST table = stg.getInstanceOf("table");
//		for(ApplicationOption<?> opt :options){
//			ST entry = stg.getInstanceOf("tableEntry");
//			if(opt.getCliOption().getOpt()!=null){
//				entry.add("shortOpt", opt.getCliOption().getOpt());
//			}
//			entry.add("longOpt", opt.getCliOption().getLongOpt());
//			if(opt.getCliOption().getArgName()!=null){
//				entry.add("arg", opt.getCliOption().getArgName());
//			}
//			if(opt.getCliOption().isRequired()){
//				entry.add("required", "true");
//			}
//			entry.add("descr", opt.getShortDescription());
//			if(opt.getCliArgumentDescription()!=null){
//				entry.add("argDescr", opt.getCliArgumentDescription());
//			}
//			table.add("options", entry.render());
//		}
//		System.out.println(table.render());
//	}
//
//	/**
//	 * Takes an array of options and returns a sorted list of them.
//	 * @param options array of options, ignored if null
//	 * @return sorted list of options
//	 */
//	protected ApplicationOption<?>[] sortOptions(ApplicationOption<?>[] options){
//		TreeMap<String, ApplicationOption<?>> so = new TreeMap<>();
//		if(options!=null){
//			for(ApplicationOption<?> opt :options){
//				String key = opt.getCliOption().getLongOpt();
//				if(opt.getCliOption().getOpt()!=null){
//					key = opt.getCliOption().getOpt() +"," + key;
//				}
//				else{
//					key = key.charAt(0) + "," + key;
//				}
//				so.put(key.toLowerCase(), opt);
//			}
//		}
//
//		LinkedList<ApplicationOption<?>> ret = new LinkedList<>();
//		for(ApplicationOption<?> opt : so.values()){
//			ret.add(opt);
//		}
//		return ret.toArray(new ApplicationOption<?>[0]);
//	}

}
