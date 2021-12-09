package controller.commands;

import controller.HtmlToLatexParser;
import controller.Singleton;
import model.VersionsManager;

public class LoadHTMLAsLatex implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	
	public LoadHTMLAsLatex() {}
	
	public void loadHTMLAsLatex() {
		HtmlToLatexParser parse = new HtmlToLatexParser();		
		String resultLatex = parse.parseHtmlToLatex();
		versionsManager.getEditorView().setText(resultLatex);
		versionsManager.getEditorView().getController().enact("edit");
		versionsManager.getEditorView().getEditorPane().setText(resultLatex);
	}

	@Override
	public void execute() {
		loadHTMLAsLatex();
	}

}
