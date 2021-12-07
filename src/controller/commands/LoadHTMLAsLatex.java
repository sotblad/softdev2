package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;

import controller.Singleton;
import model.Document;
import model.VersionsManager;
import view.LatexEditorView;

public class LoadHTMLAsLatex implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	private HashMap<String, String> LatexHTML = new HashMap<String, String>();
	
	
	public LoadHTMLAsLatex() {
		LatexHTML.put("\\chapter", "<h1>");
		LatexHTML.put("\\section", "<h2>");
		LatexHTML.put("\\subsection", "<h3>");
		LatexHTML.put("\\subsubsection", "<h4>");
		LatexHTML.put("\\paragraph", "<h5>");
		LatexHTML.put("\\subparagraph", "<h6>");
		LatexHTML.put("\\item", "<li>");
		LatexHTML.put("\\emph", "<em>");
		LatexHTML.put("\\textit", "<i>");
		LatexHTML.put("\\textbf", "<b>");
		LatexHTML.put("\\texttt", "<tt>");
		LatexHTML.put("\\n", "<br>");
		LatexHTML.put("\\title", "<head><center><h2>");
		LatexHTML.put("\\and", "&emsp;");
		LatexHTML.put("\\date", "<p id='date'>");
		LatexHTML.put("\\today", "CHECKIFDATE");
		LatexHTML.put("\\author", "<p>");
		LatexHTML.put("\\maketitle", "</h2></center></head>");
		LatexHTML.put("\\encl", "encl: ");// xwris close
		LatexHTML.put("\\opening", "<div id='opening'>");// xwris close
		LatexHTML.put("\\closing", "</div><p style='float: right'>");
		LatexHTML.put("\\caption", "<figcaption>");
		LatexHTML.put("\\hline", "<hr>");
		
		HashMap<String, String> reversedHashMap = new HashMap<String, String>();
		for (String key : LatexHTML.keySet()){
		    reversedHashMap.put(LatexHTML.get(key), key);
		}
	}
	
	public String parser(String line) {
		String result = "";
		
		return result;
	}
	
	public void loadAsLatex() {
		JFileChooser filechooser = new JFileChooser();
		int option = filechooser.showOpenDialog(null);
		String result = "";
		if(option == JFileChooser.APPROVE_OPTION) {
			String filename = filechooser.getSelectedFile().toString();
			
			Scanner scanner;
			try {
				scanner = new Scanner(new FileInputStream(filename));
				while (scanner.hasNextLine()) {
					  String line = scanner.nextLine();
					  
					  result += parser(line) + "\n";
					}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println(result);
		
	}

	@Override
	public void execute() {
		loadAsLatex();
	}

}
