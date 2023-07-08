package application;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TaskDialogController implements Initializable {

	@FXML
	private Button saveButton, cancelButton;

	@FXML
	private TextField noteTextField;

	@FXML
	private Spinner<Integer> pomoSpinner;
	
	private final int maxLength = 30;
	private String note;
	private int est;
	private Task task;
	private LinkedHashMap<Integer, Task> map;
	private LinkedHashMap<Integer, Button> bmap;
	private SpinnerValueFactory<Integer> valueFactory;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60);
		valueFactory.setValue(1);

		pomoSpinner.setValueFactory(valueFactory);

		saveButton.setOnMouseEntered(e -> saveButton.setStyle(
				"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		saveButton.setOnMouseExited(e -> saveButton.setStyle(
				"-fx-background-color:  #44475a; -fx-text-fill: #f8f8f2; -fx-font-size:17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
				"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
				"-fx-background-color:  #44475a; -fx-text-fill: #f8f8f2; -fx-font-size:17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
	}
	
	@FXML
	private void save() {
		if (noteTextField.getText() != "") {
			note = noteTextField.getText();
			if (note.length() > maxLength) {
				note = note.substring(0, maxLength);
			}
			est = pomoSpinner.getValue();
		} else
			return;

		task.setEstPomodoros(est);
		task.setNote(note);
		task.setButtonText(note, bmap.get(task.getTaskID())); ///
		// System.out.println(this.task);

		Window window = saveButton.getScene().getWindow();
		((Stage) window).close();
	}
	
	@FXML
	private void cancel() throws Throwable {
		Integer key = null;
		for (Entry<Integer, Task> entry: this.map.entrySet()) {
			if (entry.getValue().equals(task)) {
				key = entry.getKey();
				break;
			}
		}
		this.map.remove(key);
		Window window = cancelButton.getScene().getWindow();
		((Stage) window).close();
	}

	public String getNote() {
		return note;
	}

	public int getEst() {
		return est;
	}

	public void setTask(Task t, LinkedHashMap<Integer, Task> map, LinkedHashMap<Integer, Button> bmap) {
		this.task = t;
		this.map = map;
		this.bmap = bmap;
	}
	
	public void setNoteTextFieldValue(String inputText) {
		noteTextField.setText(inputText);
	}
	
	public void setPomoSpinnerValue(int inputNumber) {
		valueFactory.setValue(inputNumber);
	}
}
