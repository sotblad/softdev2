package controller.commands;

import controller.Singleton;
import model.VersionsManager;

public class DisableVersionsManagementCommand implements Command {

	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public DisableVersionsManagementCommand() {}

	@Override
	public void execute() {
		versionsManager.disable();
	}

}
