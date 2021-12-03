package controller;

import model.DocumentManager;
import model.VersionsManager;

public class Singleton {
	private static Singleton instance;
	public static VersionsManager versionsManager;
	public static DocumentManager documentManager;

    private Singleton(VersionsManager versionsManager, DocumentManager documentManager) {
        Singleton.versionsManager = versionsManager;
        Singleton.documentManager = documentManager;
    }

    public static Singleton getInstance(VersionsManager versionsManager, DocumentManager documentManager) {
    	if (instance == null) {
            instance = new Singleton(versionsManager, documentManager);
        }
        return instance;
    }
    
    public VersionsManager getVM() {
    	return versionsManager;
    }
    
    public void destroyInstance() { // only used on JUnit tests
    	   Singleton.instance = null;
    }
}
