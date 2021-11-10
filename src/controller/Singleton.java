package controller;

import model.VersionsManager;

public class Singleton {
	private static Singleton instance;
	public VersionsManager versionsManager;

    private Singleton(VersionsManager versionsManager) {
        this.versionsManager = versionsManager;
    }

    public static Singleton getInstance(VersionsManager versionsManager) {
    	if (instance == null) {
            instance = new Singleton(versionsManager);
        }
        return instance;
    }
}
