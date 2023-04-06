package application;

import java.io.Serializable;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	private int estPomodoros; // Estimate of number of pomodoros
	private int pomoCompleted = 0;
	private String note; // a Note
	private Integer taskID;

	public Task(Integer taskID) {
		estPomodoros = 1;
		note = "";
		this.taskID = taskID;
	}

	public Task(int inputNumber, Integer taskID, String inputString) {
		estPomodoros = inputNumber;
		setNote(inputString);
		this.taskID = taskID;
	}

	public void setEstPomodoros(int inputNumber) {
		estPomodoros = inputNumber;
	}

	public void setNote(String inputString) {
		note = inputString;
	}

	public void setPomoCompleted(int inputNumber) {
		pomoCompleted = inputNumber;
	}

	public void setButtonText(String inputString, Button button) {
		Text buttonText = new Text(inputString + "(" + pomoCompleted + "/" + estPomodoros + ")");
		buttonText.setFill(Color.WHITE);
		button.setGraphic(buttonText); ///
	}

	public int getEstPomodoros() {
		return estPomodoros;
	}

	public String getNote() {
		return note;
	}

	public Integer getTaskID() {
		return taskID;
	}

	public int getPomoCompleted() {
		return pomoCompleted;
	}

	public void incrementPomoCompleted(Button button) {
		pomoCompleted++;
		setButtonText(this.note, button);
	}

	public String toString() {
		return "Task(TaskID : " + taskID + ", Est: " + estPomodoros + ", Note: " + note;
	}

}
