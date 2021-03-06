package com.teamide.ide.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.teamide.util.StringUtil;
import com.teamide.ide.bean.SpaceEventBean;
import com.teamide.ide.bean.SpaceRepositoryOpenBean;
import com.teamide.ide.handler.SpacePermissionHandler;
import com.teamide.ide.param.ProjectProcessorParam;
import com.teamide.ide.processor.enums.ProjectModelType;
import com.teamide.ide.processor.enums.ProjectProcessorType;
import com.teamide.ide.processor.repository.RepositoryFile;
import com.teamide.ide.processor.repository.RepositoryMaven;
import com.teamide.ide.processor.repository.RepositoryNode;
import com.teamide.ide.processor.repository.RepositoryProject;
import com.teamide.ide.processor.repository.project.ProjectLoader;
import com.teamide.ide.service.impl.SpaceRepositoryOpenService;

public class ProjectProcessor extends RepositoryProcessor {
	protected final ProjectProcessorParam param;

	public ProjectProcessor(ProjectProcessorParam param) {
		super(param);
		this.param = param;
	}

	public Object onDo(String type, JSONObject data) throws Exception {
		ProjectProcessorType processorType = ProjectProcessorType.get(type);
		if (processorType == null) {
			return super.onDo(type, data);
		}
		return onDo(processorType, data);
	}

	protected Object onDo(ProjectProcessorType processorType, JSONObject data) throws Exception {
		if (processorType == null) {
			return null;
		}
		SpacePermissionHandler.checkPermission(processorType, param.getPermission());
		SpaceEventBean spaceEventBean = new SpaceEventBean();
		spaceEventBean.setType(processorType.getValue());
		spaceEventBean.setName(processorType.getText());
		spaceEventBean.setSpaceid(param.getSpaceid());
		Object value = null;
		switch (processorType) {

		case FILE_CREATE:
			String parentPath = data.getString("parentPath");
			String name = data.getString("name");
			String content = data.getString("content");
			boolean isFile = data.getBooleanValue("isFile");
			new RepositoryFile(param).create(parentPath, name, isFile, content);

			spaceEventBean.set(data);
			appendEvent(spaceEventBean);

			value = 0;

			break;
		case FILE_MOVE:
			String path = data.getString("path");
			String to = data.getString("to");
			new RepositoryFile(param).move(path, to);

			spaceEventBean.set("path", path);
			spaceEventBean.set("to", to);
			appendEvent(spaceEventBean);

			value = 0;

			break;
		case FILE_DELETE:
			path = data.getString("path");
			new RepositoryFile(param).delete(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);

			value = 0;

			break;
		case FILE_PASTE:
			path = data.getString("path");
			String source = data.getString("source");
			new RepositoryFile(param).paste(path, source);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);

			break;
		case FILE_SAVE:
			path = data.getString("path");
			content = data.getString("content");
			new RepositoryFile(param).save(path, content);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);

			break;
		case FILE_RENAME:
			path = data.getString("path");
			name = data.getString("name");
			new RepositoryFile(param).rename(path, name);

			spaceEventBean.set("path", path);
			spaceEventBean.set("name", name);
			appendEvent(spaceEventBean);

			value = 0;

			break;
		case DOWNLOAD:
			new RepositoryFile(param).download(data);
			break;
		case UPLOAD:
			new RepositoryFile(param).upload(data);
			break;
		case FILE_OPEN:
			path = data.getString("path");
			if (!StringUtil.isEmpty(path) && param.getSession().getUser() != null) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("spaceid", param.getSpaceid());
				p.put("userid", param.getSession().getUser().getId());
				p.put("path", path);
				p.put("branch", param.getBranch());
				SpaceRepositoryOpenService spaceRepositoryOpenService = new SpaceRepositoryOpenService();
				List<SpaceRepositoryOpenBean> list = spaceRepositoryOpenService.queryList(p);
				if (list.size() == 0) {
					SpaceRepositoryOpenBean spaceRepositoryOpenBean = new SpaceRepositoryOpenBean();
					spaceRepositoryOpenBean.setSpaceid(param.getSpaceid());
					spaceRepositoryOpenBean.setPath(path);
					spaceRepositoryOpenBean.setUserid(param.getSession().getUser().getId());
					spaceRepositoryOpenBean.setOpentime(new Date().getTime());
					spaceRepositoryOpenBean.setBranch(param.getBranch());
					spaceRepositoryOpenService.insert(param.getSession(), spaceRepositoryOpenBean);
				} else {
					for (SpaceRepositoryOpenBean one : list) {
						SpaceRepositoryOpenBean up = new SpaceRepositoryOpenBean();
						up.setId(one.getId());
						up.setOpentime(new Date().getTime());
						spaceRepositoryOpenService.update(param.getSession(), up);
					}
				}
			}

			break;

		case FILE_CLOSE:

			path = data.getString("path");
			if (!StringUtil.isEmpty(path) && param.getSession().getUser() != null) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("spaceid", param.getSpaceid());
				p.put("userid", param.getSession().getUser().getId());
				p.put("path", path);

				SpaceRepositoryOpenService spaceRepositoryOpenService = new SpaceRepositoryOpenService();

				value = spaceRepositoryOpenService.delete(p);
			}
			break;
		case MAVEN_CLEAN:
			path = data.getString("path");
			new RepositoryMaven(param).clean(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;
		case MAVEN_DEPLOY:
			path = data.getString("path");
			new RepositoryMaven(param).deploy(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;
		case MAVEN_INSTALL:
			path = data.getString("path");
			new RepositoryMaven(param).install(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;
		case MAVEN_PACKAGE:
			path = data.getString("path");
			new RepositoryMaven(param).doPackage(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;
		case MAVEN_COMPILE:
			path = data.getString("path");
			new RepositoryMaven(param).doCompile(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;
		case NODE_INSTALL:
			path = data.getString("path");
			new RepositoryNode(param).install(path);

			spaceEventBean.set("path", path);
			appendEvent(spaceEventBean);
			break;

		case SET_PLUGIN_OPTION:
			name = data.getString("name");
			String projectPath = data.getString("projectPath");
			JSONObject option = data.getJSONObject("option");
			new RepositoryProject(this.param).savePlugin(projectPath, name, option);

			spaceEventBean.set("option", option);
			appendEvent(spaceEventBean);

			break;

		case DELETE_PLUGIN_OPTION:
			projectPath = data.getString("projectPath");
			name = data.getString("name");
			new RepositoryProject(this.param).savePlugin(projectPath, name, null);

			appendEvent(spaceEventBean);

			break;
		}

		return value;

	}

	public Object onLoad(String type, JSONObject data) throws Exception {
		ProjectModelType modelType = ProjectModelType.get(type);
		if (modelType == null) {
			return super.onLoad(type, data);
		}
		return onLoad(modelType, data);
	}

	public Object onLoad(ProjectModelType modelType, JSONObject data) throws Exception {
		if (modelType == null) {
			return null;
		}

		Object value = null;
		switch (modelType) {
		case PROJECT:
			ProjectLoader loader = new ProjectLoader(param);
			value = loader.loadProject(param.getProjectPath());

			break;
		case FILE:
			String path = data.getString("path");
			value = new ProjectLoader(param).readFile(path);

			break;
		case FILES:
			path = data.getString("path");
			value = new ProjectLoader(param).loadFiles(param.getFile(path), null);

			break;

		case PLUGIN_OPTION:
			String projectPath = data.getString("projectPath");
			String name = data.getString("name");
			value = new RepositoryProject(this.param).readPlugin(projectPath, name);

			break;
		}
		return value;
	}

}
