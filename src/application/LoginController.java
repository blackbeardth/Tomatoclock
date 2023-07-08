package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class LoginController implements Initializable {
	// Buttons
	@FXML
	private Button loginButton, registerButton;

	// Labels
	@FXML
	private Label errorMessage;

	// Other elements
	@FXML
	private CheckBox keepLoggedInCheckbox;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private ImageView closeButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeButton.setImage(new Image(System.getProperty("user.dir") + "/resources/close_icon.png"));

		loginButton.setOnMouseEntered(e -> loginButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size:18.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		loginButton.setOnMouseExited(e -> loginButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size:18.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		registerButton.setOnMouseEntered(e -> registerButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size:18.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));
		registerButton.setOnMouseExited(e -> registerButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size:18.0px; -fx-font-family: Calibri;-fx-background-radius: 10"));

	}

	@FXML
	private void loginClicked() {
			
		String username = usernameField.getText();
		String password = passwordField.getText();
		User currentUser = null;

		// Resets error message text if there was any
		errorMessage.setTextFill(Color.rgb(255, 85, 85)); // red
		errorMessage.setText("");
		
		if (Controller.umap == null) {
			errorMessage.setText("No such user exists");
			return;
		}

		// If no username or no password inserted, display error
		if (username.isEmpty() || password.isEmpty())
			errorMessage.setText("Please fill all fields.");

		// Tries to construct user with given username and password
		else {
			try {
				currentUser = new User(username);
			} catch (Exception e) {
				errorMessage.setText("Such user does not exist.");
				currentUser = null;
			}
		}

		// If user construction was successful & password is correct logs in the user
		if (currentUser != null) {
			if (!currentUser.passwordsMatch(password)) {
				errorMessage.setText("Wrong password");
			}
			else {
				// Saves user in the session
				Session.beginSession(currentUser);
				// Updates keepLoggedIn variable
				currentUser.setKeepLoggedIn(keepLoggedInCheckbox.isSelected());

				// display login message
				errorMessage.setTextFill(Color.rgb(80, 250, 123)); // green
				errorMessage.setText("Login Successful");
			}
		}
	}

	@FXML
	private void registerClicked() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			
			stage.initOwner(((Stage) loginButton.getScene().getWindow()).getOwner());
			stage.setTitle("Registration");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
			stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
			stage.setOnShown(event -> {
				Window window = loginButton.getScene().getWindow();
				((Stage) window).close();
			});
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();

		} catch (Exception e) {
			System.out.println("Exception whilst changing scene from Login to Register by registerButton.");
		}
	}

	@FXML
	private void keyPressed(KeyEvent event) {
		// Enter pressed
		if (event.getCode().toString().equals("ENTER"))
			loginClicked();
	}

	@FXML
	private void closeClicked(MouseEvent event) {
		Window window = registerButton.getScene().getWindow();
		((Stage) window).close();
	}
}
