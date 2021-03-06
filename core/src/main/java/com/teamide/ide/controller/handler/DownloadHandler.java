package com.teamide.ide.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.teamide.client.ClientHandler;
import com.teamide.client.ClientSession;
import com.teamide.ide.processor.WorkspaceProcessor;
import com.teamide.ide.processor.enums.ProjectProcessorType;
import com.teamide.util.LogUtil;
import com.teamide.util.StringUtil;

public class DownloadHandler {

	Logger logger = LogUtil.get();

	public void handle(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject data = new JSONObject();
		data.put("request", request);
		data.put("response", response);

		String token = null;
		if (path.startsWith("/api/download/")) {
			token = path.substring("/api/download/".length());
		}
		if (StringUtil.isEmpty(token)) {
			throw new Exception("token is null.");
		}

		ClientSession session = ClientHandler.getSession(request);
		WorkspaceProcessor workspaceProcessor = new WorkspaceProcessor(session, token, "");

		workspaceProcessor.onDo(ProjectProcessorType.DOWNLOAD.getValue(), data);
	}

}
