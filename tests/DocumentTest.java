import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.LatexEditorController;
import model.Document;
import model.DocumentManager;
import model.VersionsManager;
import model.strategies.VersionsStrategy;
import model.strategies.VolatileVersionsStrategy;
import view.LatexEditorView;

class DocumentTest {
	private DocumentManager documentManager = new DocumentManager();
	private VersionsStrategy strategy = new VolatileVersionsStrategy();
	private LatexEditorView latexEditorView = new LatexEditorView();
	private VersionsManager versionsManager = new VersionsManager(strategy, latexEditorView);
	private LatexEditorController controller = new LatexEditorController(versionsManager);
	private String defaultStrategy = "volatile";
	private Document document;
	
	@BeforeEach
	void init() {
		document = new Document();
		latexEditorView.setCurrentDocument(document);
		latexEditorView.setVersionsManager(versionsManager);
		latexEditorView.setController(controller);
	}
	
	@Test
	void createEmptyDocument() { // US-1 -- ✅
		String template = "emptyTemplate";
		latexEditorView.setType(template);
		latexEditorView.getController().enact("create");
		
		assertEquals(latexEditorView.getCurrentDocument().getContents(), "");
	}
	
	@Test
	void createDocumentFromTemplate() { // US-1 -- ✅
		String template = "articleTemplate";
		latexEditorView.setType(template);
		latexEditorView.getController().enact("create");
		
		assertEquals(latexEditorView.getCurrentDocument().getContents(), documentManager.getContents(template));
	}
	
	@Test
	void editDocument() { // US-2 -- ✅
		String template = "articleTemplate";
		latexEditorView.setType(template);
		latexEditorView.getController().enact("create");
		String textAfterEdit = documentManager.getContents(template) + "\nEdited new line.";
		latexEditorView.setText(textAfterEdit);
		latexEditorView.getController().enact("edit");

		assertEquals(textAfterEdit, latexEditorView.getCurrentDocument().getContents());
	}
	
	@Test
	void enableTrackingMechanism() { // US-4 -- ✅
		latexEditorView.setStrategy(defaultStrategy);
		latexEditorView.getController().enact("enableVersionsManagement");
		
		assertEquals(versionsManager.isEnabled(), true);
	}
	
	@Test
	void changeStorageStrategy() { // US-5 -- ✅
		latexEditorView.setStrategy("stable");
		latexEditorView.getController().enact("changeVersionsStrategy");
		
		latexEditorView.setStrategy("volatile");
		latexEditorView.getController().enact("changeVersionsStrategy");
		
		assertEquals(latexEditorView.getStrategy(), "volatile");
	}
	
	@Test
	void disableTrackingMechanism() { // US-6 -- ✅
		latexEditorView.setStrategy(defaultStrategy);
		latexEditorView.getController().enact("enableVersionsManagement");
		latexEditorView.getController().enact("disableVersionsManagement");
		
		assertEquals(versionsManager.isEnabled(), false);
	}
	
	@Test
	void rollbackVersion() { // US-7 -- ✅
		String template = "emptyTemplate";
		latexEditorView.setType(template);
		latexEditorView.setStrategy(defaultStrategy);
		latexEditorView.getController().enact("enableVersionsManagement");
		latexEditorView.getController().enact("create");
		
		String version1 = "Version 1";
		latexEditorView.setText(version1);		
		latexEditorView.getController().enact("edit");
		
		String version2 = "Version 2";
		latexEditorView.setText(version2);		
		latexEditorView.getController().enact("edit");
		
		String version3 = "Version 3";
		latexEditorView.setText(version3);		
		latexEditorView.getController().enact("edit");
		
		latexEditorView.getController().enact("rollbackToPreviousVersion"); // rollback to v2
		latexEditorView.getController().enact("rollbackToPreviousVersion"); // rollback to v1
		
		assertEquals(latexEditorView.getCurrentDocument().getVersionID(), "1");
	}
	
	@Test
	void saveDocumentToPath() { // US-8 -- ✅
		String template = "bookTemplate";
		String docSavePath = "C:/Users/Sot/Desktop/test.txt";
		File file = new File(docSavePath);
		latexEditorView.setType(template);
		latexEditorView.getController().enact("create");
		latexEditorView.setFilename(docSavePath);
		latexEditorView.getController().enact("save");

		assertTrue(file.exists());
	}
	
	@Test
	void loadDocumentFromDisk() { // US-9 -- ✅
		String filePath = "C:/Users/Sot/Desktop/test.txt";
		File file = new File(filePath);
		latexEditorView.setFilename(filePath);
		if(file.exists()) {
			latexEditorView.getController().enact("load");
		}
		
		assertNotNull(latexEditorView.getCurrentDocument().getContents());
	}

}
