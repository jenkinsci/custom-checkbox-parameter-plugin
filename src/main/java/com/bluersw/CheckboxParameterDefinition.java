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
import hudson.util.ListBoxModel;
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
	private String displayNodePath;
	private String valueNodePath;
	private String tlsVersion;
	private boolean useInput;

	@DataBoundConstructor
	public CheckboxParameterDefinition(String name, Protocol protocol, Format format,
			String submitContent, String uri, String displayNodePath,
			String valueNodePath, String tlsVersion, boolean useInput) {
		super(name, DESCRIPTION);
		this.uuid = UUID.randomUUID();
		this.protocol = protocol;
		this.format = format;
		this.submitContent = submitContent;
		this.uri = uri;
		this.displayNodePath = displayNodePath;
		this.valueNodePath = valueNodePath;
		this.tlsVersion = tlsVersion;
		this.useInput = useInput;
	}

	public boolean getUseInput() {
		return this.useInput;
	}

	@DataBoundSetter
	public void setUseInput(boolean useInput) {
		this.useInput = useInput;
	}

	public String getDisplayNodePath() {
		return this.displayNodePath;
	}

	@DataBoundSetter
	public void setDisplayNodePath(String displayNodePath) {
		this.displayNodePath = displayNodePath;
	}

	public String getValueNodePath() {
		return this.valueNodePath;
	}

	@DataBoundSetter
	public void setValueNodePath(String valueNodePath) {
		this.valueNodePath = valueNodePath;
	}

	public String getTlsVersion() {
		return this.tlsVersion;
	}

	@DataBoundSetter
	public void setTlsVersion(String tlsVersion) {
		this.tlsVersion = tlsVersion;
	}

	public String getUri() {
		return this.uri;
	}

	@DataBoundSetter
	public void setUri(String submitContent) {
		this.uri = uri;
	}

	public String getSubmitContent() {
		return this.submitContent;
	}

	@DataBoundSetter
	public void setSubmitContent(String submitContent) {
		this.submitContent = submitContent;
	}

	public Format getFormat() {
		return this.format;
	}

	@DataBoundSetter
	public void setFormat(Format format) {
		this.format = format;
	}

	public Protocol getProtocol() {
		return this.protocol;
	}

	@DataBoundSetter
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

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

	public String getDivId() {
		return String.format("%s-%s", getName().replaceAll("\\W", "_"), this.uuid);
	}

	@Symbol("checkboxParameter")
	@Extension
	public static class DescriptorImpl extends ParameterDescriptor {
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
			Protocol protocol = Protocol.valueOf(formData.getString("protocol"));
			Format format = Format.valueOf(formData.getString("format"));
			String submitContent = formData.getString("submitContent");
			String uri = formData.getString("uri");
			String displayNodePath = formData.getString("displayNodePath");
			String valueNodePath = formData.getString("valueNodePath");
			String tlsVersion = formData.getString("tlsVersion");
			boolean useInput = formData.getBoolean("useInput");
			return new CheckboxParameterDefinition(name, protocol, format, submitContent, uri, displayNodePath, valueNodePath, tlsVersion, useInput);
		}
	}
}
