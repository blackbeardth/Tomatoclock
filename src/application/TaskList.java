package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

public class TaskList {
	private LinkedHashMap<Integer, Task> taskList = new LinkedHashMap<Integer, Task>();

	public LinkedHashMap<Integer, Task> getTaskList() {
		return taskList;
	}

	public void serialize(String templateName) {
		try {
			String directoryPath = System.getProperty("user.dir") + "/appdata/templates";
			File directory = new File(directoryPath);

			if (directory.exists() && directory.isDirectory()) {
				FileOutputStream myFileOutStream = new FileOutputStream(directoryPath + "/" + templateName + ".ser");

				ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);

				// overwrites the taskList
				myObjectOutStream.writeObject(this.taskList);

				// closing FileOutputStream and
				// ObjectOutputStream
				myObjectOutStream.close();
				myFileOutStream.close();

				// System.out.println("Templated created sucessfully");
			} 
			else {
				throw new IOException();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<Integer, Task> deserialize(String templateName) {
		LinkedHashMap<Integer, Task> newTaskList = null;

		try {
			String directoryPath = System.getProperty("user.dir") + "/appdata/templates";
			File directory = new File(directoryPath);

			if (directory.exists() && directory.isDirectory()) {
				FileInputStream fileInput = new FileInputStream(directoryPath + "/" + templateName + ".ser");

				ObjectInputStream objectInput = new ObjectInputStream(fileInput);

				newTaskList = (LinkedHashMap<Integer, Task>) objectInput.readObject();

				objectInput.close();
				fileInput.close();

				return newTaskList; // deserialization successful
			} else {
				throw new IOException();
			}
		} catch (IOException obj1) {
			// obj1.printStackTrace();
		} catch (ClassNotFoundException obj2) {
			// obj2.printStackTrace();
		}
		return newTaskList;
	}
}
