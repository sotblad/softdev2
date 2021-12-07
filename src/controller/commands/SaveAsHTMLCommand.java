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
	private ArrayList<String> tableContents = new ArrayList<String>();
	private HashMap<String, String> begins = new HashMap<String, String>();
	private int titleStart = -1;
	private int titleEnd = -1;
	private int ChapterCount;
	private int sectionCount;
	private int subsectionCount;
	private boolean numberingEnabled = true;
	private String letterSignature;
	private String letterSignerAddress;
	private boolean isLetter = false;
	private boolean readTable = false;
	private String letterDestination;
	private int subsubsectionCount = 1;
	private boolean tableWithBorder = true;
	
	public SaveAsHTMLCommand() {
		Date date = new Date();
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
		LatexHTML.put("\\title", "<head><center><h2>");
		LatexHTML.put("\\and", "&emsp;");
		LatexHTML.put("\\date", "<p id='date'>");
		LatexHTML.put("\\today", date.toString());
		LatexHTML.put("\\author", "<p>");
		LatexHTML.put("\\begin", "<>");
		LatexHTML.put("\\maketitle", "</h2></center></head>");
		LatexHTML.put("\\end", "<>");// xwris close
		LatexHTML.put("\\encl", "encl: ");// xwris close
		LatexHTML.put("\\opening", "<div id='opening'>");// xwris close
		LatexHTML.put("\\closing", "</div><p style='float: right'>");
		LatexHTML.put("\\ps", "");
		LatexHTML.put("\\caption", "<figcaption>");
		LatexHTML.put("\\hline", "<hr>");
		
		noClosingTags.add("\\maketitle");
		noClosingTags.add("\\title");
		noClosingTags.add("\\encl");
		noClosingTags.add("\\opening");
		noClosingTags.add("\\ps");
		noClosingTags.add("\\hline");
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
			
			if(LatexHTML.containsKey(FirstPart.replace("*", ""))) {
				if(!noClosingTags.contains(FirstPart)) {
					String extra = "";

					if(FirstPart.contains("\\chapter")) {
						if(!FirstPart.contains("*")) {
							if(numberingEnabled) {
								extra += extra = "Chapter "+ ChapterCount + ": \n<br>\n<br>\n";
							}
							ChapterCount++;
						}
					}else if(FirstPart.contains("\\section")) {
						if(!FirstPart.contains("*")) {
							extra = sectionCount + " ";
							sectionCount++;
							subsectionCount = 1;
						}
					}else if(FirstPart.equals("\\closing")) {
						extra = LatexHTML.get(FirstPart) + SecondPart + "<br>" + letterSignature + "</p>";
						return extra;
					}else if(FirstPart.equals("\\subsection")) {
						subsubsectionCount = 1;
						extra = sectionCount-1 + "." + subsectionCount + " ";
						subsectionCount++;
					}else if(FirstPart.equals("\\subsubsection")) {
						extra = sectionCount-1 + "." + (subsectionCount-1) + "." + subsubsectionCount + " ";
						subsubsectionCount++;
					}else if(FirstPart.equals("\\begin")) {
						if(SecondPart.equals("document")) {
							result = "<body>";
							return result;
						}else if(SecondPart.equals("abstract")) {
							result = "<div id='abstract'><center><h3>Abstract</h3></center>";
							return result;
						}else if(SecondPart.equals("tabular")) {
							if(ThirdPart.contains("|")) {
								result = "<table style='border:1px solid black;'>";
								tableWithBorder = true;
							}else {
								result = "<table>";
							}
							readTable = true;
							return result;
						}else if(SecondPart.equals("center")) {
							result = "<center>";
							return result;
						}else if(SecondPart.equals("figure")) {
							result = "<div id='figure'>";
							return result;
						}else if(SecondPart.equals("itemize")) {
							result = "<ul>";
							return result;
						}else if(SecondPart.equals("enumerate")) {
							result = "<ol>";
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
							result = "</div>";
							return result;
						}else if(SecondPart.equals("tabular")) {
							result = "</table>";
							tableWithBorder = false;
							readTable = false;
							return result;
						}else if(SecondPart.equals("figure")) {
							result = "</div>";
							return result;
						}else if(SecondPart.equals("center")) {
							result = "</center>";
							return result;
						}else if(SecondPart.equals("itemize")) {
							result = "</ul>";
							return result;
						}else if(SecondPart.equals("enumerate")) {
							result = "</ol>";
							return result;
						}else if(SecondPart.equals("letter")) {
							result = "</div>";
							return result;
						}
					}
					
					FirstPart = LatexHTML.get(FirstPart.replace("*", ""));
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
				}else if(line.contains("includegraphics")) {
					String sizes = line.substring(line.indexOf("[")+1, line.indexOf("]"));
					String[] widthHeight = sizes.split(",");
					float width = Float.parseFloat(widthHeight[0].substring(widthHeight[0].indexOf("=")+1));
					float height = Float.parseFloat(widthHeight[1].substring(widthHeight[1].indexOf("=")+1));
					String imageLocation = line.substring(line.indexOf("]")+1);
					imageLocation = (imageLocation.replace("{", "")).replace("}", "");
					
					
					return "<img src='" + imageLocation + "' width='" + width + "' height='" + height + "'>";
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
			}else if(line.contains("tableofcontents")) {
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
			}else if(readTable) {
				line  = line.replace("\\\\", "");
				String[] parts = line.split("&");
				if(tableWithBorder) {
					result += "<tr style='border:1px solid black;'>";
				}else {
					result += "<tr>";
				}
				for(int i = 0;i<parts.length;i++) {
					if(tableWithBorder) {
						result += "<td style='border:1px solid black;'>" + parts[i] + "</td>";
					}else {
						result += "<td>" + parts[i] + "</td>";
					}
				}
				result += "</tr>";
				return result;
			}
			return line;
		}
		return result;
	}
	
	public void saveAsHTML() {
		LatexEditorView latexEditorView = versionsManager.getEditorView();
		String documentContents = latexEditorView.getCurrentDocument().getContents();
		
		numberingEnabled = true;
		ChapterCount = 1;
		sectionCount = 1;
		letterSignature = "";
		isLetter = false;
		letterSignerAddress = "";
		letterDestination = "";
		readTable = false;
		subsectionCount = 1;
		subsubsectionCount = 1;
		tableWithBorder = false;
		
		Scanner scanner = new Scanner(documentContents);
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
			result = "<html>\n<p style='float: right'>"+letterSignerAddress+"\n<br>" + date.toString() + "</p>\n<br>\n<br>" + letterDestination + result;
		  }else {
			  result = "<html>" + result;
		  }
		result += "</html>";
		String replace = result.replace("\\n", "\n<br>");
		System.out.println(replace);
		scanner.close();
	}

	@Override
	public void execute() {
		saveAsHTML();
	}

}
