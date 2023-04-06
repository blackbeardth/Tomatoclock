package application;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable{
	private static final long serialVersionUID = 3636132678451544076L;
	private static final int PASSWORD_MIN_LENGTH = 8;
	private String username;
	private String password;
	private int id;
	private int totalHoursFocused;
	private boolean keepLoggedIn;
	private LinkedHashMap<LocalDate, Integer> hoursFocusedMap; // Hashmap to store data on how many hours focused by user for 7 days
	
	
	// constructor for existing user from hashMap
	public User(String username) throws Exception {
		if (Controller.umap == null)
			return;
		
		boolean found = false;

		// check if user with such username exists
		Integer key = null;
		try {
			for (Entry<Integer, User> entry : Controller.umap.entrySet()) {
				if (entry.getValue().getUsername().equals(username)) {
					key = entry.getKey();
					User existingUser = Controller.umap.get(key);
					
					// Gets the user data from HashMap
					this.username = username;
					this.password = existingUser.getPassword();
					this.id = existingUser.getId();
					this.keepLoggedIn = existingUser.getKeepLoggedIn();
					this.totalHoursFocused = existingUser.getTotalHoursFocused();
					this.hoursFocusedMap = existingUser.getHoursFocusedMap();
					
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!found)
			throw new Exception();
	}
	
	// contructs new user with given parameters
	// To be used whilst registering a user
	public User(String username, String password, Integer id) {
		this.username = username;
		this.password = password;
		this.id = id;
		this.keepLoggedIn = false;
		this.totalHoursFocused = 0;
		this.hoursFocusedMap = new LinkedHashMap<LocalDate, Integer>();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKeepLoggedIn(boolean status) {
		this.keepLoggedIn = status;
	}
	
	public void setTotalHoursFocused(int hours) {
		this.totalHoursFocused += hours;
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public boolean getKeepLoggedIn() {
		return this.keepLoggedIn;
	}

	public int getId() {
		return this.id;
	}
	
	public int getTotalHoursFocused() {
		return this.totalHoursFocused;
	}
	
	public LinkedHashMap<LocalDate, Integer> getHoursFocusedMap() {
		return this.hoursFocusedMap;
	}

	public boolean passwordsMatch(String password) {
		return (this.password.equals(password));
	}

	public static boolean usernameAvailable(String username) {
		try {
			for (Entry<Integer, User> entry : Controller.umap.entrySet()) {
				if (entry.getValue().getUsername().equals(username))
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public static boolean isPasswordStrong(String password) {
		// Creates patterns
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern number = Pattern.compile("[0-9]");

        // Creates matchers
        Matcher hasUpperCase = upperCase.matcher(password);
        Matcher hasLowerCase = lowerCase.matcher(password);
        Matcher hasNumber = number.matcher(password);

        // Checks whether password is strong
        return hasUpperCase.find() && hasLowerCase.find() && hasNumber.find() &&
                password.length() >= PASSWORD_MIN_LENGTH;
	}

	public void addUser(User newUser) {
		Controller.umap.put(this.id, newUser);
	}
	
	
	@Override
	public String toString() {
		return ("Username: " + username + ", Password: " + password + ", ID: " + id + ", totalHoursFocused: " + this.totalHoursFocused);
	}
}
