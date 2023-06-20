package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SettingsController implements Initializable {
	@FXML
	private TextField pomoTextField, sbreakTextField, lbreakTextField, lbreakInterval;

	@FXML
	private CheckBox autoBreaks, autoPomodoros;

	@FXML
	private Hyperlink githubLink;

	@FXML
	private Button saveButton, cancelButton;

	@FXML
	private ImageView settingsImageView, timerImageView, copyrightImageView, aboutImageView, resetImageView; 
	
	private boolean cancel = false; // flag to store if cancel is pressed or not
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		settingsImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/settings_icon.png"));
		timerImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/timer_icon.png"));
		aboutImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/questionmark_icon.png"));
		copyrightImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/copyright_icon.png"));
		resetImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/reset_icon.png"));

		saveButton.setOnMouseEntered(e -> saveButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		saveButton.setOnMouseExited(e -> saveButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
		cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 17.0px; -fx-font-family: Calibri; -fx-background-radius: 10"));
	}

	@FXML
	private void showGithubLink() throws IOException {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(java.net.URI.create("https://github.com/chiragwad/Tomatoclock"));
	}

	@FXML
	private void save() {
		try {
			if (getPomoTextField() > 0 && getsbreakTextField() > 0 && getlbreakTextField() > 0
					&& getlbreakTextField() > 0) {
				Window window = saveButton.getScene().getWindow();
				((Stage) window).close();
			}
		} catch (Exception e) {
			return;
		}
	}
	
	@FXML
	private void cancel() {
		cancel = true;
		Window window = saveButton.getScene().getWindow();
		((Stage) window).close();
	}
	
	public boolean getCancelStatus() {
		return cancel;
	}

	public void setPomoTextField(int inputNumber) {
		pomoTextField.setText(Integer.toString(inputNumber));
	}

	public void setSbreakTextField(int inputNumber) {
		sbreakTextField.setText(Integer.toString(inputNumber));
	}

	public void setLbreakTextField(int inputNumber) {
		lbreakTextField.setText(Integer.toString(inputNumber));
	}

	public void setLbreakInterval(int inputNumber) {
		lbreakInterval.setText(Integer.toString(inputNumber));
	}

	public double getPomoTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			// if (Integer.parseInt(pomoTextField.getText()) > 60)
			// return 59;
			return Double.parseDouble(pomoTextField.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public double getsbreakTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			// if (Integer.parseInt(pomoTextField.getText()) > 60)
			// return 59;
			return Double.parseDouble(sbreakTextField.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public double getlbreakTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			// if (Integer.parseInt(pomoTextField.getText()) > 60)
			// return 59;
			return Double.parseDouble(lbreakTextField.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public int getlbreakInterval() throws Exception {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			return Integer.parseInt(lbreakInterval.getText());
		} catch (Exception e) {
			return -1;
		}
	}
	
	public boolean autoBreaksIsOn() {
		return autoBreaks.isSelected();
	}

	public boolean autoPomodorosIsOn() {
		return autoPomodoros.isSelected();
	}

	public void setAutoBreaks(Boolean flag) {
		if (flag)
			autoBreaks.setSelected(true);
		else
			autoBreaks.setSelected(false);
	}

	public void setAutoPomodoros(Boolean flag) {
		if (flag)
			autoPomodoros.setSelected(true);
		else
			autoPomodoros.setSelected(false);
	}
	
	@FXML
	private void reset() throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("This will reset all settings");
		alert.setContentText("Do you want to continue?");
		alert.initOwner(((Stage) saveButton.getScene().getWindow()).getOwner());

		DialogPane dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image icon = new Image(System.getProperty("user.dir") + "/resources/warning_icon.png");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(icon);

		if (alert.showAndWait().get() == ButtonType.OK) {
			Config.resetConfig();
			
			setPomoTextField(25);
			setSbreakTextField(5);
			setLbreakTextField(15);
			setLbreakInterval(4);
			setAutoBreaks(false);
			setAutoPomodoros(false);
		}
	}

}
