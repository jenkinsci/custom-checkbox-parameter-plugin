package com.bluersw;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.CheckForNull;

import com.bluersw.analyze.Configuration;
import com.bluersw.analyze.ConfigurationFactory;
import com.bluersw.analyze.Format;
import com.bluersw.model.CheckboxList;
import com.bluersw.model.Result;
import com.bluersw.source.DataSource;
import com.bluersw.source.DataSourceFactory;
import com.bluersw.source.Protocol;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.cli.CLICommand;
import hudson.model.Job;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.ParametersDefinitionProperty;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import static org.apache.commons.lang.StringUtils.*;

/**
 * 自定义复选框构建参数定义类 Custom check box construction parameter definition class
 * @author sunweisheng
 */
public class CheckboxParameterDefinition extends ParameterDefinition implements Comparable<CheckboxParameterDefinition> {

	private static final long serialVersionUID = 5171255336407195201L;
	private static final Logger LOGGER = Logger.getLogger(CheckboxParameterDefinition.class.getName());
	private static final String DEFAULT_NAME_NODE = "//CheckboxParameter/key";
	private static final String DEFAULT_VALUE_NODE = "//CheckboxParameter/value";

	private final UUID uuid;
	private String defaultValue;
	private Protocol protocol;
	private Format format;
	private String submitContent;
	private String uri;
	private String displayNodePath;
	private String valueNodePath;
	private boolean useInput;

	@DataBoundConstructor
	public CheckboxParameterDefinition(String name, String description, Protocol protocol,
			Format format, String uri, String displayNodePath,
			String valueNodePath, JSONObject useInput, String pipelineSubmitContent) {
		super(name, description);
		this.uuid = UUID.randomUUID();
		this.protocol = protocol == null ? Protocol.HTTP_HTTPS : protocol;
		this.format = format == null ? Format.Empty : format;
		this.uri = isNotBlank(uri) ? uri : "";
		this.displayNodePath = isNotBlank(displayNodePath) ? displayNodePath : DEFAULT_NAME_NODE;
		this.valueNodePath = isNotBlank(valueNodePath) ? valueNodePath : DEFAULT_VALUE_NODE;
		this.submitContent = "";
		this.useInput = false;
		this.defaultValue = "";
		if (isNotBlank(pipelineSubmitContent)) {
			this.setPipelineSubmitContent(pipelineSubmitContent);
		} else {
			this.setUseInputAndSubmitContent(useInput);
		}
	}

	private void setPipelineSubmitContent(String submitContent){
		if (isNotBlank(submitContent)) {
			this.submitContent = submitContent;
			this.useInput = true;
		}
	}

	private void setUseInputAndSubmitContent(JSONObject jsonObject) {
		if (jsonObject != null) {
			final String submitContentName = "submitContent";
			if (jsonObject.get(submitContentName) != null) {
				this.submitContent = jsonObject.size() == 0 ? "" : jsonObject.getString(submitContentName);
				this.useInput = isNotBlank(this.submitContent);
			}
		}
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public boolean getUseInput() {
		return this.useInput;
	}

	@DataBoundSetter
	public void setUseInput(boolean useInput) { this.useInput = useInput; }

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


	public String getUri() {
		return this.uri;
	}

	@DataBoundSetter
	public void setUri(String uri) {
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

	/**
	 * 根据用户勾选的复选框创建构建参数返回值 Create the return value of the build parameter according to the check box checked by the user
	 * @param staplerRequest  staplerRequest
	 * @param jsonObject 用户提交的健值对数据 User submitted map data
	 * @return 用户勾选复选框的值，用","分割的字符串 The user selects the value of the check box, and the string separated by ","
	 */
	@CheckForNull
	@Override
	@SuppressWarnings("rawtypes")
	public ParameterValue createValue(StaplerRequest staplerRequest, JSONObject jsonObject) {
		StringBuilder result = new StringBuilder();
		for (Object o : jsonObject.entrySet()) {
			Map.Entry entry = (Map.Entry) o;
			if (entry.getKey().toString().startsWith("checkbox_")) {
				if (Boolean.parseBoolean(entry.getValue().toString())) {
					result.append(decodeBase64(entry.getKey().toString().replace("checkbox_", "")));
					result.append(',');
				}
			}
		}

		if (result.toString().length() > 0) {
			this.defaultValue = result.toString().substring(0, result.toString().length() - 1);
		}
		else {
			this.defaultValue = "";
		}
		return new CheckboxParameterValue(jsonObject.getString("name"), this.defaultValue);
	}

	@CheckForNull
	@Override
	public ParameterValue createValue(StaplerRequest staplerRequest) {
		String[] value = staplerRequest.getParameterValues(this.getName());
		if (value == null || value.length == 0 || isBlank(value[0])) {
			return this.getDefaultParameterValue();
		}
		else {
			return new CheckboxParameterValue(this.getName(), value[0]);
		}
	}

	@Override
	public ParameterValue createValue(CLICommand command, String value) {
		if (isNotEmpty(value)) {
			return new CheckboxParameterValue(this.getName(), value);
		}
		return getDefaultParameterValue();
	}

	@Override
	public ParameterValue getDefaultParameterValue() {
		return new CheckboxParameterValue(this.getName(), this.getDefaultValue());
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

	/**
	 * 获取文件内容 Get file content
	 * @return Result<String>
	 */
	private Result<String> getFileContent() {
		Result<String> result = new Result<>();
		if (this.useInput) {
			result.setContent(this.submitContent);
			result.setSucceed(true);
		}
		else {
			try {
				DataSource source = DataSourceFactory.createDataSource(this.protocol, this.uri);
				result.setContent(source.get());
				result.setSucceed(true);
				if (source.getStatusCode() != HttpStatus.SC_OK) {
					result.setMessage(source.getStatusLine());
					result.setContent("");
					result.setSucceed(false);
				}
			}
			catch (Exception e) {
				String exMessage = e.getMessage() == null ? e.toString() : e.getMessage();
				result.setContent("");
				result.setSucceed(false);
				result.setMessage(String
						.format("%s: %s", Messages.CheckboxParameterDefinition_GetFileFailed(), exMessage));
				LOGGER.log(Level.SEVERE, String
						.format("Failed to get file content: protocol: %s,uri: %s,exception info: %s"
								, this.protocol, this.uri, exMessage));
			}
		}

		return result;
	}

	private boolean isExist(String[] array, String value) {
		for (String item : array) {
			if (item.equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据文件内容创建复选框列表 Create checkbox list based on file content
	 * @return 复选框列表 Checkbox list
	 */
	public CheckboxList getCheckboxList() {
		CheckboxList checkboxList = new CheckboxList();
		Result<String> fileContent = getFileContent();
		try {
			if (!fileContent.isSucceed()) {
				checkboxList.message = fileContent.getMessage();
			}
			else {
				Configuration config = ConfigurationFactory.createConfiguration(this.format, fileContent.getContent());
				List<String> names = config.getValueListBySearch(this.displayNodePath);
				List<String> values = config.getValueListBySearch(this.valueNodePath);
				String[] defaultValues = this.defaultValue.split(",");

				int count = Math.min(names.size(), values.size());

				for (int i = 0; i < count; i++) {
					boolean selected = isExist(defaultValues, values.get(i));
					checkboxList.add(names.get(i), encodeBase64(values.get(i)), selected);
				}
			}
		}
		catch (Exception e) {
			String exMessage = e.getMessage() == null ? e.toString() : e.getMessage();
			checkboxList.message = String
					.format("%s: %s", Messages.CheckboxParameterDefinition_CreateCheckboxFailed(), exMessage);
			LOGGER.log(Level.SEVERE, String
					.format("Failed to create checkbox list,exception info: %s", exMessage));
		}

		return checkboxList;
	}

	private static String encodeBase64(String origin){
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
		return encoder.encodeToString(originBytes);
	}

	private static String decodeBase64(String origin){
		Base64.Decoder decoder = Base64.getDecoder();
		try {
			return new String(decoder.decode(origin), StandardCharsets.UTF_8);
		}catch (Exception e){
			String exMessage = e.getMessage() == null ? e.toString() : e.getMessage();
			LOGGER.log(Level.SEVERE, String
					.format("Failed to DecodeBase64,exception info: %s", exMessage));
			return origin;
		}
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Symbol("checkboxParameter")
	@Extension
	public static class DescriptorImpl extends ParameterDescriptor {
		public DescriptorImpl() {
			load();
		}

		public FormValidation doCheckName(@QueryParameter String name) {
			if (isBlank(name)) {
				return FormValidation.error(Messages.CheckboxParameterDefinition_Name_IsBlank());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckDisplayNodePath(@QueryParameter String displayNodePath) {
			if (isBlank(displayNodePath)) {
				return FormValidation.error(Messages.CheckboxParameterDefinition_DisplayNodePath_IsBlank());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckValueNodePath(@QueryParameter String valueNodePath) {
			if (isBlank(valueNodePath)) {
				return FormValidation.error(Messages.CheckboxParameterDefinition_ValueNodePath_IsBlank());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckTlsVersion(@QueryParameter String tlsVersion) {
			if (isBlank(tlsVersion)) {
				return FormValidation.error(Messages.CheckboxParameterDefinition_TlsVersion_IsBlank());
			}
			return FormValidation.ok();
		}

		@NonNull
		@Override
		public String getDisplayName() {
			return Messages.CheckboxParameterDefinition_DescriptorImpl_DisplayName();
		}

		@Override
		public ParameterDefinition newInstance(@Nullable StaplerRequest req, @NonNull JSONObject formData) {
			assert req != null;
			return req.bindJSON(CheckboxParameterDefinition.class,formData);
		}

		public CheckboxList doFillCheckboxItems(@AncestorInPath Job job, @QueryParameter String name) {
			CheckboxList checkboxList = null;
			ParametersDefinitionProperty prop = (ParametersDefinitionProperty) job
					.getProperty(ParametersDefinitionProperty.class);
			if (prop != null) {
				ParameterDefinition pd = prop.getParameterDefinition(name);
				if (pd instanceof CheckboxParameterDefinition) {
					CheckboxParameterDefinition cpd = (CheckboxParameterDefinition) pd;
					checkboxList = cpd.getCheckboxList();
				}
			}
			if (checkboxList == null) {
				checkboxList = new CheckboxList();
				checkboxList.message = "Parameter Definition Not Found.";
			}
			return checkboxList;
		}
	}
}
