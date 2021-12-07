package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JEditorPane;

import controller.commands.Command;
import controller.commands.CommandFactory;
import model.Document;
import model.DocumentManager;
import model.VersionsManager;

public class LatexEditorController{
	private HashMap<String, Command> commands;
	private LatexEditorController controller;
	private Document currentDocument;
	private String type;
	private String text;
	private String filename;
	private String strategy;
	private VersionsManager versionsManager;
	private JEditorPane editorPane;
	
	public LatexEditorController() {}
	
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
						"saveAsHTML",
						"loadAsLatex"
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
	
	public VersionsManager getVersionsManager() {
		return versionsManager;
	}
	
	public JEditorPane getEditorPane() {
		return editorPane;
	}
	
	public void setEditorPane(JEditorPane editorPane) {
		this.editorPane = editorPane;
	}
	
	public void setVersionsManager(VersionsManager versionsManager) {
		this.versionsManager = versionsManager;
	}
	
	public String getStrategy() {
		return strategy;
	}
	
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public LatexEditorController getController() {
		return controller;
	}
	
	public void setController(LatexEditorController controller) {
		this.controller = controller;
	}
	
	public Document getCurrentDocument() {
		return currentDocument;
	}
	
	public void setCurrentDocument(Document currentDocument) {
		this.currentDocument = currentDocument;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
