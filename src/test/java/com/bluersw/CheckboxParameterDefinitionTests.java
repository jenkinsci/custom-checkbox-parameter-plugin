package com.bluersw;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.bluersw.analyze.Format;
import com.bluersw.model.CheckboxList;
import hudson.model.ParametersDefinitionProperty;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.structs.describable.DescribableModel;
import org.jenkinsci.plugins.structs.describable.UninstantiatedDescribable;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.stapler.StaplerRequest2;

import static com.bluersw.Constants.*;

import static org.junit.Assert.*;

public class CheckboxParameterDefinitionTests {
	@Rule
	public JenkinsRule jenkins = new JenkinsRule();

	@Test
	public void testScriptedPipeline() throws Exception{
		CheckboxParameterDefinition param = new CheckboxParameterDefinition(name,description,protocol,format,"","","",useInput,"");
		param.setDefaultValue(defaultValue);
		WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-scripted-pipeline");
		job.addProperty(new ParametersDefinitionProperty(param));
		String pipelineScript
				= "node {\n"
				+ "  print params['SELECT_NODES'] \n"
				+ "}";
		job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
		WorkflowRun completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		String expectedString = defaultValue;
		jenkins.assertLogContains(expectedString, completedBuild);
	}

	@Test
	public void testPipelineSubmitContentCanBeReadByStructs() throws Exception {
		String pipelineSubmitContent = "{\"CheckboxParameter\":[{\"key\":\"linux\",\"value\":\"linux\"}]}";
		CheckboxParameterDefinition param = new CheckboxParameterDefinition(
				name, description, protocol, format, "", "", "", null, pipelineSubmitContent);

		UninstantiatedDescribable declarativeModel = DescribableModel.of(CheckboxParameterDefinition.class)
				.uninstantiate2(param);

		assertEquals(pipelineSubmitContent, declarativeModel.getArguments().get("pipelineSubmitContent"));
	}

	@Test
	public void testIndexViewUsesExternalScriptInitialization() throws Exception {
		try (InputStream view = CheckboxParameterDefinition.class
				.getResourceAsStream("CheckboxParameterDefinition/index.jelly")) {
			assertNotNull(view);
			String jelly = new String(view.readAllBytes(), StandardCharsets.UTF_8);
			assertFalse(jelly.contains("<script"));
			assertTrue(jelly.contains("data-checkbox-url"));
			assertTrue(jelly.contains("data-parameter-name"));
		}
	}

	@Test
	public void testYamlCheckedValuesProvideInitialSelection() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    checked: true\n"
				+ "  - key: windows\n"
				+ "    value: windows\n"
				+ "    checked: false\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("checked", checkboxList.list.get(0).checked);
		assertEquals("", checkboxList.list.get(1).checked);
	}

	@Test
	public void testJsonCheckedValuesProvideInitialSelection() {
		String content = "{\"CheckboxParameter\":["
				+ "{\"key\":\"linux\",\"value\":\"linux\",\"checked\":true},"
				+ "{\"key\":\"windows\",\"value\":\"windows\",\"checked\":false}]}";
		CheckboxParameterDefinition param = createInlineParameter(Format.JSON, content);

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("checked", checkboxList.list.get(0).checked);
		assertEquals("", checkboxList.list.get(1).checked);
	}

	@Test
	public void testPreviousSelectionOverridesConfiguredCheckedValues() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    checked: true\n"
				+ "  - key: windows\n"
				+ "    value: windows\n"
				+ "    checked: false\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
		param.setDefaultValue("windows");

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("", checkboxList.list.get(0).checked);
		assertEquals("checked", checkboxList.list.get(1).checked);
	}

	@Test
	public void testEmptyPreviousSelectionDoesNotRestoreConfiguredDefaults() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    checked: true\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
		JSONObject submittedValue = new JSONObject();
		submittedValue.put("name", name);
		param.createValue((StaplerRequest2) null, submittedValue);

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("", checkboxList.list.get(0).checked);
	}

	@Test
	public void testConfigurationWithoutCheckedValuesRemainsSupported() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("", checkboxList.list.get(0).checked);
	}

	@Test
	public void testCustomCheckedNodePath() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    selected: true\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
		param.setCheckedNodePath("//CheckboxParameter/selected");

		CheckboxList checkboxList = param.getCheckboxList();

		assertEquals("checked", checkboxList.list.get(0).checked);
	}

	@Test
	public void testCheckedValueMustBePresentForEveryCheckbox() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    checked: true\n"
				+ "  - key: windows\n"
				+ "    value: windows\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

		CheckboxList checkboxList = param.getCheckboxList();

		assertTrue(checkboxList.list.isEmpty());
		assertTrue(checkboxList.getMessage().contains("true or false"));
	}

	@Test
	public void testCheckedValueMustBeBoolean() {
		String content = "CheckboxParameter:\n"
				+ "  - key: linux\n"
				+ "    value: linux\n"
				+ "    checked: invalid\n";
		CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

		CheckboxList checkboxList = param.getCheckboxList();

		assertTrue(checkboxList.list.isEmpty());
		assertTrue(checkboxList.getMessage().contains("true or false"));
	}

	private CheckboxParameterDefinition createInlineParameter(Format documentFormat, String content) {
		return new CheckboxParameterDefinition(
				name, description, protocol, documentFormat, "", "", "", null, content);
	}
}
