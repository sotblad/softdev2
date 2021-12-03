package controller.commands;

import controller.Singleton;
import model.Document;
import model.VersionsManager;

public class SaveCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public SaveCommand() {}
	
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