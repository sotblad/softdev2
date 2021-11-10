package controller.commands;

import model.DocumentManager;
import model.VersionsManager;

public class Singleton {
	private static Singleton instance;
    public static DocumentManager documentManager;
    public static VersionsManager versionsManager;

    private Singleton(DocumentManager documentManager, VersionsManager versionsManager) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.documentManager = documentManager;
        this.versionsManager = versionsManager;
    }
    
    public static void initInstance(DocumentManager documentManager, VersionsManager versionsManager) {
        if (instance == null) {
            instance = new Singleton(documentManager, versionsManager);
        }
    }
    
    public void setVersionManager(VersionsManager versionsManager) {
        this.versionsManager = versionsManager;
    }

    public static Singleton getInstance() {
        return instance;
    }
}
