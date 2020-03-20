package com.teamide.ide;

import java.sql.DriverManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.teamide.ide.handler.DeployServerHandler;
import com.teamide.ide.handler.SpaceHandler;
import com.teamide.ide.service.IInstallService;
import com.teamide.ide.service.impl.InstallService;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		IInstallService installService = new InstallService();
		boolean installed = installService.installed();

		if (installed) {
			DeployServerHandler.loadServers();
			SpaceHandler.loadSpaces();
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		try {
			while (DriverManager.getDrivers().hasMoreElements()) {
				DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
