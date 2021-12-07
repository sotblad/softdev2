package controller.commands;

import java.util.HashMap;

import javax.swing.JEditorPane;

import controller.LatexEditorController;
import controller.Singleton;
import model.VersionsManager;

public class AddLatexCommand implements Command  {
	private VersionsManager versionsManager = Singleton.versionsManager;
	private HashMap<String, String> commandsContentsList = new HashMap<String, String>();
	
	public AddLatexCommand() {
		commandsContentsList.put("chapter", "\\n\\\\chapter{...}");
		commandsContentsList.put("section", "\\n\\\\section{...}");
		commandsContentsList.put("subsection", "\\n\\\\subsection{...}");
		commandsContentsList.put("subsubsection", "\\n\\\\subsubsection{...}");
		commandsContentsList.put("enumerate", "\\begin{enumerate}\n"+
						"\\item ...\n"+
						"\\item ...\n"+
						"\\end{enumerate}\n");
		commandsContentsList.put("itemize", "\\begin{itemize}\n"+
						"\\item ...\n"+
						"\\item ...\n"+
						"\\end{itemize}\n");
		commandsContentsList.put("table", "\\begin{table}\n"+
					"\\caption{....}\\label{...}\n"+
					"\\begin{tabular}{|c|c|c|}\n"+
					"\\hline\n"+
					"... &...&...\\\\\n"+
					"... &...&...\\\\\n"+
					"... &...&...\\\\\n"+
					"\\hline\n"+
					"\\end{tabular}\n"+
					"\\end{table}\n");
		commandsContentsList.put("figure","\\begin{figure}\n"+
					"\\includegraphics[width=...,height=...]{...}\n"+
					"\\caption{....}\\label{...}\n"+
					"\\end{figure}\n");
	}
	
	public void editContents(String type) {
		LatexEditorController latexEditorController = versionsManager.getEditorView();
		JEditorPane editorPane = latexEditorController.getEditorPane();
		String contents = editorPane.getText();
		String before = contents.substring(0, editorPane.getCaretPosition());
		String after = contents.substring(editorPane.getCaretPosition());
		
		if(commandsContentsList.containsKey(type)) {
			contents = before + commandsContentsList.get(type) +after;
		}
		latexEditorController.setText(contents);
		latexEditorController.getController().enact("addLatex");
		editorPane.setText(contents);
	}

	@Override
	public void execute() {
		EditCommand editCommandObj = new EditCommand();
		editCommandObj.saveContents();
	}

}
