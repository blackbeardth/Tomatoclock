package application;

import java.time.LocalDateTime;

public class Session {
	// User currently using the system
    private static User loggedUser = null;
    private static LocalDateTime startTime = null;
    
    
	public static void beginSession(User currentUser) {
		loggedUser = currentUser;
		startTime = LocalDateTime.now();
	}
	
	public static User getSession(){
        return loggedUser;
    }
	
	public static LocalDateTime getSessionStartTime(){
        return startTime;
    }
	
	public static void endSession() {
		loggedUser = null;
	}
}
