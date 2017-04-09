package info;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.InfoManager;

public class InfoTextAreaController implements Editable {

	private InfoManager infoManager;
	private ChangeListener<Boolean> onFocusListener;
	
    @FXML
    private TextArea infoTextArea;

	public TextArea getInfoTextArea() {
		return infoTextArea;
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager = infoManager;
		this.infoTextArea.editableProperty().bind(this.infoManager.getInfoPanelController().getIsEditable());
		
		this.onFocusListener =new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(newValue.equals(true)){
						InfoTextAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(false);
					}else{
						InfoTextAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(true);
					}
			}
		};
		this.infoTextArea.focusedProperty().addListener(this.onFocusListener);
	} 

	public void setText(String text) {
		this.infoTextArea.setText(text);
	}


	@Override
	public void onChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public void onDelete() {
		this.infoTextArea.focusedProperty().removeListener(this.onFocusListener);
	}
	
	
}