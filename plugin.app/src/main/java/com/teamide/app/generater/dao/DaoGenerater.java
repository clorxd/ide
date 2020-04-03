package com.teamide.app.generater.dao;

import java.io.File;

import com.teamide.app.AppContext;
import com.teamide.app.bean.DaoBean;
import com.teamide.app.enums.DaoProcessType;
import com.teamide.app.plugin.AppBean;
import com.teamide.app.process.DaoProcess;
import com.teamide.app.process.dao.DaoSqlProcess;
import com.teamide.util.StringUtil;

public class DaoGenerater extends SQLDaoGenerater {

	public DaoGenerater(DaoBean dao, File sourceFolder, AppBean app, AppContext context) {
		super(dao, sourceFolder, app, context);
	}

	public String getPackage() {
		return getDaoPackage();
	}

	@Override
	public String getClassName() {
		return "I" + super.getClassName();
	}

	@Override
	public String getMergeClassName() {
		String className = super.getMergeClassName();
		if (StringUtil.isNotEmpty(className)) {
			className = "I" + className;
		}
		return className;
	}

	@Override
	public String getPropertyname() {
		String className = getClassName();
		return className.substring(1, 2).toLowerCase() + className.substring(2);
	}

	@Override
	public String getMergePropertyname() {
		String mergeClassname = getMergeClassName();
		if (StringUtil.isEmpty(mergeClassname)) {
			return "";
		}
		return mergeClassname.substring(1, 2).toLowerCase() + mergeClassname.substring(2);
	}

	@Override
	public void buildData() {
		DaoProcess daoProcess = dao.getProcess();
		if (daoProcess != null) {
			if (DaoProcessType.SQL.getValue().equals(daoProcess.getType())) {
				buildSQLData();
			}
		}
		data.put("$method_name", "invoke");
		data.put("$requestmethod", null);
		if (StringUtil.isNotEmpty(dao.getRequestmethod())) {
			data.put("$requestmethod", dao.getRequestmethod());
		}

	}

	@Override
	public String getTemplate() throws Exception {

		DaoProcess daoProcess = dao.getProcess();

		if (daoProcess != null) {
			if (DaoProcessType.SQL.getValue().equals(daoProcess.getType())) {

				DaoSqlProcess sqlProcess = (DaoSqlProcess) dao.getProcess();
				if (sqlProcess.getSqlType().indexOf("SELECT") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/select";
					}
					return "template/java/dao/sql/select";
				} else if (sqlProcess.getSqlType().indexOf("INSERT") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/insert";
					}
					return "template/java/dao/sql/insert";
				} else if (sqlProcess.getSqlType().indexOf("UPDATE") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/update";
					}
					return "template/java/dao/sql/update";
				} else if (sqlProcess.getSqlType().indexOf("DELETE") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/delete";
					}
					return "template/java/dao/sql/delete";
				} else if (sqlProcess.getSqlType().indexOf("CUSTOM") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/custom";
					}
					return "template/java/dao/sql/custom";
				} else if (sqlProcess.getSqlType().indexOf("SAVE") >= 0) {
					if (isUsemybatis()) {
						return "template/java/dao/mapper/save";
					}
					return "template/java/dao/sql/save";
				} else {
					throw new Exception("sql type [" + sqlProcess.getSqlType() + "] template does not exist.");
				}
			} else if (DaoProcessType.HTTP.getValue().equals(daoProcess.getType())) {
				return "template/java/dao/http";
			} else if (DaoProcessType.CACHE.getValue().equals(daoProcess.getType())) {
				return "template/java/dao/cache";
			}
		}
		return "template/java/dao/default";
	}

}