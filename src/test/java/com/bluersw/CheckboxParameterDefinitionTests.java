package com.bluersw;

import com.bluersw.analyze.Format;
import com.bluersw.model.CheckboxList;
import hudson.model.ParametersDefinitionProperty;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.structs.describable.DescribableModel;
import org.jenkinsci.plugins.structs.describable.UninstantiatedDescribable;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.kohsuke.stapler.StaplerRequest2;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.bluersw.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@WithJenkins
class CheckboxParameterDefinitionTests {

    private JenkinsRule jenkins;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        jenkins = rule;
    }

    @Test
    void testScriptedPipeline() throws Exception {
        CheckboxParameterDefinition param = new CheckboxParameterDefinition(NAME, DESCRIPTION, PROTOCOL, FORMAT, "", "", "", USE_INPUT, "");
        param.setDefaultValue(DEFAULT_VALUE);
        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-scripted-pipeline");
        job.addProperty(new ParametersDefinitionProperty(param));
        String pipelineScript
                = """
                node {
                  print params['SELECT_NODES']
                }""";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        WorkflowRun completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
        String expectedString = DEFAULT_VALUE;
        jenkins.assertLogContains(expectedString, completedBuild);
    }

    @Test
    void testPipelineSubmitContentCanBeReadByStructs() {
        String pipelineSubmitContent = "{\"CheckboxParameter\":[{\"key\":\"linux\",\"value\":\"linux\"}]}";
        CheckboxParameterDefinition param = new CheckboxParameterDefinition(
                NAME, DESCRIPTION, PROTOCOL, FORMAT, "", "", "", null, pipelineSubmitContent);

        UninstantiatedDescribable declarativeModel = DescribableModel.of(CheckboxParameterDefinition.class)
                .uninstantiate2(param);

        assertEquals(pipelineSubmitContent, declarativeModel.getArguments().get("pipelineSubmitContent"));
    }

    @Test
    void testIndexViewUsesExternalScriptInitialization() throws Exception {
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
    void testYamlCheckedValuesProvideInitialSelection() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    checked: true
                  - key: windows
                    value: windows
                    checked: false
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("checked", checkboxList.list.get(0).checked);
        assertEquals("", checkboxList.list.get(1).checked);
    }

    @Test
    void testJsonCheckedValuesProvideInitialSelection() {
        String content = """
                {
                  "CheckboxParameter": [
                    {
                      "key": "linux",
                      "value": "linux",
                      "checked": true
                    },
                    {
                      "key": "windows",
                      "value": "windows",
                      "checked": false
                    }
                  ]
                }""";
        CheckboxParameterDefinition param = createInlineParameter(Format.JSON, content);

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("checked", checkboxList.list.get(0).checked);
        assertEquals("", checkboxList.list.get(1).checked);
    }

    @Test
    void testPreviousSelectionOverridesConfiguredCheckedValues() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    checked: true
                  - key: windows
                    value: windows
                    checked: false
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
        param.setDefaultValue("windows");

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("", checkboxList.list.get(0).checked);
        assertEquals("checked", checkboxList.list.get(1).checked);
    }

    @Test
    void testEmptyPreviousSelectionDoesNotRestoreConfiguredDefaults() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    checked: true
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
        JSONObject submittedValue = new JSONObject();
        submittedValue.put("name", NAME);
        param.createValue((StaplerRequest2) null, submittedValue);

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("", checkboxList.list.get(0).checked);
    }

    @Test
    void testConfigurationWithoutCheckedValuesRemainsSupported() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("", checkboxList.list.get(0).checked);
    }

    @Test
    void testCustomCheckedNodePath() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    selected: true
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);
        param.setCheckedNodePath("//CheckboxParameter/selected");

        CheckboxList checkboxList = param.getCheckboxList();

        assertEquals("checked", checkboxList.list.get(0).checked);
    }

    @Test
    void testCheckedValueMustBePresentForEveryCheckbox() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    checked: true
                  - key: windows
                    value: windows
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

        CheckboxList checkboxList = param.getCheckboxList();

        assertTrue(checkboxList.list.isEmpty());
        assertTrue(checkboxList.getMessage().contains("true or false"));
    }

    @Test
    void testCheckedValueMustBeBoolean() {
        String content = """
                CheckboxParameter:
                  - key: linux
                    value: linux
                    checked: invalid
                """;
        CheckboxParameterDefinition param = createInlineParameter(Format.YAML, content);

        CheckboxList checkboxList = param.getCheckboxList();

        assertTrue(checkboxList.list.isEmpty());
        assertTrue(checkboxList.getMessage().contains("true or false"));
    }

    private CheckboxParameterDefinition createInlineParameter(Format documentFormat, String content) {
        return new CheckboxParameterDefinition(
                NAME, DESCRIPTION, PROTOCOL, documentFormat, "", "", "", null, content);
    }
}
