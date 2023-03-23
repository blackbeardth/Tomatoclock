package application;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Controller implements Initializable{
	@FXML
	private Label timerLabel, messageLabel;

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private VBox myVBox;

	@FXML
	private Button pomodoroButton, sbreakButton, lbreakButton, startButton, pomodoroCountButton, addTaskButton;
	
	@FXML
	private ImageView tomatoImageView;
	
	private int POMODORO = 1500; // default: 25 minutes
	private int SBREAK = 300; // default: 5 minutes
	private int LBREAK = 900; // default: 15 minutes
	private int longBreakInterval = 4;// default: 4

	static private TaskList taskList = new TaskList();
	static HashMap<String, Task> map = taskList.getTaskList();
	private Timeline timeline;
	private DialogPane dialog;
	private Task currentTask = null;

	private int mode = 1; // 1 -> pomodoro, 2 -> short break, 3 -> long break
	private int secondsRemaining = 1500;
	private boolean startHasBeenClicked = false;
	private int pomoCount = 0; // number of pomodoros
	private int taskNo = 0;
	private boolean autoBreaks = false;
	private boolean autoPomodoros = false;

	public void start(ActionEvent e) {
		playClickSound();

		// switch to a mode
		switch (mode) {
		default:
			if (!startHasBeenClicked) {
				// modePomodoro();
				startTimer();
			} else
				stopTimer();
			break;
		case 2:
			if (!startHasBeenClicked) {
				// modeShortBreak();
				startTimer();
			} else
				stopTimer();
			break;
		case 3:
			if (!startHasBeenClicked) {
				// modeLongBreak();
				startTimer();
			} else
				stopTimer();
			break;
		}
	}

	protected void startTimer() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), new javafx.event.EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (secondsRemaining > 0) {
					secondsRemaining--;
					updateTimerLabel(secondsRemaining);
					if (secondsRemaining == 0 && mode == 1) {
						updatePomodoroCount();
						if (currentTask != null) {
							currentTask.incrementPomoCompleted();
						}
					} else if (secondsRemaining == 0 && mode == 3) {
						modePomodoro();
						playAlarmSound();
						if (autoPomodoros)
							startTimer();
					}
				} else if (pomoCount % longBreakInterval == 0 && pomoCount != 0) {
					// long break
					modeLongBreak();
					playAlarmSound();
					if (autoBreaks)
						startTimer();
				} else if (mode == 1) {
					// short break if previously mode was 1
					modeShortBreak();
					playAlarmSound();
					if (autoBreaks)
						startTimer();
				} else {
					modePomodoro();
					playAlarmSound();
					if (autoPomodoros)
						startTimer();
				}
			}

		}));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		startHasBeenClicked = true; // set mode
		startButton.setText("PAUSE");
	}

	protected void stopTimer() {
		timeline.pause();
		startHasBeenClicked = false; // reset mode
		startButton.setText("START");
	}

	protected void resetTimer(int defaultTime) {
		if (timeline == null)
			return;
		timeline.stop();
		startButton.setText("START");
		updateTimerLabel(defaultTime);
	}

	protected void updateTimerLabel(int time) {
		int minutes = time / 60;
		int seconds = time % 60;

		String timeString = String.format("%02d:%02d", minutes, seconds);
		timerLabel.setText(timeString);
	}

	protected void updatePomodoroCount() {
		pomoCount++;
		pomodoroCountButton.setText("#" + pomoCount);
	}

	public void resetPomodoroCount() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("This will refresh the pomodoro count");
		alert.setContentText("Continue?");

		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK) {
			pomoCount = 0;
			pomodoroCountButton.setText("#" + pomoCount);
		}
	}
	
	protected void playClickSound() {
		Media sound = new Media(new File("./resources/click.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.seek(Duration.ZERO);
		mediaPlayer.play();
	}

	protected void playAlarmSound() {
		Media sound = new Media(new File("./resources/alarm.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.seek(Duration.ZERO);
		mediaPlayer.play();
	}

	public void modePomodoro() {
		if (secondsRemaining != 0) { // the user is not swithcing , it is auto-switching
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: #282A36"); // to highlight the currently selected mode
		sbreakButton.setStyle("-fx-background-color: transparent");
		lbreakButton.setStyle("-fx-background-color: transparent");

		mode = 1;
		secondsRemaining = POMODORO;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(POMODORO);
	}

	public void modeShortBreak() {
		if (secondsRemaining != 0) {
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: transparent");
		sbreakButton.setStyle("-fx-background-color: #282A36");
		lbreakButton.setStyle("-fx-background-color: transparent");

		mode = 2;
		secondsRemaining = SBREAK;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(SBREAK);
	}

	public void modeLongBreak() {
		if (secondsRemaining != 0) {
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: transparent");
		sbreakButton.setStyle("-fx-background-color: transparent");
		lbreakButton.setStyle("-fx-background-color: #282A36");

		mode = 3;
		secondsRemaining = LBREAK;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(LBREAK);
	}
	
	private boolean switchModeConfirmation() {

		// if timer is not running, return true
		if (mode == 1 && secondsRemaining == POMODORO)
			return true;
		else if (mode == 2 && secondsRemaining == SBREAK)
			return true;
		else if (mode == 3 && secondsRemaining == LBREAK)
			return true;

		// timer is running
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Timer is still running!");
		alert.setContentText("Are you sure you want to switch?");

		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK)
			return true;

		return false;
	}

	public void report() {
		//System.out.println("Report button pressed");
	}

	public void settings() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));

		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		
		stage.setTitle("Settings");
		stage.setResizable(false);// dialog is non-resizable
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // dialog is unmovable
		stage.setOnCloseRequest(event -> {
			return;
		});
		stage.setScene(scene);
		SettingsController controller = loader.getController();

		// set default field values to current values
		controller.setPomoTextField(POMODORO / 60);
		controller.setsbreakTextField(SBREAK / 60);
		controller.setlbreakTextField(LBREAK / 60);
		controller.setlbreakInterval(longBreakInterval);

		// set checkbox to on/off based on previous response
		controller.setAutoBreaks(autoBreaks);
		controller.setAutoPomodoros(autoPomodoros);

		stage.showAndWait();

		POMODORO = (int)controller.getPomoTextField() * 60;
		SBREAK = (int)controller.getsbreakTextField() * 60;
		LBREAK = (int)controller.getlbreakTextField() * 60;
		longBreakInterval = controller.getlbreakInterval();

		
		if (controller.autoBreaksIsOn() && autoBreaks == false)
			autoBreaks = true;
		if (!controller.autoBreaksIsOn() && autoBreaks == true)
			autoBreaks = false;

		if (controller.autoPomodorosIsOn() && autoPomodoros == false)
			autoPomodoros = true;
		if (!controller.autoPomodorosIsOn() && autoPomodoros == true)
			autoPomodoros = false;

		
		// update timer label and secondsRemaining when timer isn't running
		if ((mode == 1 && secondsRemaining == POMODORO) || (mode == 1 && secondsRemaining == 1500)) {
			secondsRemaining = POMODORO;
			updateTimerLabel(POMODORO);
		} else if ((mode == 2 && secondsRemaining == SBREAK) || (mode == 2 && secondsRemaining == 300)) {
			secondsRemaining = SBREAK;
			updateTimerLabel(SBREAK);
		} else if ((mode == 3 && secondsRemaining == SBREAK) || (mode == 3 && secondsRemaining == 900)) {
			secondsRemaining = LBREAK;
			updateTimerLabel(LBREAK);
		}
	}

	public void addTask(ActionEvent e) throws Exception {
		String objectName = "task" + taskNo++;
		Task ob = new Task();
		map.put(objectName, ob);

		if (currentTask == null)
			currentTask = map.get(objectName);

		openTaskDialog(objectName);
	}
	
	public void openTaskDialog(String name) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDialog.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		
		stage.initOwner(mainAnchorPane.getScene().getWindow());
		stage.setTitle("New Task");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
		stage.setOnCloseRequest(event -> {
			map.remove(name);
			return;
		});
		stage.setScene(scene);
		TaskDialogController controller = loader.getController();
		controller.setTask((Task) map.get(name));
		stage.showAndWait();
		
		stage.centerOnScreen();
		
		if (map.get(name) != null)
			displayTask(name);
	}
	
	protected void displayTask(String name) {
		if (map.get(name) == null)
			return;

		if (currentTask != null && currentTask.equals(map.get(name)))
			messageLabel.setText(map.get(name).getNote());

		// styling
		map.get(name).getButton().setOnMouseEntered(e -> {
			map.get(name).getButton().setCursor(Cursor.HAND);
			map.get(name).getButton().setStyle(
					"-fx-background-color: #ff79c6; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
		});
		map.get(name).getButton().setOnMouseExited(e -> {
			map.get(name).getButton().setCursor(Cursor.DEFAULT);
			map.get(name).getButton().setStyle(
					"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
		});
		map.get(name).getButton().setStyle(
				"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");

		myVBox.getChildren().add(map.get(name).getButton());

		// when task button is pressed
		map.get(name).getButton().setOnAction(e -> {
			currentTask = map.get(name);
			messageLabel.setText(map.get(name).getNote());
		});
	}

	public void modifyCurrentTask() throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDialog.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		
		stage.setTitle("New Task");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
		stage.setOnCloseRequest(event -> {
			return;
		});
		stage.setScene(scene);
		TaskDialogController controller = loader.getController();

		controller.setTask(currentTask);
		controller.setNoteTextFieldValue(currentTask.getNote());
		controller.setPomoSpinnerValue(currentTask.getEstPomodoros());

		stage.showAndWait();

		currentTask.setNote(controller.getNote());
		currentTask.setEstPomodoros(controller.getEst());
		messageLabel.setText(currentTask.getNote());
	}

	public void clearCurrentTask() {
		if (currentTask == null)
			return;

		String key = null;
		for (Entry<String, Task> entry : Controller.map.entrySet()) {
			if (entry.getValue().equals(currentTask)) {
				key = entry.getKey();
				break;
			}
		}
		myVBox.getChildren().remove(currentTask.getButton());
		map.remove(key);
		taskNo--;
		messageLabel.setText("Time to focus!");
	}

	public void clearAllTasks() {
		if (map.size() == 0) // no task
			return;

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("This will clear all tasks!");
		alert.setContentText("Are you sure you want to continue?");

		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK) {
			map.clear();
			taskNo = 0;
			myVBox.getChildren().clear();
			messageLabel.setText("Time to focus!");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tomatoImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/tomato.png"));
	}

}
