package com.bluersw;

import hudson.model.StringParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * 用户勾选复选框的值，用","分割的字符串 The user selects the value of the check box, and the string separated by ","
 * @author sunweisheng
 */
public class CheckboxParameterValue extends StringParameterValue {
	private static final long serialVersionUID = 9076294870486621533L;

	@DataBoundConstructor
	public CheckboxParameterValue(String name, String value){super(name, value);}
}
