package com.bluersw.model;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.ExportConfig;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Flavor;

/**
 * 复选框列表JSON对象 Checkbox list JSON object
 * @author sunweisheng
 */
@SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
@ExportedBean
public class CheckboxList implements HttpResponse {

	@Exported
	public ArrayList<CheckboxModel> list = new ArrayList<>();
	@Exported
	public String message = "";

	/**
	 * 添加一个复选框 Add a checkbox
	 * @param name 复选框显示的文字 Checkbox text
	 * @param value 选择复选框的值 Select the value of the check box
	 * @param selected 复选框是否被勾选 Whether the check box is checked
	 */
	public void add(String name, String value,boolean selected){
		String checked = selected? "checked":"";
		this.list.add(new CheckboxModel(name,value,checked));
	}

	/**
	 * 客户端显示的信息 Information displayed by the client
	 * @param message 显示的信息 Information displayed
	 */
	public void setMessage(String message){this.message = message;}

	@Override
	public void generateResponse(StaplerRequest staplerRequest, StaplerResponse staplerResponse, Object o) throws IOException, ServletException {
		ExportConfig ec = new ExportConfig();
		ec.withFlavor(Flavor.JSON);
		staplerResponse.serveExposedBean(staplerRequest,this, ec);
	}

	/**
	 * 复选框数据模型定义 Checkbox data model definition
	 */
	@ExportedBean(defaultVisibility=999)
	public static final class CheckboxModel {
		@Exported
		public String name;
		@Exported
		public String value;
		@Exported
		public String checked;

		public CheckboxModel(String name, String value, String checked){
			this.name = name;
			this.value = value;
			this.checked = checked;
		}
	}
}
