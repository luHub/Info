package info;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import meta.working.InfoDTO;
import model.InfoInList;
import model.InfoManager;

public class InfoTextAreaController implements Editable {

	private InfoManager infoManager;
	private ChangeListener<Boolean> onFocusListener;
	private Integer id;
	
    @FXML
    private TextArea infoTextArea;
    
	private InfoInList infoInList;

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

	public void setInfoDTO(Integer textId,InfoInList infoInList, InfoDTO infoDTO) {
		this.infoTextArea.setText(infoDTO.getText());
		this.infoInList=infoInList;
		this.id=textId;
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
	
	@FXML
    void onMouseClickedTextArea(MouseEvent event) { 
		this.infoManager.getInfoPanelController().setLastSelectedINFO();
		this.infoManager.setLastInfoSelected(this.id,this.infoInList);
    }

	
	
}