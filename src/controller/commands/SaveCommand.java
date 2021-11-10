package controller.commands;

import model.Document;
import model.VersionsManager;

public class SaveCommand implements Command {
	private VersionsManager versionsManager;
	
	public SaveCommand(VersionsManager versionsManager) {
		// TODO Auto-generated constructor stub
		this.versionsManager = versionsManager;
	}
	
	public void saveToFile() {
		Document currentDocument = versionsManager.getEditorView().getCurrentDocument();
		String filename = versionsManager.getEditorView().getFilename();
		currentDocument.save(filename);
	}
	
	@Override
	public void execute() {
		saveToFile();
	}

}