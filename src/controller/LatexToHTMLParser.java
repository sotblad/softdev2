package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LatexToHTMLParser {
	private HashMap<String, String> LatexHTML = new HashMap<String, String>();
	private ArrayList<String> noClosingTags = new ArrayList<String>();
	private int ChapterCount;
	private int sectionCount;
	private int subsectionCount;
	private boolean numberingEnabled = true;
	private String letterSignature;
	private String letterSignerAddress;
	private boolean isLetter = false;
	private boolean containsAsterisk = false;
	private boolean readTable = false;
	private String letterDestination;
	private int subsubsectionCount = 1;
	private boolean tableWithBorder = true;
	
	public LatexToHTMLParser() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
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
		LatexHTML.put("\\date", "<p class='date'>");
		LatexHTML.put("\\today", date);
		LatexHTML.put("\\author", "<p>");
		LatexHTML.put("\\begin", "<>");
		LatexHTML.put("\\maketitle", "</h2></center></head>");
		LatexHTML.put("\\end", "<>");
		LatexHTML.put("\\encl", "encl: ");
		LatexHTML.put("\\opening", "<div id='opening'>");
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
	
	public String parser(String line) {
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
								extra += extra = "Chapter "+ ChapterCount + ": <br><br> ";
							}
							ChapterCount++;
						}
					}else if(line.contains("\\label")) {
						ThirdPart = ThirdPart.substring(0,ThirdPart.indexOf("\\label{"));
					}else if(FirstPart.contains("\\section")) {
						if(!FirstPart.contains("*")) {
							extra = sectionCount + " ";
							sectionCount++;
							subsectionCount = 1;
						}
					}else if(FirstPart.equals("\\closing")) {
						extra = LatexHTML.get(FirstPart) + SecondPart + "<br>\n" + letterSignature + "</p>";
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
							result = "<div id='document'>";
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
						}else if(SecondPart.equals("table")) {
							result = "";
							return result;
						}else if(SecondPart.equals("letter")) {
							letterDestination = ThirdPart.substring(ThirdPart.indexOf("{")+1, ThirdPart.indexOf("}")) + "<p>";
							result = "<div id='letter'>";
							isLetter = true;
							return result;
						}
					}else if(FirstPart.equals("\\end")) {
						if(SecondPart.equals("document")) {
							result = "</div>";
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
						}else if(SecondPart.equals("table")) {
							result = "";
							return result;
						}else if(SecondPart.equals("enumerate")) {
							result = "</ol>";
							return result;
						}else if(SecondPart.equals("letter")) {
							result = "</div>";
							return result;
						}
					}else if(FirstPart.equals("\\date")) {
						result = LatexHTML.get(FirstPart.replace("*", "")) + SecondPart + "</p>" + ThirdPart;
						return result;
					}
					
					if(FirstPart.contains("*")) {
						containsAsterisk = true;
					}
					FirstPart = LatexHTML.get(FirstPart.replace("*", ""));
					if(containsAsterisk) {
						result = extra + SecondPart + closingTag(FirstPart) + ThirdPart;
						FirstPart = FirstPart.substring(0,FirstPart.indexOf(">")) + " class='asterisk'>";
						result = FirstPart + result;
						containsAsterisk = false;
						return result;
					}
					
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
						String docType = line.substring(line.indexOf("{")+1, line.indexOf("}"));
						result = "\n<font class='" + docType + "' style='font-size:"+fontSize+"'>";
					}
					return result;
				}else if(line.contains("usepackage")) {
					return "";
				}else if(line.contains("label")) {
					return "";
				}else if(line.contains("signature")) {
					letterSignature = SecondPart; 
					return "";
				}else if(line.contains("includegraphics")) {
					String sizes = line.substring(line.indexOf("[")+1, line.indexOf("]"));
					String[] widthHeight = sizes.split(",");
					Integer width = Integer.parseInt(widthHeight[0].substring(widthHeight[0].indexOf("=")+1));
					Integer height = Integer.parseInt(widthHeight[1].substring(widthHeight[1].indexOf("=")+1));
					String imageLocation = line.substring(line.indexOf("]")+1);
					imageLocation = (imageLocation.replace("{", "")).replace("}", "");
					
					
					return "<img src='" + imageLocation + "' width='" + width + "px' height='" + height + "px'>";
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
				return "<!--" + line.substring(1).replace("\\n", "") + " -->";
			}else if(readTable) {
				line  = line.replace("\\\\", "");
				String[] parts = line.split("&");
				if(tableWithBorder) {
					result += "<tr style='border:1px solid black;'>";
				}else {
					result += "<tr>";
				}
				for(int i = 0;i<parts.length;i++) {
					if(!parts[i].contains("hline") && !parts[i].equals("")) {
						if(tableWithBorder) {
							result += "<td style='border:1px solid black;'>" + parts[i] + "</td>\n";
						}else {
							result += "<td>" + parts[i] + "</td>\n";
						}
					}else {
						return "";
					}
				}
				result += "</tr>";
				return result;
			}
			return line;
		}
		return result;
	}
	
	public String parseLatexToHTML(String documentContents) {
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
		containsAsterisk = false;
		
		Scanner scanner = new Scanner(documentContents);
		String result = "";
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  
		  result += parser(line) + "\n";
			for (Map.Entry me : LatexHTML.entrySet()) {
				  result = result.replace(me.getKey().toString(), me.getValue().toString());
			  }
		}
		if(isLetter) {
			Date date = new Date();
			result = "<html>\n<p class='letter' style='float: right'>"+letterSignerAddress+"\n<br>" + date.toString() + "</p>\n<br>\n<br>\n<p id='address'>" + letterDestination + "</p>" + result;
		  }else {
			  result = "<html>" + result;
		  }
		result += "</html>";
		String replace = result.replace("\\n", "\n<br>");

		scanner.close();
		return replace;
	}

}
