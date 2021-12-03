package controller.commands;

import controller.Singleton;
import model.VersionsManager;

public class EnableVersionsManagementCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public EnableVersionsManagementCommand() {}

	@Override
	public void execute() {
		versionsManager.enableStrategy();
	}

}
