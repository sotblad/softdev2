package controller.commands;

import model.Document;
import model.VersionsManager;

public class AddLatexCommand implements Command  {
	private VersionsManager versionsManager;
	
	
	public AddLatexCommand(VersionsManager versionsManager) {
		super();
		this.versionsManager = versionsManager;
	}

	@Override
	public void execute() {
		EditCommand editCommandObj = new EditCommand(versionsManager);
		editCommandObj.saveContents();
	}

}
