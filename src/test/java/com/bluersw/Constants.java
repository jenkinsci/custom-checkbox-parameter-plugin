package com.bluersw;

import com.bluersw.analyze.Format;
import com.bluersw.source.Protocol;
import net.sf.json.JSONObject;

public class Constants {
	static final String NAME = "SELECT_NODES";
	static final Protocol PROTOCOL = Protocol.HTTP_HTTPS;
	static final Format FORMAT = Format.JSON;
	static final JSONObject USE_INPUT = JSONObject.fromObject("{\"submitContent\" : {\"CheckboxParameter\": [{\"key\": \"node1\",\"value\": \"node1\"},{\"key\": \"node2\",\"value\": \"node2\"},{\"key\": \"node3\",\"value\": \"node3\"},{\"key\": \"node4\",\"value\": \"node4\"}]}}");
	static final String DESCRIPTION = "Select nodes";
	static final String DEFAULT_VALUE = "default value";
}
