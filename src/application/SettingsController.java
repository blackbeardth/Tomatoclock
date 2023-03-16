package application;

import java.awt.Desktop;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SettingsController {
	@FXML
	private TextField pomoTextField, sbreakTextField, lbreakTextField, lbreakInterval;

	@FXML
	private CheckBox autoBreaks, autoPomodoros;

	@FXML
	private Hyperlink githubLink;

	@FXML
	private Button saveButton;

	public void showGithubLink() throws IOException {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(java.net.URI.create("https://github.com/blackbeardth"));
	}

	public void save() {
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

	public void setPomoTextField(int inputNumber) {
		pomoTextField.setText(Integer.toString(inputNumber));
	}

	public void setsbreakTextField(int inputNumber) {
		sbreakTextField.setText(Integer.toString(inputNumber));
	}

	public void setlbreakTextField(int inputNumber) {
		lbreakTextField.setText(Integer.toString(inputNumber));
	}

	public void setlbreakInterval(int inputNumber) {
		lbreakInterval.setText(Integer.toString(inputNumber));
	}

	public int getPomoTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			if (Integer.parseInt(pomoTextField.getText()) > 60)
				return 59;
			return Integer.parseInt(pomoTextField.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public int getsbreakTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			if (Integer.parseInt(pomoTextField.getText()) > 60)
				return 59;
			return Integer.parseInt(sbreakTextField.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public int getlbreakTextField() {
		try {
			if (pomoTextField.getText() == "")
				return 0;
			if (Integer.parseInt(pomoTextField.getText()) > 60)
				return 59;
			return Integer.parseInt(lbreakTextField.getText());
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
}
