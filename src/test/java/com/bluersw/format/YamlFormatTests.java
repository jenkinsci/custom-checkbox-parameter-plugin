package com.bluersw.format;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class YamlFormatTests {

	@Test
	public void test() throws Exception{
		InputStream inputStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("test/format/test.yaml");

		System.out.println("//arrayOne:");
		YamlFormat format = new YamlFormat("//arrayOne",inputStream);
		List<String> list = format.getValueListBySearch();
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayTwo/itemName:");
		format.setSearchCommand("//arrayTwo/itemName");
		list = format.getValueListBySearch();
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayThree/item/name:");
		format.setSearchCommand("//arrayThree/item/name");
		list = format.getValueListBySearch();
		assertEquals(list.size(),4);
		System.out.println(list);

		System.out.println("//arrayOne/item:");
		format.setSearchCommand("//arrayOne/item");
		list = format.getValueListBySearch();
		assertEquals(list.size(),3);
		System.out.println(list);

		System.out.println("//arrayTwo:");
		format.setSearchCommand("//arrayTwo");
		list = format.getValueListBySearch();
		assertEquals(list.size(),6);
		System.out.println(list);

		System.out.println("//purpose:");
		format.setSearchCommand("//purpose");
		list = format.getValueListBySearch();
		assertEquals(list.size(),1);
		System.out.println(list);

		System.out.println("//SearchExample:");
		format.setSearchCommand("//SearchExample");
		list = format.getValueListBySearch();
		assertEquals(list.size(),8);
		System.out.println(list);

		System.out.println("//arrayThree/item:");
		format.setSearchCommand("//arrayThree/item");
		list = format.getValueListBySearch();
		assertEquals(list.size(),8);
		System.out.println(list);
	}
}