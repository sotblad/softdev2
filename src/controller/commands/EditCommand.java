package controller.commands;

import controller.Singleton;
import model.Document;
import model.VersionsManager;

public class EditCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public EditCommand() {}

	public void saveContents() {
		Document currentDocument = versionsManager.getEditorView().getCurrentDocument();
		String text = versionsManager.getEditorView().getText();
		if(versionsManager.isEnabled()) {
			versionsManager.putVersion(currentDocument);
			currentDocument.changeVersion();
		}
		currentDocument.setContents(text);
	}
	
	@Override
	public void execute() {
		saveContents();
	}

}
