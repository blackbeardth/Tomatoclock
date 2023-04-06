// Tomatoclock

package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	
	private double xOffset = 0;
    private double yOffset = 0;

	private DialogPane dialog;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
	    
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			
			Image icon = new Image(System.getProperty("user.dir") + "/resources/full-tomato.png");
			stage.getIcons().add(icon);
			stage.setResizable(false);
			stage.setTitle("Tomatoclock");
			stage.initStyle(StageStyle.UNDECORATED); // to make the dialog unmovable
			
			 root.setOnMousePressed(new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent event) {
		                xOffset = event.getSceneX();
		                yOffset = event.getSceneY();
		            }
		        });
		        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent event) {
		                stage.setX(event.getScreenX() - xOffset);
		                stage.setY(event.getScreenY() - yOffset);
		            }
		        });
			
			stage.setScene(scene);
			stage.show();

//			stage.setOnCloseRequest(event -> {
//				event.consume();
//				logout(stage);
//			});
//			stage.setOnCloseRequest(event -> {
//				// serialize user list
//				UserList.serialize();
//				System.out.println("Hello");
//				System.out.println(Controller.umap);
//				event.consume();
//				System.exit(0);
//			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logout(Stage stage) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You are about to log out!");
		alert.setContentText("Do you want to save before exiting?");

		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		if (alert.showAndWait().get() == ButtonType.OK) {
			System.out.println("you successfully logged out!");
			stage.close();
		} else
			stage.close();
	}
}
