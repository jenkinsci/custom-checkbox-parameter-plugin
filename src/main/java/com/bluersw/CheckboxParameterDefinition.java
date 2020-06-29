package com.bluersw;

import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.CheckForNull;

import com.bluersw.analyze.Format;
import com.bluersw.source.Protocol;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author sunweisheng
 */
public class CheckboxParameterDefinition extends ParameterDefinition implements Comparable<CheckboxParameterDefinition> {

	private static final long serialVersionUID = 5171255336407195201L;
	private static final Logger LOGGER = Logger.getLogger(CheckboxParameterDefinition.class.getName());
	private static final String DESCRIPTION = "Custom Checkbox Parameter.";

	private final UUID uuid;
	private String defaultValue;
	private Protocol protocol;
	private Format format;
	private String submitContent;
	private String uri;
	private String searchCommand;
	private String displaySearch;
	private String valueSearch;
	private String tlsVersion;

	@DataBoundConstructor
	public CheckboxParameterDefinition(String name, String protocol, String format, String submitContent, String uri, String searchCommand, String displaySearch, String valueSearch, String tlsVersion) {
		super(name, DESCRIPTION);
		this.uuid = UUID.randomUUID();
		this.protocol = Protocol.valueOf(protocol);
		this.format = Format.valueOf(format);
		this.submitContent = submitContent;
		this.uri = uri;
		this.searchCommand = searchCommand;
		this.displaySearch = displaySearch;
		this.valueSearch = valueSearch;
		this.tlsVersion = tlsVersion;
	}

	public String getSearchCommand(){return this.searchCommand;}

	@DataBoundSetter
	public void setSearchCommand(String searchCommand){this.searchCommand = searchCommand;}

	public String getDisplaySearch(){return this.displaySearch;}

	@DataBoundSetter
	public void setDisplaySearch(String displaySearch){this.displaySearch = displaySearch;}

	public String getValueSearch(){return this.valueSearch;}

	@DataBoundSetter
	public void setValueSearch(String valueSearch){this.valueSearch = valueSearch;}

	public String getTlsVersion(){return this.tlsVersion;}

	@DataBoundSetter
	public void setTlsVersion(String tlsVersion){this.tlsVersion = tlsVersion;}

	public String getUri(){return this.uri;}

	@DataBoundSetter
	public void setUri(String submitContent){this.uri = uri;}

	public String getSubmitContent(){return this.submitContent;}

	@DataBoundSetter
	public void setSubmitContent(String submitContent){this.submitContent = submitContent;}

	public String getFormat(){return this.format.name();}

	@DataBoundSetter
	public void setFormat(String format){this.format = Format.valueOf(format);}

	public String getProtocol(){return this.protocol.name();}

	@DataBoundSetter
	public void setProtocol(String protocol){this.protocol = Protocol.valueOf(protocol);}

	@CheckForNull
	@Override
	public ParameterValue createValue(StaplerRequest staplerRequest, JSONObject jsonObject) {
		return null;
	}

	@CheckForNull
	@Override
	public ParameterValue createValue(StaplerRequest staplerRequest) {
		return null;
	}

	@SuppressFBWarnings(value = "EQ_COMPARETO_USE_OBJECT_EQUALS")
	@Override
	public int compareTo(CheckboxParameterDefinition o) {
		if (o.uuid.equals(this.uuid)) {
			return 0;
		}
		else {
			return -1;
		}
	}

	@Symbol("checkboxParameter")
	@Extension
	public static class DescriptorImpl extends ParameterDescriptor{
		public DescriptorImpl() {
			load();
		}

		public FormValidation doCheckName(@QueryParameter String name, @QueryParameter String protocol, @QueryParameter String format, @QueryParameter String submitContent, @QueryParameter String uri, @QueryParameter String searchCommand, @QueryParameter String displaySearch, @QueryParameter String valueSearch, @QueryParameter String tlsVersion) {
			return FormValidation.ok();
		}

		@NonNull
		@Override
		public String getDisplayName() {
			return Messages.CheckboxParameterDefinition_DescriptorImpl_DisplayName();
		}

		@Override
		/*
		 * We need this for JENKINS-26143 -- reflective creation cannot handle setChoices(Object). See that method for context.
		 */
		public ParameterDefinition newInstance(@Nullable StaplerRequest req, @NonNull JSONObject formData) {
			String name = formData.getString("name");
			String protocol = formData.getString("protocol");
			String format = formData.getString("format");
			String submitContent = formData.getString("submitContent");
			String uri = formData.getString("uri");
			String searchCommand = formData.getString("searchCommand");
			String displaySearch = formData.getString("displaySearch");
			String valueSearch = formData.getString("valueSearch");
			String tlsVersion = formData.getString("tlsVersion");
			return new CheckboxParameterDefinition(name, protocol,format,submitContent,uri,searchCommand,displaySearch,valueSearch,tlsVersion);
		}
	}


}