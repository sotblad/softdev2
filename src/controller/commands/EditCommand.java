package controller.commands;

import model.Document;
import model.VersionsManager;

public class EditCommand implements Command {
	private VersionsManager versionsManager;
	
	
	public EditCommand(VersionsManager versionsManager) {
		super();
		this.versionsManager = versionsManager;
	}

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
