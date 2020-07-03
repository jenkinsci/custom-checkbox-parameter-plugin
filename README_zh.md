# Custom Checkbox Parameter Plugin

这个插件可以在构建之前，动态的创造一组复选框供用户进行勾选，复选框的设置通过YAML或JSON文件进行配置，文件内容可以通过HTTP、HTTPS或本地文件获取。
用户勾选复选框之后可以在构建脚本中使用params['参数名称']来获取选择的值，用户选择的结果通过value1,value2,value3这种用“,”分割的字符串形式返回。

## 设置说明

完整配置内容示例如下，但没有必要填写所有内容，大多数配置内容使用默认值即可：
![project doc image](images/image-01_zh.png)

参数名称：在构建脚本中使用params['参数名称']获取用户选择的值。

使用协议：HTTP、HTTPS、LOCAL，LOCAL代表本地文件系统，如果使用HTTP协议，HTTP和HTTPS选项任选其一，因为可以通过URI的输入自动识别。

URI：如果“使用协议”选项是HTTP和HTTPS则URI请输入文件的URL，如果“使用协议”选项是LOCAL则URI请输入文件在Jenkins服务器上的文件路径。
