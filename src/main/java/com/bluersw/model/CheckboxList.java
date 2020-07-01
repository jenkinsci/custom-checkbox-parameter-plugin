package com.bluersw.model;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.ExportConfig;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Flavor;

/**
 * @author sunweisheng
 */
@ExportedBean
public class CheckboxList implements HttpResponse {

	@Exported
	public ArrayList<CheckboxModel> list = new ArrayList<>();
	@Exported
	public String message = "";

	public void add(String name, String value){this.list.add(new CheckboxModel(name,value));}

	public void setMessage(String message){this.message = message;}

	@Override
	public void generateResponse(StaplerRequest staplerRequest, StaplerResponse staplerResponse, Object o) throws IOException, ServletException {
		ExportConfig ec = new ExportConfig();
		ec.withFlavor(Flavor.JSON);
		staplerResponse.serveExposedBean(staplerRequest,this, ec);
	}

	@ExportedBean(defaultVisibility=999)
	public static final class CheckboxModel {
		@Exported
		public String name;
		@Exported
		public String value;

		public CheckboxModel(String name, String value){
			this.name = name;
			this.value = value;
		}
	}
}
