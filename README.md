# Custom Checkbox Parameter Plugin

This plug-in can dynamically create a set of check boxes for users to check before building. The settings of the check boxes are configured through YAML or JSON files, and the file content can be obtained through the file system of HTTP, HTTPS, or Jenkins server.
After checking the check box, the user can use params['ParameterName'] in the build script to get the selected value. The result of the user's selection is returned in the form of a string separated by "," value1, value2, value3.

[中文说明](README_zh.md)

## Setting instructions

The complete configuration content example is as follows, but it is not necessary to fill in all the content. Most configuration content can use the default values:
![project doc image](images/image-01.png)

Parameter Name: Use params['ParameterName'] in the build script to get the value selected by the user.

Description: The description text of the construction parameters.

Use Protocol: HTTP, HTTPS, JENKINS_LOCAL, JENKINS_LOCAL represents the file system of the Jenkins server. If you use the HTTP protocol, you can choose either HTTP or HTTPS, because it can be automatically recognized by the input of the URI.

URI: If the "Use Protocol" option is HTTP and HTTPS, please enter the URL of the file. If the "Use Protocol" option is JENKINS_LOCAL, then please enter the file path of the file on the Jenkins server.

Document Format: supports YAML and JSON formats. (YAML requires spaces to be aligned and spaces after ":")

Examples of YAML format and JSON format are as follows:

```yaml
CheckboxParameter:
  - key: key-1
    value: value-1
  - key: key-2
    value: value-2
  - key: key-3
    value: value-3
```

```json
{
    "CheckboxParameter": [
    {
      "key": "key-1",
      "value": "value-1"
    },
    {
      "key": "key-2",
      "value": "value-2"
    },
    {
      "key": "key-3",
      "value": "value-3"
    }
  ]
}
```

Display Node Path: Specify the node path for the check box to display the content (this format is common to different formats). The root of the node in the file is represented by "//", and the child nodes are separated by "/". The default is: / /CheckboxParameter/key, corresponding to the content selected in the above example: key-1, key-2, key-3, the node path can be customized according to this mode.

Value Node Path: Specify the node path for the check box to select the value (this format is common to different formats). The root of the node in the file is represented by "//", and the child nodes are separated by "/". The default is: / /CheckboxParameter/value, corresponding to the content selected in the above example: value-1, value-2, value-3, the node path can be customized according to this mode.

## Other settings

After selecting the "Use only the input as the document content" check box, you can directly enter the YAML format or JSON format content of the configuration check box. The content format is set by the "Document Format" option, and the URI will be ignored after directly entering the configuration content Settings.

![project doc image](images/image-02.png)

After clicking "Advanced", you can configure the TLS Version option. This option is used to query the TLS version set by the remote server when using HTTPS to obtain the remote file, and set the TLS protocol version here to try to solve the problem.

TLS versions are: SSL 1.0, SSL 2.0, SSL 3.0, TLS 1.0, TLS 1.1, TLS 1.2, TLS 1.3

![project doc image](images/image-03.png)

## Read remote HTTPS configuration checkbox example

[Configuration file address](https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml)

Configuration file content:

```yaml
CheckboxParameter:
  - key: y-1
    value: value-1
  - key: y-2
    value: value-2
  - key: y-3
    value: value-3
```

![project doc image](images/image-04.png)

Build script:

```groovy
node{
    print params['my-checkbox']
}
```

Construction parameter selection:

![project doc image](images/image-05.png)

```text
Started by user sunweisheng
Running in Durability level: MAX_SURVIVABILITY
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in /root/.jenkins/workspace/test-custom-checkbox-parameter
[Pipeline] {
[Pipeline] echo
value-1,value-3
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```

In addition: the results of the check boxes selected during each build will be saved for the user to use next time.

## Other configuration examples

Jenkins server file system:

![project doc image](images/image-06.png)

Enter the file content directly (Json format):

![project doc image](images/image-07.png)

## Declarative Pipeline

[Configuration file address](https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.json)

Configuration file content:

```json
CheckboxParameter": [
    {
      "key": "j-1",
      "value": "value-1"
    },
    {
      "key": "j-2",
      "value": "value-2"
    },
    {
      "key": "j-3",
      "value": "value-3"
    }
  ]
```

Minimum parameters:

```groovy
pipeline {
    
   agent any

   parameters{
      checkboxParameter name:'my-checkbox', format:'JSON', uri:'https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.json'
   }
   stages {
      stage('Hello') {
         steps {
            print params['my-checkbox']
         }
      }
   }
}
```

Show checkbox during build:

![project doc image](images/image-08.png)

parameter list:

- name: required
- description: not required, default ""
- protocol: not required, HTTP, HTTPS, JENKINS_LOCAL, default HTTP
- format: required, YAML, JSON, default Empty
- uri: not required, the default is "", if both uri and submitContent are empty, an error will be prompted during the build
- useInput: not required, default false
- submitContent: not required, default ""
- displayNodePath: not required, default //CheckboxParameter/key
- valueNodePath: not required, default //CheckboxParameter/value
- tlsVersion: not required, default TLSv1.2

You can create parameters in the build script, but because each execution of the build script creates a new "Custom Checkbox Parameter" build parameter, the last selected value cannot be retained.
