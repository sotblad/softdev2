package controller.commands;

import controller.Singleton;
import model.VersionsManager;

public class ChangeVersionsStrategyCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public ChangeVersionsStrategyCommand() {}

	@Override
	public void execute() {
		versionsManager.changeStrategy();
	}

}
