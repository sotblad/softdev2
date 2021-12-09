package controller.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.LatexEditorController;
import controller.LatexToHTMLParser;
import controller.Singleton;
import model.VersionsManager;

public class SaveAsHTMLCommand implements Command {
	private VersionsManager versionsManager = Singleton.versionsManager;
	
	public void saveAsHTML() {
		LatexEditorController latexEditorController = versionsManager.getEditorView();
		String documentContents = latexEditorController.getCurrentDocument().getContents();
		
		LatexToHTMLParser parse = new LatexToHTMLParser();
		String path = JOptionPane.showInputDialog("Enter the absolute path for the file creation:");
		if(path != null && !path.equals("")) {
			JFrame frame = new JFrame();
			if(path.contains(".") && !path.contains(".html")) {
				JOptionPane.showMessageDialog(frame, "File format must be html.");
			}else if(!path.contains(".")) {
				path = path + ".html";
			}
			String resultHTML = parse.parseLatexToHTML(documentContents);
			
			try {
		      File myObj = new File(path);
		  
		      if (myObj.createNewFile()) {
		    	  PrintWriter writer = new PrintWriter(path, "UTF-8");
		    	  writer.println(resultHTML);
		    	  writer.close();
		    	  JOptionPane.showMessageDialog(frame, "File created: " + myObj.getName());
		      } else {
		    	  JOptionPane.showMessageDialog(frame, "File already exists.");
		      }
		    } catch (IOException e) {
		    	JOptionPane.showMessageDialog(frame, "An error occured.");
		      e.printStackTrace();
		    }
		}
	}

	@Override
	public void execute() {
		saveAsHTML();
	}

}
