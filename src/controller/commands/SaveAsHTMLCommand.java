package controller.commands;

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

public class SaveAsHTMLCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	private HashMap<String, String> LatexHTML = new HashMap<String, String>();
	private HashMap<String, String> htmlDocument = new HashMap<String, String>();
	private ArrayList<String> noClosingTags = new ArrayList<String>();
	private ArrayList<String> toDelete = new ArrayList<String>();
	private HashMap<String, String> begins = new HashMap<String, String>();
	private int titleStart = -1;
	private int titleEnd = -1;
	private int ChapterCount;
	private int sectionCount;
	private boolean numberingEnabled = true;
	private String letterSignature;
	private String letterSignerAddress;
	private boolean isLetter = false;
	private String letterDestination;
	
	public SaveAsHTMLCommand() {
		Date date = new Date();
		LatexHTML.put("\\chapter", "<h1>");
		LatexHTML.put("\\section", "<h2>");
		LatexHTML.put("\\section*", "<h2>");
		LatexHTML.put("\\subsection", "<h3>");
		LatexHTML.put("\\subsubsection", "<h4>");
		LatexHTML.put("\\paragraph", "<h5>");
		LatexHTML.put("\\subparagraph", "<h5>");
		LatexHTML.put("\\begin{enumerate}", "<ol>");
		LatexHTML.put("\\begin{itemize}", "<ul>");
		LatexHTML.put("\\begin{document}", "<ul>");
		LatexHTML.put("\\item", "<li>");
		LatexHTML.put("\\begin{description}", "<dl>");
		LatexHTML.put("\\emph{text}", "<em>");
		LatexHTML.put("\\textit{text}", "<i>");
		LatexHTML.put("\\textbf{text}", "<b>");
		LatexHTML.put("\\texttt{text}", "<tt>");
		LatexHTML.put("\\n", "<br>");
		LatexHTML.put("\\title", "<center><h2>");
		LatexHTML.put("\\and", "&emsp;");
		LatexHTML.put("\\date", "<p>");
		LatexHTML.put("\\today", date.toString());
		LatexHTML.put("\\author", "<p>");
		LatexHTML.put("\\chapter", "<h1>");
		LatexHTML.put("\\chapter*", "<h1>");
		LatexHTML.put("\\begin", "<>");
		LatexHTML.put("\\maketitle", "</h2></center>");
		LatexHTML.put("\\end", "<>");// xwris close
		LatexHTML.put("\\encl", "encl: ");// xwris close
		LatexHTML.put("\\opening", "<div>");// xwris close
		LatexHTML.put("\\closing", "</div><p style='float: right'>");
		LatexHTML.put("\\ps", "");
		
		noClosingTags.add("\\maketitle");
		noClosingTags.add("\\title");
		noClosingTags.add("\\encl");
		noClosingTags.add("\\opening");
		noClosingTags.add("\\ps");
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
				if(!noClosingTags.contains(FirstPart)) {
					String extra = "";

					if(FirstPart.equals("\\chapter")) {
						if(numberingEnabled) {
							extra += extra = "Chapter "+ ChapterCount + ": <br><br>";
						}
						ChapterCount++;
					}else if(FirstPart.equals("\\section")) {
						extra = sectionCount + " ";
						sectionCount++;
					}else if(FirstPart.equals("\\closing")) {
						extra = LatexHTML.get(FirstPart) + SecondPart + "<br>" + letterSignature + "</p>";
						return extra;
					}else if(FirstPart.equals("\\begin")) {
						if(SecondPart.equals("document")) {
							result = "<body>";
							return result;
						}else if(SecondPart.equals("abstract")) {
							result = "<center><h3>Abstract</h3></center>";
							return result;
						}else if(SecondPart.equals("letter")) {
							letterDestination = ThirdPart.substring(ThirdPart.indexOf("{")+1, ThirdPart.indexOf("}")) + "<p>";
							result = "<div id='letter'>";
							isLetter = true;
							return result;
						}
					}else if(FirstPart.equals("\\end")) {
						if(SecondPart.equals("document")) {
							result = "</body>";
							return result;
						}else if(SecondPart.equals("abstract")) {
							result = "";
							return result;
						}else if(SecondPart.equals("letter")) {
							result = "</div>";
							return result;
						}
					}
					FirstPart = LatexHTML.get(FirstPart);
					result = FirstPart + extra + SecondPart + closingTag(FirstPart) + ThirdPart;
				}else {
					FirstPart = LatexHTML.get(FirstPart);
					result = FirstPart + SecondPart + ThirdPart;
				}
			}else {
				String fontSize = "";
				if(line.contains("documentclass")) {
					if(line.contains("[")) {
						fontSize = line.substring(line.indexOf("[")+1, line.indexOf(","));
						result = "<font style='font-size:"+fontSize+"'>";
					}
					return "";
				}else if(line.contains("usepackage")) {
					return "";
				}else if(line.contains("signature")) {
					letterSignature = SecondPart; 
					return "";
				}else if(line.contains("\\address{")) {
					letterSignerAddress = SecondPart + "<br>"; 
					return "";
				}else {				
					result = FirstPart + SecondPart + ThirdPart;
				}
			}
		}else {
			if(line.contains("backmatter")) {
				numberingEnabled = false;
				return "";
			} else if(line.contains("mainmatter")) {
				ChapterCount = 1;
				numberingEnabled = true;
				return "";
			}else if(line.contains("frontmatter")) {
				numberingEnabled = false;
				return "";
			}else if(line.contains("%") && line.substring(0, 1).equals("%")) {
				return "<!-- " + line.substring(1) + " -->";
			}
			return line;
		}
		return result;
	}
	
	public void saveAsHTML() {
		LatexEditorView latexEditorView = versionsManager.getEditorView();
		String documentContents = latexEditorView.getCurrentDocument().getContents();
		String replace = documentContents.replace("\\n", "<br>");
		numberingEnabled = true;
		ChapterCount = 1;
		sectionCount = 1;
		letterSignature = "";
		isLetter = false;
		letterSignerAddress = "";
		letterDestination = "";
		
		Scanner scanner = new Scanner(replace);
		String result = "";
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  result += cmd(line) + "\n";
			for (Map.Entry me : LatexHTML.entrySet()) {
				  result = result.replace(me.getKey().toString(), me.getValue().toString());
			  }
		}
		if(isLetter) {
			Date date = new Date();
			result = "<html><p style='float: right'>"+letterSignerAddress+"<br>" + date.toString() + "</p><br><br>" + letterDestination + result;
		  }else {
			  result = "<html>" + result;
		  }
		result += "</html>";
		System.out.println(result);
		scanner.close();
	}

	@Override
	public void execute() {
		saveAsHTML();
	}

}