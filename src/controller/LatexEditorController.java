package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import controller.commands.Command;
import controller.commands.CommandFactory;
import model.DocumentManager;
import model.VersionsManager;

public class LatexEditorController{
	private HashMap<String, Command> commands;
	
	public LatexEditorController(VersionsManager versionsManager) {
		DocumentManager documentManager = new DocumentManager();
		Singleton.getInstance(versionsManager, documentManager); // initialize Singleton Instance
		CommandFactory commandFactory = new CommandFactory();
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
						"save",
						"saveAsHTML"
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
