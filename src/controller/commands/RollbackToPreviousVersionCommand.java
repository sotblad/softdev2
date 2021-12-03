package controller.commands;

import controller.Singleton;
import model.VersionsManager;

public class RollbackToPreviousVersionCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public RollbackToPreviousVersionCommand() {}

	@Override
	public void execute() {
		versionsManager.rollback();
	}

}
