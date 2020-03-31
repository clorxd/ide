package com.teamide.ide.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.teamide.util.StringUtil;
import com.teamide.client.ClientSession;
import com.teamide.ide.bean.SpaceRepositoryOptionBean;
import com.teamide.ide.enums.OptionType;

@Resource
public class SpaceRepositoryOptionService extends BaseService<SpaceRepositoryOptionBean> {

	public List<SpaceRepositoryOptionBean> query(ClientSession session, String spaceid, String branch, String path,
			String name, OptionType type) throws Exception {
		return query(session, spaceid, branch, path, name, type.name());
	}

	public List<SpaceRepositoryOptionBean> query(ClientSession session, String spaceid, String branch, String path,
			String name, String type) throws Exception {

		SpaceRepositoryOptionService service = new SpaceRepositoryOptionService();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("spaceid", spaceid);
		if (type != null) {
			param.put("type", type);
		}
		String userid = null;
		if (session != null && session.getUser() != null) {
			userid = session.getUser().getId();
		}
		if (!StringUtil.isEmpty(path)) {
			param.put("path", path);
		}
		if (!StringUtil.isEmpty(name)) {
			param.put("name", name);
		}
		if (!StringUtil.isEmpty(branch)) {
			param.put("branch", branch);
		}
		if (type != null && type.equals(OptionType.GIT_CERTIFICATE.name())) {
			if (!StringUtil.isEmpty(userid)) {
				param.put("userid", userid);
			} else {
				param.put("userid", "0");
			}
		}
		List<SpaceRepositoryOptionBean> options = service.queryList(param);
		return options;
	}

}
