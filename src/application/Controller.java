package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Controller implements Initializable {
	@FXML
	private Label timerLabel, messageLabel;

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private VBox myVBox;

	@FXML
	private Button pomodoroButton, sbreakButton, lbreakButton, startButton, pomodoroCountButton, addTaskButton,
			reportButton, loginButton, settingsButton;

	@FXML
	private MenuButton menuButton;

	@FXML
	private ImageView tomatoImageView, taskImageView, closeButton, minimizeButton;

	private int POMODORO; // default: 25 minutes
	private int SBREAK; // default: 5 minutes
	private int LBREAK; // default: 15 minutes
	private int longBreakInterval;// default: 4

	private TaskList taskList = new TaskList();
	private ButtonList buttonList = new ButtonList();
	public static HashMap<Integer, User> umap = null;
	private LinkedHashMap<Integer, Task> map = taskList.getTaskList();
	private LinkedHashMap<Integer, Button> bmap = buttonList.getButtonList();

	private Timeline timeline;
	private Task currentTask = null;

	private int mode = 1; // 1 -> pomodoro, 2 -> short break, 3 -> long break
	private int secondsRemaining;
	private boolean startHasBeenClicked;
	private int pomoCount = 0; // number of pomodoros
	private boolean autoBreaks;
	private boolean autoPomodoros;
	private int secondsElapsedWorking = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set icon path
		tomatoImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/tomato.png"));
		taskImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/schedule_icon.png"));
		closeButton.setImage(new Image(System.getProperty("user.dir") + "/resources/close_icon.png"));
		minimizeButton.setImage(new Image(System.getProperty("user.dir") + "/resources/minimize_icon.png"));

		// set menu button icon
		ImageView imageView = new ImageView(new Image(System.getProperty("user.dir") + "/resources/more_icon.png"));
		imageView.setFitWidth(menuButton.getWidth()); // set the size of the image
		imageView.setFitHeight(menuButton.getHeight());
		menuButton.setGraphic(imageView);

		// change bakchround when mouse enters buttons
		reportButton.setOnMouseEntered(e -> reportButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		reportButton.setOnMouseExited(e -> reportButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		loginButton.setOnMouseEntered(e -> loginButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		loginButton.setOnMouseExited(e -> loginButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		settingsButton.setOnMouseEntered(e -> settingsButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		settingsButton.setOnMouseExited(e -> settingsButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));

		// checks if "data.ser" file exists, if not creates user list
		File file = new File(System.getProperty("user.dir") + "/appdata/data.ser");
		if (file.exists()) {
			// deserialize data
			umap = UserList.deserialize();
		} else {
			umap = new HashMap<Integer, User>();
		}

		// loads the settings from previous session

		boolean keepLoggedIn = false;
		int UID = 0; // to store UID of last logged in user
		try {
			Object[] ob = Config.loadSettings();
			POMODORO = (int) ob[0];
			SBREAK = (int) ob[1];
			LBREAK = (int) ob[2];
			longBreakInterval = (int) ob[3];
			autoBreaks = (boolean) ob[4];
			autoPomodoros = (boolean) ob[5];
			keepLoggedIn = (boolean) ob[6];
			UID = (int) ob[7];
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// if previously logged in & keepLoggedIn was enabled
		if (keepLoggedIn == true && UID != 0) {
			Session.beginSession(umap.get(UID));
			Session.getSession().setKeepLoggedIn(keepLoggedIn);
		}

		// set seconds remaining & update label
		secondsRemaining = POMODORO;
		updateTimerLabel(secondsRemaining);

		// System.out.println(Session.getSession());
	}

	@FXML
	private void start(ActionEvent e) {
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

	private void startTimer() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), new javafx.event.EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (secondsRemaining > 0) {
					secondsRemaining--;
					updateTimerLabel(secondsRemaining);
					if (mode == 1)
						secondsElapsedWorking++;
					if (secondsRemaining == 0 && mode == 1) {
						updatePomodoroCount();
						if (currentTask != null) {
							currentTask.incrementPomoCompleted(bmap.get(currentTask.getTaskID()));
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

	private void stopTimer() {
		timeline.pause();
		startHasBeenClicked = false; // reset mode
		startButton.setText("START");
	}

	private void resetTimer(int defaultTime) {
		if (timeline == null)
			return;
		timeline.stop();
		startButton.setText("START");
		updateTimerLabel(defaultTime);
	}

	private void updateTimerLabel(int time) {
		int minutes = time / 60;
		int seconds = time % 60;

		String timeString = String.format("%02d:%02d", minutes, seconds);
		timerLabel.setText(timeString);
	}

	private void updatePomodoroCount() {
		pomoCount++;
		pomodoroCountButton.setText("#" + pomoCount);
	}

	@FXML
	private void resetPomodoroCount() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("This will refresh the pomodoro count");
		alert.setContentText("Continue?");

		DialogPane dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK) {
			pomoCount = 0;
			pomodoroCountButton.setText("#" + pomoCount);
		}
	}

	private void playClickSound() {
		Media sound = new Media(new File("./resources/click.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.seek(Duration.ZERO);
		mediaPlayer.play();
	}

	private void playAlarmSound() {
		Media sound = new Media(new File("./resources/alarm.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.seek(Duration.ZERO);
		mediaPlayer.play();
	}

	@FXML
	private void modePomodoro() {
		if (secondsRemaining != 0) { // the user is not swithcing , it is auto-switching
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: #282A36"); // to highlight the currently selected mode
		sbreakButton.setStyle("-fx-background-color: transparent");
		lbreakButton.setStyle("-fx-background-color: transparent");
		mainAnchorPane.setStyle(
			"-fx-background-color: #282A36; -fx-border-color:  #bd93f9; -fx-border-width: 3;"
		);
		myVBox.setStyle("-fx-background-color: #282A36;");

		mode = 1;
		secondsRemaining = POMODORO;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(POMODORO);
	}

	@FXML
	private void modeShortBreak() {
		if (secondsRemaining != 0) {
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: transparent");
		sbreakButton.setStyle("-fx-background-color: #282A36");
		lbreakButton.setStyle("-fx-background-color: transparent");
		mainAnchorPane.setStyle(
			"-fx-background-color: #36464e; -fx-border-color:  #bd93f9; -fx-border-width: 3;"
		);
		myVBox.setStyle("-fx-background-color: #36464e;");
		
		mode = 2;
		secondsRemaining = SBREAK;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(SBREAK);
	}

	@FXML
	private void modeLongBreak() {
		if (secondsRemaining != 0) {
			if (!switchModeConfirmation())
				return;
		}

		pomodoroButton.setStyle("-fx-background-color: transparent");
		sbreakButton.setStyle("-fx-background-color: transparent");
		lbreakButton.setStyle("-fx-background-color: #282A36");
		mainAnchorPane.setStyle(
			"-fx-background-color: #2b5470; -fx-border-color:  #bd93f9; -fx-border-width: 3;"
		);
		myVBox.setStyle("-fx-background-color: #2b5470;");
		
		mode = 3;
		secondsRemaining = LBREAK;
		startHasBeenClicked = false;
		resetTimer(secondsRemaining);
		updateTimerLabel(LBREAK);
	}

	private boolean switchModeConfirmation() {

		// if timer is not running, return true
		if (!timerIsRunning())
			return true;

		// timer is running
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Timer is still running!");
		alert.setContentText("Are you sure you want to switch?");
		alert.initOwner(mainAnchorPane.getScene().getWindow());

		DialogPane dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image icon = new Image(System.getProperty("user.dir") + "/resources/warning_icon.png");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(icon);

		if (alert.showAndWait().get() == ButtonType.OK)
			return true;

		return false;
	}

	@FXML
	private void report() throws IOException {
		User currentUser = Session.getSession();

		if (currentUser == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Feature unavaliable");
			alert.setHeaderText("You have to login to use this feature");
			alert.initOwner(mainAnchorPane.getScene().getWindow());

			DialogPane dialog = alert.getDialogPane();
			dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Image icon = new Image(System.getProperty("user.dir") + "/resources/info_icon.png");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(icon);

			alert.showAndWait();
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Report.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("graph.css").toExternalForm());

			Image icon = new Image(System.getProperty("user.dir") + "/resources/schedule_icon.png");
			stage.getIcons().add(icon);
			stage.initOwner(mainAnchorPane.getScene().getWindow());
			stage.setTitle("Report");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
			stage.initStyle(StageStyle.DECORATED); // to make the dialog unmovable
			stage.setOnCloseRequest(event -> {
				return;
			});
			stage.setScene(scene);
			stage.showAndWait();
		}
	}

	@FXML
	private void login() throws IOException {

		User currentUser = Session.getSession();

		if (currentUser != null) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Login/Logout");
			alert.setHeaderText("You are logged in as " + Session.getSession().getUsername());
			alert.setContentText("Do you want to logout?");
			alert.initOwner(mainAnchorPane.getScene().getWindow());

			DialogPane dialog = alert.getDialogPane();
			dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Image icon = new Image(System.getProperty("user.dir") + "/resources/sign_out_icon.png");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(icon);

			if (alert.showAndWait().get() == ButtonType.OK)
				logout();
			else
				return;
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);

			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);

			Image icon = new Image(System.getProperty("user.dir") + "/resources/sign_out_icon.png");
			stage.getIcons().add(icon);
			stage.initOwner(mainAnchorPane.getScene().getWindow());
			stage.setTitle("Login");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
			stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
			stage.setOnCloseRequest(event -> {
				return;
			});
			stage.setScene(scene);
			stage.show();
		}
	}

	private void logout() {
		Session.endSession();
	}

	@FXML
	private void settings() throws Exception {

		// check if timer is running
		boolean flag = timerIsRunning();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));

		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setTitle("Settings");
		stage.setResizable(false);// dialog is non-resizable
		stage.initOwner((Stage) mainAnchorPane.getScene().getWindow());
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // dialog is unmovable
		stage.setOnCloseRequest(event -> {
			return;
		});
		stage.setScene(scene);
		SettingsController controller = loader.getController();

		// set default field values to current values
		controller.setPomoTextField(POMODORO / 60);
		controller.setSbreakTextField(SBREAK / 60);
		controller.setLbreakTextField(LBREAK / 60);
		controller.setLbreakInterval(longBreakInterval);

		// set checkbox to on/off based on previous response
		controller.setAutoBreaks(autoBreaks);
		controller.setAutoPomodoros(autoPomodoros);

		stage.showAndWait();
		
		// if cancel was pressed
		if (controller.getCancelStatus())
			return;

		POMODORO = (int) controller.getPomoTextField() * 60;
		SBREAK = (int) controller.getsbreakTextField() * 60;
		LBREAK = (int) controller.getlbreakTextField() * 60;
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
		if (!flag) {
			if (mode == 1) {
				secondsRemaining = POMODORO;
				updateTimerLabel(POMODORO);
			} else if (mode == 2) {
				secondsRemaining = SBREAK;
				updateTimerLabel(SBREAK);
			} else if (mode == 3) {
				secondsRemaining = LBREAK;
				updateTimerLabel(LBREAK);
			}
		}
	}

	private boolean timerIsRunning() {
		boolean timerIsRunning = true;

		if ((mode == 1 && secondsRemaining == POMODORO)) {
			timerIsRunning = false;
		} else if ((mode == 2 && secondsRemaining == SBREAK)) {
			timerIsRunning = false;
		} else if ((mode == 3 && secondsRemaining == LBREAK)) {
			timerIsRunning = false;
		}

		return timerIsRunning;
	}

	private Integer genRandomID() {
		Random random = new Random();
		Integer randomNumber = random.nextInt(900000000) + 100000000;
		return randomNumber;
	}

	@FXML
	private void addTask(ActionEvent e) throws Exception {
		Integer taskID = genRandomID();
		Task ob = new Task(taskID);
		Button button = new Button();
		map.put(taskID, ob);
		bmap.put(taskID, button);

		// taskNo++;

		if (currentTask == null)
			currentTask = map.get(taskID);

		openTaskDialog(taskID);
	}

	private void openTaskDialog(Integer taskID) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDialog.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.initOwner((Stage) mainAnchorPane.getScene().getWindow());
		stage.setTitle("New Task");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
		stage.setOnCloseRequest(event -> {
			map.remove(taskID);
			return;
		});
		stage.setScene(scene);
		TaskDialogController controller = loader.getController();
		controller.setTask((Task) map.get(taskID), map, bmap);
		stage.centerOnScreen();
		stage.showAndWait();

		if (map.get(taskID) != null)
			displayTask(taskID);
	}

	private void displayTask(Integer taskID) {
		if (map.get(taskID) == null)
			return;

		Button button = bmap.get(taskID);

		if (currentTask != null && currentTask.equals(map.get(taskID)))
			messageLabel.setText(map.get(taskID).getNote());

		// styling
		button.setPrefWidth(551);
		button.setPrefHeight(52);
		button.setStyle("-fx-background-color:  #44475A;");
		button.setStyle("-fx-text-fill: #f8f8f2;");

		button.setOnMouseEntered(e -> {
			button.setCursor(Cursor.HAND);
			button.setStyle(
					"-fx-background-color: #ff79c6; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
		});
		button.setOnMouseExited(e -> {
			button.setCursor(Cursor.DEFAULT);
			button.setStyle(
					"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
		});
		button.setStyle(
				"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");

		myVBox.getChildren().add(button);

		// when task button is pressed
		button.setOnAction(e -> {
			currentTask = map.get(taskID);
			messageLabel.setText(map.get(taskID).getNote());
		});
	}

	@FXML
	private void modifyCurrentTask() throws Exception {

		if (currentTask == null)
			return;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDialog.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);

		stage.setTitle("New Task");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
		stage.initOwner((Stage) mainAnchorPane.getScene().getWindow());
		stage.setOnCloseRequest(event -> {
			return;
		});
		stage.setScene(scene);
		TaskDialogController controller = loader.getController();

		controller.setTask(currentTask, map, bmap);
		controller.setNoteTextFieldValue(currentTask.getNote());
		controller.setPomoSpinnerValue(currentTask.getEstPomodoros());

		stage.showAndWait();

		currentTask.setNote(controller.getNote());
		currentTask.setEstPomodoros(controller.getEst());
		messageLabel.setText(currentTask.getNote());
	}

	@FXML
	private void clearCurrentTask() {
		if (currentTask == null)
			return;

		Integer key = null;
		for (Entry<Integer, Task> entry : this.map.entrySet()) {
			if (entry.getValue().equals(currentTask)) {
				key = entry.getKey();
				break;
			}
		}
		myVBox.getChildren().remove(bmap.get(currentTask.getTaskID()));
		map.remove(key);
		bmap.remove(key);
		// taskNo--;

		// set new current task
		if (map.isEmpty()) {
			currentTask = null;
			messageLabel.setText("Time to focus!");
		} else {
			Map.Entry<Integer, Task> firstEntry = map.entrySet().iterator().next();
			currentTask = firstEntry.getValue();
			messageLabel.setText(currentTask.getNote());
		}
	}

	@FXML
	private void clearAllTasks() {
		if (map.size() == 0) // no task
			return;

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.initOwner(mainAnchorPane.getScene().getWindow());
		alert.setHeaderText("This will clear all tasks!");
		alert.setContentText("Are you sure you want to continue?");

		Image icon = new Image(System.getProperty("user.dir") + "/resources/warning_icon.png");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(icon);

		DialogPane dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK) {
			map.clear();
			bmap.clear();
			// taskNo = 0;
			myVBox.getChildren().clear();
			messageLabel.setText("Time to focus!");
		}
	}

	@FXML
	private void markAsCompleted() {
		if (currentTask == null)
			return;
		Text buttonText = (Text) bmap.get(currentTask.getTaskID()).getGraphic();
		buttonText.setStrikethrough(true);
		buttonText.setUnderline(false);
	}

	@FXML
	private void markAsIncomplete() {
		if (currentTask == null)
			return;
		Text buttonText = (Text) bmap.get(currentTask.getTaskID()).getGraphic();
		buttonText.setStrikethrough(false);
		buttonText.setUnderline(false);
	}

	@FXML
	private void saveAsTemplate() {
		if (map.isEmpty())
			return;

		TextInputDialog textDialog = new TextInputDialog();

		textDialog.setTitle("Save template");
		textDialog.setHeaderText("Enter name for template");
		textDialog.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		textDialog.initOwner(mainAnchorPane.getScene().getWindow());

		Image icon = new Image(System.getProperty("user.dir") + "/resources/tasks_icon.png");
		Stage stage = (Stage) textDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(icon);
		stage.centerOnScreen();

		GridPane contentPane = (GridPane) textDialog.getDialogPane().getContent();
		contentPane.setPrefSize(350, 50);

		Optional<String> result = textDialog.showAndWait();

		// Handle the user's response
		if (result.isPresent()) {
			// Regular expression to match only alphabets and numbers
			String pattern = "^[a-zA-Z0-9]*$";

			// Check if the string matches the regular expression
			if (result.get().matches(pattern)) {
				taskList.serialize(result.get());
			} else
				return;
		}
	}

	@FXML
	private void addFromTemplate() {
		// Define the directory path
		String directoryPath = System.getProperty("user.dir") + "/appdata/templates/";

		// Create a File object for the directory
		File directory = new File(directoryPath);

		// Get a list of all files in the directory
		File[] files = directory.listFiles();

		// Create a list to store the names of text files
		List<String> serFileNames = new ArrayList<>();

		// Loop through the files and add the names of text files to the list
		for (File file : files) {
			if (file.isFile() && file.getName().toLowerCase().endsWith(".ser")) {
				int index = file.getName().indexOf('.');
				serFileNames.add(file.getName().substring(0, index));
			}
		}

		ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(null, serFileNames);
		choiceDialog.setTitle("Add from template");
		choiceDialog.setHeaderText("Select template");
		choiceDialog.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image icon = new Image(System.getProperty("user.dir") + "/resources/add_icon.png");
		Stage stage = (Stage) choiceDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(icon);
		stage.centerOnScreen();

		GridPane contentPane = (GridPane) choiceDialog.getDialogPane().getContent();
		contentPane.setPrefSize(350, 50);

		Optional<String> result = choiceDialog.showAndWait();

		if (result.isPresent()) {
			LinkedHashMap<Integer, Task> newTaskList = taskList.deserialize(result.get());
			loadNewTaskList(newTaskList);
		}
	}

	private void loadNewTaskList(LinkedHashMap<Integer, Task> newTaskList) {
		if (newTaskList == null)
			return;

		// clear previous tasks
		map.clear();
		bmap.clear();
		myVBox.getChildren().clear();

		// set current linkedHashMap
		map = newTaskList;

		// display imported template
		Set<Entry<Integer, Task>> set = map.entrySet();
		Iterator<Entry<Integer, Task>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Integer, Task> entry = (Map.Entry<Integer, Task>) iterator.next();

			// initialize bmap
			bmap.put(entry.getKey(), new Button());

			Button button = bmap.get(entry.getKey());

			// styling
			button.setPrefWidth(551);
			button.setPrefHeight(52);
			button.setStyle("-fx-background-color:  #44475A;");
			button.setStyle("-fx-text-fill: #f8f8f2;");

			button.setOnMouseEntered(e -> {
				button.setCursor(Cursor.HAND);
				button.setStyle(
						"-fx-background-color: #ff79c6; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
			});
			button.setOnMouseExited(e -> {
				button.setCursor(Cursor.DEFAULT);
				button.setStyle(
						"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");
			});
			button.setStyle(
					"-fx-background-color: #6272a4; -fx-text-fill: #f8f8f2; -fx-font-size:25.0px; -fx-font-family: Calibri;");

			// set button text
			map.get(entry.getKey()).setButtonText(map.get(entry.getKey()).getNote(), button);

			// display new bbuttons
			myVBox.getChildren().add(button);

			// when task button is pressed
			button.setOnAction(e -> {
				currentTask = map.get(entry.getKey());
				messageLabel.setText(map.get(entry.getKey()).getNote());
			});
		}

		// set new current task
		if (map.isEmpty()) {
			currentTask = null;
			messageLabel.setText("Time to focus!");
		} else {
			Map.Entry<Integer, Task> firstEntry = map.entrySet().iterator().next();
			currentTask = firstEntry.getValue();
			messageLabel.setText(currentTask.getNote());
		}
	}

	private void storeSessionDuration() {

		User user = Session.getSession();
		if (user == null) {
			return;
		}
		// store total hours
		// Session.getSession().setTotalHoursFocused(secondsElapsedWorking / 60);

		// store hours focused in this session
		LinkedHashMap<LocalDate, Integer> hmap = user.getHoursFocusedMap();

		// if size >= 7
		if (hmap.size() >= 7) {
			// get the key of the first element
			LocalDate firstKey = hmap.keySet().iterator().next();

			// remove the first element
			hmap.remove(firstKey);
		}

		// store / update hours focused
		if (hmap.isEmpty()) {
			hmap.put(LocalDate.now(), (secondsElapsedWorking / 3600));
		} else {
			Integer hrs = hmap.get(LocalDate.now());
			if (hrs == null)
				hmap.put(LocalDate.now(), (secondsElapsedWorking / 3600));
			else
				hmap.replace(LocalDate.now(), hrs + (secondsElapsedWorking / 3600));
		}
	}

	@FXML
	private void closeClicked(MouseEvent e) {
		// get keepLoggedIn status
		boolean keepLoggedIn = false;
		int UID = 0;

		if (Session.getSession() != null) {
			keepLoggedIn = Session.getSession().getKeepLoggedIn();
			if (keepLoggedIn == true)
				UID = Session.getSession().getId();
		} else {
			keepLoggedIn = false;
			UID = 0;
		}

		// save current settings to config
		try {
			Config.saveSettings(new Object[] { POMODORO, SBREAK, LBREAK, longBreakInterval, autoBreaks, autoPomodoros,
					keepLoggedIn, UID });
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// store hours focused in this session
		storeSessionDuration();

		// serialize the user list
		UserList.serialize();

		e.consume();
		System.exit(0);
	}

	@FXML
	private void minimizeClicked(MouseEvent e) {
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		stage.setIconified(true);
	}
}
