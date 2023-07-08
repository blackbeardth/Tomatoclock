package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Properties props = new Properties();

	private static final String configFile = System.getProperty("user.dir") + "/appdata/.config";
	
	private static String PROP_POMO = "POMODORO"; 
	private static String PROP_SBREAK = "SBREAK";
	private static String PROP_LBREAK = "LBREAK";
	private static String PROP_longBreakInterval = "longBreakInterval";
	private static String PROP_autoStartBreaks = "autoStartBreaks";
	private static String PROP_autoStartPomodoros = "autoStartPomodoros";
	private static String PROP_keepLoggedIn = "keedLoggedIn";
	private static String PROP_UID = "UID";

	public static Object[] loadSettings() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(configFile)) {
			props.load(fis);
		}
		
		int POMO = Integer.parseInt(props.getProperty(PROP_POMO, "1500")); // 25 min
		int SBREAK = Integer.parseInt(props.getProperty(PROP_SBREAK, "300")); // 5 min
		int LBREAK = Integer.parseInt(props.getProperty(PROP_LBREAK, "900")); // 15 min
		int longBreakInterval = Integer.parseInt(props.getProperty(PROP_longBreakInterval, "4"));
		boolean autoStartBreaks = Boolean.valueOf(props.getProperty(PROP_autoStartBreaks, "false"));
		boolean autoStartPomodoros = Boolean.valueOf(props.getProperty(PROP_autoStartPomodoros, "false"));
		boolean keepLoggedIn = Boolean.valueOf(props.getProperty(PROP_keepLoggedIn, "false"));
		int UID = Integer.parseInt(props.getProperty(PROP_UID, "0"));
		
		return new Object[] {POMO, SBREAK, LBREAK, longBreakInterval, autoStartBreaks, autoStartPomodoros, keepLoggedIn, UID};
	}

	public static void saveSettings(Object[] ob) throws FileNotFoundException, IOException {
		props.setProperty(PROP_POMO, String.valueOf(ob[0]));
        props.setProperty(PROP_SBREAK, String.valueOf(ob[1]));
        props.setProperty(PROP_LBREAK, String.valueOf(ob[2]));
        props.setProperty(PROP_longBreakInterval, String.valueOf(ob[3]));
        props.setProperty(PROP_autoStartBreaks, String.valueOf(ob[4]));
        props.setProperty(PROP_autoStartPomodoros, String.valueOf(ob[5]));
        props.setProperty(PROP_keepLoggedIn, String.valueOf(ob[6]));
        props.setProperty(PROP_UID, String.valueOf(ob[7]));
        
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            props.store(fos, "Tomatotimer config");
        }
	}
	
	public static void resetConfig() throws IOException {
		FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write("");
        fileWriter.close();
	}
}
