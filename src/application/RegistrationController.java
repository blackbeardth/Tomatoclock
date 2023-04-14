package application;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class RegistrationController implements Initializable {
	@FXML
	private TextField usernameField;
	
	@FXML
	private PasswordField passwordField, repeatPasswordField;
	
	@FXML
	private Button registerButton, goToLoginButton;
	
	@FXML
	private Label errorMessage;
	
	@FXML
	private ImageView closeButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeButton.setImage(new Image(System.getProperty("user.dir") + "/resources/close_icon.png"));
		
		registerButton.setOnMouseEntered(e -> registerButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 18.0px; -fx-background-radius: 10; -fx-font-family: Calibri"));
		registerButton.setOnMouseExited(e -> registerButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 18.0px; -fx-background-radius: 10; -fx-font-family: Calibri"));
		goToLoginButton.setOnMouseEntered(e -> goToLoginButton.setStyle(
				"-fx-background-color: #bd93f9; -fx-text-fill: #f8f8f2; -fx-font-size: 18.0px; -fx-background-radius: 10; -fx-font-family: Calibri"));
		goToLoginButton.setOnMouseExited(e -> goToLoginButton.setStyle(
				"-fx-background-color: #44475a; -fx-text-fill: #f8f8f2; -fx-font-size: 18.0px; -fx-background-radius: 10; -fx-font-family: Calibri"));
	}

	@FXML
	private void registerClicked() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		String passwordRepeat = repeatPasswordField.getText();
		
		errorMessage.setTextFill(Color.rgb(255, 85, 85)); // red
		errorMessage.setText("");
		boolean canRegister = true;
		
		// check if user list is null
		if (Controller.umap == null)
			Controller.umap = new HashMap<Integer, User>();

		// Checks if all fields have been filled
		if (username.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
			canRegister = false;
			errorMessage.setText("Please fill all fields.");
		}

		// Checks if username is unique
		if (!User.usernameAvailable(username)) {
			canRegister = false;
			errorMessage.setText("Username is taken. Please choose another.");
		}

		// Checks if password is strong
		if (canRegister && !User.isPasswordStrong(password)) {
			canRegister = false;
			errorMessage.setText("Password has to have: \n-At least 8 symbols;"
					+ "\n-A lowercase letter;\n-An uppercase letter;\n-A number.");
		}

		// Checks if passwords match
		if (canRegister && !password.equals(passwordRepeat)) {
			canRegister = false;
			errorMessage.setText("Passwords do not match.");
		}

		// If above checks are passed, registers the user and forwards them to the login
		// screen
		if (canRegister) {
			// Adds user to the database
			Integer id = generateID();
			User newUser = new User(username, password, id);
			newUser.addUser(newUser);

			// display login message
			errorMessage.setTextFill(Color.rgb(80, 250, 123));
			errorMessage.setText("New User created successfully");
			
			// System.out.println("Username: " + username + "\nPassword: " + password);
		}
	}

	@FXML
	private void goToLogin() throws IOException, InterruptedException {
		// open login window
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		Scene scene = new Scene(root);

		Image icon = new Image(System.getProperty("user.dir") + "/resources/sign_out_icon.png");
		stage.getIcons().add(icon);
		stage.setTitle("Login");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL); // dialog always remains on top
		stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
		stage.setOnShown(event -> {
			Window window = registerButton.getScene().getWindow();
			((Stage) window).close();
		});
		stage.setScene(scene);
		stage.show();
	}

	private Integer generateID() {
		Random random = new Random();
		Integer randomNumber = random.nextInt(900000000) + 100000000;
		return randomNumber;
	}

	@FXML
	private void keyPressed(KeyEvent event) {
		// Enter pressed
		if (event.getCode().toString().equals("ENTER"))
			registerClicked();
	}
	@FXML
	private void closeClicked(MouseEvent event) {
		Window window = registerButton.getScene().getWindow();
		((Stage) window).close();
	}

}
