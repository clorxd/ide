package com.teamide.ide.protect.processor.repository.generater.dao;

import com.teamide.app.AppContext;
import com.teamide.app.bean.DaoBean;
import com.teamide.ide.protect.processor.param.RepositoryProcessorParam;
import com.teamide.ide.protect.processor.repository.generater.CodeGenerater;
import com.teamide.ide.protect.processor.repository.project.AppBean;

public abstract class BaseDaoGenerater extends CodeGenerater {

	protected final DaoBean dao;

	public BaseDaoGenerater(DaoBean dao, RepositoryProcessorParam param, AppBean app, AppContext context) {
		super(dao, param, app, context);
		this.dao = dao;
	}

}