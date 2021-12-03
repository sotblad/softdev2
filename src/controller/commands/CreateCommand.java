package controller.commands;

import controller.Singleton;
import model.Document;
import model.DocumentManager;
import model.VersionsManager;

public class CreateCommand implements Command {
	private DocumentManager documentManager = Singleton.documentManager;
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public CreateCommand() {}

	@Override
	public void execute() {
		String type = versionsManager.getEditorView().getType();
		Document document = documentManager.createDocument(type);
		versionsManager.getEditorView().setCurrentDocument(document);
	}

}
