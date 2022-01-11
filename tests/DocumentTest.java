import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.LatexEditorController;
import controller.Singleton;
import model.Document;
import model.DocumentManager;
import model.VersionsManager;
import model.strategies.VersionsStrategy;
import model.strategies.VolatileVersionsStrategy;

class DocumentTest {
	private DocumentManager documentManager = new DocumentManager();
	private VersionsStrategy strategy = new VolatileVersionsStrategy();
	private LatexEditorController latexEditorController = new LatexEditorController();
	private VersionsManager versionsManager = new VersionsManager(strategy, latexEditorController);
	private LatexEditorController controller = new LatexEditorController(versionsManager);
	private Singleton app;
	private String defaultStrategy = "volatile";
	private Document document;
	
	@BeforeEach
	void init() {
		document = new Document();
		app = Singleton.getInstance(versionsManager, documentManager);
		app.destroyInstance(); // method used only on tests.
		latexEditorController.setCurrentDocument(document);
		latexEditorController.setVersionsManager(versionsManager);
		latexEditorController.setController(controller);
	}
	
	@Test
	void createEmptyDocument() { // US-1 -- ✅
		String template = "emptyTemplate";
		latexEditorController.setType(template);
		latexEditorController.getController().enact("create");
		
		assertEquals(latexEditorController.getCurrentDocument().getContents(), "");
	}
	
	@Test
	void createDocumentFromTemplate() { // US-1 -- ✅
		String template = "articleTemplate";
		latexEditorController.setType(template);
		latexEditorController.getController().enact("create");
		
		assertEquals(latexEditorController.getCurrentDocument().getContents(), documentManager.getContents(template));
	}
	
	@Test
	void editDocument() { // US-2 -- ✅
		String template = "articleTemplate";
		latexEditorController.setType(template);
		latexEditorController.getController().enact("create");
		String textAfterEdit = documentManager.getContents(template) + "\nEdited new line.";
		latexEditorController.setText(textAfterEdit);
		latexEditorController.getController().enact("edit");

		assertEquals(textAfterEdit, latexEditorController.getCurrentDocument().getContents());
	}
	
	@Test
	void enableTrackingMechanism() { // US-4 -- ✅
		latexEditorController.setStrategy(defaultStrategy);
		latexEditorController.getController().enact("enableVersionsManagement");
		
		assertEquals(latexEditorController.getVersionsManager().isEnabled(), true);
	}
	
	@Test
	void changeStorageStrategy() { // US-5 -- ✅
		latexEditorController.setStrategy("stable");
		latexEditorController.getController().enact("changeVersionsStrategy");
		
		latexEditorController.setStrategy("volatile");
		latexEditorController.getController().enact("changeVersionsStrategy");
		
		assertEquals(latexEditorController.getStrategy(), "volatile");
	}
	
	@Test
	void disableTrackingMechanism() { // US-6 -- ✅
		latexEditorController.setStrategy(defaultStrategy);
		latexEditorController.getController().enact("enableVersionsManagement");
		latexEditorController.getController().enact("disableVersionsManagement");
		
		assertEquals(latexEditorController.getVersionsManager().isEnabled(), false);
	}
	
	@Test
	void rollbackVersion() { // US-7 -- ✅
		String template = "emptyTemplate";
		latexEditorController.setType(template);
		latexEditorController.setStrategy(defaultStrategy);
		latexEditorController.getController().enact("enableVersionsManagement");
		latexEditorController.getController().enact("create");
		
		String version1 = "Version 1";
		latexEditorController.setText(version1);		
		latexEditorController.getController().enact("edit");
		
		String version2 = "Version 2";
		latexEditorController.setText(version2);		
		latexEditorController.getController().enact("edit");
		
		String version3 = "Version 3";
		latexEditorController.setText(version3);		
		latexEditorController.getController().enact("edit");
		
		latexEditorController.getController().enact("rollbackToPreviousVersion"); // rollback to v2
		latexEditorController.getController().enact("rollbackToPreviousVersion"); // rollback to v1
		
		assertEquals(latexEditorController.getCurrentDocument().getVersionID(), "1");
	}
	
	@Test
	void saveDocumentToPath() { // US-8 -- ✅
		String template = "bookTemplate";
		String docSavePath = "test.txt";
		File file = new File(docSavePath);
		latexEditorController.setType(template);
		latexEditorController.getController().enact("create");
		latexEditorController.setFilename(docSavePath);
		latexEditorController.getController().enact("save");

		assertTrue(file.exists());
	}
	
	@Test
	void loadDocumentFromDisk() { // US-9 -- ✅
		String filePath = "test.txt";
		File file = new File(filePath);
		latexEditorController.setFilename(filePath);
		if(file.exists()) {
			latexEditorController.getController().enact("load");
		}
		
		assertNotNull(latexEditorController.getCurrentDocument().getContents());
	}

}
