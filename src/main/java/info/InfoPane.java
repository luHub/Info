package info;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class InfoPane {

	public Pane load() {
		
		try {
			return (Pane) FXMLLoader.load(getClass().getResource("/infoPanel.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		return null;
	}
} 