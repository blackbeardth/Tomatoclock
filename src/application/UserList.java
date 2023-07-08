package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UserList {
	
	public static void serialize() {
		HashMap<Integer, User> userList = Controller.umap;
		try {
			String directoryPath = System.getProperty("user.dir") + "/appdata";
			File directory = new File(directoryPath);

			if (directory.exists() && directory.isDirectory()) {
				FileOutputStream myFileOutStream = new FileOutputStream(directoryPath + "/data.ser");

				ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);

				// overwrites the taskList
				myObjectOutStream.writeObject(userList);

				// closing FileOutputStream and
				// ObjectOutputStream
				myObjectOutStream.close();
				myFileOutStream.close();
			} else {
				throw new IOException();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<Integer, User> deserialize() {
		HashMap<Integer, User> myUserList = null;

		try {
			String directoryPath = System.getProperty("user.dir") + "/appdata";
			File directory = new File(directoryPath);

			if (directory.exists() && directory.isDirectory()) {
				FileInputStream fileInput = new FileInputStream(directoryPath + "/data.ser");

				ObjectInputStream objectInput = new ObjectInputStream(fileInput);

				myUserList = (HashMap<Integer, User>) objectInput.readObject();

				objectInput.close();
				fileInput.close();

				return myUserList; // deserialization successful
			} else {
				throw new IOException();
			}
		} catch (IOException obj1) {
			// obj1.printStackTrace();
		} catch (ClassNotFoundException obj2) {
			// obj2.printStackTrace();
		}
		return myUserList;
	}
}
