package com.example.rewear.Gestionevent.Entity;

public class SessionManager {
    private static SessionManager instance;
    private int currentUserId;
    private boolean loggedIn = false;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId) {
        this.currentUserId = userId;
        this.loggedIn = true;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}

