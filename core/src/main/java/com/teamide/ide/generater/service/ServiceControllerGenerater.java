package com.teamide.ide.generater.service;

import com.teamide.app.AppContext;
import com.teamide.app.bean.ServiceBean;
import com.teamide.ide.generater.BaseGenerater;
import com.teamide.ide.processor.param.RepositoryProcessorParam;
import com.teamide.ide.processor.repository.project.AppBean;
import com.teamide.util.StringUtil;

public class ServiceControllerGenerater extends BaseGenerater {

	protected final ServiceBean service;

	public ServiceControllerGenerater(ServiceBean service, RepositoryProcessorParam param, AppBean app,
			AppContext context) {
		super(service, param, app, context);
		this.service = service;
	}

	public void generate() throws Exception {
		if (StringUtil.isEmpty(service.getRequestmapping())) {
			return;
		}
		super.generate();
	}

	public String getPackage() {
		String pack = getControllerPackage();
		pack += ".service";
		return pack;
	}

	public String getClassName() {
		return super.getClassName() + "Controller";
	}

	@Override
	public void buildData() {
		data.put("$method_name", "invoke");

		ServiceGenerater serviceGenerater = new ServiceGenerater(service, param, app, context);
		serviceGenerater.init();
		data.put("$service", serviceGenerater.data);
	}

	@Override
	public String getTemplate() throws Exception {
		return "template/java/controller/service";
	}

}