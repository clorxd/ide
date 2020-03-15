package com.teamide.ide.processor;

import com.alibaba.fastjson.JSONObject;
import com.teamide.util.StringUtil;
import com.teamide.client.ClientSession;
import com.teamide.ide.bean.UserBean;
import com.teamide.ide.bean.UserPreferenceBean;
import com.teamide.ide.configure.IDEOptions;
import com.teamide.ide.handler.SpaceHandler;
import com.teamide.ide.handler.UserHandler;
import com.teamide.ide.processor.enums.ModelType;
import com.teamide.ide.processor.param.ProcessorParam;
import com.teamide.ide.service.IConfigureService;
import com.teamide.ide.service.IInstallService;
import com.teamide.ide.service.ISpaceService;
import com.teamide.ide.service.IUserService;
import com.teamide.ide.service.impl.ConfigureService;
import com.teamide.ide.service.impl.EnvironmentService;
import com.teamide.ide.service.impl.InstallService;
import com.teamide.ide.service.impl.SpaceService;
import com.teamide.ide.service.impl.SpaceTeamService;
import com.teamide.ide.service.impl.UserPreferenceService;
import com.teamide.ide.service.impl.UserService;

public class ProcessorLoad extends ProcessorBase {

	public ProcessorLoad(ProcessorParam param) {
		super(param);
	}

	public Object onLoad(String type, JSONObject data) throws Exception {
		ModelType modelType = ModelType.get(type);
		return onLoad(modelType, data);
	}

	public Object onLoad(ModelType modelType, JSONObject data) throws Exception {
		if (modelType == null) {
			return null;
		}
		Object value = null;
		int pageindex = data.getIntValue("pageindex");
		int pagesize = data.getIntValue("pagesize");
		ClientSession session = this.param.getSession();
		JSONObject param = new JSONObject();
		switch (modelType) {
		case ONE:
			String type = data.getString("type");
			if (!StringUtil.isEmpty(type)) {
				String id = data.getString("id");
				type = type.toUpperCase();
				switch (type) {
				case "USER":
					IUserService userService = new UserService();
					value = userService.get(id);
					break;
				case "SPACE":
					value = SpaceHandler.getFormat(id);
					break;
				case "SPACE_TEAM":
					value = new SpaceTeamService().get(id);
					break;
				}
			}
			break;
		case SESSION:
			JSONObject out = new JSONObject();
			out.put("LOGIN_USER", null);
			out.put("isManager", false);
			out.put("roles", session.getCache("roles"));
			if (session.getUser() != null) {
				JSONObject USER = UserHandler.getFormat((UserBean) session.getCache("user"));
				out.put("LOGIN_USER", USER);
				out.put("isManager", session.getCache("isManager"));
				out.put("roles", session.getCache("roles"));
				out.put("LOGIN_USER_TOKEN", session.getCache("LOGIN_USER_TOKEN"));

				JSONObject preference = new JSONObject();
				UserPreferenceService service = new UserPreferenceService();
				UserPreferenceBean one = service.get(session.getUser().getId());
				if (one != null && !StringUtil.isEmpty(one.getOption())) {
					preference = JSONObject.parseObject(one.getOption());
				}
				out.put("preference", preference);
			}
			value = out;
			break;
		case INSTALLED:
			IInstallService installService = new InstallService();
			value = installService.installed();
			break;
		case JDBC:
			value = IDEOptions.get().jdbc;
			break;
		case CONFIGURE:
			IConfigureService configureService = new ConfigureService();
			value = configureService.get("0");
			break;
		case ENVIRONMENTS:
			EnvironmentService environmentService = new EnvironmentService();
			value = environmentService.queryList(param);
			break;
		case MASTER_SPACES:
			ISpaceService spaceService = new SpaceService();
			value = spaceService.queryMasters(session, pageindex, pagesize);
			break;
		case SEARCH_SPACES:
			spaceService = new SpaceService();
			String searchText = data.getString("searchText");
			value = spaceService.querySearch(session, searchText, pageindex, pagesize);
			break;
		case SEARCH_USERS:
			IUserService userService = new UserService();
			searchText = data.getString("searchText");
			value = userService.querySearch(searchText, pageindex, pagesize);
			break;
		case USERS:
			userService = new UserService();
			value = userService.queryPage(param, pageindex, pagesize);

			break;
		}
		return value;
	}

}