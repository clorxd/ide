package com.teamide.ide.processor.repository.project;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.teamide.util.StringUtil;

public class ProjectBean {

	private String name;
	private String path;
	private boolean isRoot;
	private boolean isMaven;
	private boolean isNode;
	private String packaging;
	private List<FileBean> files = new ArrayList<FileBean>();

	private final JSONObject attribute = new JSONObject();

	private final JSONObject option = new JSONObject();

	private ProjectSetting setting;

	public boolean isNode() {
		return isNode;
	}

	public void setNode(boolean isNode) {
		this.isNode = isNode;
	}

	public ProjectSetting getSetting() {
		return setting;
	}

	public void setSetting(ProjectSetting setting) {
		this.setting = setting;
	}

	public JSONObject getOption() {
		return option;
	}

	public void setAttribute(String key, Object value) {
		this.attribute.put(key, value);
	}

	public JSONObject getAttribute() {
		return this.attribute;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		if (StringUtil.isEmpty(packaging)) {
			packaging = "jar";
		}
		this.packaging = packaging;
	}

	public boolean isMaven() {
		return isMaven;
	}

	public void setMaven(boolean isMaven) {
		this.isMaven = isMaven;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

}
