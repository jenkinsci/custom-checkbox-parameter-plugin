# Custom Checkbox Parameter Plugin

这个插件可以在构建之前，动态的创造一组复选框供用户进行勾选，复选框的设置通过YAML或JSON文件进行配置，文件内容可以通过HTTP、HTTPS或Jenkins服务器的文件系统获取。
用户勾选复选框之后可以在构建脚本中使用params['参数名称']来获取选择的值，用户选择的结果通过value1,value2,value3这种用“,”分割的字符串形式返回。

## 设置说明

完整配置内容示例如下，但没有必要填写所有内容，大多数配置内容使用默认值即可：
![project doc image](images/image-01_zh.png)

参数名称：在构建脚本中使用params['参数名称']获取用户选择的值。

说明：对这个构建参数的说明文字。

使用协议：HTTP、HTTPS、JENKINS_LOCAL，JENKINS_LOCAL代表Jenkins服务器的文件系统，如果使用HTTP协议，HTTP和HTTPS选项任选其一，因为可以通过URI的输入自动识别。

URI：如果“使用协议”选项是HTTP和HTTPS则URI请输入文件的URL，如果“使用协议”选项是JENKINS_LOCAL则URI请输入文件在Jenkins服务器上的文件路径。

文档格式：支持YAML和JSON两种格式。（YAML要求空格对齐并且“:”后要有空格）

YAML和JSON示例如下:

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

显示节点路径：指定用于复选框显示内容的节点路径（不同格式通用此路径格式），文件中根用"//"表示，节点之间用"/"分割，默认是：//CheckboxParameter/key，对应上面的示例选取的内容是：key-1、key-2、key-3，可根据此模式自定义节点路径。

值节点路径：指定用于复选框选择值的节点路径（不同格式通用此路径格式），文件中根用"//"表示，节点之间用"/"分割，默认是：//CheckboxParameter/value，对应上面的示例选取的内容是：value-1、value-2、value-3，可根据此模式自定义节点路径。

## 其他设置说明

选择“仅使用输入的内容作为配置文件内容”复选框之后，可以直接输入配置复选框的YAML格式或JSON格式内容，内容格式由"文档格式"选项设置，直接输入配置内容后将忽略URI设置。

![project doc image](images/image-02_zh.png)

点击“高级”后可以配置TLS Version选项，此选项用于使用HTTPS获取远程文件失败时，可以查询远程服务器设置的TLS版本，并在此设置TLS协议版本尝试解决问题。

TLS版本有： SSL 1.0、SSL 2.0、SSL 3.0、TLS 1.0、TLS 1.1、TLS 1.2、TLS 1.3

![project doc image](images/image-03_zh.png)

## 读取远程HTTPS配置复选框示例

[配置文件地址](https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml)

配置文件内容：

```yaml
CheckboxParameter:
  - key: y-1
    value: value-1
  - key: y-2
    value: value-2
  - key: y-3
    value: value-3
```

![project doc image](images/image-04_zh.png)

构建参数显示结果：

![project doc image](images/image-05_zh.png)

构建脚本：

```groovy
node{
    print params['my-checkbox']
}
```

构建结果：

```txt
Running in Durability level: MAX_SURVIVABILITY
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in /Users/sunweisheng/Documents/HomeCode/custom-checkbox-parameter-plugin/work/workspace/test
[Pipeline] {
[Pipeline] echo
value-1,value-3
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```

另外：每次选择的内容会保存，方便用户下次构建时重复选择。

## 其他配置示例

Jenkins服务器文件系统：

![project doc image](images/image-06_zh.png)

直接输入文件内容（Json格式）：

![project doc image](images/image-07_zh.png)

## Declarative Pipeline

[配置文件地址](https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.json)

配置文件内容：

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

最少的参数：

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

构建时显示参数：

![project doc image](images/image-08_zh.png)

参数列表：

- name：必填
- description：非必填，默认“”
- protocol：非必填，HTTP、HTTPS、JENKINS_LOCAL，默认HTTP
- format：必填，YAML、JSON，默认Empty
- uri：非必填，默认“”，如果uri和submitContent都是空，那么构建时会提示错误
- useInput：非必填，默认false
- submitContent：非必填，默认“”
- displayNodePath：非必填，默认//CheckboxParameter/key
- valueNodePath：非必填，默认//CheckboxParameter/value
- tlsVersion：非必填，默认TLSv1.2

可以在构建脚本中创建参数，但因为每次执行构建脚本都会创建一个新的"Custom Checkbox Parameter"构建参数，所以无法保留上次选择的值。
