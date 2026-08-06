package com.bluersw;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import hudson.model.ParametersDefinitionProperty;
import org.jenkinsci.plugins.structs.describable.DescribableModel;
import org.jenkinsci.plugins.structs.describable.UninstantiatedDescribable;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

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
}
