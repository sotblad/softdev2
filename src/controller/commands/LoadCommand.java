package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.Document;
import model.VersionsManager;

public class LoadCommand implements Command {
	private VersionsManager versionsManager;
	
	public LoadCommand(VersionsManager versionsManager) {
		super();
		this.versionsManager = versionsManager;
	}

	public VersionsManager getVersionsManager() {
		return versionsManager;
	}

	public void setVersionsManager(VersionsManager versionsManager) {
		this.versionsManager = versionsManager;
	}
	
	public void loadFromFile() {
		String filename = versionsManager.getEditorView().getFilename();
		String type;
		Document currentDocument;
		
		String fileContents = "";
		try {
			Scanner scanner = new Scanner(new FileInputStream(filename));
			while(scanner.hasNextLine()) {
				fileContents = fileContents + scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentDocument = new Document();
		currentDocument.setContents(fileContents);
		
		type = "emptyTemplate";
		
		fileContents = fileContents.trim();
		if(fileContents.startsWith("\\documentclass[11pt,twocolumn,a4paper]{article}")) {
			type = "articleTemplate";
		}
		else if(fileContents.startsWith("\\documentclass[11pt,a4paper]{book}")) {
			type = "bookTemplate";
		}
		else if(fileContents.startsWith("\\documentclass[11pt,a4paper]{report}")) {
			type = "reportTemplate";
		}
		else if(fileContents.startsWith("\\documentclass{letter}")) {
			type = "letterTemplate";
		}
	}

	@Override
	public void execute() {
		loadFromFile();
	}

}

