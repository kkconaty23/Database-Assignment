package org.example.javafxdb_sql_shellcode;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe implementation of UserSession using ReentrantReadWriteLock
 */
public class UserSession {
    private static volatile UserSession instance;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private String username;
    private String userRole;
    private boolean isLoggedIn;

    // Private constructor to prevent instantiation
    private UserSession() {
        this.isLoggedIn = false;
    }

    /**
     * Gets the singleton instance of UserSession in a thread-safe manner
     * @return The UserSession instance
     */
    public static UserSession getInstance() {
        if (instance == null) {
            lock.writeLock().lock();
            try {
                if (instance == null) {
                    instance = new UserSession();
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return instance;
    }

    /**
     * Sets the current user session details
     * @param username The username
     * @param userRole The user's role
     */
    public void setUser(String username, String userRole) {
        lock.writeLock().lock();
        try {
            this.username = username;
            this.userRole = userRole;
            this.isLoggedIn = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Clears the current user session
     */
    public void clearSession() {
        lock.writeLock().lock();
        try {
            this.username = null;
            this.userRole = null;
            this.isLoggedIn = false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets the current username
     * @return The username
     */
    public String getUsername() {
        lock.readLock().lock();
        try {
            return username;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the current user role
     * @return The user role
     */
    public String getUserRole() {
        lock.readLock().lock();
        try {
            return userRole;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        lock.readLock().lock();
        try {
            return isLoggedIn;
        } finally {
            lock.readLock().unlock();
        }
    }
}