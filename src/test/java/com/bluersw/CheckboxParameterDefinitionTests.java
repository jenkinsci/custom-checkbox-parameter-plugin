package com.bluersw;

import hudson.model.ParametersDefinitionProperty;
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
}