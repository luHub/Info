package info;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import model.InfoInList;
import model.InfoManager;
import model.MODE;

public class InfoPanelController {
	
	@FXML
    private Button ModeButton;

    @FXML
    private Button editButton;

    @FXML
    private MenuButton addInfoButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private ListView<InfoInList> infoView;

    @FXML
    private AnchorPane infoAnchorPane;
    
    @FXML
    private AnchorPane infoDisplay;
	
    private MODE mode = MODE.READ; 
    private InfoManager infoManager = new InfoManager();
    
    @FXML
    private void initialize(){
    	setToReadMode();
    	setNewInfoButton();
    	initializeInfoManager();
    	
    	//Customization of ListView to work with objects in callback it will use toString From object
    	infoView.setCellFactory(new Callback<ListView<InfoInList>, ListCell<InfoInList>>() {
			@Override
			public ListCell<InfoInList> call(ListView<InfoInList> param) {
				
				ListCell<InfoInList> myQuestion = new ListCell<InfoInList>(){
					@Override
                    protected void updateItem(InfoInList t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.toString());
                        }else{
                        	//When deleting
                        	setText("");
                        }
                    }
				};
				return myQuestion;
			}
		});
    	
    }

	private void initializeInfoManager() {
		this.infoManager.setInfoListView(infoView);
		this.infoManager.updateUI();
		
	}

	@FXML
    void deleteInfoButton(ActionEvent event) {
    	
    }

    @FXML
    void editInfoButton(ActionEvent event) {

    }

    @FXML
    private void ModeButtonOnAction(ActionEvent event) {
    	if(mode == MODE.READ){
    		setToEditMode();
    	}else{
    		setToReadMode();
    	}
    }
    
    private void setToEditMode(){
    	mode = MODE.EDIT;
		ModeButton.setText("Read");
		DeleteButton.setVisible(true);
		DeleteButton.setDisable(true);
		addInfoButton.setVisible(true);
		editButton.setVisible(true);
		editButton.setDisable(true);
		
	}
    
    private void setToReadMode() {
    	mode = MODE.READ;
		ModeButton.setText("Edit");
		DeleteButton.setVisible(false);
		addInfoButton.setVisible(false);
		editButton.setVisible(false);
    }
    
    
    private void setNewInfoButton() {
    	final int newFile = 0;
    	final int newText = 1;
    	final int newImage = 2;
    	final int newWeb = 3;
    	
		addInfoButton.getItems().get(newFile).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewInfoFile();
			}
		});
	}
}


