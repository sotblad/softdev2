package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import controller.Singleton;
import model.Document;
import model.VersionsManager;

public class LoadHTMLAsLatex implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	private HashMap<String, String> HTMLLatex = new HashMap<String, String>();
	
	
	public LoadHTMLAsLatex() {
		HTMLLatex.put("<head><center><h2>", "\\title{");
		HTMLLatex.put("</h2></center></head>", "\\maketitle");
		HTMLLatex.put("<html>", "");
		HTMLLatex.put("</html>", "");
		HTMLLatex.put("<h1>", "\\chapter{");
		HTMLLatex.put("<h2>", "\\section{");
		HTMLLatex.put("<h3>", "\\subsection{");
		HTMLLatex.put("<h4>", "\\subsubsection{");
		HTMLLatex.put("<h5>", "\\paragraph{");
		HTMLLatex.put("<h6>", "\\subparagraph{");
		HTMLLatex.put("<li>", "\\item{");
		HTMLLatex.put("<em>", "\\emph{");
		HTMLLatex.put("<i>", "\\textit{");
		HTMLLatex.put("<b>", "\\textbf{");
		HTMLLatex.put("<tt>", "\\texttt{");
		HTMLLatex.put("<br>", "\\n");
		HTMLLatex.put("</div>", "\\end{");
		HTMLLatex.put("&emsp;", "\\and");
		HTMLLatex.put("<p class='date'>", "\\date{");
		HTMLLatex.put("CHECKIFDATE", "\\today");
		HTMLLatex.put("<p>", "\\author{");
		HTMLLatex.put("</h2></center></head>", "");
		HTMLLatex.put("encl: ", "\\encl");// xwris close
		HTMLLatex.put("<div id='opening'>", "\\opening");// xwris close
		HTMLLatex.put("</div><p style='float: right'>", "\\closing");
		HTMLLatex.put("<figcaption>", "\\caption{");
		HTMLLatex.put("<hr>", "\\hline");
		HTMLLatex.put("<div id='", "\\begin{");
	}
	
	public String parser(String line) {
		String result = "";
		String tag = "";
		String mainContent = "";
		String id = "";
		
		Pattern regex = Pattern.compile("<[^/]*>");
		Matcher regexMatcher = regex.matcher(line);
		boolean hasTag = regexMatcher.find();
		
		regex = Pattern.compile("</.*>");
		regexMatcher = regex.matcher(line);
		boolean hasClosingTag = regexMatcher.find();
		
		if(hasTag) {
			tag = line.substring(line.indexOf("<")+1, line.indexOf(">")+1);
			if(!hasClosingTag) {
				if(tag.contains(" ")) {
					tag = tag.substring(0, line.indexOf(" "));
				}
			}else if(hasClosingTag) {
				
				mainContent = line.substring(line.indexOf(">")+1, line.indexOf("</"));
				
				try {
			        LocalDate dateTime = LocalDate.parse(mainContent.replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE);
			        line = line.replace(mainContent, "\\today");
			    } catch (Exception e) {
			    }
			}
			tag = line.substring(line.indexOf("<")+1, line.indexOf(">")+1);
			if(tag.contains("id=")) {
				id = tag.substring(tag.indexOf("id='")+4);
				line = line.replace(id, id.substring(0,id.length()-2)) + "}";
			}
		}
		if(hasClosingTag) {
			if(line.equals("</h2></center></head>")){
				return "\\maketitle";
			}else {
				if(!line.contains("</div>")) {
					line = line.replaceAll("</.*>", "}");
					if(line.equals("}")) {
						return "";
					}
				}
			}
		}
	//	System.out.println("TAG: " + tag);
	//	System.out.println(result);
		return line;
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
					  
					  for (Map.Entry me : HTMLLatex.entrySet()) {
							if(line.contains(me.getKey().toString())) {
								
							  result = result.replace(me.getKey().toString(), me.getValue().toString());
						//	  System.out.println("AAAAAAAAAAAAAAAAAAA" + line + " " + result +" FAK " + me.getValue().toString());
							}
						  }
					 
					}
				System.out.println(result);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void execute() {
		loadAsLatex();
	}

}
