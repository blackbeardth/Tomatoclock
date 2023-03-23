package application;

import javafx.scene.control.Button;

public class Task {
	private int EstPomodoros; // Estimate of number of pomodoros
	private int pomoCompleted = 0;
	private String note; // a Note
	private Button button;

	public Task() {
		EstPomodoros = 1;
		note = "";
		button = new Button();
		button.setPrefWidth(551);
		button.setPrefHeight(52);
		button.setStyle("-fx-background-color:  #44475A;");
		button.setStyle("-fx-text-fill: #f8f8f2;");
	}

	public Task(int inputNumber, String inputString) {
		EstPomodoros = inputNumber;
		setNote(inputString);
		button = new Button(inputString);
		button.setPrefWidth(551);
		button.setPrefHeight(52);
	}
	
	public void setEstPomodoros(int inputNumber) {
		EstPomodoros = inputNumber;
	}

	public void setNote(String inputString) {
		note = inputString;
	}

	public int getEstPomodoros() {
		return EstPomodoros;
	}

	public String getNote() {
		return note;
	}

	public Button getButton() {
		return button;
	}

	public void setPomoCompleted(int inputNumber) {
		pomoCompleted = inputNumber;
	}

	public int getPomoCompleted() {
		return pomoCompleted;
	}
	public void incrementPomoCompleted() {
		 pomoCompleted++;
		 setButtonText(this.note);
	}

	public void setButtonText(String inputString) {
		button.setText(inputString + "(" + pomoCompleted + "/" + EstPomodoros + ")");
	}
	
	public String toString() {
		return "Task(Est: " + EstPomodoros + ", Note: " + note + ", Button)";
	}
	
}
