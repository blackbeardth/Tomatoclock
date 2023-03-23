package application;

import java.util.HashMap;

public class TaskList {
	private HashMap<String, Task> taskList = new HashMap<String, Task>();

	public HashMap<String, Task> getTaskList() {
		return taskList;
	}
}
