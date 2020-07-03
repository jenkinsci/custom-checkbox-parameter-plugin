package com.bluersw;

import com.bluersw.analyze.Format;
import com.bluersw.source.Protocol;

public class Constants {
	static final String Name = "my-checkbox";
	static final Protocol protocol = Protocol.HTTP;
	static final Format format = Format.YAML;
	static final boolean useInput = false;
	static final String submitContent = "";
	static final String uri = "https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml";
	static final String displayNodePath = "//CheckboxParameter/key";
	static final String valueNodePath = "//CheckboxParameter/value";
	static final String tlsVersion = "TLSv1.2";
	static final String description = "";
	static final String defaultValue = "default value";
}
