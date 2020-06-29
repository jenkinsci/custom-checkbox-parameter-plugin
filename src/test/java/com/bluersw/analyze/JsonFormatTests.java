package com.bluersw.analyze;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonFormatTests {

	@Test
	public void test() throws Exception{
		InputStream inputStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("test/analyze/examples.json");

		Configuration format = ConfigurationFactory.createConfiguration(Format.JSON,inputStream);

		System.out.println("//arrayOne:");
		List<String> list = format.getValueListBySearch("//arrayOne");
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayTwo/itemName:");
		list = format.getValueListBySearch("//arrayTwo/itemName");
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayThree/item/name:");
		list = format.getValueListBySearch("//arrayThree/item/name");
		assertEquals(list.size(),4);
		System.out.println(list);

		System.out.println("//arrayOne/item:");
		list = format.getValueListBySearch("//arrayOne/item");
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayTwo:");
		list = format.getValueListBySearch("//arrayTwo");
		assertEquals(list.size(),6);
		System.out.println(list);

		System.out.println("//purpose:");
		list = format.getValueListBySearch("//purpose");
		assertEquals(list.size(),1);
		System.out.println(list);

		System.out.println("//SearchExample:");
		list = format.getValueListBySearch("//SearchExample");
		assertEquals(list.size(),8);
		System.out.println(list);

		System.out.println("//arrayThree/item:");
		list = format.getValueListBySearch("//arrayThree/item");
		assertEquals(list.size(),8);
		System.out.println(list);
	}

}