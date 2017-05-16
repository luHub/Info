package info;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import meta.working.FileDTO;
import meta.working.InfoIndexDTO;
import model.InfoInList;
import model.InfoManager;

public class InfoCellController {

	private Node infoCell;

	@FXML
	private TextField infoTextField;

	@FXML
	private Label idLabel; // Change to Order

	private InfoManager infoManager;
	private InfoIndexDTO infoIndexDTO;
	private InfoInList infoInList; 

	private ChangeListener<String> stringChangeListner = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			//TODO This should be an idependent from FileOps operation in
			//CoverInfoOps Runnale
			InfoCellController.this.infoIndexDTO.setTitle(newValue);
			infoManager.updateCover();
		}
	};

	public void initialize(final InfoManager infoManager, final InfoIndexDTO infoIndexDTO, InfoInList infoInList) {
		this.infoManager = infoManager;
		this.infoInList = infoInList;
		this.infoIndexDTO=infoIndexDTO;
		
		
		this.infoInList=infoInList;
		this.infoTextField.setText(infoIndexDTO.getTitle());
		this.idLabel.setText(infoIndexDTO.getInfoFileId().toString());
		//After text is setted activate listener to persist Titles
		activateListener();
		// this.infoManager.getInfoListView().getSelectionModel().select(obj);
		
	}

	public void setInfoCellView(Node node) {
		infoCell = node;
	}

	public Node getInfoCellView() {
		return this.infoCell;
	}

	@FXML
	void onMousePressedTextField(MouseEvent event) {
		this.infoManager.getInfoListView().getSelectionModel().select(infoInList);
	}
	
	public void activateListener(){
		infoTextField.textProperty().addListener(stringChangeListner);
	}
	public void disableListener(){
		infoTextField.textProperty().removeListener(stringChangeListner);
	}
	

}