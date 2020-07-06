package com.bluersw;

import com.bluersw.analyze.Format;
import com.bluersw.source.Protocol;
import net.sf.json.JSONObject;

public class Constants {
	static final String Name = "my-checkbox";
	static final Protocol protocol = Protocol.HTTP_HTTPS;
	static final Format format = Format.YAML;
	static final JSONObject useInput = JSONObject.fromObject("{useInput:{submitContent:''}}");
	static final String uri = "https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml";
	static final String displayNodePath = "//CheckboxParameter/key";
	static final String valueNodePath = "//CheckboxParameter/value";
	static final String description = "";
	static final String defaultValue = "default value";
}
