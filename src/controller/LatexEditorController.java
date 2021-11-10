package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import controller.commands.Command;
import controller.commands.CommandFactory;
import model.VersionsManager;

public class LatexEditorController{
	private HashMap<String, Command> commands;
	
	public LatexEditorController(VersionsManager versionsManager) {
		Singleton app = Singleton.getInstance(versionsManager);
		CommandFactory commandFactory = new CommandFactory(versionsManager);
		commands = new HashMap<String, Command>();
		
		List<String> commandsList = Arrays.asList(
				new String[] {
						"addLatex",
						"changeVersionsStrategy",
						"create",
						"disableVersionsManagement",
						"edit",
						"enableVersionsManagement",
						"load",
						"rollbackToPreviousVersion",
						"save"
				}
		);

		for(int i = 0; i < commandsList.size(); i++) {
			String command = commandsList.get(i);
			commands.put(command, commandFactory.createCommand(command));
		}
		
	}
	
	public void enact(String command) {
		commands.get(command).execute();
	}
}
