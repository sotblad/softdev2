package controller.commands;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;

import controller.Singleton;
import model.Document;
import model.VersionsManager;
import view.LatexEditorView;

public class SaveAsHTMLCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	private HashMap<String, String> LatexHTML = new HashMap<String, String>();
	private HashMap<String, String> htmlDocument = new HashMap<String, String>();
	
	public SaveAsHTMLCommand() {
		Date date = new Date();
		LatexHTML.put("\\chapter", "<h1>");
		LatexHTML.put("\\section", "<h2>");
		LatexHTML.put("\\subsection", "<h3>");
		LatexHTML.put("\\subsubsection", "<h4>");
		LatexHTML.put("\\paragraph", "<h5>");
		LatexHTML.put("\\subparagraph", "<h5>");
		LatexHTML.put("\\begin{enumerate}", "<ol>");
		LatexHTML.put("\\begin{itemize}", "<ul>");
		LatexHTML.put("\\item", "<li>");
		LatexHTML.put("\\begin{description}", "<dl>");
		LatexHTML.put("\\emph{text}", "<em>");
		LatexHTML.put("\\textit{text}", "<i>");
		LatexHTML.put("\\textbf{text}", "<b>");
		LatexHTML.put("\\texttt{text}", "<tt>");
		LatexHTML.put("\\n", "<br>");
		LatexHTML.put("\\title", "<title>");
		LatexHTML.put("\\and", "&emsp;");
		LatexHTML.put("\\date", "<p>");
		LatexHTML.put("\\today", date.toString());
		LatexHTML.put("\\author", "<p>");
		LatexHTML.put("\\begin", "<body>");
	}
	
	public String closingTag(String tag) {
		String result = "</" + tag.substring(1);
		return result;
	}
	
	public String cmd(String line) {
		String result = "";
		String FirstPart, SecondPart, ThirdPart;

		if(line.contains("{")) {
			FirstPart = line.substring(0, line.indexOf("{"));
			SecondPart = line.substring(line.indexOf("{")+1, line.indexOf("}"));
			ThirdPart = line.substring(line.indexOf("}")+1);

			if(LatexHTML.containsKey(FirstPart)) {
				FirstPart = LatexHTML.get(FirstPart);
				result = FirstPart + SecondPart + closingTag(FirstPart) + ThirdPart;
			}else {
				result = FirstPart + SecondPart + ThirdPart;
			}
		}else {
			return line;
		}
		return result;
	}
	
	public void saveAsHTML() {
		LatexEditorView latexEditorView = versionsManager.getEditorView();
		String documentContents = latexEditorView.getCurrentDocument().getContents();
		String replace = documentContents.replace("\\n", "<br>");
		
		Scanner scanner = new Scanner(replace);
		String result = "";
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  result += cmd(line) + "\n";
			for (Map.Entry me : LatexHTML.entrySet()) {
				  result = result.replace(me.getKey().toString(), me.getValue().toString());
			  }
		}
		System.out.println(result);
		scanner.close();
	}

	@Override
	public void execute() {
		saveAsHTML();
	}

}
