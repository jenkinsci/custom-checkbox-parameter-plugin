package com.bluersw.analyze;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YamlFormatTests {

	@Test
	void preservesRepeatedValuesAtTheSamePath() throws Exception {
		Configuration format = ConfigurationFactory.createConfiguration(Format.YAML,
				"projects:\n  - selected: true\n  - selected: false\n  - selected: true\n");

		assertEquals(List.of("true", "false", "true"),
				format.getValueListBySearch("//projects/selected"));
	}

	@Test
	void test() throws Exception{
		InputStream inputStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("test/analyze/examples.yaml");

		Configuration format = ConfigurationFactory.createConfiguration(Format.YAML,inputStream);

		System.out.println("//arrayOne:");
		List<String> list = format.getValueListBySearch("//arrayOne");
		assertEquals(3, list.size());
		System.out.println(list);

		System.out.println("//arrayTwo/itemName:");
		list = format.getValueListBySearch("//arrayTwo/itemName");
		assertEquals(3, list.size());
		System.out.println(list);

		System.out.println("//arrayThree/item/name:");
		list = format.getValueListBySearch("//arrayThree/item/name");
		assertEquals(4, list.size());
		System.out.println(list);

		System.out.println("//arrayOne/item:");
		list = format.getValueListBySearch("//arrayOne/item");
		assertEquals(3, list.size());
		System.out.println(list);

		System.out.println("//arrayTwo:");
		list = format.getValueListBySearch("//arrayTwo");
		assertEquals(6, list.size());
		System.out.println(list);

		System.out.println("//purpose:");
		list = format.getValueListBySearch("//purpose");
		assertEquals(1, list.size());
		System.out.println(list);

		System.out.println("//SearchExample:");
		list = format.getValueListBySearch("//SearchExample");
		assertEquals(8, list.size());
		System.out.println(list);

		System.out.println("//arrayThree/item:");
		list = format.getValueListBySearch("//arrayThree/item");
		assertEquals(8, list.size());
		System.out.println(list);
	}
}
