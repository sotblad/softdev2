package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.LatexEditorController;
import model.VersionsManager;
import model.strategies.VersionsStrategy;
import model.strategies.VolatileVersionsStrategy;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpeningWindow {

	private JFrame frame;
	private LatexEditorController latexEditorController;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpeningWindow window = new OpeningWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OpeningWindow() {
		VersionsStrategy versionsStrategy = new VolatileVersionsStrategy();
		latexEditorController = new LatexEditorController();
		VersionsManager versionsManager = new VersionsManager(versionsStrategy, latexEditorController);
		LatexEditorController controller = new LatexEditorController(versionsManager);
		latexEditorController.setController(controller);
		latexEditorController.setVersionsManager(versionsManager);
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnCreateNewDocument = new JButton("Create New Document");
		btnCreateNewDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChooseTemplate chooseTemplate = new ChooseTemplate(latexEditorController, "opening");
				frame.dispose();
			}
		});
		btnCreateNewDocument.setBounds(89, 26, 278, 36);
		frame.getContentPane().add(btnCreateNewDocument);
		
		JButton btnOpenExistingDocument = new JButton("Open Existing Document");
		btnOpenExistingDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				int option = filechooser.showOpenDialog(null);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("File formats", "tex"); //"html");
				filechooser.setFileFilter(filter);
				if(option == JFileChooser.APPROVE_OPTION) {
					try {
						String filename = filechooser.getSelectedFile().toString();
						JEditorPane editorPane = new JEditorPane();
						latexEditorController.setFilename(filename);
						
						latexEditorController.setType("emptyTemplate");
						latexEditorController.getController().enact("load");
						MainWindow mainWindow = new MainWindow(latexEditorController, editorPane);
					frame.dispose();
					}catch(Exception e1){
						JOptionPane.showMessageDialog(frame, "Invalid file format entered.");
					}
				}
			}
		});
		btnOpenExistingDocument.setBounds(89, 92, 278, 36);
		frame.getContentPane().add(btnOpenExistingDocument);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(99, 169, 268, 25);
		frame.getContentPane().add(btnExit);
	}
}
