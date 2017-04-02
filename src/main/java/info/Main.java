package info;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("/infoPanel.fxml"));
				primaryStage.setTitle("Info Display");
				primaryStage.setMinWidth(700);
				primaryStage.setMinHeight(600);
				Scene scene = new Scene(root, 800, 600);
				scene.getStylesheets().add("/Style.css");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}