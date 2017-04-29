package info;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import meta.working.InfoDTO;
import meta.working.InfoLayoutDTO;
import model.Autosave;
import model.InfoInList;
import model.InfoManager;
import util.InfoLayout;

public class WebViewAreaController implements Editable {
 	
	private InfoManager infoManager;
	private InfoInList infoInList;
	private Integer id;
	
	@FXML
	private GridPane webViewGridPane;

    @FXML
    private WebView webViewArea;
    
    @FXML
    private TextField urlTextField;
    
	private ChangeListener<Boolean> onFocusListener;

    
	public void setInfoDTO(Integer id, InfoInList infoInList, InfoDTO infoDTO) {
		this.id=id;
		this.infoInList = infoInList;
		final WebEngine webEngine = webViewArea.getEngine();
   	 	webEngine.load(infoDTO.getText());
   	 	urlTextField.setText(infoDTO.getText());
   	 	
   	 	InfoLayoutDTO infoLayoutDTO = this.infoManager.readInfoLayoutDTO(id, infoInList);
		this.webViewGridPane.setMinHeight(  infoLayoutDTO.getHeight());
		InfoLayout.resize(webViewGridPane,infoManager,infoLayoutDTO);
	} 

	public void setInfoManager(InfoManager infoManager) {
		
		this.infoManager = infoManager;
		this.urlTextField.editableProperty().bind(this.infoManager.getInfoPanelController().getIsEditable());
		
		
	   	 this.onFocusListener =new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if(newValue.equals(true)){
							WebViewAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(false);
						}else{
							WebViewAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(true);
						}
				}
			};
			
		this.webViewArea.focusedProperty().addListener(this.onFocusListener);
	   	this.urlTextField.focusedProperty().addListener(this.onFocusListener);
	   	
	   	
	   	this.urlTextField.textProperty().addListener(new ChangeListener<String>() {

			private Autosave autosave=new Autosave();


			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				autosave.runWeb(newValue,infoManager,id,infoInList);
			}
		});
		
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
		this.webViewArea.focusedProperty().removeListener(this.onFocusListener);
		this.urlTextField.focusedProperty().removeListener(this.onFocusListener);
	}
	
	
	@FXML
    void onMouseClickedWebArea(MouseEvent event) { 
		this.infoManager.getInfoPanelController().setLastSelectedINFO();
		this.infoManager.setLastInfoSelected(this.id,this.infoInList);
    }
	
	@FXML
    void OnKeyPressedTextField(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER){
			webViewArea.getEngine().load(this.urlTextField.getText());
		}
    }

	
}