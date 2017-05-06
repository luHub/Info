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

	public void initialize(final InfoManager infoManager, final InfoIndexDTO infoIndexDTO, InfoInList infoInList) {
		this.infoManager = infoManager;
		infoTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// FileDTO<Integer,InfoIndexDTO> fileDTO = new File
				// infoManager.updateInfoDetails(infoDTO);
			}
		});
		this.infoInList=infoInList;
		this.infoTextField.setText("INFO");
		this.idLabel.setText(infoIndexDTO.getInfoFileId().toString());
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
	

}