package de.vandermeer.svg2vector.applications.is;

import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.vandermeer.execs.options.AbstractApplicationOption;
import de.vandermeer.svg2vector.applications.base.SvgTargets;

public abstract class IsTargetOption extends AbstractApplicationOption<String> {

	/** The target the option applies to. */
	protected SvgTargets target;

	/** The Inkscape command line option for this target option. */
	protected String isCli;

//	/**
//	 * Returns the new option as not required and without a short option.
//	 * @param target the target to which this option applies, must not be null
//	 * @param isCli the Inkscape command line option to be used, must not be blank
//	 * @param longOption the long option, must not be blank
//	 * @param description the short description, must not be blank
//	 * @param longDescription the long description, must not be blank
//	 * @throws NullPointerException - if any parameter is null
//	 * @throws IllegalArgumentException - if any parameter is null
//	 */
//	public IsTargetOption(SvgTargets target, String isCli, String longOption, String description, String longDescription){
//		this(target, isCli, longOption, null, description, longDescription);
//	}

	/**
	 * Returns the new option as not required and without a short option.
	 * @param target the target to which this option applies, must not be null
	 * @param isCli the Inkscape command line option to be used including any leading dashs, must not be blank
	 * @param longOption the long option, must not be blank
	 * @param argument the argument, no argument added if blank
	 * @param description the short description, must not be blank
	 * @param longDescription the long description, must not be blank
	 * @throws NullPointerException - if any required parameter is null
	 * @throws IllegalArgumentException - if any required parameter is null
	 */
	public IsTargetOption(SvgTargets target, String isCli, String longOption, String argument, String description, String longDescription){
		super(description, longDescription);
		Validate.notNull(target);
		Validate.notBlank(isCli);
		Validate.notBlank(longOption);

		Option.Builder builder = Option.builder();
		builder.longOpt(longOption);
		if(!StringUtils.isBlank(argument)){
			builder.hasArg().argName(argument);
		}
		builder.required(false);
		this.setCliOption(builder.build());

		this.target = target;
		this.isCli = isCli;
	}

	/**
	 * Returns the target the option applies to.
	 * @return the target
	 */
	public SvgTargets getTarget(){
		return this.target;
	}

	/**
	 * Returns the Inkscape CLI option.
	 * @return Inkscape CLI option
	 */
	public String getIsCmd(){
		return this.isCli;
	}

	@Override
	public String convertValue(Object value) {
		if(value==null){
			return null;
		}
		return value.toString();
	}
}
