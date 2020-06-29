package com.bluersw;

import hudson.model.StringParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author sunweisheng
 */
public class CheckboxParameterValue extends StringParameterValue {
	private static final long serialVersionUID = 9076294870486621533L;

	@DataBoundConstructor
	public CheckboxParameterValue(String name, String value){super(name, value);}
}
