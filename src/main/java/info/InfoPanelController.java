package info;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.InfoInList;
import model.InfoManager;
import model.MODE;
 
public class InfoPanelController { 
	 
	@FXML
    private Button ModeButton;

   
    @FXML
    private MenuButton addInfoButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<InfoInList> infoView;
    
    @FXML
    private VBox displayVBox;

    @FXML
    private AnchorPane infoAnchorPane;
    
    @FXML
    private AnchorPane infoDisplay;
	
    private MODE mode = MODE.READ; 
    
    private LastSelected lastSelected = LastSelected.NONE;
    
    private InfoManager infoManager = new InfoManager(this);
    
    
    
    private BooleanProperty isEditable = new SimpleBooleanProperty();
    
    @FXML
    private void initialize(){
    	setToReadMode();
    	setNewInfoButton();
    	initializeInfoManager();
    	
    	
    	this.infoView.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if(newValue.equals(true)){
				InfoPanelController.this.getDelteButton().setDisable(false);
			}else{
				InfoPanelController.this.getDelteButton().setDisable(true);
			}
		}});
    	
    	//Customization of ListView to work with objects in callback it will use toString From object:
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
	private void deleteInfoButton(ActionEvent event) {
		//Continue here to delete items
		if(lastSelected.equals(LastSelected.LIST)){
			System.out.println("Delete from List");
			this.infoManager.deleteFile();
		}else if(lastSelected.equals(LastSelected.INFO)){
			System.out.println("Delete from Info");
			this.infoManager.deleteInfo();
		}
    }

    @FXML
    private void editInfoButton(ActionEvent event) {

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
    	this.ModeButton.setText("Read");
		deleteButton.setVisible(true);
		deleteButton.setDisable(true);
		addInfoButton.setVisible(true);
		this.infoManager.setEditMode();
		this.isEditable.set(true);
	}
    
    private void setToReadMode() {
    	mode = MODE.READ;
		this.ModeButton.setText("Edit");
		deleteButton.setVisible(false);
		addInfoButton.setVisible(false);
		this.infoManager.setReadMode();
		this.isEditable.set(false);
    }
    
    
    private void setNewInfoButton() {
    	final int newFile = 0;
    	final int newText = 1;
    	final int newImage = 2;
    	final int newWeb = 3;
    	
    	//TODO Disable New Text Image Web until and enable only when an INFO is selected
    	
		addInfoButton.getItems().get(newFile).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewInfoFile();
			}
		});
		addInfoButton.getItems().get(newText).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewText();
			}
		});
		
		
	}

	public VBox getDisplayVBox() {
		return displayVBox;
	}

	public BooleanProperty getIsEditable() {
		return isEditable;
	}

	

	public Button getDelteButton() {
		return deleteButton;
	}

	public void setLastSelectedINFO() {
		lastSelected = LastSelected.INFO;
		
	}
	
	@FXML
    void onMoudeClickedList(MouseEvent event) {
		lastSelected = LastSelected.LIST;
		
    }
	
}