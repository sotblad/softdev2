package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DocumentManager {
	private HashMap<String, Document> templates;
	
	public DocumentManager() {
		templates = new HashMap<String, Document>();
		List<String> templatesList = Arrays.asList(
				new String[] {
						"reportTemplate",
						"bookTemplate",
						"articleTemplate",
						"letterTemplate",
						"emptyTemplate"
				}
		);
		
		for(int i = 0; i < templatesList.size(); i++) {
			String template = templatesList.get(i);
			
			Document document = new Document();
			document.setContents(getContents(template));
			templates.put(template, document);
		}
	}
	
	public Document createDocument(String type) {
		return templates.get(type).clone();
	}
	
	public String getTemplatesFolder(){
        String userDirectory = System.getProperty("user.dir");

        return userDirectory + "\\resources\\templates\\";
    }
	
	private static String readFileContents(String filePath){
        String content = "";
 
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } 
        catch (IOException e){
            e.printStackTrace();
        }
 
        return content;
    }
	
	public String getContents(String type) {
		String filePath = getTemplatesFolder() + type + ".tex";
	    String fileContents = readFileContents(filePath);
	    
		return fileContents;
	}
}