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
	private HashMap<String, String> basic = new HashMap<String, String>();
	private ArrayList<String> begins = new ArrayList<String>();
	private String docType = "";
	private String destAddress = "";
	private String senderName = "";
	private boolean senderNameNext = false;
	
	public LoadHTMLAsLatex() {
		basic.put("<head><center><h2>", "\\title{");
		basic.put("</h2></center></head>", "\\maketitle");
		basic.put("<div id='abstract'><center><h3>Abstract</h3></center>", "\\begin{abstract}\n");
		basic.put("<div id='document'>", "\\begin{document}\\n");
		basic.put("<table style='border:1px solid black;'>", "\\begin{table}\n\\begin{tabular}{|c|c|c|}");
		basic.put("<table>", "\\begin{table}\n\\begin{tabular}{ccc}");
		
		basic.put("</table>", "\\end{tabular}\n\\end{table}");
		basic.put("<ul>", "\\begin{itemize}");
		basic.put("<ol>", "\\begin{enumerate}");
		basic.put("<li>", "\\item");
		basic.put("</ul>", "\\end{itemize}");
		basic.put("</ol>", "\\end{enumerate}");
		basic.put("<p id='address'>", "");
		basic.put("<p class='letter' style='float: right'>", "\\documentclass{letter}\\n\nPLACEHOLDERSIGNATURE_1\n\\address{");
		basic.put("<tr style='border:1px solid black;'><td style='border:1px solid black;'>", "");
		basic.put("<tr><td>", "");
		
		HTMLLatex.put("<html>", "");
		HTMLLatex.put("</html>", "");
		basic.put("</tr>", "\\\\");
		HTMLLatex.put("<h1>", "\\chapter{");
		HTMLLatex.put("<h2>", "\\section{");

		HTMLLatex.put("<td style='border:1px solid black;'>", " & ");
		HTMLLatex.put("<td>", " & ");
		
		
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
		HTMLLatex.put("<!--", "%");
		HTMLLatex.put(" -->", "\\n\\n");
		HTMLLatex.put("</div>", "\\end{");
		HTMLLatex.put("&emsp;", "\\and");
		basic.put("<p class='date'>", "\\date{");
		HTMLLatex.put("</p>", "");
		HTMLLatex.put("CHECKIFDATE", "\\today");
		HTMLLatex.put("<p>", "\\author{");
		HTMLLatex.put("encl: ", "\\encl{");// xwris close
		basic.put("<div id='opening'>", "\\opening{");// xwris close
		basic.put("</div><p style='float: right'>", "\\closing{");
		HTMLLatex.put("<figcaption>", "\\caption{");
		HTMLLatex.put("<hr>", "\\hline");
		basic.put("<p class='date'>", "\\date{");
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
			if(tag.contains("font")) {
				String fontSize = "14";
				docType = line.substring(line.indexOf("class='")+7,line.indexOf("' style"));

				if(!docType.equals("letter")) {
					fontSize = line.substring(line.indexOf(":")+1,line.indexOf("pt"));
				}else {
					return "\\documentclass{" + docType + "}\\n";
				}
				return "\\documentclass[" + fontSize + "pt,twocolumn,a4paper]{" + docType + "}\\n\\n";
			}
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
					line = line.replaceAll("</.*>", "");
					if(line.equals("}")) {
						return "";
					}
				}else if(!line.contains("</div>") && !hasTag) {
					return "";
				}
			}
		}
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
					  boolean isAddress = false;
					  boolean isTableContent = false;
					  for (Map.Entry me : basic.entrySet()) {
							if(line.contains(me.getKey().toString())) {
								String value = me.getValue().toString();
								String key = me.getKey().toString();
								if(value.contains("begin")) {
									if(!value.contains("tabular") &&  !value.contains("itemize") && !value.contains("enumerate")){
									begins.add(value.substring(value.indexOf("{")+1, value.indexOf("}")));
									}
								}else if(key.equals("<p id='address'>")) {
									isAddress = true;
								}else if(key.contains("<td style='border:1px solid black;'>") || key.contains("<td>")) {
									isTableContent = true;
								}
								
								line = line.replace(me.getKey().toString(), me.getValue().toString());
							}
						  }
					  String parsedLine = parser(line);
					  if(isAddress) {
						  destAddress = parsedLine.substring(0,parsedLine.indexOf("<"));
						  basic.put("<div id='letter'>", "\\begin{letter}{"+destAddress+"}\\n");
						  isAddress = false;
						  continue;
					  }
					  
					  for (Map.Entry me : HTMLLatex.entrySet()) {
							if(parsedLine.contains(me.getKey().toString())) {
								
								parsedLine = parsedLine.replace(me.getKey().toString(), me.getValue().toString());
								if(parsedLine.contains("end{")) {
									parsedLine += begins.get(begins.size()-1);
									begins.remove(begins.size()-1);
								}else if(me.getKey().toString().equals("<td style='border:1px solid black;'>") || me.getKey().toString().contains("<td>")) {
									isTableContent = true;
								}
							}
						  }
					  
					  if(parsedLine.contains("{") && !parsedLine.contains("}")) {
						  parsedLine += "}";
						  
						  if(parsedLine.contains("date{")) {
								String date = parsedLine.substring(parsedLine.indexOf("{")+1, parsedLine.indexOf("}"));
								parsedLine = parsedLine.replace(date, "\\today");
							}
					  }
					  if(senderNameNext) {
						  senderName = parsedLine;
						  result = result.replaceAll("PLACEHOLDERSIGNATURE_1", "\\\\signature{" + senderName.toString() + "}\\\\n");
						  senderNameNext = false;
						  continue;
					  }
					  if(parsedLine.contains("\\address{")) {
						  parsedLine += "}";
					  }else if(parsedLine.contains("\\closing")) {
						  senderNameNext = true;
					  }
					  parsedLine = parsedLine.replaceAll("Chapter \\d+: \\\\n\\\\n ", "");
					  parsedLine = parsedLine.replaceAll("\\\\section\\{\\d+ ", "\\\\section{");
					  parsedLine = parsedLine.replaceAll("\\\\subsection\\{[+-]?([0-9]*[.])?[0-9]+ ", "\\\\subsection{");
					  parsedLine = parsedLine.replaceAll("\\\\subsubsection\\{[+-]?([0-9]*[.])([0-9]*[.])?[0-9]+ ", "\\\\subsubsection{");
					  
					  Pattern regex = Pattern.compile("\\d+:\\d+:\\d+");
						Matcher regexMatcher = regex.matcher(line);
						boolean hasDate = regexMatcher.find();
						if(hasDate) {
							continue;
						}

					  result += parsedLine;
					  if(!isTableContent) {
						  result += "\n";
					  }
					  
					 
					}
				System.out.println(result);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void execute() {
		loadAsLatex();
	}

}
