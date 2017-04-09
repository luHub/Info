package info;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.InfoManager;

public class Loader {

	
	public  InfoTextAreaController addTextArea(VBox parent,InfoManager infoManager){
		InfoTextAreaController infoTextAreaController=null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/InfoTextArea.fxml"));
			Node node = loader.load();
			AnchorPane.setBottomAnchor(node, 0d);
			AnchorPane.setTopAnchor(node, 0d);
			AnchorPane.setLeftAnchor(node, 0d);
			AnchorPane.setRightAnchor(node, 0d);
			parent.getChildren().add(node);
			infoTextAreaController = loader.<InfoTextAreaController>getController();
			infoTextAreaController.setInfoManager(infoManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return infoTextAreaController;
	}

}
